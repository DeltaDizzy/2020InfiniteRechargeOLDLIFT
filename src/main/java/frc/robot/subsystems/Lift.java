/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.lib.util.drivers.Talon.CANTalonFactory;
import frc.robot.loops.Loop;
import frc.robot.loops.Looper;
import frc.robot.state_machines.ClimbingHelper.ElevatorState;
import frc.robot.subsystems.Subsystem;
import frc.robot.Constants;

/**
 * The Lift controls the ascent/descent of the elevator that carries the levelling unit.
 */
public class Lift extends Subsystem
{
    private static Lift sInstance = null;
    private TalonFX liftmotor;
    private ElevatorState mLiftState;
    private ElevatorState mWantedLiftState;
    private boolean mStateChanged;
    private double mCurrentStateStartTime;

    public static Lift getInstance()
    {
        if (sInstance == null) 
        {
            sInstance = new Lift();    
        }
        return sInstance;
    }

    private Lift()
    {
        // Falcon Init
        liftmotor = CANTalonFactory.createFalcon(Constants.kLiftFalconId, Constants.kLiftFalconInverted, Constants.kLiftFalconNeutralMode, 
            Constants.kLiftFalconFeedbackDevice, Constants.kLiftFalconControlID, Constants.kLiftFalconReverse);
        
        // TODO: Hard/Soft Limits/PID Loops
    }

    private Loop mLoop = new Loop()
    {
        @Override
        public void onStart(double timestamp)
        {
            // make sure we are stopped
            stop();
            synchronized(Lift.this)
            {
                
            }
        }

        @Override
        public void onLoop(double timestamp)
        {
            synchronized(Lift.this)
            {
                ElevatorState newState;
                switch(mLiftState)
                {
                    case RETRACTED:
                        newState = handleRetracted();
                        break;
                    case EXTENDING_LEAP:
                        newState = handleExtendingLeap();
                        break;
                    case EXTENDED:
                        newState = handleExtended();
                        break;
                    default:
                        newState = ElevatorState.RETRACTED;
                }

                if (newState != mLiftState) 
                {
                    System.out.println("Winch state " + mLiftState + " to " + newState);    
                    mLiftState = newState;
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

        }
    };

    private ElevatorState handleExtended() {
        return null;
    }

    private ElevatorState handleExtendingLeap() {
        return null;
    }

    private ElevatorState handleRetracted() {
        if (mStateChanged) 
        {
            stopMotors();    
        }
        return defaultRetractedTest();
    }

    private ElevatorState defaultRetractedTest() 
    {
        if (mLiftState == mWantedLiftState) 
        {
            mWantedLiftState = ElevatorState.RETRACTED;
            return ElevatorState.RETRACTED;
        }
        else return mWantedLiftState;
    }

    private void stopMotors()
    {
        liftmotor.set(ControlMode.Velocity, 0);
    }

    // CLOSED LOOP
    private double mWantedPosition;
    private double mTravelingPosition;

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

    public synchronized void setWantedState(ElevatorState state)
    {
        mWantedLiftState = state;
    }

    @Override
    public void stop()
    {

    }

    @Override
    public void outputToSmartDashboard()
    {

    }

    @Override
    public void zeroSensors()
    {

    }

    @Override
    public void registerEnabledLoops(Looper in)
    {
        in.register(mLoop);
    }
}
