// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.xrp.XRPRangefinder;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This autonomous routine maintains a certain distance to an object in front of the robot.
 * If the robot is too far, it will drive forward, and if it is too close, it will drive backward.
 */
public class AutonomousFollow extends SequentialCommandGroup {
  
  private static final double SPEED = 0.75;
  private static final double FOLLOW_DISTANCE = 15;

  public AutonomousFollow(Drivetrain drivetrain, XRPRangefinder rangefinder) {
    addCommands(
      Commands.repeatingSequence(
        // Drive forward while far from the object
        new DriveSpeed(drivetrain, SPEED).onlyWhile(() -> rangefinder.getDistanceInches() > FOLLOW_DISTANCE + 1),
        // Drive backward while close to the object
        new DriveSpeed(drivetrain, -SPEED).onlyWhile(() -> rangefinder.getDistanceInches() < FOLLOW_DISTANCE)
      )
    );
  }

}
