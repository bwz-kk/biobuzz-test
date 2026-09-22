package org.firstinspires.ftc.teamcode.commands.shooter;

import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class ShooterHiveCommand extends InstantCommand {
    public ShooterHiveCommand(Shooter shooter) {
        super(shooter::hive, shooter);
    }
}
