package com.team5430.swerve;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.Timer;



public class SimModuleIO implements ModuleIO
{

  //THANKS TO BRONC BOTZ FOR THIS WORK OF ART
  // * Do note this is modified to include getting the change in module position as to use for a simulated gyroscope

  /**
   * Main timer to simulate the passage of time.
   */
  private final Timer             timer;
  /**
   * Time delta since last update
   */
  private       double            dt;
  /**
   * Fake motor position.
   */
  private       double            pos;
  /**
   * The fake speed of the previous state, used to calculate {@link SimModuleIO#fakePos}.
   */
  private       double            speed;
  /**
   * Last time queried.
   */
  private       double            lastTime;

  private       double             lastPos;

  /**
   * Current simulated swerve module state.
   */
  private SwerveModuleState state;
  private SwerveModulePosition last;


  /**
   * Create simulation class and initialize everything at 0.
   */
  public SimModuleIO()
  {
    timer = new Timer();
    timer.start();
    lastTime = timer.get();
    state = new SwerveModuleState(0, Rotation2d.fromDegrees(0));
    speed = 0;
    pos = 0;
    dt = 0;
    lastPos = 0;
    last = new SwerveModulePosition(0, Rotation2d.fromDegrees(0));
  }

  /**
   * Update the position and state of the module. Called from {@link SwerveModuleIO.SwerveModule#setDesiredState} function
   * when simulated.
   *
   * @param desiredState State the swerve module is set to.
   */
  @Override
  public void setState(SwerveModuleState desiredState)
  {
    //get time delta
    dt = timer.get() - lastTime;
    lastTime = timer.get();

    
    state = desiredState;
    speed = desiredState.speedMetersPerSecond;

    pos += (speed * dt);

    lastPos = pos;
  }

  /**
   * Get the simulated swerve module position.
   *
   * @return {@link SwerveModulePosition} of the simulated module.
   */
  @Override
  public SwerveModulePosition getPosition(boolean refresh)
  {
    return new SwerveModulePosition(pos, state.angle);
  }


  /**
   * Get the {@link SwerveModuleState} of the simulated module.
   *
   * @return {@link SwerveModuleState} of the simulated module.
   */
  @Override
  public SwerveModuleState getState(boolean refresh)
  {
    state = new SwerveModuleState(speed, state.angle);

    return state;
  }

    /**
   * get the change in the simulated swerve module position
   * @return {@link SwerveModulePosition} of the simulated module.
   */
  @Override
  public SwerveModulePosition getModuleDelta(){

    var delta = new SwerveModulePosition(getPosition(true).distanceMeters - last.distanceMeters, state.angle);

     last = new SwerveModulePosition(lastPos, state.angle);

    return delta;

  }

  @Override
  /** TODO: Simulate motors with DC motors @{see edu.wpi.first.wpilibj.simulation.DCMotorSim} */
  public void setVoltage(Voltage volts) {}


  /**
   * Get the robot's current angle as a simulated Rotation2d.
   * 
   * @return Robot's current angle
   */
  @Override
  public Rotation2d getRotation2d() {
   
    getModuleDelta();

    var twist = new Twist2d(getModuleDelta().distanceMeters, 0, 0);
  
    Rotation2d moduleAngle = Rotation2d.fromDegrees(0);
    moduleAngle = moduleAngle.plus(new Rotation2d(twist.dtheta));
    return moduleAngle;
    
  } 

  /**
   * Stops the module.
   */
  @Override
  public void Stop() {
    setState(new SwerveModuleState());
  }


}