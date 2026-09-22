package org.firstinspires.ftc.teamcode.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterConstants.*;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;


public class Shooter extends SubsystemBase {
    public enum State { OFF, HIVE, FLOWER }

    private final MotorEx flywheel;
    private final PIDFController controller = new PIDFController(kP, kI, kD, kF);

    private State state = State.OFF;
    private double power = 0;

    public Shooter(HardwareMap hardwareMap) {
        flywheel = new MotorEx(hardwareMap, MOTOR_NAME);
        flywheel.setRunMode(Motor.RunMode.RawPower);
        flywheel.setInverted(MOTOR_REVERSED);
        flywheel.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        controller.setTolerance(VELOCITY_TOLERANCE);
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void hive() {
        setState(State.HIVE);
    }

    public void flower() {
        setState(State.FLOWER);
    }

    public void stop() {
        setState(State.OFF);
    }

    public double getTargetVelocity() {
        switch (state) {
            case HIVE:   return rpmToTicks(HIVE);
            case FLOWER: return rpmToTicks(FLOWER);
            default:     return 0;
        }
    }

    private static double rpmToTicks(double rpm) {
        return Range.clip(rpm, 0, MAX_RPM) * TICKS_PER_REV / 60.0;
    }

    public double getPower() {
        return power;
    }

    public double getRPM() {
        return getVelocity() * 60.0 / TICKS_PER_REV;
    }

    public double getVelocity() {
        return flywheel.motorEx.getVelocity();
    }

    public int getPosition() {
        return flywheel.motorEx.getCurrentPosition();
    }

    public boolean atTarget() {
        return state != State.OFF && controller.atSetPoint();
    }

    @Override
    public void periodic() {
        controller.setPIDF(kP, kI, kD, kF);
        controller.setTolerance(VELOCITY_TOLERANCE);
        flywheel.setInverted(MOTOR_REVERSED);

        double target = getTargetVelocity();
        if (state == State.OFF) {
            power = 0;
            flywheel.set(0);
            return;
        }

        controller.setSetPoint(target);
        double pid = controller.calculate(getVelocity());
        power = USE_MANUAL_POWER ? Range.clip(MANUAL_POWER, -1, 1) : Range.clip(pid, 0, 1); // flywheel never driven backwards by the PID
        flywheel.set(power);
    }
}
