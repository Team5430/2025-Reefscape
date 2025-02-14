package frc.robot.subsystems.vision;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.team5430.vision.LimelightHelpers;
import com.team5430.vision.VisionEstimate;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;

public class LimelightIO implements CameraIO {


   // private LimeLight camera;
    private String name;

    public LimelightIO(String name, Transform3d location) {
     //   camera = new LimeLight(name, location);
        this.name = name;
        
    }


    @Override
    public Optional<VisionEstimate> getVisionEstimate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPose2d'");
    }

    @Override
    public DoubleSupplier proportionalX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'proportionalX'");
    }

    @Override
    public DoubleSupplier proportionalY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'proportionalY'");
    }

    @Override
    public void setPose2d(Pose2d pose) {
        LimelightHelpers.SetRobotOrientation( name, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean TaginRange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'TaginRange'");
    }
     
    
}
