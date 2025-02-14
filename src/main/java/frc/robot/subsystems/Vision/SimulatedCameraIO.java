package frc.robot.subsystems.vision;

import java.util.Optional;
// Import necessary libraries and classes
import java.util.function.DoubleSupplier;
import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;

import com.team5430.vision.VisionEstimate;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;

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
     
    double targetYaw = 0.0;
    //
    Transform3d translationToTarget = new Transform3d();


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
    public Optional<VisionEstimate> getVisionEstimate() {
    // Return the robot's pose as a Pose2d object

    return Optional.of(new VisionEstimate(() -> visionSim.getRobotPose().toPose2d(), () -> Timer.getFPGATimestamp()));

    }

    @Override
    public DoubleSupplier proportionalX() {
        // Return a DoubleSupplier that provides the robot's X position scaled by a factor
        return () -> translationToTarget.getX() * .3 * 5;
    }

    //auto align
    @Override
    public DoubleSupplier proportionalY() {
        // Return a DoubleSupplier that provides the robot's Y position scaled by a factor
        return () -> {
            var results = cam.getAllUnreadResults();
            if (!results.isEmpty()) {
                // Camera processed a new frame since last
                // Get the last one in the list.
                var result = results.get(results.size() - 1);
                if (result.hasTargets()) {
                    // At least one AprilTag was seen by the camera
                    for (var target : result.getTargets()) {
                    
                          if (target.getFiducialId() == 7) {
                            // Found Tag 7, record its information
                             targetYaw = -target.getYaw() * 50 * 5;
                    
                    }
                }
            }
        }
         return targetYaw;
        };
    }

    @Override
    public void setPose2d(Pose2d pose) {
        // Update the robot's pose in the vision simulation system
        visionSim.update(pose);
    }

    @Override
    // Method to check if a tag is in range, always returns true for simulated camera
    public boolean TaginRange() {
        var result = camera.getCamera().getLatestResult();
        return result.hasTargets();
    }
}
