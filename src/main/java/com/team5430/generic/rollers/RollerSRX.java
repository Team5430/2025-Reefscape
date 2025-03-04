package com.team5430.generic.rollers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team5430.generic.rollers.RollerIO.RollerSettings;

public class RollerSRX implements RollerIO{
    
    //motors that run the rollers
    TalonSRX motor;

    public RollerSRX(RollerSettings settings){
        motor = new TalonSRX(settings.id());
        
        motor.setInverted(settings.invert());
    }

    @Override
    public void runOpenLoop(double output) {
        motor.set(ControlMode.PercentOutput, output);
    }

    @Override
    public void runVelocity(double velocity) {
        motor.set(ControlMode.Velocity, velocity);
    }

    @Override
    public void stop() {
        motor.set(ControlMode.PercentOutput, 0);
    }

    

}
