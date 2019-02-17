package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {

    //Init Driver and Functional Mech Joysticks
    public Joystick driveStick = new Joystick(RobotMap.OI_DRIVESTICK);
    public Joystick funStick = new Joystick(RobotMap.OI_FUNSTICK);
    public Joystick testStick = new Joystick(RobotMap.OI_TESTSTICK);

    //Set the buttons
    Button ButtonGrabOpen = new JoystickButton(funStick, RobotMap.OI_FUNSTICK_GrabOpen);
    Button ButtonGrabClose = new JoystickButton(funStick, RobotMap.OI_FUNSTICK_GrabClose);

    Button ButtonDriveGrabOpen = new JoystickButton(driveStick, RobotMap.OI_DRIVESTICK_GrabOpen);
    Button ButtonDriveGrabClose = new JoystickButton(driveStick, RobotMap.OI_DRIVESTICK_GrabClose);

    Button ButtonLayoutSwitch = new JoystickButton(funStick, RobotMap.OI_FUNSTICK_LayoutSwitch);
    Button ButtonSeqClimbOut = new JoystickButton(funStick, RobotMap.OI_FUNSTICK_SeqClimbOut);
    Button ButtonSeqClimbIn = new JoystickButton(funStick, RobotMap.OI_FUNSTICK_SeqClimbIn);

    Button ButtonToggle = new JoystickButton(funStick, 6);
    
    
}