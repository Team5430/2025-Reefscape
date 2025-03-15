package frc.robot.subsystems.superstructure.Elevator;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.superstructure.SuperConstants.ElevatorConstants;;

public class ElevatorFX extends SubsystemBase {
    

//TODO: test
//preset positions for the elevator 
    public enum ElevatorState{

        IDLE(0),
        L1(1),
        L2(2),
        L3(3),
        L4(4);

        private double POSITION;

        private ElevatorState(double Position){
            POSITION = Position;
        }

    }


    //motors for elevator
    private TalonFX L_motor;
    private TalonFX R_motor;

    //position control
    private PositionDutyCycle posSetter = new PositionDutyCycle(0);

    //save state 
    private ElevatorState savedState = ElevatorState.IDLE;

    //init
    public ElevatorFX(){
        L_motor = new TalonFX(ElevatorConstants.L_CANID);
        R_motor = new TalonFX(ElevatorConstants.R_CANID);


    motorConfig();        

    }

//configure the motors here
    private void motorConfig(){

        TalonFXConfiguration R_elevatorConfig = new TalonFXConfiguration();
        TalonFXConfiguration L_elevatorConfig = new TalonFXConfiguration();


    // set kP and gear ratio
        R_elevatorConfig.Slot0.kP = ElevatorConstants.ELEVATOR_KP;
        R_elevatorConfig.Feedback.SensorToMechanismRatio = ElevatorConstants.ELEVATOR_GRATIO;

    //invert left
        L_elevatorConfig.Slot0.kP = ElevatorConstants.ELEVATOR_KP;
        L_elevatorConfig.Feedback.SensorToMechanismRatio = ElevatorConstants.ELEVATOR_GRATIO;
        L_elevatorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    //apply the configs to the motors
        L_motor.getConfigurator().apply(L_elevatorConfig);
        R_motor.getConfigurator().apply(R_elevatorConfig);
        
    }

//sets the motors to run to a position based on the wanted state
    private void setState(ElevatorState wantedState){

        savedState = wantedState;

        //set position
        posSetter.withPosition(wantedState.POSITION);

        //set position to motors
        L_motor.setControl(posSetter);
        R_motor.setControl(posSetter);

    }

//wraps setState as a command
    public Command IDLE(){
        return new InstantCommand(() -> setState(ElevatorState.IDLE), this);
    }

    public Command L1(){
        return new InstantCommand(() -> setState(ElevatorState.L1), this);
    }

    public Command L2(){
        return new InstantCommand(() -> setState(ElevatorState.L2), this);
    }

    public Command L3(){
        return new InstantCommand(() -> setState(ElevatorState.L3), this);
    }

    public Command L4(){
        return new InstantCommand(() -> setState(ElevatorState.L4), this);
    }

// returns the current target state of the subsystem
    private String getState(){
        return savedState.name();
    }

//loop
    @Override
    public void periodic(){
        SmartDashboard.putNumber("Left motor encoder", L_motor.getPosition().getValueAsDouble());
        SmartDashboard.putString(getName(), getState());

    }

}
