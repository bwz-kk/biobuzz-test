package org.firstinspires.ftc.teamcode.subsystems.intake;

import static org.firstinspires.ftc.teamcode.subsystems.intake.IntakeConstants.*;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;


public class Intake extends SubsystemBase {
    private final MotorEx motor;
    private boolean on = false;

    public Intake(HardwareMap hardwareMap) {
        motor = new MotorEx(hardwareMap, MOTOR_NAME);
        motor.setRunMode(Motor.RunMode.RawPower);
        motor.setInverted(MOTOR_REVERSED);
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    public void on() {
        on = true;
    }

    public void off() {
        on = false;
    }

    public boolean isOn() {
        return on;
    }

    @Override
    public void periodic() {
        motor.setInverted(MOTOR_REVERSED);
        motor.set(on ? Range.clip(ON_POWER, -1, 1) : 0);
    }
}
