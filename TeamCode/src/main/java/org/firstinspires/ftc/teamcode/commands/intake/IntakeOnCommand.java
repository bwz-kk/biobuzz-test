package org.firstinspires.ftc.teamcode.commands.intake;

import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;

public class IntakeOnCommand extends InstantCommand {
    public IntakeOnCommand(Intake intake) {
        super(intake::on, intake);
    }
}
