package frc.robot.subsystems.vision;

import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;

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
    private AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025Reefscape);

    private List<PhotonPipelineResult> targets;
    private PhotonPipelineResult bestResult = null;

    public PhotonCameraIO(String cameraName) {
        // setup camera and pose estimator
        camera = new PhotonCamera(cameraName);
        poseEstimator = new PhotonPoseEstimator(fieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, new Transform3d());
        targets = camera.getAllUnreadResults();
    }

    public Optional<VisionEstimate> getVisionEstimate() {
        Optional<VisionEstimate> visionEstimate = Optional.empty();
        double highestConfidence = Double.MAX_VALUE;

        // Filter out the best target
        for (PhotonPipelineResult result : targets) {
            // Get the best target with the lowest pose ambiguity
            if (result.getBestTarget().getPoseAmbiguity() < highestConfidence) {
                highestConfidence = result.getBestTarget().getPoseAmbiguity();
                bestResult = result;
            }
        }

        // If the best result is not null and the tag pose is present
        if (bestResult != null && fieldLayout.getTagPose(bestResult.getBestTarget().getFiducialId()).isPresent()) {
            // Estimate the robot pose
            Pose2d estimatedPose = PhotonUtils.estimateFieldToRobotAprilTag(
                bestResult.getBestTarget().getBestCameraToTarget(),
                fieldLayout.getTagPose(bestResult.getBestTarget().getFiducialId()).get(),
                new Transform3d()
            ).toPose2d();

            visionEstimate = Optional.of(new VisionEstimate(() -> estimatedPose, () -> bestResult.getTimestampSeconds()));
        }

        return visionEstimate;
    }

    @Override
    public boolean TaginRange() {
        // check if the best result is not null and the tag pose is present
        return bestResult != null && fieldLayout.getTagPose(bestResult.getBestTarget().getFiducialId()).isPresent();
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
    public void setPose2d(Pose2d pose) {
        poseEstimator.setReferencePose(pose);
    }
}
