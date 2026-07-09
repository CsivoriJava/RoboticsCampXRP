// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This command turns the robot at a given speed until it has rotated a given number of degrees.
 */
public class TurnRandom extends Command {
  private final Drivetrain m_drivetrain;
  private double m_degrees;
  private double m_speed;

  public TurnRandom(Drivetrain drivetrain, double speed) {
    m_drivetrain = drivetrain;
    m_speed = speed;
    addRequirements(drivetrain);
}


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Set motors to stop, read encoder values for starting point
    m_drivetrain.arcadeDrive(0, 0);
    m_drivetrain.resetEncoders();
    m_degrees = (Math.random() * 180) - 90;
    if (m_degrees < 0) {
    m_degrees = -m_degrees;
    m_speed = -m_speed;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drivetrain.arcadeDrive(0, m_speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    /* Need to convert distance travelled to degrees. The Standard
       XRP Chassis found here, https://www.sparkfun.com/products/22230,
       has a wheel placement diameter (163 mm) - width of the wheel (8 mm) = 155 mm
       or 6.102 inches. We then take into consideration the width of the tires.
    */
    double inchPerDegree = Math.PI * 6.102 / 360;
    // Compare distance travelled from start to distance based on degree turn
    return getAverageTurningDistance() >= (inchPerDegree * m_degrees);
  }

  private double getAverageTurningDistance() {
    double leftDistance = Math.abs(m_drivetrain.getLeftDistanceInch());
    double rightDistance = Math.abs(m_drivetrain.getRightDistanceInch());
    return (leftDistance + rightDistance) / 2.0;
  }
}
