package frc.robot.modules;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import frc.robot.Constants;
import frc.robot.math.SwerveCalcs;

import static frc.robot.Constants.*;
import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class SwerveCombo {

    // class that defines one swerve module on the bot

    // 3 things: drive motor, steering motor, canCODER
    WPI_TalonFX axisMotor;
    WPI_TalonFX driveMotor;
    int mPosition;

    double absEncDeg;
    CANCoder coder;

    // Note: Phoenix Lib init knocks motors out of alignment
    // Wait until you see that on the console before running, else realign

    public SwerveCombo(WPI_TalonFX axisInit, WPI_TalonFX driveInit, CANCoder coderInit, int position) {

        new Constants();

        // get coder position on start up
        this.coder = coderInit;

        this.absEncDeg = coderInit.getAbsolutePosition();

        // configure axis motor on set up
        this.axisMotor = axisInit;
        this.axisMotor.setInverted(TalonFXInvertType.Clockwise);
        this.axisMotor.configNeutralDeadband(0.0001);
        this.axisMotor.configSelectedFeedbackSensor(FeedbackDevice.valueOf(1));

        // set encoder to match CanCoder on wakeup
        this.axisMotor.setSelectedSensorPosition(-(this.absEncDeg/360)*2048*STEERING_RATIO);
        this.axisMotor.config_kF(0, Constants.AXIS_kF);
        this.axisMotor.config_kP(0, Constants.AXIS_kP);
        this.axisMotor.config_kI(0, Constants.AXIS_kI);
        this.axisMotor.config_kD(0, Constants.AXIS_kD);
        this.axisMotor.setNeutralMode(NeutralMode.Coast);

        // initialize drive motor
        this.driveMotor = driveInit;
        this.driveMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        this.driveMotor.setInverted(TalonFXInvertType.CounterClockwise);
        this.driveMotor.configNeutralDeadband(0.01);
        this.driveMotor.setSelectedSensorPosition(0);
        this.driveMotor.config_kF(0, Constants.DRIVE_kF);
        this.driveMotor.config_kP(0, Constants.DRIVE_kP);
        this.driveMotor.config_kI(0, Constants.DRIVE_kI);
        this.driveMotor.config_kD(0, Constants.DRIVE_kD);
        this.driveMotor.setNeutralMode(NeutralMode.Coast);

        this.mPosition = position;

        // in hindsight, probably needed to current limit

    }


    public void passArgs(double speed, double angle) {

        // pass a speed and angle to a module
        new Constants();

        // get the current angle, as well a driveConstant and angleConstant to convert ticks to meters
        double encAngle = axisMotor.getSelectedSensorPosition()/STEERING_RATIO/2048*(2*PI);

        // thank you CTRE for making this per 100ms
        // I'm sure it was for a good reason at some point
        double driveConstant = 204.8/(2*PI)*DRIVING_RATIO/WHEEL_RADIUS_METERS;
        double angleConstant = 2048/(2*PI)*STEERING_RATIO;

        // convert speed to ticks / 100 ms
        speed *= driveConstant;

        // loop angle to 0-2PI
        double encTrue = encAngle%(2*PI);

        // get the difference in theta
        double dTheta = angle - encTrue;

        // logic to make the axle motion continuous
        // (thank you 2910)
        if (abs(-2*PI + dTheta) < abs(dTheta)) {
            if (abs(-2*PI + dTheta) < abs(2*PI + dTheta)) {
                dTheta = -2*PI + dTheta;
            } else {
                dTheta = 2*PI + dTheta;
            }
        } else if (abs(dTheta) > abs(2*PI + dTheta)) {
            dTheta = 2*PI + dTheta;
        }

        // convert to final position target
        double angleFinal = encAngle + dTheta;
        angleFinal *= angleConstant;

        // set some deadzones
        this.driveMotor.set(ControlMode.Velocity, speed);
        if (speed < 120) {
            this.axisMotor.set(ControlMode.Velocity, 0);
            this.driveMotor.set(ControlMode.Velocity, 0);
        // push values to PID controllers
        } else {
            this.axisMotor.set(ControlMode.Position, angleFinal);
            this.driveMotor.set(ControlMode.Velocity, speed);
        }
    }

    // resets encoders from CanCoder value
    public void zero() {
        absEncDeg = this.coder.getAbsolutePosition();
        this.axisMotor.setSelectedSensorPosition(-(absEncDeg/360)*2048*STEERING_RATIO);
        this.axisMotor.set(ControlMode.Velocity, 0);
    }




}
