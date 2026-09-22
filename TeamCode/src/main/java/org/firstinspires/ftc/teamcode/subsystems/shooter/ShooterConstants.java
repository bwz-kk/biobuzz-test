package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class    ShooterConstants {
    public static String MOTOR_NAME = "flywheel";
    public static boolean MOTOR_REVERSED = false;
    public static double TICKS_PER_REV = 28;

    public static double HIVE = 3040;
    public static double FLOWER = 2300;

    public static double kP = 0.009844;
    public static double kI = 0.000115;
    public static double kD = 0.000018;
    public static double kF = 0.000325;
    public static boolean USE_MANUAL_POWER = false;
    public static double MANUAL_POWER = 0.5;

    public static double VELOCITY_TOLERANCE = 20;
    public static double MAX_RPM = 6000;
}
