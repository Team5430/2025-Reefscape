package frc.robot.subsystems.Vision;

import java.util.Optional;
// Import necessary libraries and classes
import java.util.function.DoubleSupplier;
import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;

public class SimulatedCameraIO implements CameraIO {

    // Create a simulated vision system named "Simulated Cameras"
    VisionSystemSim visionSim = new VisionSystemSim("Simulated Cameras");

    // Load the field layout for AprilTag detection
    AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025Reefscape);

    // Create a PhotonCamera instance for the simulated camera
    PhotonCamera cam;

    // Define properties for the simulated camera
    SimCameraProperties cameraProp = new SimCameraProperties();

    // Create a PhotonCameraSim instance
    PhotonCameraSim camera;

    // Constructor to initialize the simulated camera with a given location
    public SimulatedCameraIO(String cameraName, Transform3d cameraLocation) {
        visionSim.addAprilTags(fieldLayout);

        // Create a PhotonCamera instance with the given camera name
        cam = new PhotonCamera(cameraName);

        // Setup the simulated camera properties
        setupSimulatedCamera();
        // Initialize the PhotonCameraSim with the camera and its properties
        camera = new PhotonCameraSim(cam, cameraProp);
        // Add the camera to the vision simulation system
        visionSim.addCamera(camera, cameraLocation);
    }

    // Method to setup the simulated camera properties
    private void setupSimulatedCamera() {
        // Set camera resolution and field of view
        cameraProp.setCalibration(640, 480, Rotation2d.fromDegrees(100));
        // Set detection noise with average and standard deviation error in pixels
        cameraProp.setCalibError(0.25, 0.08);
        // Set the camera image capture framerate
        cameraProp.setFPS(20);
        // Set the average and standard deviation of image data latency
        cameraProp.setAvgLatencyMs(35);
        cameraProp.setLatencyStdDevMs(5);
    }

    // Override methods from the CameraIO interface
    @Override
    public Optional<Pose2d> getPose2d() {
        // Return the robot's pose as a Pose2d object
        return Optional.of(visionSim.getRobotPose().toPose2d());
    }

    @Override
    public DoubleSupplier proportionalX() {
        // Return a DoubleSupplier that provides the robot's X position scaled by a factor
        return () -> visionSim.getRobotPose().toPose2d().getX() * .3 * 5;
    }

    @Override
    public DoubleSupplier proportionalY() {
        // Return a DoubleSupplier that provides the robot's Y position scaled by a factor
        return () -> visionSim.getRobotPose().toPose2d().getY() * .3 * 5;
    }

    @Override
    public void setPose2d(Pose2d pose) {
        // Update the robot's pose in the vision simulation system
        visionSim.update(pose);
    }

    @Override
    // Method to check if a tag is in range, always returns true for simulated camera
    public boolean TaginRange() {
        return true;
    }
}
