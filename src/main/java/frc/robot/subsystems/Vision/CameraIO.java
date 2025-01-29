package frc.robot.subsystems.Vision;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose2d;

/**
 * Interface representing the Camera Input/Output operations.
 */
public interface CameraIO {

    /**
     * Retrieves the current pose of the camera.
     *
     * @return an Optional containing the current Pose2d if available, otherwise an empty Optional.
     */
    public Optional<Pose2d> getPose2d();

    /**
     * Provides a DoubleSupplier for the proportional X value.
     *
     * @return a DoubleSupplier that supplies the proportional X value.
     */
    public DoubleSupplier proportionalX();

    /**
     * Provides a DoubleSupplier for the proportional Y value.
     *
     * @return a DoubleSupplier that supplies the proportional Y value.
     */
    public DoubleSupplier proportionalY();

    /**
     * Sets the current reference pose of the robot.
     *
     * @param pose the Pose2d to set as the current pose.
     */
    public void setPose2d(Pose2d pose);

    /**
     * Checks if a tag is within range.
     *
     * @return true if a tag is in range, false otherwise.
     */
    public boolean TaginRange();
}
