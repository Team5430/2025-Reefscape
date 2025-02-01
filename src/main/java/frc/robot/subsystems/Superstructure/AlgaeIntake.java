package frc.robot.subsystems.Superstructure;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.team5430.control.ControlSystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class AlgaeIntake extends ControlSystem{


//singleton setup
    private static AlgaeIntake instance;

    public static AlgaeIntake getInstance(){
        if (instance == null) {
            instance = new AlgaeIntake();
        }
            return instance;
    }

//declare states for finite state machine 
    private enum state{

        IDLE(0,0),
        INTAKE(45, -1),
        OUTTAKE(25, 1);

        private double POSITION;
        private double OUTPUT;

        private state(double POSITION, double OUTPUT){
            this.POSITION = POSITION;
            this.OUTPUT = OUTPUT;
        }
    }

    //save state, and start off as idle
    private state savedState = state.IDLE;


//declare motors

    private TalonSRX pivot;
    private TalonSRX rollers;

//runs on instance declaration -> settings to be set here.
    private AlgaeIntake(){
        pivot = new TalonSRX(0);
        rollers = new TalonSRX(02);
        motorConfig();

    }

//configure motor settings here
    private void motorConfig(){
    // default all settings to prevent any unexpected behaviors
        pivot.configFactoryDefault();
    //select SRX magencoder for feedback
        pivot.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 ,10);
    //invert sensor readings
        pivot.setSensorPhase(true);
    //invert motor output ^ does not affect sensor phase
        pivot.setInverted(false);

    //use slot0 on TalonSRX with set PID values
        pivot.config_kP(0, .9);
        pivot.config_kI(0, 0);
        pivot.config_kD(0, 0);

    //Zero position on the pivot
        pivot.setSelectedSensorPosition(0);

        
    }

//coontrol the flow of states; pass them through a single function to keep track of states
    private Command setState(state wantedState){
        //save state
        savedState = wantedState;
        //tell motors to shift their setpoints to wanted state
        return Commands.runOnce(
            () -> {
                pivot.set(ControlMode.Position, savedState.POSITION);
                rollers.set(ControlMode.PercentOutput, savedState.OUTPUT);
                },
        this)
        //register command
        .withName(savedState.toString());
    }


//commands to interact with to set states
    public Command IDLE(){
        return setState(state.IDLE);
    }

    public Command INTAKE(){
        return setState(state.INTAKE);
    }

    public Command OUTTAKE(){
        return setState(state.OUTTAKE);
    }

//triggers to check the state
    public Trigger isIdle = new Trigger(() -> savedState == state.IDLE);

    public Trigger isIntaking = new Trigger(() -> savedState == state.INTAKE);

    public Trigger isOuttaking = new Trigger(() -> savedState == state.OUTTAKE);

    //return to idle state when told to stop
    @Override
    public void Stop() {
        IDLE();
    }

    @Override
    public boolean configureTest() {
    //try to execute a consecutive state change for pit testing
        try {

            INTAKE();
            new WaitCommand(0);    
            IDLE();

            return true;
        } catch (Exception e) {
        //return false if it fails for any software reasons
            return false;
        }
    }

    //TODO: implement logic to check motors and sensors
    @Override
    public boolean checkStatus() {
        return true;
    }
    

    
}
