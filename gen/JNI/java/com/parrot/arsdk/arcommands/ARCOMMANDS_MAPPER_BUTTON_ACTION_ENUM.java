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
 * Java copy of the eARCOMMANDS_MAPPER_BUTTON_ACTION enum
 */
public enum ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM {
    /** Dummy value for all unknown cases */
    UNKNOWN (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
    /** Action handled by the application */
    APP_0 (0, "Action handled by the application"),
    /** Action handled by the application */
    APP_1 (1, "Action handled by the application"),
    /** Action handled by the application */
    APP_2 (2, "Action handled by the application"),
    /** Action handled by the application */
    APP_3 (3, "Action handled by the application"),
    /** Action handled by the application */
    APP_4 (4, "Action handled by the application"),
    /** Action handled by the application */
    APP_5 (5, "Action handled by the application"),
    /** Action handled by the application */
    APP_6 (6, "Action handled by the application"),
    /** Action handled by the application */
    APP_7 (7, "Action handled by the application"),
    /** Action handled by the application */
    APP_8 (8, "Action handled by the application"),
    /** Action handled by the application */
    APP_9 (9, "Action handled by the application"),
    /** Action handled by the application */
    APP_10 (10, "Action handled by the application"),
    /** Action handled by the application */
    APP_11 (11, "Action handled by the application"),
    /** Action handled by the application */
    APP_12 (12, "Action handled by the application"),
    /** Action handled by the application */
    APP_13 (13, "Action handled by the application"),
    /** Action handled by the application */
    APP_14 (14, "Action handled by the application"),
    /** Action handled by the application */
    APP_15 (15, "Action handled by the application"),
    /** Return to home */
    RETURN_HOME (16, "Return to home"),
    /** Take off or land */
    TAKEOFF_LAND (17, "Take off or land"),
    /** Start/stop video record */
    VIDEO_RECORD (18, "Start/stop video record"),
    /** Take a picture */
    TAKE_PICTURE (19, "Take a picture"),
    /** Increment camera exposition */
    CAMERA_EXPOSITION_INC (20, "Increment camera exposition"),
    /** Decrement camera exposition */
    CAMERA_EXPOSITION_DEC (21, "Decrement camera exposition"),
    /** Flip left */
    FLIP_LEFT (22, "Flip left"),
    /** Flip right */
    FLIP_RIGHT (23, "Flip right"),
    /** Flip front */
    FLIP_FRONT (24, "Flip front"),
    /** Flip back */
    FLIP_BACK (25, "Flip back");


    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM> valuesList;

    ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM (int value, String comment) {
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
     * Gets the ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM [] valuesArray = ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM> (valuesArray.length);
            for (ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_MAPPER_BUTTON_ACTION_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = UNKNOWN;
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
