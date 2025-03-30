package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.DriveControlSystem;

import java.util.function.DoubleSupplier;



public class DriveCommand extends Command {

    // Subsystem to require
    private final DriveControlSystem mDrive;

    // Double suppliers for human inputs
    private final DoubleSupplier xTranslation, yTranslation, rTranslation, Limiter;

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
            DoubleSupplier Limiter,
            DriveControlSystem subsystem) {

        // Create request

        this.xTranslation = X;
        this.yTranslation = Y;
        this.rTranslation = Rotation;
        this.Limiter = Limiter;
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
    

        // Drive with inputs
        mDrive.control(ChassisSpeeds.fromFieldRelativeSpeeds(
            Math.abs(Limiter.getAsDouble()) * -x * 5,
             Math.abs(Limiter.getAsDouble()) * -y * 5,
              Math.abs(Limiter.getAsDouble()) * rotation * 2,
               mDrive.getRotation2d()));
    
    }


    public DriveCommand withX(DoubleSupplier X) {
        return new DriveCommand(X, yTranslation, rTranslation, Limiter, mDrive);
    }

    public DriveCommand withY(DoubleSupplier Y) {
        return new DriveCommand(xTranslation, Y, rTranslation, Limiter,  mDrive);
    }

    public DriveCommand withRotation(DoubleSupplier Rotation) {
        return new DriveCommand(xTranslation, yTranslation, Rotation, Limiter, mDrive);
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