package com.team5430.control;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ControllerManager {

  static ControllerManager instance;
  static CommandJoystick driveController;
  static CommandJoystick controlBoard;
    // deadzone
    double axisThreshold = .1;

  
    // init controllers
    private ControllerManager() {
      driveController = new CommandJoystick(0);
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
    return MathUtil.applyDeadband(driveController.getX(), axisThreshold);
  }

  public double getY() {
    return MathUtil.applyDeadband(driveController.getY(), axisThreshold);
  }

  public double getTwist(){
    return MathUtil.applyDeadband(driveController.getZ(), axisThreshold);
  }

  public double getZ(){
    return MathUtil.applyDeadband(driveController.getThrottle(), axisThreshold);
  }

  public Trigger getTrigger(){
    return driveController.button(1);
  }
  public Trigger getLeftAlign(){
    return driveController.button(3);
  }

  public Trigger getRightAlign(){
    return driveController.button(4);
  }

  // CoPilot Controls
  public Trigger getL4(){
    return controlBoard.button(1);
  }
  
  public Trigger getL3(){
    return controlBoard.button(5);
  }
  
  public Trigger getL2(){
    return controlBoard.button(3);
  }
  
  public Trigger getL1(){
    return controlBoard.button(12);
  }
  
  public Trigger getConfirmShot(){
    return controlBoard.button(2);
  }
  
  public Trigger getAlgaeIn(){
    return controlBoard.button(4);
  }
  
  public Trigger getAlgaeOut(){
    return controlBoard.button(12);
  }
  
  public Trigger getOverride(){
    return controlBoard.button(6);
  }


}
