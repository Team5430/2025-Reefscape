package com.team5430.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubsystemProperties {

    @JsonProperty("name")
    public String name;

    @JsonProperty("gearRatio")
    public double gearRatio[];

    @JsonProperty("maxVelocity")
    public double maxVelocity[];

    @JsonProperty("kP")
    public double kP[];

    @JsonProperty("kI")
    public double kI[];

    @JsonProperty("kD")
    public double kD[];

    @JsonProperty("minPosition")
    public double minPosition[];

    @JsonProperty("maxPosition")
    public double maxPosition[];

    @JsonProperty("CANids")
    public int CANids[];

    @JsonProperty("isInverted")
    public boolean isInverted[];

    
}
