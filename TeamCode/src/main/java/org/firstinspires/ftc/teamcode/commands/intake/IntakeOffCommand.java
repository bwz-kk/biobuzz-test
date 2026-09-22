package org.firstinspires.ftc.teamcode.commands.intake;

import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;

public class IntakeOffCommand extends InstantCommand {
    public IntakeOffCommand(Intake intake) {
        super(intake::off, intake);
    }
}
