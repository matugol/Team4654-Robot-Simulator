package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

public abstract class BaseOpMode extends OpMode {

    public final HashMap<String, DcMotor> motors = new HashMap<>();
    public final HashMap<String, Servo> servos = new HashMap<>();

    public DcMotor leftFront, rightFront, leftBack, rightBack;


    @Override
    public void init() {    	
        leftFront = hardwareMap.dcMotor.get("leftfront");
        leftBack = hardwareMap.dcMotor.get("leftback");
        rightFront = hardwareMap.dcMotor.get("rightfront");
        rightBack = hardwareMap.dcMotor.get("rightback");

        motors.put("leftfront", leftFront);
        motors.put("leftback", leftBack);
        motors.put("rightfront", rightFront);
        motors.put("rightback", rightBack);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

    }

}