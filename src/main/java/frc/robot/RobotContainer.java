// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.team5430.control.ControllerManager;
import com.team5430.control.ControlSystemManager;

import edu.wpi.first.epilogue.Logged;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.superstructure.Algae.AlgaeIntakeSRX;
import frc.robot.subsystems.superstructure.Coral.CoralIntakeFX;
import frc.robot.subsystems.superstructure.Elevator.ElevatorFX;
import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.vision.LimelightIO;
import frc.robot.subsystems.vision.SimulatedCameraIO;
import frc.robot.subsystems.vision.VisionSub;

public class RobotContainer {

  //dashboard choosers
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<Boolean> testChooser;
    
    //Subsystems
      protected ControlSystemManager controlSystemManager;

      protected DriveControlSystem mDrive;
      protected VisionSub m_Vision;
      protected AlgaeIntakeSRX m_AlgaeIntake;
      protected ElevatorFX m_Elevator;

    //Odometry
      protected Odometry odometryThread;
    
    //Controllers
      protected ControllerManager mControllerManager;

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

            //declare subsystems
                mDrive = new DriveControlSystem();
                
                //drive command
                driveCommand = new DriveCommand(mControllerManager::getX, mControllerManager::getY, mControllerManager::getTwist, mDrive);
             //   m_Superstructure = new Superstructure(new AlgaeIntakeSRX());
                m_AlgaeIntake = new AlgaeIntakeSRX();
                 
                //set vision setup based on robot mode
                m_Vision = Constants.RobotisReal().getAsBoolean() ? new VisionSub(
                  new LimelightIO(VisionConstants.CameraName, VisionConstants.CameraToRobot)) :
                  new VisionSub(new SimulatedCameraIO("Sim-camera1", VisionConstants.CameraToRobot));
             
              //controller bindings based on subsystem
                DriveBindings(true);
                TuningBindings(false);
                CoralBindings(true);
                ElevatorBindings(true);
                AlgaeBindings(true);

    
      // Initialize control system manager
      controlSystemManager = ControlSystemManager.getInstance().addControlSystem(mDrive);

      // Initialize odometry
      odometryThread = new Odometry(mDrive, m_Vision);

      //named commands for path planner
      configureNamedCommands();

      // Setup auto chooser
      autoChooser = AutoBuilder.buildAutoChooser();
      SmartDashboard.putData("Auto Chooser", autoChooser);

      // Setup test chooser
      testChooser = ControlSystemManager.buildTestChooser();
      SmartDashboard.putData("Test Chooser", testChooser);
          
    }
      
      private void DriveBindings(boolean enable){
        if(!enable) return;
        
        mDrive.setDefaultCommand(driveCommand);


      }

      private void TuningBindings(boolean enable){
        if(!enable) return;
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
      }

      private void AlgaeBindings(boolean enable){
        if(!enable) return;
              //temporary algae bindings
              mControllerManager
          .getAlgaeIn()
          .onTrue(m_AlgaeIntake.INTAKE())
          .onFalse(m_AlgaeIntake.IDLE());
          
              mControllerManager
          .getAlgaeOut()
          .onTrue(m_AlgaeIntake.OUTTAKE())
          .onFalse(m_AlgaeIntake.IDLE());

      }

      private void ElevatorBindings(boolean enable){
        if(!enable) return;


      }

      private void CoralBindings(boolean enable){
        if(!enable) return;
      }
      
      private void  configureNamedCommands(){
                // Pathplanner example to register commands for GUI usage
        NamedCommands.registerCommand("Score Algae", new PrintCommand("SCORED ALGAE"));
        NamedCommands.registerCommand("Pickup Algae", new PrintCommand("PICKED UP ALGAE"));


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

      // stops and resets all control systems
      public void Stop(){
        controlSystemManager.stopAll();
        RobotStatus.set(true);
      }

      // get auto command
      public Command getAutonomousCommand(){
        return autoChooser.getSelected();
      }

  
}
