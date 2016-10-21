/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

/*
 * Copyright (c) 2016 Molly Nicholas
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
 * Neither the name of Molly Nicholas nor the names of its contributors may be used to
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
/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.qualcomm.ftccommon;

import android.app.Activity;

import com.qualcomm.ftccommon.configuration.FtcConfigurationActivity;
import com.qualcomm.ftccommon.configuration.RobotConfigFile;
import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.WriteXMLFileHandler;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * {@link FtcEventLoopBase} is an abstract base that handles defines core event processing
 * logic that's available whether or not a Robot is currently extant or not
 */
public abstract class FtcEventLoopBase implements EventLoop
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "FtcEventLoop";

    protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
    protected Activity activityContext;
    protected RobotConfigFileManager robotCfgFileMgr;
    protected FtcEventLoopHandler ftcEventLoopHandler;
    protected boolean runningOnDriverStation = false;
    protected final ProgrammingModeController programmingModeController;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    protected FtcEventLoopBase(HardwareFactory hardwareFactory, UpdateUI.Callback callback, Activity activityContext, ProgrammingModeController programmingModeController)
        {
        this.activityContext = activityContext;
        this.robotCfgFileMgr = new RobotConfigFileManager(activityContext);
        this.ftcEventLoopHandler = new FtcEventLoopHandler(hardwareFactory, callback, activityContext);
        this.programmingModeController = programmingModeController;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override
    public CallbackResult processCommand(Command command) throws InterruptedException, RobotCoreException
        {
        CallbackResult result = CallbackResult.HANDLED;

        String name = command.getName();
        String extra = command.getExtra();

        if (name.equals(CommandList.CMD_RESTART_ROBOT))
            {
            handleCommandRestartRobot();
            }
        else if (name.equals(CommandList.CMD_REQUEST_CONFIGURATIONS))
            {
            handleCommandRequestConfigurations();
            }
        else if (name.equals(CommandList.CMD_REQUEST_CONFIGURATION_TEMPLATES))
            {
            handleCommandRequestConfigurationTemplates();
            }
        else if (name.equals(CommandList.CMD_REQUEST_PARTICULAR_CONFIGURATION))
            {
            handleCommandRequestParticularConfiguration(extra);
            }
        else if (name.equals(CommandList.CMD_ACTIVATE_CONFIGURATION))
            {
            handleCommandActivateConfiguration(extra);
            }
        else if (name.equals(CommandList.CMD_REQUEST_ACTIVE_CONFIGURATION))
            {
            handleCommandRequestActiveConfiguration(extra);
            }
        else if (name.equals(CommandList.CMD_SAVE_CONFIGURATION))
            {
            handleCommandSaveConfiguration(extra);
            }
        else if (name.equals(CommandList.CMD_DELETE_CONFIGURATION))
            {
            handleCommandDeleteConfiguration(extra);
            }
        else if (name.equals(CommandList.CMD_START_PROGRAMMING_MODE))
            {
            handleCommandStartProgrammingMode();
            }
        else if (name.equals(CommandList.CMD_STOP_PROGRAMMING_MODE))
            {
            handleCommandStopProgrammingMode();
            }
        else
            {
            result = CallbackResult.NOT_HANDLED;
            }
        return result;
        }


    protected void handleCommandActivateConfiguration(String data)
        {
        RobotConfigFile cfgFile = robotCfgFileMgr.getConfigFromString(data);
        robotCfgFileMgr.setActiveConfigAndUpdateUI(runningOnDriverStation, cfgFile);
        }

    protected void handleCommandRequestActiveConfiguration(String extra)
        {
        RobotConfigFile configFile = robotCfgFileMgr.getActiveConfig();
        String serialized = configFile.toString();
        networkConnectionHandler.sendCommand(new Command(CommandList.CMD_REQUEST_ACTIVE_CONFIGURATION_RESP, serialized));
        }

    protected void handleCommandRestartRobot()
        {
        ftcEventLoopHandler.restartRobot();
        }

    /*
     * The driver station wants the contents of the configuration file.
     */
    protected void handleCommandRequestParticularConfiguration(String data)
        {
        RobotConfigFile file = robotCfgFileMgr.getConfigFromString(data);
        ReadXMLFileHandler parser = new ReadXMLFileHandler();

        if (file.isNoConfig())
            {
            // don't try to parse if there's no file
            return;
            }

        try
            {
            WriteXMLFileHandler writeXMLFileHandler = new WriteXMLFileHandler(activityContext);
            ArrayList<ControllerConfiguration> deviceList = (ArrayList<ControllerConfiguration>) parser.parse(file.getXml());
            String xmlData = writeXMLFileHandler.toXml(deviceList);
            RobotLog.vv(FtcConfigurationActivity.TAG, "FtcEventLoop: handleCommandRequestParticularConfigFile, data: " + xmlData);
            networkConnectionHandler.sendCommand(new Command(CommandList.CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP, xmlData));
            }
        catch (RobotCoreException e)
            {
            e.printStackTrace();
            }
        }

    protected void handleCommandDeleteConfiguration(String fileInfo)
        {
        RobotConfigFile cfgFile = robotCfgFileMgr.getConfigFromString(fileInfo);
        File file = RobotConfigFileManager.getFullPath(cfgFile.getName());
        if (file.delete())
            {
            /* all is well */
            }
        else
            {
            DbgLog.error("Tried to delete a file that does not exist: " + cfgFile.getName());
            }
        }

    protected void handleCommandSaveConfiguration(String fileInfo)
        {
        String[] fileInfoArray = fileInfo.split(RobotConfigFileManager.FILE_LIST_COMMAND_DELIMITER);
        try
            {
            RobotConfigFile cfgFile = robotCfgFileMgr.getConfigFromString(fileInfoArray[0]);
            robotCfgFileMgr.writeToFile(cfgFile, false, fileInfoArray[1]);
            robotCfgFileMgr.setActiveConfigAndUpdateUI(false, cfgFile);
            }
        catch (RobotCoreException | IOException e)
            {
            e.printStackTrace();
            }
        }

    /**
     * Serialize the entire list of config file metadata and send to the driver station
     */
    protected void handleCommandRequestConfigurations()
        {
        ArrayList<RobotConfigFile> fileList = robotCfgFileMgr.getXMLFiles();
        String objsSerialized = RobotConfigFileManager.serializeXMLConfigList(fileList);
        networkConnectionHandler.sendCommand(new Command(CommandList.CMD_REQUEST_CONFIGURATIONS_RESP, objsSerialized));
        }

    /**
     * Serialize the entire list of config file metadata and send to the driver station
     */
    protected void handleCommandRequestConfigurationTemplates()
        {
        ArrayList<RobotConfigFile> fileList = robotCfgFileMgr.getXMLTemplates();
        String objsSerialized = RobotConfigFileManager.serializeXMLConfigList(fileList);
        networkConnectionHandler.sendCommand(new Command(CommandList.CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP, objsSerialized));
        }

    /**
     * Starts programming mode on the robot controller, as requested by driver station.
     */
    protected void handleCommandStartProgrammingMode()
        {
        programmingModeController.startProgrammingMode(ftcEventLoopHandler);
        }

    /**
     * Stops programming mode on the robot controller, as requested by driver station.
     */
    protected void handleCommandStopProgrammingMode()
        {
        programmingModeController.stopProgrammingMode();
        ftcEventLoopHandler.restartRobot();
        }
    }
