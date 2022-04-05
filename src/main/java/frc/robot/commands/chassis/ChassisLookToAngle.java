package frc.robot.commands.chassis;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.StrictMath.PI;

public class ChassisLookToAngle extends CommandBase {
// TODO: Make command that aligns wheels w/o movement overall

    Chassis mChassis;

    SlewRateLimiter turnLimit;

    boolean reset;

    double mFwdSpeed;
    double mStrSpeed;
    double mRotSpeed;
    double mTime;

    double mAngle;

    double tx;

    private Timer timer = new Timer();

    public ChassisLookToAngle(Chassis chassis, double angle, double time) {

        mChassis = chassis;

        mAngle = angle;

        mTime = time;

        turnLimit = new SlewRateLimiter(4.5);

        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        mChassis.solveAngles(-mFwdSpeed, -mStrSpeed, mRotSpeed);


    }

    @Override
    public void execute() {

        tx = -mChassis.ahrs.getAngle() + mAngle;


        if (abs(tx) <= 0.8) {
            tx = 0;
        }

        if (tx > 30) {
            tx = 30;
        } else if (tx < -30) {
            tx = -30;
        }




        mChassis.runSwerve(0,
                0,
                turnLimit.calculate(0.0135*tx + (tx/(abs(tx)+0.02))*0.006));

    }

    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mChassis.runSwerve(0.0, 0.0, 0.0);

    }

}

