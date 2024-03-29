package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.Climb.*;

public class Climb extends SubsystemBase {

    // one set of arms is actuated, everything else is passive

    // two synced motors
    WPI_TalonFX climb0;
    WPI_TalonFX climb1;

    // servo toggle var
    public boolean isClosed = true;

    // synced servos
    public Servo servo_l;
    public Servo servo_r;

    // climb toggle var
    public boolean isPause = true;

    public Climb() {
        // climb motor configs
        climb0 = new WPI_TalonFX(Constants.Climb.CLIMB_0_ID);
        climb1 = new WPI_TalonFX(Constants.Climb.CLIMB_1_ID);

        climb0.setInverted(TalonFXInvertType.Clockwise);

        SupplyCurrentLimitConfiguration limits =
            new SupplyCurrentLimitConfiguration(true, 30., 30, 0.25);

        climb0.configSupplyCurrentLimit(limits);
        climb1.configSupplyCurrentLimit(limits);

        // sync
        climb1.follow(climb0);
        climb1.setInverted(InvertType.FollowMaster);

        climb0.setNeutralMode(NeutralMode.Brake);
        climb1.setNeutralMode(NeutralMode.Brake);

        climb0.setSelectedSensorPosition(0);

        climb0.config_kF(0, CLIMB_kF);
        climb0.config_kP(0, CLIMB_kP);
        climb0.config_kI(0, CLIMB_kI);
        climb0.config_kD(0, CLIMB_kD);

        // servo setup
        servo_l = new Servo(SERVO_L_ID);
        servo_r = new Servo(SERVO_R_ID);

        servo_l.setAngle(52);
        servo_r.setAngle(130);

    }

    public void setClimb(double pct) {
        climb0.set(pct);
    }

    // set both servo angles, used to tune
    public void setServoAngles(double left, double right) {
        servo_l.setAngle(left);
        servo_r.setAngle(right);
    }

    public void setServoFree() {
        servo_l.setDisabled();
        servo_r.setDisabled();
    }

    public int getClimbPosKey() {

        // get a integer key for the climb state
        // might be better to handle with Strings to increase readability

        int key;

        // climb below min limit
        if (climb0.getSelectedSensorPosition() < CLIMB_MIN_POS) {
            key = 0;
        // climb between min + lower
        } else if (climb0.getSelectedSensorPosition() < CLIMB_LOWER_POS) {
            key = 1;
        // middle/default position
        } else if (climb0.getSelectedSensorPosition() < CLIMB_UPPER_POS) {
            key = 2;
        // upper pos
        } else if (climb0.getSelectedSensorPosition() < CLIMB_MAX_POS){
            key = 3;
        // above max
        } else {
            key = 4;
        }

        return key;
    }

    // get and set on encoder w/ PID
    public double getClimbPos() {
        return climb0.getSelectedSensorPosition();
    }

    public void setClimbPos(double pos) {
        climb0.set(TalonFXControlMode.Position, pos);
    }

}
