// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.AutonomousDistance;
import frc.robot.commands.AutonomousFollow;
import frc.robot.commands.BackAndForth;
import frc.robot.commands.DriveForward;
import frc.robot.commands.FollowLine;
import frc.robot.commands.TurnDegrees;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.xrp.XRPRangefinder;
import edu.wpi.first.wpilibj.xrp.XRPReflectanceSensor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** The container for the robot. Contains subsystems and teleop controls. */
public class RobotContainer {

  // The robot's subsystems (drivetrain also contains the gyro and accelerometer sensors)
  private final Drivetrain drivetrain = new Drivetrain();
  private final Arm arm = new Arm();

  // External sensors plugged into the robot
  private final XRPRangefinder rangefinder = new XRPRangefinder();
  private final XRPReflectanceSensor lineFollower = new XRPReflectanceSensor();

  // The controller plugged into the computer
  private final CommandXboxController controller = new CommandXboxController(0);

  // SmartDashboard chooser for autonomous routines
  private final SendableChooser<Command> autonomousChooser = new SendableChooser<>();
  
  int servoAngle = 90;

  public RobotContainer() {
    // Configure the controller bindings for teleop
    configureTeleopBindings();

    // Configure autonomous routines
    configureAutonomousRoutines();
  }

  private void configureTeleopBindings() {
    // Default command when nothing else is happening
    drivetrain.setDefaultCommand(getArcadeDriveCommand());

    // Quickly rotate left or right with the bumper buttons
    controller.button(9).onTrue(new TurnDegrees(drivetrain, 1, 90));
    controller.button(10).onTrue(new TurnDegrees(drivetrain, 1, -90));

    // Hold the A button to follow a line
    controller.button(2).whileTrue(new FollowLine(drivetrain, lineFollower));

    // Hold the B button to ram
    controller.button(3).whileTrue(new BackAndForth(drivetrain));

    // Hold the X button to rotate the servo out
    controller.button(1)
      .onTrue(new InstantCommand(() -> {
        if (servoAngle == 90) {
          servoAngle = 180;
        }
        else {
          servoAngle = 90;
        }
        
      }));
      

    // Hold the Y button to drive straight forward
    controller.button(4).whileTrue(new DriveForward(drivetrain, -1));

    controller.button(5).or(controller.button(8))
      .onTrue(new InstantCommand(() -> drivetrain.leftMotorMultiplier = 0))
      .onFalse(new InstantCommand(() -> drivetrain.leftMotorMultiplier = 1));

    controller.button(6).or(controller.button(7))
      .onTrue(new InstantCommand(() -> drivetrain.rightMotorMultiplier = 0))
      .onFalse(new InstantCommand(() -> drivetrain.rightMotorMultiplier = 1));


  }
  
  private void configureAutonomousRoutines() {
    // Add all autonomous routines to the chooser so they can be selected from the dashboard
    autonomousChooser.setDefaultOption("Distance", new AutonomousDistance(drivetrain, rangefinder));
    autonomousChooser.addOption("Follow", new AutonomousFollow(drivetrain, rangefinder));
    autonomousChooser.addOption("Back and Forth", new BackAndForth(drivetrain));
    SmartDashboard.putData(autonomousChooser);
  }

  public void teleopPeriodic() {
    // This code toggles the positon of the Servo in accordance to how the DPad is pressed.
      int dPad = controller.getHID().getPOV();
      if (dPad == -1) {
        dPad = -45;
      }
      else {
        dPad = dPad/4;
      }

      // This code combines the DPad angle with the Servo angle.
      if (drivetrain.getCurrentCommand() instanceof BackAndForth) {
        arm.setAngle(68);
      }
      else{
        arm.setAngle(servoAngle-dPad);
      }
      SmartDashboard.putNumber("Servo Angle" , arm.getAngle());
      SmartDashboard.putNumber("dPad Angle" , dPad);
      SmartDashboard.putNumber("Servo Angle minus dPad" , servoAngle-dPad);
  }

  public void dashboardPeriodic() {
    // Update the dashboard
    SmartDashboard.putNumber("Rangefinder", rangefinder.getDistanceInches());
    SmartDashboard.putNumber("Line Follower Left", lineFollower.getLeftReflectanceValue());
    SmartDashboard.putNumber("Line Follower Right", lineFollower.getRightReflectanceValue());
    SmartDashboard.putNumber("Drivetrain Distance Traveled" , drivetrain.getAverageDistanceInch());
    SmartDashboard.putNumber("Gyro Angle" , drivetrain.getGyroAngleZ());
    SmartDashboard.putNumber("X-Axis Acceleration" , drivetrain.getAccelX());
    SmartDashboard.putNumber("Z-Axis Acceleration" , drivetrain.getAccelZ());
    SmartDashboard.putNumber("Y-Axis Acceleration" , drivetrain.getAccelY());

    double averageAccel = (drivetrain.getAccelX() + drivetrain.getAccelY() + drivetrain.getAccelZ()) / 3;
    SmartDashboard.putNumber("Average Acceleration" , averageAccel);
    
  }

  // Returns the autonomous routine selected on the dashboard (used in Robot.java)
  public Command getAutonomousCommand() {
    return autonomousChooser.getSelected();
  }

  // Returns the default driving command for teleop
  public Command getArcadeDriveCommand() {
    return new ArcadeDrive(drivetrain, controller);
  }
}
