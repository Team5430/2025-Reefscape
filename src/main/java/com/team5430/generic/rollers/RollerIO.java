package com.team5430.generic.rollers;

public interface RollerIO {

    
public record RollerSettings(int id, boolean invert) {}

    
    public void runOpenLoop(double output);

    public void runVelocity(double velocity);

    public void stop();

}
