package org.firstinspires.ftc.teamcode.subsystems.gate;

import static org.firstinspires.ftc.teamcode.subsystems.gate.GateConstants.CLOSED_POSITION;
import static org.firstinspires.ftc.teamcode.subsystems.gate.GateConstants.HM_GATE;
import static org.firstinspires.ftc.teamcode.subsystems.gate.GateConstants.OPEN_POSITION;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.hardware.TauraServo;

public class Gate extends SubsystemBase {

    private final TauraServo servo;

    private boolean open = false;

    public Gate(HardwareMap hardwareMap) {
        servo = new TauraServo(hardwareMap.get(Servo.class, HM_GATE));
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public double getPosition() {
        return open ? OPEN_POSITION : CLOSED_POSITION;
    }

    @Override
    public void periodic() {
        servo.setPosition(getPosition());
    }
}
