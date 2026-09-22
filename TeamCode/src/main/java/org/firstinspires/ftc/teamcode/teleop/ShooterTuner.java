package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.intake.IntakeOffCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeOnCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterFlowerCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterHiveCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterStopCommand;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterConstants;


@TeleOp(name = "Shooter Tuner", group = "Tuning")
public class ShooterTuner extends CommandOpMode {
    private interface Getter { double get(); }
    private interface Setter { void set(double value); }

    private static class Param {
        final String name;
        final Getter getter;
        final Setter setter;
        final double baseStep;
        double stepScale = 1.0;

        Param(String name, double baseStep, Getter getter, Setter setter) {
            this.name = name;
            this.baseStep = baseStep;
            this.getter = getter;
            this.setter = setter;
        }

        double step() {
            return baseStep * stepScale;
        }

        void change(double direction) {
            setter.set(Math.max(0, getter.get() + direction * step()));
        }
    }

    private Shooter shooter;
    private Intake intake;
    private GamepadEx gamepadEx1;
    private Param[] params;
    private int selected = 0;

    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        shooter = new Shooter(hardwareMap);
        register(shooter);

        intake = new Intake(hardwareMap);
        register(intake);

        params = new Param[] {
                new Param("HIVE (RPM)", 100, () -> ShooterConstants.HIVE, v -> ShooterConstants.HIVE = v),
                new Param("FLOWER (RPM)", 100, () -> ShooterConstants.FLOWER, v -> ShooterConstants.FLOWER = v),
                new Param("kF", 0.00001, () -> ShooterConstants.kF, v -> ShooterConstants.kF = v),
                new Param("kP", 0.0001, () -> ShooterConstants.kP, v -> ShooterConstants.kP = v),
                new Param("kI", 0.000001, () -> ShooterConstants.kI, v -> ShooterConstants.kI = v),
                new Param("kD", 0.00001, () -> ShooterConstants.kD, v -> ShooterConstants.kD = v),
                new Param("VELOCITY_TOLERANCE (ticks/s)", 1, () -> ShooterConstants.VELOCITY_TOLERANCE, v -> ShooterConstants.VELOCITY_TOLERANCE = v),
                new Param("MANUAL_POWER", 0.05, () -> ShooterConstants.MANUAL_POWER, v -> ShooterConstants.MANUAL_POWER = Math.min(1, v)),
        };

        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(new ShooterHiveCommand(shooter));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(new ShooterFlowerCommand(shooter));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(new ShooterStopCommand(shooter));
        new Trigger(() -> gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1)
                .whenActive(new IntakeOnCommand(intake))
                .whenInactive(new IntakeOffCommand(intake));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new InstantCommand(
                () -> ShooterConstants.USE_MANUAL_POWER = !ShooterConstants.USE_MANUAL_POWER));

        gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new InstantCommand(
                () -> selected = (selected + 1) % params.length));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(new InstantCommand(
                () -> selected = (selected + params.length - 1) % params.length));

        gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(new InstantCommand(() -> params[selected].change(1)));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(new InstantCommand(() -> params[selected].change(-1)));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(new InstantCommand(() -> params[selected].stepScale *= 10));
        gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(new InstantCommand(() -> params[selected].stepScale /= 10));
    }

    @Override
    public void run() {
        super.run();

        double targetRpm = shooter.getTargetVelocity() * 60.0 / ShooterConstants.TICKS_PER_REV;
        double currentRpm = shooter.getRPM();
        Param p = params[selected];

        telemetry.addLine("A=HIVE B=FLOWER X=STOP Y=manual on/off");
        telemetry.addLine("Bumpers=select | Dpad up/down=change | Dpad right/left=step x10 / x0.1");
        telemetry.addData("SELECTED", p.name + " = " + format(p.getter.get()) + "   (step " + format(p.step()) + ")");
        telemetry.addLine();

        telemetry.addData("state", shooter.getState());
        telemetry.addData("targetRPM", targetRpm);
        telemetry.addData("currentRPM", currentRpm);
        telemetry.addData("errorRPM", targetRpm - currentRpm);
        telemetry.addData("motorPower", shooter.getPower());
        telemetry.addData("atTarget", shooter.atTarget());
        telemetry.addData("intake", intake.isOn() ? "ON" : "OFF");
        telemetry.addData("manual power mode", ShooterConstants.USE_MANUAL_POWER);
        telemetry.addLine();

        telemetry.addData("HIVE", format(ShooterConstants.HIVE));
        telemetry.addData("FLOWER", format(ShooterConstants.FLOWER));
        telemetry.addData("kP", format(ShooterConstants.kP));
        telemetry.addData("kI", format(ShooterConstants.kI));
        telemetry.addData("kD", format(ShooterConstants.kD));
        telemetry.addData("kF", format(ShooterConstants.kF));
        telemetry.addData("VELOCITY_TOLERANCE", format(ShooterConstants.VELOCITY_TOLERANCE));
        telemetry.update();
    }

    private static String format(double value) {
        return String.format("%.6f", value);
    }
}
