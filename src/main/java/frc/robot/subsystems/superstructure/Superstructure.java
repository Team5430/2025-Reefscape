package frc.robot.subsystems.superstructure;

import com.team5430.control.ControlSystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.superstructure.Algae.AlgaeIO;


public class Superstructure extends ControlSystem{


    SuperState savedState = SuperState.IDLE;


//declare all superstructure components
    AlgaeIO algae;

//constructor for all superstructure components
    public Superstructure(AlgaeIO algae){
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
        algae.periodic();    
    }
}
