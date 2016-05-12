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
 * Interface for the command <code>DroneListItem</code> in feature <code>DroneManager</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandDroneManagerDroneListItemListener {

    /**
     * Called when a command <code>DroneListItem</code> in feature <code>DroneManager</code> is decoded
     * @param _serial Serial number of the drone.
     * @param _model Model of the drone.
     * @param _name Name (SSID) of the drone.
     * @param _connection_order 0 if the drone is unknwon (never connected).\nElse, order of last connection (1 = most recent)
     * @param _active 1 if the drone is active (the drone manager tries to connect or is connected to it)\n0 if the drone is not the active one.
     * @param _visible 1 if the drone is currently visible, 0 otherwise.
     * @param _security The security of the drone network.
     * @param _has_saved_key 1 if the drone manager has a saved security key for the drone, 0 otherwise.\nIf security is not none, and this value is 0, then the controller should prompt the user for a passphrase when sending a connect.
     * @param _rssi The drone rssi. The value is meaningless if the drone is not visible.
     * @param _list_flags Flags use by maps and lists
     */
    void onDroneManagerDroneListItemUpdate (String serial, short model, String name, byte connection_order, byte active, byte visible, ARCOMMANDS_DRONE_MANAGER_SECURITY_ENUM security, byte has_saved_key, byte rssi, byte list_flags);
}
