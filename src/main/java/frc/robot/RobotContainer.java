// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.team5430.control.CollisionDetection;
import com.team5430.control.ControllerManager;
import com.team5430.util.booleans;
import com.team5430.control.ControlSystemManager;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.superstructure.Superstructure;
import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.vision.SimulatedCameraIO;
import frc.robot.subsystems.vision.VisionSub;

public class RobotContainer {

  //dashboard chooser
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<Boolean> testChooser;
    
      // init subsystems
    
      @Logged
      protected DriveControlSystem mDrive;
      protected VisionSub m_Vision;
      protected Superstructure m_Superstructure;

      protected ControlSystemManager controlSystemManager;

      @Logged
      protected Odometry odometryThread;
      
      protected ControllerManager mControllerManager;
      private CollisionDetection collisionFeedback;

      private DriveCommand driveCommand;

    
    public RobotContainer() {
    //init  
        //init subsystems
        mDrive = DriveControlSystem.getInstance();
        m_Superstructure = null;
        m_Vision = new VisionSub(new SimulatedCameraIO("SimCAMERA1", new Transform3d(0, 0, .2, new Rotation3d(0, Math.toRadians(-15), 0))));

        controlSystemManager = ControlSystemManager.getInstance().addControlSystem(mDrive);

        //init feedback
        mControllerManager = ControllerManager.getInstance();
        collisionFeedback = CollisionDetection.getInstance();

        odometryThread = new Odometry(mDrive, m_Vision);
      
      
    
    
        //Pathplanner example to register commands for gui usage
        NamedCommands.registerCommand("Score Algae", new PrintCommand("SCORED ALGAE"));
    
        
        //setup autochooser
        autoChooser = AutoBuilder.buildAutoChooser();
            SmartDashboard.putData("Auto Chooser", autoChooser);

        //setup test chooser
        testChooser = ControlSystemManager.buildTestChooser();
            SmartDashboard.putData("Test Chooser", testChooser);

        //default commands
        driveCommand = new DriveCommand(mControllerManager::getX, mControllerManager::getY, mControllerManager::getRotation, mDrive);

        configureBindings();
        
        
      }
        // controller bindings here
        private void configureBindings() {
          Trigger feedback = collisionFeedback.DetectionTrigger();

          // setup controls depending on robot type 
          switch (booleans.getRobot()) {
            case SIM_ROBOT:
              configureSimRobotBindings();
              break;
            case REAL_ROBOT:
              configureRealRobotBindings(feedback);
              break;
            case TUNING_ROBOT:
              configureTuningRobotBindings();
              break;
          }
        }

        private void configureSimRobotBindings() {
          // setup drive
          mDrive.setDefaultCommand(driveCommand);

          mControllerManager
            .quickTrigger()
            .and(m_Vision.TagInRange)
            .whileTrue(
              driveCommand
                .withX(m_Vision::proportionalRange)
                .withRotation(m_Vision::proportionalAim));
        }

        private void configureRealRobotBindings(Trigger feedback) {
          // setup drive
          mDrive.setDefaultCommand(
            driveCommand
              .withRotation(mControllerManager::getRightX));

          // Auto aim and direct towards april tag in sight
          // TODO: test -> NOTE: overrides normal drive control !!!
          mControllerManager
            .A()
            .and(m_Vision.TagInRange)
            .onTrue(
              driveCommand
                .withX(m_Vision::proportionalRange)
                .withRotation(m_Vision::proportionalAim));

          // rumble driver whenever there is a hard collision
          feedback
            .onTrue(new InstantCommand(mControllerManager::setRumbleOn))
            .onFalse(new InstantCommand(mControllerManager::setRumbleOff));
        }

        private void configureTuningRobotBindings() {
          // setup SysId bindings
          // phoenix logger
          mControllerManager.LeftBumper().onTrue(Commands.runOnce(SignalLogger::start));
          mControllerManager.RightBumper().onTrue(Commands.runOnce(SignalLogger::stop));

          // run sysid routines in this order
          mControllerManager.PovUp().whileTrue(mDrive.sysIdQuasistatic(Direction.kForward));
          mControllerManager.PovDown().whileTrue(mDrive.sysIdQuasistatic(Direction.kReverse));
          mControllerManager.Y().whileTrue(mDrive.sysIdDynamic(Direction.kForward));
          mControllerManager.A().whileTrue(mDrive.sysIdDynamic(Direction.kReverse));
        }
    
          
        // use for any object detection when doing camera work?
        //new Trigger(() -> m_Drive.getPose().getX() > 10).onTrue(new PrintCommand("tracking"));
      
    
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

      // stops and resets all control systems
      public void Stop(){
        controlSystemManager.stopAll();
      }

      // get auto command
      public Command getAutonomousCommand(){
        return autoChooser.getSelected();
      }

  
}
