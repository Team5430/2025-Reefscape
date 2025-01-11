package com.team5430.control;

import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 *
 * A standard for subsystems to implement if they are some sort of  physical mechanism,
 * as to be able to quickly manipulate them in different situations.
 *  
 */
public abstract class ControlSystem implements Subsystem{

     static ControlSystem getInstance(){
        return null;
      }

      //do some logic with this 
     public boolean hasEmergency = false; 

    //stop the subsystem
    public abstract void Stop();

    // Configure the subsystem for testing
    public abstract boolean configureTest();

    // Check the status of the subsystem
    public abstract boolean checkStatus();

  


}
