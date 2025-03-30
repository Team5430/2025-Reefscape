package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.SignalLogger;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;


public class SwerveModuleGroup {

  // Maximum of 4 swerve modules; accounted for array
  protected ModuleIO[] swerveModules = new ModuleIO[4];
  protected int moduleCount;
  protected SwerveModuleConstants constants;
  
  //back up heading
  protected Rotation2d robotAngle;
  protected SwerveModulePosition deltaPositions[];

  /**
   * Modular Swerve creation, can be used to create up to 4 modules at a time.
   * NOTE: Consider reserving CANids 0-8 for motors and CANCoders
   *
   * <pre>Supports:
   *   CANCoders
   *   TalonFX based motors
   * </pre>
   *
   * to configure this to your use case, utilize SwerveModuleConstants
   *
   * @param modules Allows creation of up to 4 SwerveModules, based on your given config
   * @param config Configuration for the swerve modules
   * @see frc.robot.subsystems.drive.SwerveModuleConstants
   */
  public SwerveModuleGroup(SwerveModuleConstants config, ModuleIO... modules) {
    this.moduleCount = modules.length;    
    this.constants = config;

    this.robotAngle = Rotation2d.fromDegrees(0);


    this.deltaPositions = new SwerveModulePosition[moduleCount];

    for (int i = 0; i < moduleCount; i++) {
      this.swerveModules[i] = modules[i];
      this.deltaPositions[i] = new SwerveModulePosition(0, new Rotation2d(0));
    }
    
  }

  /** Set Module States to desired state */
  public void setStates(SwerveModuleState... currentStates) {
    // Prevent speed from surpassing max speed
    SwerveDriveKinematics.desaturateWheelSpeeds(currentStates, constants.MAX_VELOCITY_MPS);
    // Apply states in a loop
    for (int i = 0; i < moduleCount; i++) {
      this.swerveModules[i].setState(currentStates[i]);
    }
  }

  /** Forward is relative to the robot's forward */
  @Deprecated
  public void robotRelativeDrive(ChassisSpeeds speeds) {
    SwerveModuleState[] states = constants.KINEMATICS.toSwerveModuleStates(speeds);
    setStates(states);
  }

  /** Drive with a gyroscope to keep heading relative to the field */
  @Deprecated
  public void fieldCentricDrive(ChassisSpeeds speeds, Rotation2d robotAngle) {
    robotRelativeDrive(ChassisSpeeds.fromFieldRelativeSpeeds(speeds, robotAngle));
  }

  /**Drive Based on given request {@link frc.robot.subsystems.drive.Requests} */
  public void control(ChassisSpeeds request) {
    SwerveModuleState[] states = constants.KINEMATICS.toSwerveModuleStates(request);
    setStates(states);
  }
  /**
   * Get the current speeds of the robot.
   *
   * @return the angle and velocity of the robot
   */
  public ChassisSpeeds getCurrentSpeeds() {
    return this.constants.KINEMATICS.toChassisSpeeds(getStates(true));
  }

  /** Stop all swerve modules */
  public void Stop() {
    for (ModuleIO s : swerveModules) {
      s.Stop();
    }
  }

  /** Get positions of all modules */
  public SwerveModulePosition[] getPositions(boolean refresh) {
    return new SwerveModulePosition[] {
            swerveModules[0].getPosition(refresh),
            swerveModules[1].getPosition(refresh),
            swerveModules[2].getPosition(refresh),
            swerveModules[3].getPosition(refresh)
    };
  }

  /** Get states of all modules */
  public SwerveModuleState[] getStates(boolean refresh) {
    return new SwerveModuleState[] {
            swerveModules[0].getState(refresh),
            swerveModules[1].getState(refresh),
            swerveModules[2].getState(refresh),
            swerveModules[3].getState(refresh)
    };
  }

  private void setVoltage(Voltage volts) {
    for (ModuleIO s : swerveModules) {
      s.setVoltage(volts);
    }
  }

  //get back up heading in cae gyro fails
  public Rotation2d getRotation2d() {
//
    // Get the change in the simulated swerve module position
        for(int i = 0; i < moduleCount; i++){
            deltaPositions[i] = swerveModules[i].getModuleDelta();
        }
        
        var twist = constants.KINEMATICS.toTwist2d(deltaPositions);
    // Get the robot's current angle as a simulated Rotation2d.
        robotAngle = robotAngle.plus(new Rotation2d(twist.dtheta));
        return robotAngle;
      
  }

  public final SysIdRoutine SysIdTuning(Subsystem subsystem) {
    //setups the system identification routine
   var routine = new SysIdRoutine(
      new SysIdRoutine.Config(
         null,        // Use default ramp rate (1 V/s)
         Volts.of(4), // Reduce dynamic step voltage to 4 to prevent brownout
         null,        // Use default timeout (10 s)
                      // Log state with Phoenix SignalLogger class
         (state) -> SignalLogger.writeString("state", state.toString())
      ),
      new SysIdRoutine.Mechanism(
         this::setVoltage, // Set voltage
         null,
          subsystem // Subsystem to require
      )
   );

    return routine;
  }

  

 

}
