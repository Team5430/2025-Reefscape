package com.team5430.control;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ControlBoard {
    
    private GenericHID USBencoder;

    public ControlBoard(int port){
        USBencoder = new GenericHID(port);
    }

    public Trigger L1(){
        return new Trigger(() -> USBencoder.getRawButton(0));
    }

    public Trigger L2(){
        return new Trigger(() -> USBencoder.getRawButton(3));
    }

    public Trigger L3(){
        return new Trigger(() -> USBencoder.getRawButton(5));
    }

    public Trigger L4(){
        return new Trigger(() -> USBencoder.getRawButton(1));
    }

    public Trigger ConfirmShot(){
        return new Trigger(() -> USBencoder.getRawButton(2));
    }

    public Trigger AlgaeOut(){
        return new Trigger(() -> USBencoder.getRawButton(12));
    }

    public Trigger OverrideSwitch(){
        return new Trigger(() -> USBencoder.getRawButton(12));
    }

}
