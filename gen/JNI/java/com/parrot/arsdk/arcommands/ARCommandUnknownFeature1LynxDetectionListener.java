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
 * Interface for the command <code>LynxDetection</code> in feature <code>UnknownFeature1</code> listener
 * @author Parrot (c) 2013
 */
public interface ARCommandUnknownFeature1LynxDetectionListener {

    /**
     * Called when a command <code>LynxDetection</code> in feature <code>UnknownFeature1</code> is decoded
     * @param _target_pan Pan angle of detected target in radian
     * @param _target_pan Send vision detection results.
     * @param _target_tilt Tilt angle of detected target in radian
     * @param _target_tilt Send vision detection results.
     * @param _change_of_scale Target's change of scale : new width = (1+ changOfScale) * old width
     * @param _change_of_scale Send vision detection results.
     * @param _confidence_index Confidence index of the Lynx detection (from 0 to 255, the highest is the best)
     * @param _confidence_index Send vision detection results.
     * @param _is_new_selection Boolean. 1 if the selection is new, 0 otherwise
     * @param _is_new_selection Send vision detection results.
     * @param _timestamp Acquisition time of processed picture in millisecond
     * @param _timestamp Send vision detection results.
     */
    void onUnknownFeature1LynxDetectionUpdate (float target_pan, float target_tilt, float change_of_scale, byte confidence_index, byte is_new_selection, long timestamp);
}
