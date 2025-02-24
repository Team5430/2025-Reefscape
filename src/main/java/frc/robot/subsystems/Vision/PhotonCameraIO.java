package frc.robot.subsystems.vision;

import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import com.team5430.vision.VisionEstimate;

import org.photonvision.PhotonUtils;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj2.command.button.Trigger;

@SuppressWarnings("unused")
public class PhotonCameraIO implements CameraIO {

    private PhotonCamera camera;
    private PhotonPoseEstimator poseEstimator;
    private AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);

    private List<PhotonPipelineResult> targets;

    public PhotonCameraIO(String cameraName) {
        // setup camera and pose estimator
        camera = new PhotonCamera(cameraName);
        poseEstimator = new PhotonPoseEstimator(fieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, new Transform3d());
        targets = camera.getAllUnreadResults();
    }

    public Optional<VisionEstimate> getVisionEstimate() {
        Optional<EstimatedRobotPose> robotEstimate = Optional.empty();
        
        //update the pose estimator with all unread results
        for (var result : camera.getAllUnreadResults()){
            robotEstimate = poseEstimator.update(result);
        }
        
        //convert the estimated pose to a VisionEstimate if present
        return robotEstimate.map(
            (estimate) -> new VisionEstimate(() -> estimate.estimatedPose.toPose2d(), () -> estimate.timestampSeconds));
    }

    @Override
    public boolean TaginRange() {
        // check if the any result is has a tag pose present
        return camera.getAllUnreadResults().stream().anyMatch(result -> result.hasTargets());
    }

//use photonvision for pose estimation only 
    @Override
    public DoubleSupplier proportionalX() {
        return () -> 0;
    }

    @Override
    public DoubleSupplier proportionalAim() {
        return () -> 0;
    }

    @Override
    public void setPose2d(Pose2d pose) {}
}
