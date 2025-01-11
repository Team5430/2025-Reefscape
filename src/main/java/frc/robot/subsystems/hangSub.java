package frc.robot.subsystems;

import java.io.IOException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.team5430.control.ControlSystem;
import com.team5430.util.ConstantsParser;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class hangSub extends ControlSystem {

//init motors
  protected TalonSRX L;
  protected TalonSRX R;
 
  // Enum for the state of the hang
  private enum HangState {
    IDLE(0),
    DOWN(-1);

   private double output;

    HangState(double output) {
      this.output = output;
    }

  }

  private static ConstantsParser.LocalConstants constants;

  // Singleton instance
  protected static hangSub mInstance = new hangSub();

  public static hangSub getInstance(){
    return mInstance;
  }
  
  public hangSub() {

    // Parse constants


      try {
        constants = new ConstantsParser("constants/hangproperties.json").createLocalConstants();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    // init motors
    L = new TalonSRX(constants.getCANids()[0]);
    R = new TalonSRX(constants.getCANids()[1]);
    
    // invert motor
    R.setInverted(constants.getIsInverted()[0]);

  }

//set output to the hang
  private Command setOutput(HangState state) {
    return Commands.runOnce(
        () -> {
          L.set(ControlMode.PercentOutput, state.output);
          R.set(ControlMode.PercentOutput, state.output);
        },
        this);
  }

  // Set the state of the hang
  public Command Down() { return setOutput(HangState.DOWN);}

  public Command Idle() { return setOutput(HangState.IDLE);}


  @Override
  //pretty much just unwind the string
  public boolean configureTest(){
    try {
      Down();
      Idle();
      DriverStation.reportWarning("HangSub Test Passed", false);
      return true;
    } catch (Exception e) {
      DriverStation.reportError("HangSub Test Failed:" + e.getMessage(), true);
      return false;
    }
        
  
  }

  // Stop motors
  @Override
  public void Stop() {
    Idle().execute();
  }

  //any problems will appear with the motors
  @Override
  public boolean checkStatus() {
    return true;
  }




  
}
