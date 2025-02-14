package frc.robot.subsystems.vision;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.team5430.vision.LimelightHelpers;
import com.team5430.vision.VisionEstimate;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.Constants;


public class LimelightIO implements CameraIO {


    //Limelight Name as set on dashboard
    private String name;

    //saved VisionEstimate
    private Optional<VisionEstimate> cachedVisionEstimate = Optional.empty();


    public LimelightIO(String name, Transform3d location) {
     //   camera = new LimeLight(name, location);
        this.name = name;
        LLsettings(location);
        
    }

    //set Limelights positon
    private void LLsettings(Transform3d Limelight_Location){
        LimelightHelpers.setCameraPose_RobotSpace(name, 
        0,
        0,
        Limelight_Location.getZ(), //measurement off ground
        0,
        Limelight_Location.getRotation().getY(), //Forward facing angle
        0);

    }


    @Override
    public Optional<VisionEstimate> getVisionEstimate() {
        
    // Get the pose estimate
        var limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name);

    //save measurements
        cachedVisionEstimate = Optional.of(
            new VisionEstimate(() -> limelightMeasurement.pose,
                               () -> limelightMeasurement.timestampSeconds));

        return cachedVisionEstimate;

    }

    @Override
    public DoubleSupplier proportionalX() {
        var error = LimelightHelpers.getTY(name) * Constants.SwerveConstants.MAX_VELOCITY_MPS * Constants.VisionConstants.kP * -1;
        return () -> error;
    }

    @Override
    public DoubleSupplier proportionalAim() {
        var error = LimelightHelpers.getTX(name) * Constants.SwerveConstants.MAX_OMEGA_RADIANS * Constants.VisionConstants.kP * -1;
       return () -> error;
    }

    @Override
    public void setPose2d(Pose2d pose) {
        LimelightHelpers.SetRobotOrientation(name, pose.getRotation().getDegrees(), 0, 0, 0, 0, 0);
    }

    @Override
    public boolean TaginRange() {
        var mt1 = LimelightHelpers.getBotPoseEstimate_wpiBlue(name);
        if (mt1.rawFiducials.length > 0){
            return true;
        }
        return false;

    }
     
    
}
