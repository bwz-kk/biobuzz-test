package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;


@Config
@Configurable
public class IntakeConstants {
    public static String MOTOR_NAME = "intake";
    public static boolean MOTOR_REVERSED = false;

    public static double ON_POWER = 1.0;
}
