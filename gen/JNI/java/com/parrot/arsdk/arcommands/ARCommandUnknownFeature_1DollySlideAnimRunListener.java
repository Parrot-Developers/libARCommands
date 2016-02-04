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
 * Interface for the command <code>DollySlideAnimRun</code> in feature <code>UnknownFeature_1</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandUnknownFeature_1DollySlideAnimRunListener {

    /**
     * Called when a command <code>DollySlideAnimRun</code> in feature <code>UnknownFeature_1</code> is decoded
     * @param _start 1 to start the anim, 0 to stop it
     * @param _start Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed should be used
     * @param _speed_is_default Not used when start is 0
     * @param _speed_is_default Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     * @param _speed The desired speed of the anim in m/s
     * @param _speed Not used when speed_is_default is 1
     * @param _speed Not used when start is 0
     * @param _speed Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     * @param _angle_is_default 0 if the angle is set by user, 1 if default value should be used
     * @param _angle_is_default Not used when start is 0
     * @param _angle_is_default Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     * @param _angle Desired angle Product-User-Target in rad
     * @param _angle Not used when angle_is_default is 1
     * @param _angle Not used when start is 0
     * @param _angle Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     * @param _horizontal_distance_is_default 0 if the horizontal distance is set by user, 1 if default value should be used
     * @param _horizontal_distance_is_default Not used when start is 0
     * @param _horizontal_distance_is_default Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     * @param _horizontal_distance Distance that should be made by the product to reach its target in m
     * @param _horizontal_distance Not used when horizontalDistance_is_default is 1
     * @param _horizontal_distance Not used when start is 0
     * @param _horizontal_distance Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.
     */
    void onUnknownFeature_1DollySlideAnimRunUpdate (byte start, byte speed_is_default, float speed, byte angle_is_default, float angle, byte horizontal_distance_is_default, float horizontal_distance);
}
