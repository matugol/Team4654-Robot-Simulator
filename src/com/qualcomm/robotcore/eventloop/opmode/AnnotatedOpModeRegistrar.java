/*
 * Copyright (c) 2015 Robert Atkinson
 *
 *    Ported from the Swerve library by Craig MacFarlane
 *    Based upon contributions and original idea by dmssargent.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Robert Atkinson, Craig MacFarlane nor the names of their contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.robotcore.eventloop.opmode;

import android.content.Context;
import android.util.Log;

import com.qualcomm.robotcore.util.ClassFilter;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.AppUtil;

import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;

/**
 * Call {@linkplain AnnotatedOpModeRegistrar#register(OpModeManager)} from FtcOpModeRegister.register()
 * in order to automatically register OpMode classes that you have annotated with @Autonomous
 * or @TeleOp.
 *
 * @see Autonomous
 * @see TeleOp
 */
public class AnnotatedOpModeRegistrar implements ClassFilter
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "OpmodeRegistration";

    final OpModeManager                                 opModeManager;

    final String                                        defaultOpModeGroupName = OpModeMeta.DefaultGroup;
    private final static HashMap<String, LinkedList<OpModeAndMeta>> opModeGroups = new HashMap<String, LinkedList<OpModeAndMeta>>();      // key == group name
    private final static HashSet<Class>                 classesSeen = new HashSet<Class>();
    private final static HashMap<Class, OpModeMeta>     classNameOverrides = new HashMap<Class, OpModeMeta>();

    private final static List<Class>                    annotatedOpModeList = new LinkedList<Class>();
    private final static Set<Method>                    registrarMethods = new HashSet<Method>();
    private Context                                     context;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Call this method from FtcOpModeRegister.register() in order to register OpModes which
     * have been annotated as {@linkplain Autonomous} or {@linkplain TeleOp} OpModes.
     *
     * @param manager   the manager used to carry out the actual registration
     */
    public static void register(final OpModeManager manager)
        {
        AnnotatedOpModeRegistrar registrar = null;
        try {
            registrar = new AnnotatedOpModeRegistrar(manager);
            }
        catch (Exception e)
            {
            Log.wtf(TAG, e);
            registrar = null;
            }

        if (registrar != null)
            registrar.doRegistration();
        }

    public AnnotatedOpModeRegistrar()
        {
        this.opModeManager = null;
        }

    private AnnotatedOpModeRegistrar(final OpModeManager opModeManager)
        {
        this.opModeManager = opModeManager;
        this.context       = AppUtil.getInstance().getApplication();
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    void doRegistration()
        // The body of this is from the following, without which we could not have been successful
        // in this endeavor.
        //      https://github.com/dmssargent/Xtensible-ftc_app/blob/master/FtcRobotController/src/main/java/com/qualcomm/ftcrobotcontroller/opmodes/FtcOpModeRegister.java
        // Many thanks.
        {
        this.processAnnotatedStaticMethods();

        /*
         * ClassManager must have been called at some point.
         */
        for (Class clazz : annotatedOpModeList)
            {
            addAnnotatedOpMode(clazz);
            }

        // Flatten the list
        List<OpModeAndMeta> opModesToRegister = new LinkedList<OpModeAndMeta>();
        for (LinkedList<OpModeAndMeta> opModeList : opModeGroups.values())
            {
            for (OpModeAndMeta opMode : opModeList)
                {
                opModesToRegister.add(opMode);
                }
            }

        // Finally, register all the OpModes
        for (OpModeAndMeta opModeAndMeta : opModesToRegister)
            {
            String name = getOpModeName(opModeAndMeta);
            this.opModeManager.register(OpModeMeta.forName(name, opModeAndMeta.meta), opModeAndMeta.clazz);
            }
        }

    void reportOpModeConfigurationError(String format, Object... args)
        {
        String message = String.format(format, args);
        // Show the message in the log
        Log.w(TAG, String.format("configuration error: %s", message));
        // Make the message appear on the driver station (only the first one will actually appear)
        RobotLog.setGlobalErrorMsg(message);
        }

    boolean checkOpModeClassConstraints(Class clazz, String opModeName)
        {
        // If the class doesn't extend OpMode, that's an error, we'll ignore it
        if (!isOpMode(clazz))
            {
            reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", clazz.getSimpleName());
            return false;
            }

        // If it's not 'public', it can't be loaded by the system and won't work. We report
        // the error and ignore
        if (!Modifier.isPublic(clazz.getModifiers()))
            {
            reportOpModeConfigurationError("'%s' class is not declared 'public'", clazz.getSimpleName());
            return false;
            }

        // Some opmode names aren't allowed to be used
        if (opModeName == null)
            {
            opModeName = getOpModeName((Class<OpMode>)clazz);
            }
        if (!isLegalOpModeName(opModeName))
            {
            reportOpModeConfigurationError("\"%s\" is not a legal OpMode name", opModeName);
            return false;
            }

        return true;
        }

    private void processAnnotatedStaticMethods()
        {
        // Call the OpMode registration methods now
        AnnotationOpModeManagerImpl manager = new AnnotationOpModeManagerImpl();
        for (Method method : registrarMethods)
            {
            try {
                // We support both with and without a context for compatibility
                if (getParameterCount(method)==1)
                    method.invoke(null, manager);
                else if (getParameterCount(method)==2)
                    method.invoke(null, context, manager);
                }
            catch (Exception e)
                {
                // ignored
                }
            }
        }

    private int getParameterCount(Method method)
        {
        Class<?>[] parameters = method.getParameterTypes();
        return parameters.length;
        }

    /*
     * filter(Class class)
     *
     * The class manager calls us as it iterates through the APK's class on startup.
     *
     * We will use this opportunity to cache classes/methods that we are interested in
     * for later use.
     *
     * Note that the cached lists are declared static as the class manager
     * may be instantiated with a different instance of this class than the one that is
     * used later when registration is actually performed.  This is fine because there's
     * one and only one list of classes in an APK and it's immutable within any given run
     * of the application.
     *
     * Doing it this way allows localization of different entities that may be interested
     * in the set of classes packaged in the APK without forcing multiple iterations over the
     * entire set.  See ClassManager usage in event loop initialization.
     */
    @Override
    public void filter(Class clazz)
        {
            checkForOpModeRegistrar(clazz);

            /*
             * Is this an annotated OpMode?
             */
            boolean isTeleOp     = clazz.isAnnotationPresent(TeleOp.class);
            boolean isAutonomous = clazz.isAnnotationPresent(Autonomous.class);

            // If it's neither teleop or autonomous, then it's not interesting to us
            if (!isTeleOp && !isAutonomous)
                return;

            // If we have BOTH Autonomous and TeleOp annotations on a class, that's an error, we'll ignore it.
            if (isTeleOp && isAutonomous)
                {
                reportOpModeConfigurationError("'%s' class is annotated both as 'TeleOp' and 'Autonomous'; please choose at most one", clazz.getSimpleName());
                return;
                }

            // There's some things we need to check about the actual class
            if (!checkOpModeClassConstraints(clazz, null))
                return;

            // If the class has been annotated as @Disabled, then ignore it
            if (clazz.isAnnotationPresent(Disabled.class))
                return;

            annotatedOpModeList.add(clazz);
        }

    /*
     * Does this class have a custom registration annotated method?
     */
    void checkForOpModeRegistrar(Class clazz)
    {
        List<Method> methods = ClassUtil.getDeclaredMethodsIncludingSuper(clazz);
        for (Method method : methods)
        {
            int requiredModifiers   = Modifier.STATIC | Modifier.PUBLIC;
            int prohibitedModifiers = Modifier.ABSTRACT;
            if (!((method.getModifiers() & requiredModifiers) == requiredModifiers && (method.getModifiers() & prohibitedModifiers) == 0))
                continue;

            Class<?>[] parameters = method.getParameterTypes();

            if (method.isAnnotationPresent(OpModeRegistrar.class))
            {
                // the 1-parameter version is legacy (just a manager) instead of also a context
                if (getParameterCount(method)==1 || getParameterCount(method)==2)
                    registrarMethods.add(method);
            }
        }
    }

    class AnnotationOpModeManagerImpl implements AnnotatedOpModeManager
        {
        public void register(Class clazz)
            {
            if (checkOpModeClassConstraints(clazz, null))
                {
                addAnnotatedOpMode((Class<OpMode>)clazz);
                }
            }

        public void register(String name, Class clazz)
            {
            if (checkOpModeClassConstraints(clazz, name))
                {
                addUserNamedOpMode((Class<OpMode>)clazz, new OpModeMeta(name));
                }
            }

        public void register(OpModeMeta meta, Class clazz)
            {
            if (checkOpModeClassConstraints(clazz, meta.name))
                {
                addUserNamedOpMode((Class<OpMode>)clazz, meta);
                }
            }

        public void register(String name, OpMode opModeInstance)
            {
            // We just go ahead and register this, as there's nothing else to do.
            opModeManager.register(name, opModeInstance);
            Log.d(TAG, String.format("registered instance {%s} as {%s}", opModeInstance.toString(), name));
            }

        public void register(OpModeMeta meta, OpMode opModeInstance)
            {
            // We just go ahead and register this, as there's nothing else to do.
            opModeManager.register(meta, opModeInstance);
            Log.d(TAG, String.format("registered instance {%s} as {%s}", opModeInstance.toString(), meta.name));
            }
        }

    /** add this class, which has opmode annotations, to the map of classes to register */
    private void addAnnotatedOpMode(Class<OpMode> clazz)
        {
        if (clazz.isAnnotationPresent(TeleOp.class))
            {
            Annotation annotation = clazz.getAnnotation(TeleOp.class);
            String groupName = ((TeleOp) annotation).group();
            addOpModeWithGroupName(clazz, OpModeMeta.Flavor.TELEOP, groupName);
            }

        if (clazz.isAnnotationPresent(Autonomous.class))
            {
            Annotation annotation = clazz.getAnnotation(Autonomous.class);
            String groupName = ((Autonomous) annotation).group();
            addOpModeWithGroupName(clazz, OpModeMeta.Flavor.AUTONOMOUS, groupName);
            }
        }

    private void addOpModeWithGroupName(Class<OpMode> clazz, OpModeMeta.Flavor flavor, String groupName)
        {
        OpModeAndMeta meta = new OpModeAndMeta(new OpModeMeta(flavor, groupName), clazz);
        if (groupName.equals(""))
            addToOpModeGroup(defaultOpModeGroupName, meta);
        else
            addToOpModeGroup(groupName, meta);
        }

    /** Add a class for which the user has provided the name as opposed to
     *  the name being taken from the class and its own annotations */
    private void addUserNamedOpMode(Class<OpMode> clazz, OpModeMeta meta)
        {
        addToOpModeGroup(defaultOpModeGroupName, new OpModeAndMeta(meta, clazz));
        this.classNameOverrides.put(clazz, meta);
        }

    /** Add a class to the map under the indicated key */
    private void addToOpModeGroup(String groupName, OpModeAndMeta opModeAndMetaData)
        {
        Class clazz = opModeAndMetaData.clazz;
        opModeAndMetaData = new OpModeAndMeta(OpModeMeta.forGroup(groupName, opModeAndMetaData.meta), clazz);

        // Have we seen this class before?
        if (!this.classesSeen.contains(clazz))
            {
            this.classesSeen.add(clazz);

            if (this.opModeGroups.containsKey(groupName))
                {
                this.opModeGroups.get(groupName).add(opModeAndMetaData);
                }
            else
                {
                LinkedList<OpModeAndMeta> temp = new LinkedList<OpModeAndMeta>();
                temp.add(opModeAndMetaData);
                this.opModeGroups.put(groupName, temp);
                }
            }
        else
            {
            // We've already got this class somewhere; don't put it in a second time.
            }
        }

    private String getOpModeName(OpModeAndMeta opModeAndMetaData)
        {
        return getOpModeName(opModeAndMetaData.clazz);
        }

    /** Returns the name we are to use for this class in the driver station display */
    private String getOpModeName(Class<OpMode> opMode)
        {
        String name;

        if (this.classNameOverrides.containsKey(opMode))
            name = this.classNameOverrides.get(opMode).name;
        else if (opMode.isAnnotationPresent(TeleOp.class))
            name = opMode.getAnnotation(TeleOp.class).name();
        else if (opMode.isAnnotationPresent(Autonomous.class))
            name = opMode.getAnnotation(Autonomous.class).name();
        else
            name = opMode.getSimpleName();

        if (name.trim().equals(""))
            name = opMode.getSimpleName();

        return name;
        }

    private boolean isLegalOpModeName(String name)
        {
        if (name == null)
            return false;
        if ((name.equals(OpModeManager.DEFAULT_OP_MODE_NAME)) ||
            (name.trim().equals("")))
            return false;
        else
            return true;
        }

    private boolean isOpMode(Class clazz)
        {
        return ClassUtil.inheritsFrom(clazz, OpMode.class);
        }

    }
