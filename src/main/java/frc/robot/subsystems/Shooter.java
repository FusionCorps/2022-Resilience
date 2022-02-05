package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.Shooter.*;

public class Shooter extends SubsystemBase {

    WPI_TalonFX shooter0;

    public double target;

    public double min_vel;
    public double max_vel;

    public Shooter() {
        shooter0 = new WPI_TalonFX(SHOOTER_ID);
        shooter0.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        target = SHOOTER_TARGET;

        min_vel = SHOOTER_LOWER_VEL;
        max_vel = SHOOTER_UPPER_VEL;

    }

    public void setShooter(double pct) {
        shooter0.set(pct);
    }

    public double getShooterVel() {
        return shooter0.getSelectedSensorVelocity();
    }

    public boolean isTarget() {
        double v = shooter0.getSelectedSensorVelocity();
        return (v > min_vel && v < max_vel);
    }

}
