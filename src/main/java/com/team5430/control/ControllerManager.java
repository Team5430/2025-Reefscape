package com.team5430.control;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ControllerManager {

  static ControllerManager instance;
  static CommandJoystick DriverJoystick;
  static CustomXboxController coPilotController;
    // deadzone
    double axisThreshold = .3;
    //  1/100th of a second  from 0 to 100%
    double mRate = 100;
  
    SlewRateLimiter Xoptimize = new SlewRateLimiter(mRate);
    SlewRateLimiter Yoptimize = new SlewRateLimiter(mRate);
    SlewRateLimiter Roptimize = new SlewRateLimiter(mRate);
  
    // init controllers
    private ControllerManager() {
      DriverJoystick = new CommandJoystick(0);
      coPilotController = new CustomXboxController(1);
    }
  
    public static ControllerManager getInstance() {
      if (instance == null) {
        instance = new ControllerManager();
      }
      return instance;
  }

  // Driver Controls
  public double getX() {
    return MathUtil.applyDeadband(Xoptimize.calculate(DriverJoystick.getX()), axisThreshold);
  }

  public double getY() {
    return MathUtil.applyDeadband(Yoptimize.calculate(DriverJoystick.getY()), axisThreshold);
  }
 
  public double getRotation() {
    return MathUtil.applyDeadband(Roptimize.calculate(DriverJoystick.getRawAxis(2)), .6);
  }

  public double getRightX(){
    return MathUtil.applyDeadband(Roptimize.calculate(DriverJoystick.getRawAxis(4)), axisThreshold);
  }
  
  public double getThrottleSwitch() {
    return DriverJoystick.getRawAxis(3);
  }

  public Trigger quickTrigger() {
    return DriverJoystick.button(1);
  }

  // CoPilot Controls
  public Trigger A() {
    return coPilotController.a();
  }

  public Trigger B() {
    return coPilotController.b();
  }

  public Trigger X() {
    return coPilotController.x();
  }

  public Trigger Y() {
    return coPilotController.y();
  }

  public void setRumbleOn() {
    coPilotController.setRumble(true);
  }

  public void setRumbleOff() {
    coPilotController.setRumble(false);
  }

  public Trigger LeftBumper() {
    return coPilotController.leftBumper();
  }

  public Trigger RightBumper() {
    return coPilotController.rightBumper();
  }

  public Trigger LeftTrigger() {
    return coPilotController.leftTrigger();
  }

  public Trigger RightTrigger() {
    return coPilotController.rightTrigger();
  }

  public Trigger PovUp() {
    return coPilotController.povUp();
  }

  public Trigger PovDown() {
    return coPilotController.povDown();
  }

  public Trigger PovLeft() {
    return coPilotController.povLeft();
  }

  public Trigger PovRight() {
    return coPilotController.povRight();
  }
}
