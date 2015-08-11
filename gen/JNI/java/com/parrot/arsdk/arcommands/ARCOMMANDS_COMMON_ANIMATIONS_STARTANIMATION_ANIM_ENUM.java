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
 * Java copy of the eARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM enum
 */
public enum ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM {
   /** Dummy value for all unknown cases */
    eARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_UNKNOWN_ENUM_VALUE (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
   /** Flash headlights. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_HEADLIGHTS_FLASH (0, "Flash headlights."),
   /** Blink headlights. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_HEADLIGHTS_BLINK (1, "Blink headlights."),
   /** Oscillating headlights. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_HEADLIGHTS_OSCILLATION (2, "Oscillating headlights."),
   /** Spin animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_SPIN (3, "Spin animation."),
   /** Tap animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_TAP (4, "Tap animation."),
   /** Slow shake animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_SLOW_SHAKE (5, "Slow shake animation."),
   /** Metronome animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_METRONOME (6, "Metronome animation."),
   /** Standing dance animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ONDULATION (7, "Standing dance animation."),
   /** Spin jump animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_SPIN_JUMP (8, "Spin jump animation."),
   /** Spin that end in standing posture, or in jumper if it was standing animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_SPIN_TO_POSTURE (9, "Spin that end in standing posture, or in jumper if it was standing animation."),
   /** Spiral animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_SPIRAL (10, "Spiral animation."),
   /** Slalom animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_SLALOM (11, "Slalom animation."),
   /** Boost animation. */
    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_BOOST (12, "Boost animation."),
   ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_MAX (13);

    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM> valuesList;

    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM (int value, String comment) {
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
     * Gets the ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM [] valuesArray = ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM> (valuesArray.length);
            for (ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = eARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_UNKNOWN_ENUM_VALUE;
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
