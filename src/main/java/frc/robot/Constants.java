package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Constants 
{

    public final static double kLooperDt = 0;
    
    // -------------------- LIFT --------------------
	public static int kLiftFalconId = 0;
	public static boolean kLiftFalconInverted = false;
	public static NeutralMode kLiftFalconNeutralMode = NeutralMode.Brake;
	public static FeedbackDevice kLiftFalconFeedbackDevice = FeedbackDevice.IntegratedSensor;
	public static boolean kLiftFalconReverse = false;
	public static int kLiftFalconControlID = 0;


	// -------------------- WINCH --------------------
	public static NeutralMode kWinchFalconNeutralMode;
	public static boolean kWinchFalconInverted;
	public static int kWinchFalconId;
	public static FeedbackDevice kWinchFalconFeedbackDevice;
	public static int kWinchFalconControlID;
	public static boolean kWinchFalconReverse;

	public static double kWinchScanPos;

}
