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

/**
 * Interface for the command <code>Info</code> of class <code>DebugSettingsState</code> in project <code>CommonDebug</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandCommonDebugDebugSettingsStateInfoListener {

    /**
     * Called when a command <code>Info</code> of class <code>DebugSettingsState</code> in project <code>CommonDebug</code> is decoded
     * @param _listFlags List entry attribute Bitfield.
     * @param _listFlags 0x01: First: indicate it's the first element of the list.
     * @param _listFlags 0x02: Last:  indicate it's the last element of the list.
     * @param _listFlags 0x04: Empty: indicate the list is empty (implies First/Last). All other arguments should be ignored.
     * @param _id Setting Id.
     * @param _label Setting displayed label (single line).
     * @param _type Setting type.
     * @param _mode Setting mode.
     * @param _range_min Setting range minimal value for decimal type.
     * @param _range_max Setting range max value for decimal type.
     * @param _range_step Setting step value for decimal type
     * @param _value Current Setting value (string encoded).
     */
    void onCommonDebugDebugSettingsStateInfoUpdate (byte listFlags, short id, String label, ARCOMMANDS_COMMONDEBUG_DEBUGSETTINGSSTATE_INFO_TYPE_ENUM type, ARCOMMANDS_COMMONDEBUG_DEBUGSETTINGSSTATE_INFO_MODE_ENUM mode, String range_min, String range_max, String range_step, String value);
}
