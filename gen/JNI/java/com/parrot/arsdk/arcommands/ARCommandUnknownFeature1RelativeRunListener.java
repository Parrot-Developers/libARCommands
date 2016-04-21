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
 * Interface for the command <code>RelativeRun</code> in feature <code>UnknownFeature1</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandUnknownFeature1RelativeRunListener {

    /**
     * Called when a command <code>RelativeRun</code> in feature <code>UnknownFeature1</code> is decoded
     * @param _start 1 to start relative unknown feature_1 process, 0 to stop it
     * @param _start Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     * @param _distance_is_default 0 if distance is set by user, 1 if set by current distance (in this case distance value is not used) (not used when arg start is at 0)
     * @param _distance_is_default Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     * @param _distance The distance leader-follower in meter (not used when arg start is at 0)
     * @param _distance Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     * @param _elevation_is_default 0 if elevation is set by user, 1 if set by current elevation (in this case elevation value is not used) (not used when arg start is at 0)
     * @param _elevation_is_default Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     * @param _elevation The elevation leader-follower in rad (not used when arg start is at 0)
     * @param _elevation Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     * @param _azimuth_is_default 0 if azimuth is set by user, 1 if set by current azimuth (in this case azimuth value is not used) (not used when arg start is at 0)
     * @param _azimuth_is_default Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     * @param _azimuth The azimuth north-leader-follower in rad (not used when arg start is at 0)
     * @param _azimuth Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.
     */
    void onUnknownFeature1RelativeRunUpdate (byte start, byte distance_is_default, float distance, byte elevation_is_default, float elevation, byte azimuth_is_default, float azimuth);
}
