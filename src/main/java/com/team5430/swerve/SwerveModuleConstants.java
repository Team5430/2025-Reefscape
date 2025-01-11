package com.team5430.swerve;

import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Pound;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Mass;
import edu.wpi.first.units.measure.MomentOfInertia;

// CONFIGURATIONS FOR SWERVE MODULES OVERALL
public class SwerveModuleConstants {

  // Maximum velocity in meters per second
  public final double MAX_VELOCITY_MPS = 5;

  // Maximum angular velocity in radians per second
  public final double MAX_OMEGA_RADIANS = 10;

  // Radius of the drive base in meters
  public final double DRIVE_BASE_RADIUS = 0.567;

  // Wheel radius in meters
  public final double WHEEL_RADIUS = 0.0508;

  // Steering and throttle ratios
  public final double STEER_RATIO = 150.0 / 7.0;
  public final double THROTTLE_RATIO = 8.14;

  // PID constants for steering
  public final double STEER_KP = 0.95;
  public final double STEER_KI = 0;
  public final double STEER_KD = 0;

  // Feedforward constants for steering
  public final double STEER_KV = 0.12; // A velocity target of 1 rps results in 0.12 V output
  public final double STEER_KS = 0.24; // Add 0.25 V output to overcome static friction
  public final double STEER_KA = 0.1;  // An acceleration of 1 rps/s requires 0.01 V output

  // PID constants for throttle
  public final double THROTTLE_KP = 0.1;

  // Robot mass and moment of inertia
  public final Mass ROBOT_MASS = Mass.ofBaseUnits(70, Pound);
  public final MomentOfInertia ROBOT_MOI = MomentOfInertia.ofBaseUnits(1, KilogramSquareMeters);

  // Module configuration
  public final ModuleConfig MODULE_CONFIG = new ModuleConfig(WHEEL_RADIUS, MAX_VELOCITY_MPS, .0309, DCMotor.getKrakenX60(1), 30, 4);

  // Module-specific offsets for steering (in radians)
  // All arrays follow the order of A = 0, B = 1, C = 2, D = 3
  public final double[] STEERING_MODULE_OFFSET = {0.22192, -0.2307, 0.114, 0.4921};

  // CAN IDs for steering motors
  public final int[] STEERING_MODULE_MOTORID = {0, 2, 4, 6};

  // CAN IDs for throttle motors
  public final int[] THROTTLE_MODULE_MOTORID = {1, 3, 5, 7};

  // CAN IDs for encoders
  public final int[] CANCODER_ID = {0, 1, 2, 3};

  // Module locations for kinematics calculations
  protected final Translation2d[] MODULE_LOCATIONS = {
          new Translation2d(0.267, 0.267),  // A
          new Translation2d(-0.267, -0.267), // B
          new Translation2d(0.267, -0.267),  // C
          new Translation2d(-0.267, 0.267)   // D
  };


  // Swerve drive kinematics
  public final SwerveDriveKinematics KINEMATICS =
          new SwerveDriveKinematics(MODULE_LOCATIONS);

//path planner
  // Holonomic drive controller for autonomous following
  public final PPHolonomicDriveController AUTO_FOLLOWER_CONFIG = 
          new PPHolonomicDriveController(
                new PIDConstants(STEER_KP),
                new PIDConstants(STEER_KP)); 

  // Robot configuration
  public final RobotConfig ROBOT_CONFIG = new RobotConfig(ROBOT_MASS, ROBOT_MOI, MODULE_CONFIG, MODULE_LOCATIONS);
}