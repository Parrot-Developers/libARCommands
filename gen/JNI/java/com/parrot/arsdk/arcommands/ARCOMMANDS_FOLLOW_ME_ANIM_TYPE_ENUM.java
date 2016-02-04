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
 * Java copy of the eARCOMMANDS_FOLLOW_ME_ANIM_TYPE enum
 */
public enum ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM {
   /** Dummy value for all unknown cases */
    eARCOMMANDS_FOLLOW_ME_ANIM_TYPE_UNKNOWN_ENUM_VALUE (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
   /** No current unknown feature_1 animation */
    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_NONE (0, "No current unknown feature_1 animation"),
   /** Current unknown feature_1 animation is spiral */
    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_SPIRAL (1, "Current unknown feature_1 animation is spiral"),
   /** Current unknown feature_1 animation is swing */
    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_SWING (2, "Current unknown feature_1 animation is swing"),
   /** Current unknown feature_1 animation is boomerang */
    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_BOOMERANG (3, "Current unknown feature_1 animation is boomerang"),
   /** Current unknown feature_1 animation is candle */
    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_CANDLE (4, "Current unknown feature_1 animation is candle"),
   /** Current unknown feature_1 animation is dolly slide */
    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_DOLLY_SLIDE (5, "Current unknown feature_1 animation is dolly slide"),
   ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_MAX (6);

    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM> valuesList;

    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM (int value, String comment) {
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
     * Gets the ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM [] valuesArray = ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM> (valuesArray.length);
            for (ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = eARCOMMANDS_FOLLOW_ME_ANIM_TYPE_UNKNOWN_ENUM_VALUE;
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
