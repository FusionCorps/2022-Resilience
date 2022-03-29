package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.Shooter.*;

public class Shooter extends SubsystemBase {

    WPI_TalonFX shooter0;

    public double shootK = 1.0;

    public double target;
    public double target_velocity;

    public double min_vel;
    public double max_vel;

    private ShuffleboardTab tab = Shuffleboard.getTab("General");
    public NetworkTableEntry shootKTab =
            tab.add("shootK", 1.0)
                    .getEntry();
    public NetworkTableEntry shootFPIDTab =
            tab.add("shootF PID", SHOOTER_kF)
                    .getEntry();

    public Shooter() {
        shooter0 = new WPI_TalonFX(Constants.Shooter.SHOOTER_ID);
        shooter0.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        shooter0.config_kF(0, SHOOTER_kF);
        shooter0.config_kP(0, SHOOTER_kP);
        shooter0.config_kI(0, SHOOTER_kI);
        shooter0.config_kD(0, SHOOTER_kD);

        shooter0.setInverted(TalonFXInvertType.Clockwise);

        target = Constants.Shooter.SHOOTER_TARGET;
        target_velocity = Constants.Shooter.SHOOTER_TARGET * 21000;


        shootK = 0.985;
        shootKTab.setDouble(shootK);

        min_vel = Constants.Shooter.SHOOTER_LOWER_VEL;
        max_vel = Constants.Shooter.SHOOTER_UPPER_VEL;

    }

    public void setShooter(double pct) {
        shooter0.set(pct);
    }

    public void setShooterVelocity(double vel) {
        shooter0.set(TalonFXControlMode.Velocity, vel);
    }

    public void setShooter_kF(double kf) {
        shooter0.config_kF(0, kf);
    }

    public double getShooterVel() {
        return shooter0.getSelectedSensorVelocity();
    }

    public boolean isTarget() {
        double v = shooter0.getSelectedSensorVelocity();
        return (v > min_vel && v < max_vel);
    }

    public boolean isShooting() {
        double v = shooter0.getSelectedSensorVelocity();
        return (v > (target*20000 - 2000) && v < (target*20000 + 2000));
    }

}
