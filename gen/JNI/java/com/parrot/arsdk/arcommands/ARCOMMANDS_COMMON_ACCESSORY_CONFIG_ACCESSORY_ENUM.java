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
 * Java copy of the eARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY enum
 */
public enum ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM {
   /** Dummy value for all unknown cases */
    eARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_UNKNOWN_ENUM_VALUE (Integer.MIN_VALUE, "Dummy value for all unknown cases"),
   /** No accessory. */
    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_NO_ACCESSORY (0, "No accessory."),
   /** Standard wheels */
    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_STD_WHEELS (1, "Standard wheels"),
   /** Truck wheels */
    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_TRUCK_WHEELS (2, "Truck wheels"),
   /** Hull */
    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_HULL (3, "Hull"),
   /** Hydrofoil */
    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_HYDROFOIL (4, "Hydrofoil"),
   ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_MAX (5);

    private final int value;
    private final String comment;
    static HashMap<Integer, ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM> valuesList;

    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM (int value) {
        this.value = value;
        this.comment = null;
    }

    ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM (int value, String comment) {
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
     * Gets the ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM instance from a C enum value
     * @param value C value of the enum
     * @return The ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM instance, or null if the C enum value was not valid
     */
    public static ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM getFromValue (int value) {
        if (null == valuesList) {
            ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM [] valuesArray = ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM.values ();
            valuesList = new HashMap<Integer, ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM> (valuesArray.length);
            for (ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM entry : valuesArray) {
                valuesList.put (entry.getValue (), entry);
            }
        }
        ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM retVal = valuesList.get (value);
        if (retVal == null) {
            retVal = eARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_UNKNOWN_ENUM_VALUE;
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
