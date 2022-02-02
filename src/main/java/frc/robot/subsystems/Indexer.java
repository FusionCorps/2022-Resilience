package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.Indexer.*;

public class Indexer extends SubsystemBase {

    WPI_TalonFX indexer0;
    public DigitalInput break_beam;

    public Indexer() {
        indexer0 = new WPI_TalonFX(INDEXER_ID);
        break_beam = new DigitalInput(IR_PORT);
    }

    public void setIndexer(double pct) {
        indexer0.set(pct);
    }

}
