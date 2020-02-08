/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.loops.Looper;

/**
 * Add your docs here.
 */
public class Leveller extends Subsystem
{
    private static Leveller sInstance = null;

    public static Leveller getInstance()
    {
        if (sInstance == null) 
        {
            sInstance = new Leveller();    
        }
        return sInstance;
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
