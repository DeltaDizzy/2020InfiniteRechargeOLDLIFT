/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.lib.util.drivers.Talon.CANTalonFactory;
import frc.robot.Constants;
import frc.robot.loops.Loop;
import frc.robot.loops.Looper;
import frc.robot.state_machines.ClimbingHelper.ElevatorState;

/**
 * Add your docs here.
 */
public class Winch extends Subsystem
{
    private static Winch sInstance = null;
    private TalonFX falcon;
    public static Winch getInstance()
    {
        if (sInstance == null) 
        {
            sInstance = new Winch();    
        }
        return sInstance;
    }

    /**
     * COntrols the Winch
     * @param IDLE - Motionless
     * @param ASCENT_2IN - Intial Ascent to 2in under the bar
     * @param ASCENT_FINAL - seek until the hook is in position
     * @param DESCENT - Lower the robot from the bar, for use after match end
     * @param MANUAL - drivers can manually controll ascent/descent
     */
    public enum WinchState 
    {
        IDLE,
        ASCENT_2IN,
        ASCENT_FINAL,
        DESCENT,
        MANUAL
    }


    public WinchState mCurrentState = WinchState.IDLE;
    public WinchState mWantedState = WinchState.IDLE;

    private double mCurrentStateStartTime;
    private boolean mStateChanged;
    public boolean seekMode;

    public Winch()
    {
        falcon = CANTalonFactory.createFalcon(Constants.kWinchFalconId, Constants.kWinchFalconInverted, Constants.kWinchFalconNeutralMode, 
            Constants.kWinchFalconFeedbackDevice, Constants.kWinchFalconControlID, Constants.kWinchFalconReverse);
    }

    private Loop mLoop = new Loop()
    {
        @Override
        public void onStart(double timestamp)
        {
            // make sure we are stopped
            stop();
            synchronized(Winch.this)
            {
                mCurrentState = WinchState.IDLE;
                mStateChanged = true;
                mCurrentStateStartTime = timestamp;
            }
        }

        @Override
        public void onLoop(double timestamp)
        {
            synchronized(Winch.this)
            {
                WinchState newState;
                switch(mCurrentState)
                {
                    case IDLE:
                        newState = handleIdle();
                        break;
                    case ASCENT_2IN:
                        newState = handleAscentInitial();
                        break;
                    case DESCENT:
                        newState = handleDescent();
                        break;
                    case MANUAL:
                        newState = handleManual();
                        break;
                    default:
                        newState = WinchState.IDLE;
                }

                if (newState != mCurrentState) 
                {
                    System.out.println("Winch state " + mCurrentState + " to " + newState);    
                    mCurrentState = newState;
                    mCurrentStateStartTime = timestamp;
                    mStateChanged = true;
                }
                else
                {
                    mStateChanged = false;
                }
            }
        }

        @Override
        public void onStop(double timestamp)
        {
            stop();
        }
    };

    private WinchState handleIdle() 
    {
        if (mStateChanged) 
        {
            stop();
            defaultIdleTest();
        }
        return null;
    }

    private WinchState handleAscentInitial() {
        return null;
    }

    private WinchState handleDescent() {
        return null;
    }

    private WinchState handleManual() {
        return null;
    }

    private WinchState defaultIdleTest() 
    {
        if (mCurrentState == mWantedState) 
        {
            mWantedState = WinchState.IDLE;
            return WinchState.IDLE;
        }
        else return mWantedState;
    }

    public synchronized void setWantedState(WinchState state)
    {
        mWantedState = state;
    }

    // CLOSED LOOP
    private double mWantedPosition;
    private double mCurrentPosition;

    public synchronized void setPosition(double pos)
    {
        if (pos >= Constants.kWinchScanPos) 
        {
            pos = Constants.kWinchScanPos;
        } 
        else if (pos < 0) 
        {
            pos = 0;
        }
    }


    @Override
    public void stop()
    {
        setWantedState(WinchState.IDLE);
        falcon.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void zeroSensors()
    {

    }

    @Override
    public void outputToSmartDashboard()
    {

    }

    @Override
    public void registerEnabledLoops(Looper looper)
    {
        looper.register(mLoop);
    }
}
