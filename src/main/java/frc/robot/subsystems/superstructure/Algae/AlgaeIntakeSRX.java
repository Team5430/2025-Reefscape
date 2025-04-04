package frc.robot.subsystems.superstructure.Algae;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.superstructure.SuperConstants.AlgaeConstants;


public class AlgaeIntakeSRX extends SubsystemBase  {

//save state, and start off as idle    //declare algae states
    //starts with letter of corresponding system
  public  enum Algaestate{
        IDLE(-30, 0),
        INTAKE(-90, -.7),
        OUTTAKE(-90, .6),
        HOLD(-79, -.2);
    
        public double POSITION;
        public double OUTPUT;

        private Algaestate(double POSITION, double OUTPUT){
            this.POSITION = POSITION;
            this.OUTPUT = OUTPUT;
        }

    }

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
    //select SRX magencoder for feedback
        pivot_L.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    //invert sensor readings
        pivot_L.setSensorPhase(true);
    //invert motor output ^ does not affect sensor phase
        pivot_L.setInverted(false);

    //use slot0 on TalonSRX with set PID values
        pivot_L.config_kP(0, AlgaeConstants.PIVOT_KP);
        pivot_L.config_kI(0, AlgaeConstants.PIVOT_KI);
        pivot_L.config_kD(0, AlgaeConstants.PIVOT_KD);

    //not inverted because it is mechanically designed to be opposite
        pivot_R.follow(pivot_L);
        pivot_R.setInverted(InvertType.FollowMaster);

    //Zero position on the pivot
       // pivot_L.setSelectedSensorPosition(0);

        //TODO: current limitings

        
    }

//coontrol the flow of states; pass them through a single function to keep track of states
    public void setState(Algaestate wantedState){
        //save state
        savedState = wantedState;
            //tell motors to shift their setpoints to wanted state        
                pivot_L.set(ControlMode.Position, degreestoTicks(savedState.POSITION));
                rollers.set(ControlMode.PercentOutput, savedState.OUTPUT);
       
    }
    

    public Command IDLE() {
        return new InstantCommand(
            () -> setState(Algaestate.IDLE), this
            );
    }


    public Command INTAKE() {
        return new InstantCommand(
            () -> setState(Algaestate.INTAKE), this
        );
        
    }
    
    public Command OUTTAKE() {
        return new InstantCommand(
            () -> setState(Algaestate.OUTTAKE), this
        );
    }

    public Command HOLD(){
        return new InstantCommand(
            () -> setState(Algaestate.HOLD), this
        );
    }
    
    //convert degrees to ticks as the magencoder reads in ticks; 4096 ticks per rotation per documentation
    private double degreestoTicks(double degrees){
        return degrees * 4096 / 360;
    }

/* 
    //convert encoder ticks into degrees 
    private double tickstoDegrees(double ticks){
        return ticks * 360 / 4096;
    }
*/
    
    public void runOpenLoop(double speed){
        pivot_L.set(ControlMode.PercentOutput, speed);
    }


//return currentas state of the algae intake
    private String getState(){
        return savedState.name();
    }

//for any sensor verification or logging
    @Override
    public void periodic(){
        SmartDashboard.putString("ALGAE INTAKE", getState());
    }

}
