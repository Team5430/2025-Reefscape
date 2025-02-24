package frc.robot.subsystems.vision;

import java.util.Optional;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.team5430.vision.VisionEstimate;

// VisionSub class to manage multiple CameraIO objects
public class VisionSub extends SubsystemBase {

  // Array of CameraIO objects
  private final CameraIO[] cameraIOs;

  // Trigger to indicate if a tag is in range
  public Trigger TagInRange;

  // Constructor to initialize the VisionSub with CameraIO objects
    //accepts a variable number of Cameras to be managed
  public VisionSub(CameraIO... cameraIOs) {
    this.cameraIOs = cameraIOs;
    // Initialize the TagInRange trigger with the isTaginRange boolean supplier
    TagInRange = new Trigger(isTaginRange());
  }

  // Method to get the Pose2d from the first available CameraIO
  public Optional<VisionEstimate> getVisionEstimate() {
    return Arrays.stream(cameraIOs)
                 .map(CameraIO::getVisionEstimate)
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .findFirst();
  }

  // Method to get the average value from multiple DoubleSuppliers, find biggest value x > 0
  private double getMax(DoubleSupplier... suppliers) {
    return Arrays.stream(suppliers)
                 .mapToDouble(DoubleSupplier::getAsDouble)
                 .max()
                 .orElse(0.0);
  }

  // BooleanSupplier to check if any Camera has a tag in range
  private BooleanSupplier isTaginRange() {
    return () -> Arrays.stream(cameraIOs)
                       .anyMatch(CameraIO::TaginRange);
  }

  // Method to get the average proportional X value from all Cameras
  public double proportionalAim() {
    return getMax(Arrays.stream(cameraIOs)
                            .map(CameraIO::proportionalX)
                            .toArray(DoubleSupplier[]::new));
  }

  // Method to get the average proportional Y value from all Cameras
  public double proportionalRange() {
    return getMax(Arrays.stream(cameraIOs)
                            .map(CameraIO::proportionalAim)
                            .toArray(DoubleSupplier[]::new));
  }

  // Method to set the Pose2d for all Cameras
  public void setPose2d(Pose2d pose) {
    Arrays.stream(cameraIOs).forEach(cameraIO -> cameraIO.setPose2d(pose));
  }
}