package frc.robot.subsystems.Vision;

import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
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
        poseEstimator = new PhotonPoseEstimator(fieldLayout,  PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, new Transform3d());
        targets = camera.getAllUnreadResults();
        
    }


    @Override
    public Optional<Pose2d> getPose2d() {
        Optional<Pose2d> robotPose;
        double highestConfidence = 0.0;

        //filter out the best target
        for (PhotonPipelineResult result : targets) {
            // get the best target with the lowest pose ambiguity
            // less ambiguity means more accurate pose
            if (result.getBestTarget().getPoseAmbiguity() < highestConfidence) {
                highestConfidence = result.getBestTarget().getPoseAmbiguity();
                bestResult = result;
            }
        }
        // if the best result is not null and the tag pose is present
        if (bestResult != null && fieldLayout.getTagPose(bestResult.getBestTarget().getFiducialId()).isPresent()) {
            // estimate the robot pose
             robotPose = Optional.of(PhotonUtils.estimateFieldToRobotAprilTag(
                bestResult.getBestTarget().getBestCameraToTarget(),
                fieldLayout.getTagPose(bestResult.getBestTarget().getFiducialId()).get(),
                new Transform3d()
            ).toPose2d());

            return robotPose;
        }
        // return empty if no pose is found
        return Optional.empty(); 
    }

    @Override
    public DoubleSupplier proportionalX() {
        var target = bestResult.getBestTarget();
        return () -> target.getBestCameraToTarget().getTranslation().getX() * 0.3 * 5;  
        
    }

    @Override
    public DoubleSupplier proportionalY() {
        var target = bestResult.getBestTarget();
        return () -> target.getBestCameraToTarget().getTranslation().getY() * 0.3 * 5;  
    }

    @Override
    public void setPose2d(Pose2d pose) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean TaginRange() {
        // check if the best result is not null and the tag pose is present
        return bestResult != null && fieldLayout.getTagPose(bestResult.getBestTarget().getFiducialId()).isPresent();
    }
    
}
