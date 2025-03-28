// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.commands.FollowPathCommand;

import edu.wpi.first.net.PortForwarder;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class Robot extends TimedRobot {

  private RobotContainer m_robotContainer;

  public Robot() {

  m_robotContainer = new RobotContainer();

  //update odometry in the background
  addPeriodic(m_robotContainer.odometryThread::updateOdometry, .02);

//try it out
// Configure Epilogue logging
    // Epilogue.configure(config -> {
    //   // Log only to disk, instead of the default NetworkTables logging

    //   // Note that this means data cannot be analyzed in realtime by a dashboard
      

    //   switch (getRuntimeType()) {

    //   case kSimulation:
    //     // If running in simulation, then we'd want to re-throw any errors that
    //     // occur so we can debug and fix them!
      
    //     config.errorHandler = ErrorHandler.crashOnError();
    //     config.minimumImportance = Logged.Importance.DEBUG;
    //     break;

    //   case kRoboRIO:
    //     // Only log critical information instead of the default DEBUG level.
    //     // This can be helpful in a pinch to reduce network bandwidth or log file size
    //     // while still logging important information
    //     config.backend = new FileBackend(DataLogManager.getLog());
    //     config.minimumImportance = Logged.Importance.CRITICAL;
    //     break;
    //     default:

    //       break;
  
    //   }
    // });



  // Set up logging
  //Epilogue.bind(this);
  
  }

  @Override
  public void robotInit() 
  {
  // Make sure only configure port forwarding once in your robot code.
  //Don not place these function calls in any periodic functions

  for (int port = 5800; port <= 5810; port++) {
  PortForwarder.add(port, "limelight-driver.local", port);
   }
    
    FollowPathCommand.warmupCommand().schedule();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
     
     var m_Auto = m_robotContainer.getAutonomousCommand();

    m_Auto.schedule();
  
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
    m_robotContainer.configureTests();
  }

  @Override
  public void simulationInit() {}

  @Override 
  public void simulationPeriodic(){
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {
    m_robotContainer.Stop();
  }
}
