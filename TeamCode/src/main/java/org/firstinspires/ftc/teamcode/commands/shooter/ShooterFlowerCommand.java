package org.firstinspires.ftc.teamcode.commands.shooter;

import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class ShooterFlowerCommand extends InstantCommand {
    public ShooterFlowerCommand(Shooter shooter) {
        super(shooter::flower, shooter);
    }
}
