package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.Indexer.*;

public class Indexer extends SubsystemBase {

    WPI_TalonFX indexer0;
    public DigitalInput break_beam;

    public Indexer() {
        indexer0 = new WPI_TalonFX(INDEXER_ID);
        break_beam = new DigitalInput(IR_PORT);
        indexer0.setNeutralMode(NeutralMode.Brake);

        indexer0.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        indexer0.setInverted(TalonFXInvertType.CounterClockwise);
        indexer0.configNeutralDeadband(0.01);
        indexer0.setSelectedSensorPosition(0);
        indexer0.config_kF(0, Constants.DRIVE_kF);
        indexer0.config_kP(0, Constants.DRIVE_kP);
        indexer0.config_kI(0, Constants.DRIVE_kI);
        indexer0.config_kD(0, Constants.DRIVE_kD);

    }

    public void configIndexer() {
        indexer0.setNeutralMode(NeutralMode.Brake);
    }

    public void setIndexer(double pct) {
        indexer0.set(pct);
    }

    public void setIndexerVel(double vel) {
        indexer0.set(TalonFXControlMode.Velocity, vel);
    }

}
