package frc.robot.subsystems.superstructure;

import com.team5430.control.ControlSystem;

import frc.robot.subsystems.superstructure.Algae.AlgaeIntakeSRX;


public class Superstructure extends ControlSystem{


    SuperState savedState = SuperState.IDLE;


//declare all superstructure components
    AlgaeIntakeSRX algae;

//constructor for all superstructure components
    public Superstructure(AlgaeIntakeSRX algae){
        this.algae = algae;


    //defaults all to idle
  

    }


//TODO: implement
    @Override
    public void Stop() {
        
    }
    @Override
    public boolean configureTest() {
       return false;
    }

    @Override
    public boolean checkStatus() {
        return true;
    }
    
    @Override
    public void  periodic() {
    }
}
