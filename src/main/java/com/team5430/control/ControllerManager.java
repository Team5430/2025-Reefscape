package com.team5430.control;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ControllerManager {

  static ControllerManager instance;
  static  CommandXboxController XboxController;
  static CommandJoystick controlBoard;
    // deadzone
    double axisThreshold = .3;
    //  1/6th of a second  from 0 to 100%
    double mRate = 6;
  
    SlewRateLimiter Xoptimize = new SlewRateLimiter(mRate);
    SlewRateLimiter Yoptimize = new SlewRateLimiter(mRate);
    SlewRateLimiter Roptimize = new SlewRateLimiter(mRate);
  
    // init controllers
    private ControllerManager() {
      XboxController = new CommandXboxController(0);
      controlBoard = new CommandJoystick(1);
    }
  
    public static ControllerManager getInstance() {
      if (instance == null) {
        instance = new ControllerManager();
      }
      return instance;
  }

  // Driver Controls
  public double getX() {
    return MathUtil.applyDeadband(Xoptimize.calculate(XboxController.getLeftX()), axisThreshold);
  }

  public double getY() {
    return MathUtil.applyDeadband(Yoptimize.calculate(XboxController.getLeftY()), axisThreshold);
  }

  public double getRightX(){
    return MathUtil.applyDeadband(Roptimize.calculate(XboxController.getRightY()), axisThreshold);
  }

  public Trigger getRightTrigger(){
    return XboxController.rightTrigger();
  }


  // CoPilot Controls
  public Trigger getL4(){
    return controlBoard.button(1);
  }
  // CoPilot Controls
  public Trigger getL3(){
    return controlBoard.button(5);
  }
  // CoPilot Controls
  public Trigger getL2(){
    return controlBoard.button(3);
  }
  // CoPilot Controls
  public Trigger getL1(){
    return controlBoard.button(12);
  }
  // CoPilot Controls
  public Trigger getConfirmShot(){
    return controlBoard.button(2);
  }
  // CoPilot Controls
  public Trigger getAlgaeIn(){
    return controlBoard.button(4);
  }
  // CoPilot Controls
  public Trigger getAlgaeOut(){
    return controlBoard.button(13);
  }
  // CoPilot Controls
  public Trigger getOverride(){
    return controlBoard.button(6);
  }

}
