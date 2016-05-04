/*
    Copyright (C) 2014 Parrot SA
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    * Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.
    * Neither the name of Parrot nor the names
    of its contributors may be used to endorse or promote products
    derived from this software without specific prior written
    permission.
    
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
    FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
    COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
    OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
    AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
    SUCH DAMAGE.
*/

package com.parrot.arsdk.arcommands;

import java.util.HashMap;

/**
 * Java copy of the eARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING enum
 */
public enum ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM {
    /** Dummy value for all unknown cases */
    eARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_UNKNOWN_ENUM_VALUE (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
    /** Max roll value. In degree. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_MAX_ROLL (0, "Max roll value. In degree."),
    /** How fast the plane reaches the desired roll angle. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ROLL_KP (1, "How fast the plane reaches the desired roll angle. No unit."),
    /** How fast the plane reaches the desired roll rate. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ROLL_RATE_KP (2, "How fast the plane reaches the desired roll rate. No unit."),
    /** Max pitch value. In degree. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_MAX_PITCH (3, "Max pitch value. In degree."),
    /** Min pitch value. In degree. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_MIN_PITCH (4, "Min pitch value. In degree."),
    /** How fast the plane reaches the desired pitch angle. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_PITCH_KP (5, "How fast the plane reaches the desired pitch angle. No unit."),
    /** How fast the plane reaches the desired pitch rate. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_PITCH_RATE_KP (6, "How fast the plane reaches the desired pitch rate. No unit."),
    /** How fast the plane reaches the desired yaw angle. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_YAW_KP (7, "How fast the plane reaches the desired yaw angle. No unit."),
    /** How fast the plane reaches the desired yaw rate. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_YAW_RATE_KP (8, "How fast the plane reaches the desired yaw rate. No unit."),
    /** Portion of thrust that is added to motors according to the roll/yaw command to compensate a dive during turn. No unit. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ROLL_TO_THROTTLE_RATE (9, "Portion of thrust that is added to motors according to the roll/yaw command to compensate a dive during turn. No unit."),
    /** Angle of attack of a plane needed horizontal flight. */
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ANGLE_OF_ATTACK (10, "Angle of attack of a plane needed horizontal flight."),
    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_MAX (11);


    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM> valuesList;

    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM (int value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    /**
     * Gets the int value of the enum
     * @return int value of the enum
     */
    public int getValue () {
        return value;
    }

    /**
     * Gets the ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM [] valuesArray = ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM> (valuesArray.length);
            for (ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = eARCOMMANDS_POWERUP_PILOTINGSETTINGSSTATE_SETTINGCHANGED_SETTING_UNKNOWN_ENUM_VALUE;
        }
        return retVal;    }

    /**
     * Returns the enum comment as a description string
     * @return The enum description
     */
    public String toString () {
        if (this.comment != null) {
            return this.comment;
        }
        return super.toString ();
    }
}
