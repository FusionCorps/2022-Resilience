package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class TestCommand extends InstantCommand {

    // for debugging triggers

    @Override
    public void initialize() {
         System.out.println("Testing");
    }


}
