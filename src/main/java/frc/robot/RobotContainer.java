// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.team5430.control.CollisionDetection;
import com.team5430.control.ControllerManager;
import com.team5430.control.ControlSystemManager;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Constants.RobotType;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.superstructure.Superstructure;
import frc.robot.subsystems.superstructure.Algae.AlgaeIntakeSRX;
import frc.robot.subsystems.superstructure.Algae.AlgaeIO.Algaestate;
import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.vision.LimelightIO;
import frc.robot.subsystems.vision.PhotonCameraIO;
import frc.robot.subsystems.vision.SimulatedCameraIO;
import frc.robot.subsystems.vision.VisionSub;

public class RobotContainer {

  //dashboard choosers
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<Boolean> testChooser;
    
    //Subsystems
      protected ControlSystemManager controlSystemManager;

      @Logged
      protected DriveControlSystem mDrive;
      protected VisionSub m_Vision;
      protected AlgaeIntakeSRX m_AlgaeIntake;

    //Odometry
      @Logged
      protected Odometry odometryThread;
    
    //Controllers
      protected ControllerManager mControllerManager;
      private CollisionDetection collisionFeedback;

    //Drive Command
      private DriveCommand driveCommand;

    //Alerts
      private Alert RobotStatus;

    
    public RobotContainer() {

    // Initialize stopping alert
    RobotStatus = new Alert("ROBOT IS STOPPED", AlertType.kError);
    RobotStatus.set(false);

    // Initialize feedback
    mControllerManager = ControllerManager.getInstance();
    collisionFeedback = new CollisionDetection();


          // Initialize subsystems based on robot mode
          switch (Constants.getRobot()) {
            case SIM_ROBOT:
                mDrive = new DriveControlSystem();
            //  m_Superstructure = null;
                m_Vision = new VisionSub(new SimulatedCameraIO("SimCAMERA1", Constants.VisionConstants.CameraToRobot));

                configureBindings(RobotType.SIM_ROBOT);
              break;
            case REAL_ROBOT:
                mDrive = new DriveControlSystem();
                driveCommand = new DriveCommand(
                  mControllerManager::getX,
                  mControllerManager::getY,
                  mControllerManager::getRightX, mDrive);

            //  m_Superstructure = new Superstructure(new AlgaeIntakeSRX());
                m_AlgaeIntake = new AlgaeIntakeSRX();
                /* 
                
                m_Vision = new VisionSub(
                  new LimelightIO("limelight", Constants.VisionConstants.CameraToRobot),
                  new PhotonCameraIO("photon")
                );
                */

                configureBindings(RobotType.REAL_ROBOT);
              break;
            case TUNING_ROBOT:
                mDrive = new DriveControlSystem();
                //m_Superstructure = null;
                m_Vision = new VisionSub();

                configureBindings(RobotType.TUNING_ROBOT);
              break;
          }

    // Initialize control system manager
    controlSystemManager = ControlSystemManager.getInstance().addControlSystem(mDrive);

    // Initialize odometry
    odometryThread = new Odometry(mDrive, m_Vision);

    // Register commands for Pathplanner
    registerCommands();

    // Setup auto chooser
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    // Setup test chooser
    testChooser = ControlSystemManager.buildTestChooser();
    SmartDashboard.putData("Test Chooser", testChooser);
        
    }
        // controller bindings here
        private void configureBindings(RobotType robotMode) {

          // setup controls depending on robot type 
            switch (robotMode) {
            case SIM_ROBOT:
              configureDriveBindings(true);
              configureGeneralBindings(true);
              break;

          //disable if any subsystem is unavailable
            case REAL_ROBOT:
              configureDriveBindings(true);
              configureVisionBindings(true);
              configureAlgaeBindings(true);
              configureGeneralBindings(true);
              break;
            
            case TUNING_ROBOT:
              // setup SysId bindings
              // phoenix logger
              /*
              mControllerManager.LeftBumper().onTrue(Commands.runOnce(SignalLogger::start));
              mControllerManager.RightBumper().onTrue(Commands.runOnce(SignalLogger::stop));

              // run sysid routines in this order
              mControllerManager.PovUp().whileTrue(mDrive.sysIdQuasistatic(Direction.kForward));
              mControllerManager.PovDown().whileTrue(mDrive.sysIdQuasistatic(Direction.kReverse));
              mControllerManager.Y().whileTrue(mDrive.sysIdDynamic(Direction.kForward));
              mControllerManager.A().whileTrue(mDrive.sysIdDynamic(Direction.kReverse));
              */
              break;
            }
        }

    // configure drive bindings
      private void configureDriveBindings(boolean enabled) {
            if (!enabled) return;
          //Feedback for controllers

            mDrive.setDefaultCommand(driveCommand);

            mControllerManager
            .getRightTrigger()
            .onTrue(driveCommand);

            mControllerManager
            .getOverride()
            .whileTrue(new InstantCommand(() -> Stop()))
            .onFalse(new InstantCommand(() -> RobotStatus.set(false)));
          }
    // configure vision bindings
      private void configureVisionBindings(boolean enabled) {
            if (!enabled) return;
            Trigger feedback = new Trigger(collisionFeedback.getDetection);

            mDrive.setDefaultCommand(
            driveCommand.withRotation(mControllerManager::getRightX)
            );

            mControllerManager
            .getRightTrigger()
            .onTrue(driveCommand);

            feedback
            .onTrue(mControllerManager.setDriverRumble(true))
            .onFalse(mControllerManager.setDriverRumble(false));
          }
    // configure algae bindings
      private void configureAlgaeBindings(boolean enabled) {
            if (!enabled) return;

            mControllerManager
            .getAlgaeIn()
            .onTrue(new InstantCommand(() -> m_AlgaeIntake.setState(Algaestate.INTAKE)))
            .onFalse(new InstantCommand(() -> m_AlgaeIntake.setState(Algaestate.IDLE)));

            mControllerManager
            .getAlgaeOut()
            .onTrue(new InstantCommand(() -> m_AlgaeIntake.setState(Algaestate.OUTTAKE)))
            .onFalse(new InstantCommand(() -> m_AlgaeIntake.setState(Algaestate.IDLE)));
          }
    // configure general bindings
      private void configureGeneralBindings(boolean enabled) {
            if (!enabled) return;

            // Add any general bindings that apply to all robot types here
          }

    // Named commands for Pathplanner
      private void registerCommands() {
                      
              // Pathplanner example to register commands for GUI usage
              NamedCommands.registerCommand("Score Algae", new PrintCommand("SCORED ALGAE"));
              NamedCommands.registerCommand("Pickup Algae", new PrintCommand("PICKED UP ALGAE"));   
          }

      // stops and resets all control systems
      public void Stop(){
        controlSystemManager.stopAll();
        RobotStatus.set(true);
      }

      // get auto command
      public Command getAutonomousCommand(){
        return autoChooser.getSelected();
      }

      // publish to dashboard
      public void publishToDashboard(){
        SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());
        SmartDashboard.putString("ALGAE STATE", m_AlgaeIntake.getState());
             
      }


       // configure tests for each control system
       public void configureTests(){

        var testTab = Shuffleboard.getTab("Tests");
        
        // Check if the test control chooser already exists before adding it
        if (testTab.getComponents().stream().noneMatch(component -> component.getTitle().equals("Test Control Systems"))) {
          {
            testTab.add("Test Control Systems", testChooser);
          }

            // Check if the SmartDashboard entry already exists before adding it
          if (!SmartDashboard.containsKey("TEST RESULT:")) {
            SmartDashboard.putBoolean("TEST RESULT:", testChooser.getSelected());
          }
        } 
      }
  
}
