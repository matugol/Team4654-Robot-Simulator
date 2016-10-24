package com.qualcomm.ftcrobotcontroller.opmodes;

public class ExampleOpMode extends BaseOpMode {
    float driveModeButton, driveControlStickX, driveControlStickY,
            tankModeButton, tankLeftControlStickY, tankRightControlStickY,
            splitModeButton, splitControlStickX, splitControlStickY;

    private State state = State.DRIVE;
    private boolean climberButtonPressed = false, climberHolderDown = false, lCPressed = false, rCPressed = false, leftClimberDown = false, rightClimberDown = false,
            lHPressed = false, rHPressed = false, lHUp = false, rHUp = false;


    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {   	
        // Define control variables
        driveModeButton = gamepad1.dpad_left ? 1 : 0;
        driveControlStickX = gamepad1.left_stick_x;
        driveControlStickY = gamepad1.left_stick_y;

        tankModeButton = gamepad1.dpad_up ? 1 : 0;
        tankLeftControlStickY = gamepad1.left_stick_y;
        tankRightControlStickY = gamepad1.right_stick_y;

        splitModeButton = gamepad1.dpad_right ? 1 : 0;
        splitControlStickX = gamepad1.right_stick_x;
        splitControlStickY = gamepad1.left_stick_y;

        // Switch control state if a corresponding button is pressed
        if (driveModeButton >= 0.75)
            state = State.DRIVE;
        if (tankModeButton >= 0.75)
            state = State.TANK;
        if (splitModeButton >= 0.75)
            state = State.SPLIT;

        switch (state) {
            case DRIVE:
                // DRIVE mode
                // 1 control stick controls all movement directions and velocities.
                float driveLeftPower = -driveControlStickY - driveControlStickX;
                float driveRightPower = -driveControlStickY + driveControlStickX;

                driveLeftPower = Math.max(Math.min(1, driveLeftPower), -1);
                driveRightPower = Math.max(Math.min(1, driveRightPower), -1);

                leftFront.setPower(driveRightPower);
                leftBack.setPower(driveRightPower);
                rightFront.setPower(driveLeftPower);
                rightBack.setPower(driveLeftPower);
                break;
            case TANK:
                // TANK mode
                // The left and right control sticks are used to control the wheels on their respective sides.
                leftFront.setPower(-tankLeftControlStickY);
                leftBack.setPower(-tankLeftControlStickY);
                rightFront.setPower(-tankRightControlStickY);
                rightBack.setPower(-tankRightControlStickY);
                break;
            case SPLIT:
                // SPLIT mode
                // Very similar to drive mode, except that two seperate control sticks control the forward/backward axis and the left/right axis..
                float splitLeftPower = -splitControlStickY - splitControlStickX;
                float splitRightPower = -splitControlStickY + driveControlStickX;

                splitLeftPower = Math.max(Math.min(1, splitLeftPower), -1);
                splitRightPower = Math.max(Math.min(1, splitLeftPower), -1);

                leftFront.setPower(splitLeftPower);
                leftBack.setPower(splitLeftPower);

                rightFront.setPower(splitRightPower);
                rightBack.setPower(splitRightPower);
                break;
        }

//         Telemetry output stream used for debugging and knowing the current state.
//        telemetry.addData("Text", "***Robot Data***");
//        telemetry.addData("State", state);
//        telemetry.addData("Gamepad Left Y", gamepad1.left_stick_y);
//        telemetry.addData("Left Motor F Power", leftMotorFront.getPower());
//        telemetry.addData("Left Motor B Power", leftMotorBack.getPower());
//        telemetry.addData("Right Motor F Power", rightMotorFront.getPower());
//        telemetry.addData("Right Motor B Power", rightMotorBack.getPower());
//        telemetry.addData("GP2 LB LT", gamepad2.left_bumper + " " + gamepad2.left_trigger);
//        telemetry.addData("GP2 RB RT", gamepad2.right_bumper + " " + gamepad2.right_trigger);


//        telemetry.addData("Color Sensor", sensorIsRed() ? "Redder" : "Bluer");
    }

    // An enum used to store all possible control states.
    private enum State {
        DRIVE,
        TANK,
        SPLIT
    }
}