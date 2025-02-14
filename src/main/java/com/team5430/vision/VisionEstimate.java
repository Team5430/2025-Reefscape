package com.team5430.vision;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;

public class VisionEstimate {
    

    private Pose2d savedPose;
    private double savedTimestamp;

    public VisionEstimate(Supplier<Pose2d> poseSupplier, Supplier<Double> timestampSupplier) {
        this.savedPose = poseSupplier.get();
        this.savedTimestamp = timestampSupplier.get();
    }

    public Pose2d getPose() {
        return savedPose;
    }

    public double getTimestamp() {
        return savedTimestamp;
    }

}
