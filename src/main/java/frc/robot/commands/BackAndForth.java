// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This command repeatedly drives forward and backward.
 */
public class BackAndForth extends SequentialCommandGroup {

    private static final double SPEED = 1;
    private static final double DISTANCE = 10;

    public BackAndForth(Drivetrain drivetrain) {
        addCommands(
            Commands.repeatingSequence(
                new DriveDistance(drivetrain, SPEED, DISTANCE),
                new DriveDistance(drivetrain, -SPEED, DISTANCE)
            )
        );
    }

}

