package frc.robot.subsystems.superstructure.elevator;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Elevator implements ElevatorIO {
   // final MotionMagicExpoVoltage m_request = new MotionMagicExpoVoltage(0);


    //declared wantedState that will be updated over time.
    //By default for every robot turned on, set it to LEVEL0 (which is idle and at first position)
    private ElevatorState savedState = ElevatorState.LEVEL0;

    //delcare motor variables
    private  TalonFX L_Elevator; 
    private  TalonFX R_Elevator;
     
    //initalize TalonFX library into motor variables to be used as TalonFX
    //deviceId set to 0 until we know actual canbus call numbers, then replace values
    private Elevator(){
        L_Elevator = new TalonFX(0);
        R_Elevator = new TalonFX(0);
        MotorConfig();
    }


   //Motor Configurations
    @SuppressWarnings("removal") 
    //it's there because setInverted() gets lined out if this code is not here, and can't run elevator:  is set as deprecated and removal without it
    private void MotorConfig(){
        //Right elevator to be inverted because it's mirrored that its on the right side and facing oposite heading of left elevator
    R_Elevator.setInverted(true);


     //configs as the variables to be stored with every PID and motionmagics settings
     //slots are pid profiles - onboard memory of motor
     //up to 3 slots per motor
     var talonFXConfigs = new TalonFXConfiguration();
     var slot0Configs = talonFXConfigs.Slot0; 

     //PID Profiles, better to use P only in testing for safety
     slot0Configs.kP = 2.4; // An error of 1 rotation results in 2.4 V output
     //slot0Configs.kI = 0; // no output for integrated error
     //slot0Configs.kD = 0.1;  // A velocity of 1 rps results in 0.1 V output]

     // set Motion Magic settings into variables
     var motionMagicConfigs = talonFXConfigs.MotionMagic;
     motionMagicConfigs.MotionMagicCruiseVelocity = 80; 
     // Target cruise velocity of 80 rps
     //documentation says 80rps is max?
     motionMagicConfigs.MotionMagicAcceleration = 160; 
     // Target acceleration of 160 rps/s (0.5 seconds)
     //documentation says 160 is max
     motionMagicConfigs.MotionMagicJerk = 100;
    // Target jerk of 1600 rps/s/s (0.1 seconds)
    //why is it 1600??  what is the ideal #???

     //apply PID Configurations (talonFXConfigs) to Elevators
     L_Elevator.getConfigurator().apply(talonFXConfigs);
     R_Elevator.getConfigurator().apply(talonFXConfigs);
    }
    


    //Return the states to the "command scheduler " that determines which function or end of state to run 
    public Command LEVEL0(){
        return setState(ElevatorState.LEVEL0);
    }
    public Command LEVEL1(){
        return setState(ElevatorState.LEVEL1);
    }
    public Command LEVEL2(){
        return setState(ElevatorState.LEVEL2);
    }
    public Command LEVEL3(){
        return setState(ElevatorState.LEVEl3);
    }


    //Triggers as to be pressed and display which states is it
    public Trigger LEVEL0 = new Trigger(() -> savedState == ElevatorState.LEVEL0);
    public Trigger LEVEl1 = new Trigger(() -> savedState == ElevatorState.LEVEL1);
    public Trigger LEVEL2 = new Trigger(() -> savedState == ElevatorState.LEVEL2);
    public Trigger LEVEL3 = new Trigger(() -> savedState == ElevatorState.LEVEl3);

    @Override
    public void periodic() {}



    @Override
    //Function to run the Commands (action) in the positions according to the wantedState
    public Command setState(ElevatorState wantedState) {
        //save ElevatorState so the savedState is updated over time
        savedState = wantedState;
        //tell motors to shift their setpoints to wanted ElevatorState
        //position variable is in elevatorIO - not a new creation here
        return Commands.runOnce(
            () -> {
                L_Elevator.setControl(new DutyCycleOut(wantedState.POSITION));
                R_Elevator.setControl(new DutyCycleOut(wantedState.POSITION));
                
                }
        )
        //register command and display on Driver Station Screen or shuffleboard
        .withName("Elevator State is now at " + savedState.toString());
    }
}