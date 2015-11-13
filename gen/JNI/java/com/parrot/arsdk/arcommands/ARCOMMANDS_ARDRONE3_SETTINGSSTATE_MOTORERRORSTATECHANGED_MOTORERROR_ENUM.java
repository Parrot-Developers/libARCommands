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
/*
 * GENERATED FILE
 *  Do not modify this file, it will be erased during the next configure run 
 */

package com.parrot.arsdk.arcommands;

import java.util.HashMap;

/**
 * Java copy of the eARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR enum
 */
public enum ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM {
   /** Dummy value for all unknown cases */
    eARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_UNKNOWN_ENUM_VALUE (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
   /** No error detected */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_NOERROR (0, "No error detected"),
   /** EEPROM access failure */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERROREEPROM (1, "EEPROM access failure"),
   /** Motor stalled */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORMOTORSTALLED (2, "Motor stalled"),
   /** Propeller cutout security triggered */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORPROPELLERSECURITY (3, "Propeller cutout security triggered"),
   /** Communication with motor failed by timeout */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORCOMMLOST (4, "Communication with motor failed by timeout"),
   /** RC emergency stop */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORRCEMERGENCYSTOP (5, "RC emergency stop"),
   /** Motor controler scheduler real-time out of bounds */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORREALTIME (6, "Motor controler scheduler real-time out of bounds"),
   /** One or several incorrect values in motor settings */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORMOTORSETTING (7, "One or several incorrect values in motor settings"),
   /** Too hot or too cold Cypress temperature */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORTEMPERATURE (8, "Too hot or too cold Cypress temperature"),
   /** Battery voltage out of bounds */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORBATTERYVOLTAGE (9, "Battery voltage out of bounds"),
   /** Incorrect number of LIPO cells */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORLIPOCELLS (10, "Incorrect number of LIPO cells"),
   /** Defectuous MOSFET or broken motor phases */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORMOSFET (11, "Defectuous MOSFET or broken motor phases"),
   /** Not use for BLDC but useful for HAL */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORBOOTLOADER (12, "Not use for BLDC but useful for HAL"),
   /** Error Made by BLDC_ASSERT() */
    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ERRORASSERT (13, "Error Made by BLDC_ASSERT()"),
   ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_MAX (14);

    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM> valuesList;

    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM (int value, String comment) {
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
     * Gets the ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM [] valuesArray = ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM> (valuesArray.length);
            for (ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = eARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_UNKNOWN_ENUM_VALUE;
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
