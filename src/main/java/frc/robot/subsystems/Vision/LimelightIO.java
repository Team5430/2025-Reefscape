package frc.robot.subsystems.Vision;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.team5430.vision.LimeLight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;

public class LimelightIO implements CameraIO {


    private LimeLight camera;


    public LimelightIO(String name, Transform3d location) {
        camera = new LimeLight(name, location);
        
    }

    @Override
    public Optional<Pose2d> getPose2d() {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPose2d'");
    }

    @Override
    public boolean TaginRange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'TaginRange'");
    }
     
    
}
