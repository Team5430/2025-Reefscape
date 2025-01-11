package com.team5430.control;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class
CustomXboxController extends CommandXboxController {

  public CustomXboxController(int port) {
    super(port);
  }

  // if true = 1, else 0
  public void setRumble(boolean on) {
    this.getHID().setRumble(RumbleType.kBothRumble, on ? 1 : 0);
  }

  public double getLeftStickDirectionDegrees() {
    return Math.toDegrees(Math.atan2(this.getLeftX(), -getLeftY()));
  }

  public double getRightStickDirectionDegrees() {
    return Math.toDegrees(Math.atan2(this.getRightX(), -getRightY()));
  }

  public double getLeftMagnitude() {
    return Math.hypot(getLeftX(), getLeftY());
  }

  public double getRightMagnitude() {
    return Math.hypot(getRightX(), getRightY());
  }

  public CommandXboxController getController() {
    return this;
  }
}
