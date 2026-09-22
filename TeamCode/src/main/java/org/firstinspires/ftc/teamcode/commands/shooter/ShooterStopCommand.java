package org.firstinspires.ftc.teamcode.commands.shooter;

import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class ShooterStopCommand extends InstantCommand {
    public ShooterStopCommand(Shooter shooter) {
        super(shooter::stop, shooter);
    }
}
