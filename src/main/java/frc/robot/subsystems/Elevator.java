package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.team5430.control.ControlSystem;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {

    /* first step ccw means up function with a 2x falcoon 500 cc means down function. the controls are two buttons 
    which will be used on xbox 360 controller with up being y and a being down on the dpad 
        
   */

 

    //initalized motor
   TalonFX Lvador = new TalonFX (0);
   TalonFX Rvador = new TalonFX (1);
   


 

}
  //Enum for the state for the elevator 
  //position values are not final, need values tested from the complete elvator build
   enum ElevatorState  { 
    Level1(120),
    Level2(360),
    Level3(420),
    Level4(550);
    //use predefined variable with enum
    private double position;
    //connect it to this function
 ElevatorState(double position){
    this.position = position;
 }
   

   ElevatorState currentState = ElevatorState.Level1;
   
   private Command SetPosition(ElevatorState wantedState){
      currentState = wantedState;
      return Commands.runOnce(
         () -> {
            Lvador.SetPosition(wantedState);
         }
      )
   }
  
  
  //

  /* 
  ElevatorState currentElevatorState = ElevatorState.IDLE;

  
  private Command setPower(ElevatorState wantedState ) {
    currentElevatorState = wantedState; 
 return Commands. runOnce(
 () -> {
    Lvador.set(ControlMode.PercentOutput,  wantedState.power);
    Rvador.set(ControlMode.PercentOutput,  wantedState.power);
 }
 */


 


// To set and excute the state of the Elevator 

/* 
Public Command Command DOWN(){ 
    return setPower (ElevatorState.DOWN); 
}

public Command IDLE() { return setPower (ElevatorState.IDLE); }   

public Command DOWN(){return setPower(ElevatorState.DOWN)}
//get the state of the Elevator 
public trigger isIDLE = new Trigger(() -> ElevatorState.IDLE == (currentElevatorState);
public trigger isDOWN = new Trigger(() -> ElevatorState.DOWN == (currentElevatorState);

*/




   

                   
