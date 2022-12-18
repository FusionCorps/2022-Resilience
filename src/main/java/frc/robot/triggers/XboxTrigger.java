package frc.robot.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class XboxTrigger extends Trigger {

    // Special trigger to run shooter command from the Right Triggers
    // actually pretty useful, might use for other stuff in the future

    XboxController mController;
    int axis_val;
    double threshold;


    public XboxTrigger(XboxController controller, int axis, double thresh) {
        mController = controller;
        axis_val = axis;
        threshold = thresh;
    }

    @Override
    public boolean get() {
        return(mController.getRawAxis(axis_val) > threshold);
    }

}
