/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.state_machines;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.loops.Loop;
import frc.robot.loops.Looper;
import frc.robot.subsystems.Leveller;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Subsystem;
import frc.robot.subsystems.Winch;
import frc.robot.subsystems.Winch.WinchState;

/**
 * Add your docs here.
 */
public class ClimbingHelper extends Subsystem
{
    static ClimbingHelper mInstance = null;
    public static ClimbingHelper getInstance()
    {
        if (mInstance == null) 
        {
            mInstance = new ClimbingHelper();    
        }
        return mInstance;
    }

    private final Lift mLift = Lift.getInstance();
    private final Winch mWinch = Winch.getInstance();
    private final Leveller mLevel = Leveller.getInstance();

    /**
     * Contains the Climber system states. NO PARAMETERS, IT IS SIMPLY FOR FORMATTING.
     * @param IDLE - At lowest point, everything stopped. Most of the match will be spent like this.
     * @param ALIGN_LIFTZONE - align with the floor bars
     * @param RAISE_TO_BAR - Initial raise period, stopping at 2in below the bar's lowest point. Leads directly into {@link FIND_BAR}
     * @param FIND_BAR - Track upwards at constant speed until we find the bar.
     * @param DRIVE_FORWARD - Drive forward to place the hook over the bar
     * @param RELEASE_HOOK - send hook
     * @param RETRACT_WINCH - Retract the winch so we gan go up
     * @param LEVEL - Power the hook to move us back and forth
     */
    public enum ClimbingState
    {
        IDLE,
        ALIGN_LIFTZONE,
        RAISE_TO_BAR,
        FIND_BAR,
        DRIVE_FORWARD,
        RELEASE_HOOK,
        RETRACT_WINCH,
        LEVEL
    };

    /**
     * Contains the states applicable to the elevator.
     * @param RETRACTED - Most of the match, retracted and locked
     * @param EXTENDING_LEAP - Initial jump to bar-2in
     * @param EXTENDING_SCAN - Look for bar
     * @param EXTENDING_SEEK - slowly get to bar, stay above
     * @param EXTENDED - fully extended, drop off the leveler
     * @param RETRACTING - going back down during climb
     */
    public enum ElevatorState
    {
        RETRACTED, // IDLE
        EXTENDING_LEAP,
        EXTENDING_SCAN,
        EXTENDED,
    };

    private ClimbingState mSystemState = ClimbingState.IDLE;
    private ClimbingState mWantedState = ClimbingState.IDLE;
    private WinchState mWantedWinchState = WinchState.IDLE;
    
    private double mCurrentStateStartTime;
    private boolean mStateChanged; // has the state been changed so far this cycle?

    private Loop mLoop = new Loop()
    {
        // Every time we transition states, we update the current state start
        // time and the state changed boolean (for one cycle)

        @Override
        public void onStart(double timestamp)
        {
            synchronized(ClimbingHelper.this)
            {
                mWantedState = ClimbingState.IDLE;
                mCurrentStateStartTime = timestamp;
                mSystemState = ClimbingState.IDLE;
                mStateChanged = true;
            }
        }

        @Override
        public void onLoop(double timestamp)
        {
            // this is where the magic happens
            synchronized(ClimbingHelper.this)
            {
                ClimbingState newState = mSystemState;
                switch (mSystemState)
                {
                    case IDLE:
                        newState = handleIdle();
                        break;
                    case ALIGN_LIFTZONE:
                        newState = handleAlignLiftZone();
                        break;
                    case RAISE_TO_BAR:
                        newState = handleRaiseToBar();
                        break;
                    case FIND_BAR:
                        newState = handleFindBar();
                        break;
                    case DRIVE_FORWARD:
                        newState = handleDriveForward();
                        break;
                    case RELEASE_HOOK:
                        newState = handleReleaseHook();
                    case RETRACT_WINCH:
                        newState = handleRetractWinch();
                    case LEVEL:
                        newState = handleLevel();
                        break;
                    default:
                        newState = ClimbingState.IDLE;
                }

                if (newState != mSystemState) 
                {
                    System.out.println("Climber state " + mSystemState + " to " + newState + " Timestamp: " + Timer.getFPGATimestamp());
                    // If the new state is different than the old one, do the switch
                    mSystemState = newState;
                }
            }
        }

        @Override
        public void onStop(double timestamp)
        {
            stop();
        }
    };

    private ClimbingState defaultIdleTest()
    {
        if (mSystemState ==  mWantedState) 
        {
            mWantedState = ClimbingState.IDLE;
            return ClimbingState.IDLE;    
        }
        else return mWantedState;
    }

    private ClimbingState handleIdle() 
    {
        if (mStateChanged) // if we have changed our state
        {
            stop(); // stop us
            mWinch.stop(); // stop the winch
            mLevel.stop(); // stop the leveller
            mLift.stop(); // stop the lift
        }

        return defaultIdleTest();
    }
    private ClimbingState handleLevel() {
        return null;
    }

    private ClimbingState handleRetractWinch() 
    {
        if (mStateChanged) 
        {
            
            mLift.setWantedState(ElevatorState.RETRACTED); 
        }
        return ClimbingState.RETRACT_WINCH;
    }

    private ClimbingState handleReleaseHook() {
        return null;
    }

    private ClimbingState handleDriveForward() {
        return null;
    }

    private ClimbingState handleFindBar() {
        return null;
    }

    private ClimbingState handleRaiseToBar() {
        return null;
    }

    private ClimbingState handleAlignLiftZone() {
        return null;
    }

    @Override
    public void stop()
    {

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

    }
}
