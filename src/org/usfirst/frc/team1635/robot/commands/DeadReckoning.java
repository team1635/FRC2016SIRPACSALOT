package org.usfirst.frc.team1635.robot.commands;

import org.usfirst.frc.team1635.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DeadReckoning extends Command {
	double speed = 24.16667;
	double distance;

    public DeadReckoning(double dist) {
    	this.distance = dist;
    	double time = dist/speed;
    	setTimeout(time + 0.7);
    	// Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.unlimitedDrive(-0.3);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
