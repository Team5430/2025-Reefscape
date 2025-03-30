// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.team5430.control.ControllerManager;
import com.team5430.control.ControlSystemManager;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.superstructure.Algae.AlgaeIntakeSRX;
import frc.robot.subsystems.drive.DriveControlSystem;

public class RobotContainer {

  //dashboard choosers
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<Boolean> testChooser;
    
    //Subsystems
      protected ControlSystemManager controlSystemManager;

      protected DriveControlSystem mDrive;
      protected AlgaeIntakeSRX m_AlgaeIntake;

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
                driveCommand = new DriveCommand
                (mControllerManager::getX,
                mControllerManager::getY,
                mControllerManager::getTwist,
                mControllerManager::getZ,
                mDrive);

                m_AlgaeIntake = new AlgaeIntakeSRX();
                
                // Initialize odometry
                odometryThread = new Odometry(mDrive);
                
              //controller bindings based on subsystem
                DriveBindings(true);
                TuningBindings(false);
                CoralBindings(false);
                ElevatorBindings(true);
                AlgaeBindings(true);

    
      // Initialize control system manager
      controlSystemManager = ControlSystemManager.getInstance().addControlSystem(mDrive);

      

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



        mControllerManager
        .getTrigger()
        .onTrue(odometryThread.autoProcessor());

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
          .onFalse(m_AlgaeIntake.HOLD());
          
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
