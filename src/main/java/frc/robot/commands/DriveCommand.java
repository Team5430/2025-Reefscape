package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.drive.Requests.FieldCentricRequest;

import java.util.function.DoubleSupplier;


public class DriveCommand extends Command {

    // Subsystem to require
    private final DriveControlSystem mDrive;

    // Double suppliers for human inputs
    private final DoubleSupplier xTranslation, yTranslation, rTranslation;

    private final FieldCentricRequest request;
    /**
     * Command for driving the simulated DriveTrain
     * Same as DriveCommand, without any modifiers to inputs
     *
     * @param X x translation control
     * @param Y y translation control
     * @param Rotation rotational control
     * @param subsystem drive subsystem that is required
     */
    public DriveCommand(
            DoubleSupplier X,
            DoubleSupplier Y,
            DoubleSupplier Rotation,
            DriveControlSystem subsystem) {

        // Create request
        request = new FieldCentricRequest();

        this.xTranslation = X;
        this.yTranslation = Y;
        this.rTranslation = Rotation;
        this.mDrive = subsystem;

        // Require Drive subsystem
        addRequirements(subsystem);
    }

    @Override
    public void execute() {
        
        // Get inputs
        double x = xTranslation.getAsDouble();
        double y = yTranslation.getAsDouble();
        double rotation = rTranslation.getAsDouble();


    //TODO: the multipliier is meant to be the max speed of the robot -> better way to do so?
    
        // Apply inputs; invert to normal cordinate system 
        request.withX(-x * 5)
               .withY(-y * 5)
               .withRot(rotation * 10)
               .withRobotAngle(mDrive.getRotation2d());
               
    
        request.apply();
        // Drive with inputs
        mDrive.control(request);
    }

    public DriveCommand withX(DoubleSupplier X) {
        return new DriveCommand(X, yTranslation, rTranslation, mDrive);
    }

    public DriveCommand withY(DoubleSupplier Y) {
        return new DriveCommand(xTranslation, Y, rTranslation, mDrive);
    }

    public DriveCommand withRotation(DoubleSupplier Rotation) {
        return new DriveCommand(xTranslation, yTranslation, Rotation, mDrive);
    }


    // Stop the drivetrain on end
    @Override
    public void end(boolean interrupted) {
        mDrive.Stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}