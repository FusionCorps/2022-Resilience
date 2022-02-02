package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.Climb.*;

public class Climb extends SubsystemBase {

    WPI_TalonFX climb0;
    WPI_TalonFX climb1;

    public Climb() {
        climb0 = new WPI_TalonFX(Constants.Climb.CLIMB_0_ID);
        climb1 = new WPI_TalonFX(Constants.Climb.CLIMB_1_ID);

        climb1.follow(climb0);
        climb1.setInverted(InvertType.FollowMaster);

        climb0.setSelectedSensorPosition(0);

        climb0.config_kF(0, CLIMB_kF);
        climb0.config_kP(0, CLIMB_kP);
        climb0.config_kI(0, CLIMB_kI);
        climb0.config_kD(0, CLIMB_kD);

    }

    public void setClimb(double pct) {
        climb0.set(pct);
    }

    public void setClimbPos(double pos) {
        climb0.set(TalonFXControlMode.Position, pos);
    }

}
