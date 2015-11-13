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
 * Java copy of the eARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID enum
 */
public enum ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM {
   /** Dummy value for all unknown cases */
    eARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_UNKNOWN_ENUM_VALUE (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
   /** Stop ongoing animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_STOP (0, "Stop ongoing animation."),
   /** Start a spin animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SPIN (1, "Start a spin animation."),
   /** Start a tap animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_TAP (2, "Start a tap animation."),
   /** Start a slow shake animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SLOWSHAKE (3, "Start a slow shake animation."),
   /** Start a Metronome animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_METRONOME (4, "Start a Metronome animation."),
   /** Start a standing dance animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ONDULATION (5, "Start a standing dance animation."),
   /** Start a spin jump animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SPINJUMP (6, "Start a spin jump animation."),
   /** Start a spin that end in standing posture, or in jumper if it was standing animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SPINTOPOSTURE (7, "Start a spin that end in standing posture, or in jumper if it was standing animation."),
   /** Start a spiral animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SPIRAL (8, "Start a spiral animation."),
   /** Start a slalom animation. */
    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SLALOM (9, "Start a slalom animation."),
   ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_MAX (10);

    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM> valuesList;

    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM (int value, String comment) {
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
     * Gets the ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM [] valuesArray = ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM> (valuesArray.length);
            for (ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = eARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_UNKNOWN_ENUM_VALUE;
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
