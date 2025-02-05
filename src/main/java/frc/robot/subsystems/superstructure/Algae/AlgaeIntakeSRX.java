package frc.robot.subsystems.superstructure.Algae;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class AlgaeIntakeSRX implements AlgaeIO {


    //save state, and start off as idle
    private Astate savedState = Astate.IDLE;


//declare motors

    private TalonSRX pivot;
    private TalonSRX rollers;

//runs on instance declaration -> settings to be set here.
    public AlgaeIntakeSRX(){
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
@Override
    public Command setState(Astate wantedState){
        //save state
        savedState = wantedState;
        //tell motors to shift their setpoints to wanted state
        return Commands.runOnce(
            () -> {
                pivot.set(ControlMode.Position, savedState.POSITION);
                rollers.set(ControlMode.PercentOutput, savedState.OUTPUT);
                }
            )
        //register command
        .withName("Algae Intake" + savedState.toString());
    }

//triggers to check the state
    public Trigger isIdle = new Trigger(() -> savedState == Astate.IDLE);

    public Trigger isIntaking = new Trigger(() -> savedState == Astate.INTAKE);

    public Trigger isOuttaking = new Trigger(() -> savedState == Astate.OUTTAKE);




//for any sensor verification or logging
    @Override
    public void periodic(){}
    
}
