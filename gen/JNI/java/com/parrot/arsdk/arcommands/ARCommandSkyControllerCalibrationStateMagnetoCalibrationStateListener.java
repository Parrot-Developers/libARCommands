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
 * Interface for the command <code>MagnetoCalibrationState</code> of class <code>CalibrationState</code> in project <code>SkyController</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener {

    /**
     * Called when a command <code>MagnetoCalibrationState</code> of class <code>CalibrationState</code> in project <code>SkyController</code> is decoded
     * @param _status The global status of the calibration
     * @param _X_Quality Calibration quality on X axis.
     * @param _X_Quality 0 is bad, 255 is perfect
     * @param _Y_Quality Calibration quality on Y axis.
     * @param _Y_Quality 0 is bad, 255 is perfect
     * @param _Z_Quality Calibration quality on Z axis.
     * @param _Z_Quality 0 is bad, 255 is perfect
     */
    void onSkyControllerCalibrationStateMagnetoCalibrationStateUpdate (ARCOMMANDS_SKYCONTROLLER_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATE_STATUS_ENUM status, byte X_Quality, byte Y_Quality, byte Z_Quality);
}
