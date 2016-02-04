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
 * Interface for the command <code>GeographicConfigChanged</code> in feature <code>FollowMe</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandFollowMeGeographicConfigChangedListener {

    /**
     * Called when a command <code>GeographicConfigChanged</code> in feature <code>FollowMe</code> is decoded
     * @param _distance_is_default 0 if distance is set by user, 1 if set by current distance
     * @param _distance_is_default Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.
     * @param _distance The distance leader-follower in meter, if distance is default, this value is the current drone distance
     * @param _distance Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.
     * @param _elevation_is_default 0 if elevation is set by user, 1 if set by current elevation
     * @param _elevation_is_default Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.
     * @param _elevation The elevation leader-follower in rad, if elevation is default, this value is the current leader to drone elevation angle
     * @param _elevation Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.
     * @param _azimuth_is_default 0 if azimuth is set by user, 1 if set by current azimuth
     * @param _azimuth_is_default Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.
     * @param _azimuth The azimuth north-leader-follower in rad, if azimuth is default, this value is the current leader to drone azimuth
     * @param _azimuth Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.
     */
    void onFollowMeGeographicConfigChangedUpdate (byte distance_is_default, float distance, byte elevation_is_default, float elevation, byte azimuth_is_default, float azimuth);
}
