package frc.robot.subsystems.superstructure.Algae;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.superstructure.SuperConstants.AlgaeConstants;

public class AlgaeIntakeSRX implements AlgaeIO {


//save state, and start off as idle
    private Algaestate savedState = Algaestate.IDLE;


//declare motors

    private TalonSRX pivot_L;
    private TalonSRX pivot_R;
    private TalonSRX rollers;

//runs on instance declaration -> settings to be set here.
//TODO: fix can ids 
    public AlgaeIntakeSRX(){
        pivot_L = new TalonSRX(AlgaeConstants.L_PIVOT_CANID);
        pivot_R = new TalonSRX(AlgaeConstants.R_PIVOT_CANID);
        rollers = new TalonSRX(AlgaeConstants.ROLLER_CANID);
        motorConfig();

    }

//configure motor settings here
    private void motorConfig(){
    // default all settings to prevent any unexpected behaviors
        
        pivot_L.configFactoryDefault();
        pivot_R.configFactoryDefault();
    
    //tell motor on right to follow same output
        pivot_R.follow(pivot_L);
    //select SRX magencoder for feedback
        pivot_L.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 ,10);
    //invert sensor readings
        pivot_L.setSensorPhase(true);
    //invert motor output ^ does not affect sensor phase
        pivot_L.setInverted(false);

    //use slot0 on TalonSRX with set PID values
        pivot_L.config_kP(0, AlgaeConstants.PIVOT_KP);
        pivot_L.config_kI(0, AlgaeConstants.PIVOT_KI);
        pivot_L.config_kD(0, AlgaeConstants.PIVOT_KD);

        pivot_R.setInverted(InvertType.FollowMaster);

    //Zero position on the pivot
        pivot_L.setSelectedSensorPosition(0);

        //TODO: current limitings

        
    }

//coontrol the flow of states; pass them through a single function to keep track of states
@Override
    public Command setState(Algaestate wantedState){
        //save state
        savedState = wantedState;
        //tell motors to shift their setpoints to wanted state
        return Commands.runOnce(
            () -> {
                pivot_L.set(ControlMode.Position, savedState.POSITION);
               // rollers.set(ControlMode.PercentOutput, savedState.OUTPUT);
                }
            )
        //register command
        .withName("Algae Intake" + savedState.toString());
    }

//triggers to check the state
    public Trigger isIdle = new Trigger(() -> savedState == Algaestate.IDLE);

    public Trigger isIntaking = new Trigger(() -> savedState == Algaestate.INTAKE);

    public Trigger isOuttaking = new Trigger(() -> savedState == Algaestate.OUTTAKE);


//for any sensor verification or logging
    @Override
    public void periodic(){}
    
}
