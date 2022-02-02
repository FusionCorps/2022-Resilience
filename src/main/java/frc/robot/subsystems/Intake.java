package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

    private WPI_TalonFX intake0 = new WPI_TalonFX(Constants.INDEXER_ID);

    public Intake() {

    }

    public void runIntake(double pct) {
        intake0.set(pct);
    }

}
