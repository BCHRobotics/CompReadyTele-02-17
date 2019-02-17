/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static final String nearRocketRight = "nearRocketRight";
  private static final String nearRocketRightCowabunga = "nearRocketRightCowabunga";
  private static final String middleCargoStraight = "middleCargoStraight";
  private static final String rightMiddleCargo = "rightMiddleCargo";
  private static final String testAuto = "testAuto";

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static Drivetrain m_drivetrain = null;
  public static OI m_oi = null;
  public static Grabber m_grabber = null;
  public static Lift m_lift = null;
  public static Auto m_auto = null;
  public static Climber m_climber = null;

  //Gyro
  public ADXRS450_Gyro m_gyro = null;
  //public AHRS ahrs = null;

  public boolean funModeClimb = false;

  public double y = 0;           //variable for forward/backward movement
	public double x = 0;           //variable for side to side movement
	public double turn = 0;        //variable for turning movement
	public double deadzone = 0.07;	//variable for amount of deadzone
  
  public double pidTime = 0;

  public double lastTimeAuto = 0;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    CameraServer.getInstance().startAutomaticCapture();

    SmartDashboard.putNumber("ERROR", 454);
    SmartDashboard.putNumber("AUTO", 666);
    SmartDashboard.putNumber("ClimberAvg", 420);
    SmartDashboard.putNumber("EncoderLeg", 420);
    
    SmartDashboard.putNumber("EncoderFR", 6969);
    SmartDashboard.putNumber("EncoderBR", 6969);
    SmartDashboard.putNumber("EncoderFL", 6969);
    SmartDashboard.putNumber("EncoderBL", 6969);
    SmartDashboard.putNumber("EncoderAvg", 6969);

    SmartDashboard.putNumber("EncoderFRlast", 2222);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    SmartDashboard.putNumber("NAVX", 4546);

    m_drivetrain = new Drivetrain();
    m_oi = new OI();
    m_grabber = new Grabber();
    m_lift = new Lift();   
    m_gyro = new ADXRS450_Gyro();
    m_auto = new Auto();
    m_climber = new Climber();

    //ahrs = new AHRS(SerialPort.Port.kUSB);

    //m_gyro.reset();
    //m_gyro.calibrate();

    m_drivetrain.resetEncoders();
    m_climber.resetEncoderLEG();
    m_climber.resetEncoders();

    /*
    m_drivetrain.encoderBLlast = 0;
    m_drivetrain.encoderBRlast = 0;
    m_drivetrain.encoderFLlast = 0;
    m_drivetrain.encoderFRlast = 0;
    */

    m_auto.resetGyro();

    m_grabber.compressorOn();
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    SmartDashboard.putNumber("ClimberAvg", m_climber.getEncoderAvg());
    SmartDashboard.putNumber("EncoderLeg", m_climber.getEncoderLEG());

    SmartDashboard.putBoolean("CLIMBING", funModeClimb);

    SmartDashboard.putNumber("Nav Gyro", m_auto.getGyro());
    SmartDashboard.putNumber("EncoderFR", m_drivetrain.getEncoderFR());
    SmartDashboard.putNumber("EncoderBR", m_drivetrain.getEncoderBR());
    SmartDashboard.putNumber("EncoderFL", m_drivetrain.getEncoderFL());
    SmartDashboard.putNumber("EncoderBL", m_drivetrain.getEncoderBL());
    SmartDashboard.putNumber("EncoderAvg", m_drivetrain.getEncoderAvg());

    SmartDashboard.putNumber("EncoderFRlast", m_drivetrain.encoderFRlast);

  }

  @Override
  public void autonomousInit() {

    m_drivetrain.resetEncoders();
    lastTimeAuto = 0;
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

   m_auto.resetGyro();

   m_drivetrain.resetEncoders();
   m_grabber.compressorOn();
   m_climber.resetEncoderLEG();
   m_climber.resetEncoders();
  }

  @Override
  public void autonomousPeriodic() {

    m_drivetrain.setRamp(1.78);

    //m_auto.turn(setpoint, speed, timeout);
    //m_auto.turnDrive(GYROsetpoint, ENCODERsetpoint, GYROspeed, ENCODERspeed, timeout);

    double theSet = 0;

    String autoChoice = "testAuto";
      
    switch (autoChoice) {
    case nearRocketRight:
    m_auto.turnDrive(0, 60, 0.2, 0.01, 3);
    m_auto.turn(55, 0.2, 3);
    m_auto.turnDrive(55, 107, 0.2, 0.01, 3);
    m_auto.turn(29, 0.2, 100);
    break;

    case nearRocketRightCowabunga:
      while(theSet < 1){

        m_lift.MoveLift(-0.5);

        theSet = theSet + 0.0000025;

      }

      m_lift.MoveLift(-0.2);

      m_auto.turnDrive(0, 112, 0.2, 0.01, 5);
      m_auto.turn(55, 0.2, 3);
      m_auto.turnDrive(55, 107, 0.2, 0.01, 3);
      m_auto.turn(29, 0.2, 100);
    break;

    case middleCargoStraight:
      m_auto.turnDrive(0, 125, 0.2, 0.01, 10);
    break;

    case rightMiddleCargo:
      m_auto.turnDrive(0, 60, 0.2, 0.01, 3);
      m_auto.turn(25, 0.2, 3);
      m_auto.turnDrive(25, 156, 0.2, 0.01, 5);
      m_auto.turn(-90, 0.2, 100);
    break;
    case testAuto:
      m_auto.strafe(0, 72, 0.2, 0.05, 100);
    break;

    }

  }

  //Reset gyro when the robot is disabled
  @Override
  public void disabledInit(){
    m_gyro.reset();
    m_auto.resetGyro();
    
    m_drivetrain.resetEncoders();
    lastTimeAuto = 0;

    m_grabber.compressorOff();
    m_climber.resetEncoderLEG();
    m_climber.resetEncoders();
  }

  
  //Reset gyro when the robot is entering teleop
  @Override
  public void teleopInit() {
    super.teleopInit();
    //m_gyro.reset();
    m_auto.resetGyro();

    m_drivetrain.resetEncoders();
    m_grabber.compressorOn();
    m_climber.resetEncoderLEG();
    m_climber.resetEncoders();
  }

  @Override
  public void teleopPeriodic() {

    //FUNSTICK CONTROL

    if(funModeClimb == true){

      if(Robot.m_oi.ButtonLayoutSwitch.get() == true){
        funModeClimb = false;
        Timer.delay(0.25);
      }

      //CLIMB FUNC MECH

      if(Robot.m_oi.ButtonSeqClimbOut.get() == true){
        //Do Seq Climb Out
      }

      if(Robot.m_oi.ButtonSeqClimbIn.get() == true){
        //Do Seq Climb In
      }

      if(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_PegLeg) > deadzone || Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_PegLeg) < -deadzone) {
        Robot.m_climber.legExtend(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_PegLeg));
      } else {
        Robot.m_climber.legExtend(0);
      }

      if(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_Arms) > deadzone || Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_Arms) < -deadzone) {
        Robot.m_climber.armsMove(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_Arms));
      } else {
        Robot.m_climber.armsMove(0);
      }


      double pegLegSpeed = Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_PegLegBackward) + -Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_PegLegForward);

      Robot.m_climber.legWheel(pegLegSpeed);

    } else if(funModeClimb == false) {

      if(Robot.m_oi.ButtonLayoutSwitch.get() == true){
        funModeClimb = true;
        Timer.delay(0.25);
      }

      //REG FUNC MECH

      if(Robot.m_oi.ButtonGrabOpen.get() == true){
        Robot.m_grabber.grabberOpen();
      } else if(Robot.m_oi.ButtonGrabClose.get() == true){
        Robot.m_grabber.grabberClose();
      } 

      if(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_LIFT) > deadzone || Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_LIFT) < -deadzone) {
        Robot.m_lift.MoveLift(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_LIFT));
      } else {
        Robot.m_lift.MoveLift(-0.21);
      }

      m_grabber.tilt(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_Scissor)*0.8);

      double extendSpeed = -Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_TiltDown) + Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_TiltUp);
      m_grabber.extend(extendSpeed*-0.85);

    }

    double turboSpeed = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_TURBO)*0.3 + 0.7;
    double snailSpeed = -Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_SNAIL)*0.3 + 0.7;

    double moveSpeed = turboSpeed + snailSpeed;
    


  if(Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEY) > deadzone || Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEY) < -deadzone) {
     y = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEY);
  } else {
    y = 0;
  }

  if(Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEX) > deadzone || Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEX) < -deadzone) {
    x = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEX);
  } else {
    x = 0;
  }

  if(Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_ROTATE) > deadzone || Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_ROTATE) < -deadzone){
    turn = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_ROTATE);
  } else {
    turn = 0;
  }
    
  // m_drivetrain.mecanumDrive(y, x, turn, ahrs.getAngle(), moveSpeed);

  m_drivetrain.mecanumDrive(y, x, turn, 0, moveSpeed);
   
  if(Robot.m_oi.ButtonDriveGrabOpen.get() == true){
    Robot.m_grabber.grabberOpen();
  } else if(Robot.m_oi.ButtonDriveGrabClose.get() == true){
    Robot.m_grabber.grabberClose();
  } 



    }

  @Override
  public void testPeriodic() {

  }
}



