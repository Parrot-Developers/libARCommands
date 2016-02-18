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

import com.parrot.arsdk.arsal.ARSALPrint;

/**
 * Java implementation of a C ARCommandsFilter object.<br>
 * @author Parrot (c) 2014
 */
public class ARCommandsFilter
{
    private long cFilter;
    private boolean valid;
    private static final String TAG = ARCommandsFilter.class.getSimpleName();

    /**
     * Creates a new ARCommandsFilter which allows all commands.
     */
    public ARCommandsFilter () {
        this(ARCOMMANDS_FILTER_STATUS_ENUM.ARCOMMANDS_FILTER_STATUS_ALLOWED);
    }

    /**
     * Creates a new ARCommandsFilter with the given default behavior.
     * @param behavior The default behavior of the filter.
     * @warning Only ALLOWED and BLOCK are allowed as default behavior. Providing any other value will create an invalid object.
     */
    public ARCommandsFilter (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        this.cFilter = nativeNewFilter (behavior.getValue());
        this.valid = (this.cFilter != 0);
        if (! this.valid) {
            dispose();
        }
    }

    /**
     * Checks the object validity.
     * @return <code>true</code> if the object is valid<br><code>false</code> if the object is invalid.
     */
    public boolean isValid () {
        return valid;
    }

    /**
     * Marks a ARCommandsFilter as unused (so C-allocated memory can be freed)<br>
     * A disposed ARCommandsFilter is marked as invalid.
     */
    public void dispose () {
        if (valid) {
            nativeDeleteFilter (cFilter);
        }
        this.valid = false;
    }

    /**
     * Gets the native pointer for this filter
     * @return The pointer.
     */
    public long getFilter () {
        return cFilter;
    }

    protected void finalize () throws Throwable {
        try {
            if (valid) {
                ARSALPrint.e (TAG, this + ": Finalize error -> dispose () was not called !");
                dispose ();
            }
        }
        finally {
            super.finalize ();
        }
    }

    /**
     * Filters a command.<br>
     * This function returns the filter behavior for the given ARCommand.<br>
     * @param command The command to be filtered.
     * @return The filter status.
     */
    public ARCOMMANDS_FILTER_STATUS_ENUM filterCommand (ARCommand command) {
        if (! valid) { return ARCOMMANDS_FILTER_STATUS_ENUM.ARCOMMANDS_FILTER_STATUS_ERROR; }
        int cStatus = nativeFilterCommand (cFilter, command.getData(), command.getDataSize());
        return ARCOMMANDS_FILTER_STATUS_ENUM.getFromValue(cStatus);
    }

    private native long nativeNewFilter (int behavior);
    private native void nativeDeleteFilter (long cFilter);
    private native int nativeFilterCommand (long cFilter, long command, int len);

    // Project ARDrone3
    // - Class ProEvent
    private native int nativeSetARDrone3Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Piloting
    private native int nativeSetARDrone3PilotingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.Piloting.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingFlatTrimBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.FlatTrim.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingFlatTrimBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingFlatTrimBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingTakeOffBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.TakeOff.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingTakeOffBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingTakeOffBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingPCMDBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.PCMD.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingPCMDBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingPCMDBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingLandingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.Landing.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingLandingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingLandingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingEmergencyBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.Emergency.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingEmergencyBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingEmergencyBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingNavigateHomeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.NavigateHome.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingNavigateHomeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingNavigateHomeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingAutoTakeOffModeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.AutoTakeOffMode.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingAutoTakeOffModeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingAutoTakeOffModeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingMoveByBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Piloting.MoveBy.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingMoveByBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingMoveByBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Animations
    private native int nativeSetARDrone3AnimationsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.Animations.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AnimationsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AnimationsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3AnimationsFlipBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Animations.Flip.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AnimationsFlipBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AnimationsFlipBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Camera
    private native int nativeSetARDrone3CameraBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.Camera.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3CameraBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3CameraBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3CameraOrientationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Camera.Orientation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3CameraOrientationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3CameraOrientationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecord
    private native int nativeSetARDrone3MediaRecordBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.MediaRecord.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordPictureBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecord.Picture.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordPictureBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordPictureBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordVideoBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecord.Video.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordVideoBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordVideoBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordPictureV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecord.PictureV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordPictureV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordPictureV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordVideoV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecord.VideoV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordVideoV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordVideoV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecordState
    private native int nativeSetARDrone3MediaRecordStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.MediaRecordState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordStatePictureStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecordState.PictureStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordStatePictureStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordStatePictureStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordStateVideoStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecordState.VideoStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordStateVideoStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordStateVideoStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordStatePictureStateChangedV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecordState.PictureStateChangedV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordStatePictureStateChangedV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordStatePictureStateChangedV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordStateVideoStateChangedV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecordState.VideoStateChangedV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordStateVideoStateChangedV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordStateVideoStateChangedV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecordEvent
    private native int nativeSetARDrone3MediaRecordEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.MediaRecordEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordEventPictureEventChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecordEvent.PictureEventChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordEventPictureEventChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordEventPictureEventChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaRecordEventVideoEventChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaRecordEvent.VideoEventChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaRecordEventVideoEventChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaRecordEventVideoEventChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingState
    private native int nativeSetARDrone3PilotingStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PilotingState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateFlatTrimChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.FlatTrimChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateFlatTrimChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateFlatTrimChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateFlyingStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.FlyingStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateFlyingStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateFlyingStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateAlertStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.AlertStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateAlertStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateAlertStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateNavigateHomeStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.NavigateHomeStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateNavigateHomeStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateNavigateHomeStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStatePositionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.PositionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStatePositionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStatePositionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.SpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateAttitudeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.AttitudeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateAttitudeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateAttitudeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateAutoTakeOffModeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.AutoTakeOffModeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateAutoTakeOffModeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateAutoTakeOffModeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingStateAltitudeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingState.AltitudeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingStateAltitudeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingStateAltitudeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingEvent
    private native int nativeSetARDrone3PilotingEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PilotingEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingEventMoveByEndBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingEvent.MoveByEnd.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingEventMoveByEndBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingEventMoveByEndBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Network
    private native int nativeSetARDrone3NetworkBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.Network.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkWifiScanBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Network.WifiScan.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkWifiScanBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkWifiScanBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkWifiAuthChannelBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Network.WifiAuthChannel.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkWifiAuthChannelBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkWifiAuthChannelBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkState
    private native int nativeSetARDrone3NetworkStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.NetworkState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkStateWifiScanListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkState.WifiScanListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkStateWifiScanListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkStateWifiScanListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkStateAllWifiScanChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkState.AllWifiScanChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkStateAllWifiScanChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkStateAllWifiScanChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkStateWifiAuthChannelListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkState.WifiAuthChannelListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkStateWifiAuthChannelListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkStateWifiAuthChannelListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkStateAllWifiAuthChannelChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkState.AllWifiAuthChannelChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkStateAllWifiAuthChannelChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkStateAllWifiAuthChannelChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingSettings
    private native int nativeSetARDrone3PilotingSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PilotingSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsMaxAltitudeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.MaxAltitude.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsMaxAltitudeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsMaxAltitudeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsMaxTiltBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.MaxTilt.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsMaxTiltBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsMaxTiltBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsAbsolutControlBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.AbsolutControl.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsAbsolutControlBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsAbsolutControlBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsMaxDistanceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.MaxDistance.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsMaxDistanceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsMaxDistanceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsNoFlyOverMaxDistanceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.NoFlyOverMaxDistance.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsNoFlyOverMaxDistanceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsNoFlyOverMaxDistanceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.SetAutonomousFlightMaxHorizontalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.SetAutonomousFlightMaxVerticalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.SetAutonomousFlightMaxHorizontalAcceleration.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.SetAutonomousFlightMaxVerticalAcceleration.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettings.SetAutonomousFlightMaxRotationSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingSettingsState
    private native int nativeSetARDrone3PilotingSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PilotingSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateMaxAltitudeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.MaxAltitudeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateMaxAltitudeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateMaxAltitudeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateMaxTiltChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.MaxTiltChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateMaxTiltChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateMaxTiltChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateAbsolutControlChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.AbsolutControlChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateAbsolutControlChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateAbsolutControlChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateMaxDistanceChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.MaxDistanceChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateMaxDistanceChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateMaxDistanceChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.NoFlyOverMaxDistanceChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.AutonomousFlightMaxHorizontalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.AutonomousFlightMaxVerticalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.AutonomousFlightMaxHorizontalAcceleration.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.AutonomousFlightMaxVerticalAcceleration.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PilotingSettingsState.AutonomousFlightMaxRotationSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SpeedSettings
    private native int nativeSetARDrone3SpeedSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.SpeedSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsMaxVerticalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettings.MaxVerticalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsMaxVerticalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsMaxVerticalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsMaxRotationSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettings.MaxRotationSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsMaxRotationSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsMaxRotationSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsHullProtectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettings.HullProtection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsHullProtectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsHullProtectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsOutdoorBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettings.Outdoor.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsOutdoorBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsOutdoorBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SpeedSettingsState
    private native int nativeSetARDrone3SpeedSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.SpeedSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsStateMaxVerticalSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettingsState.MaxVerticalSpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsStateMaxVerticalSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsStateMaxVerticalSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsStateMaxRotationSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettingsState.MaxRotationSpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsStateMaxRotationSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsStateMaxRotationSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsStateHullProtectionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettingsState.HullProtectionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsStateHullProtectionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsStateHullProtectionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SpeedSettingsStateOutdoorChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SpeedSettingsState.OutdoorChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SpeedSettingsStateOutdoorChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SpeedSettingsStateOutdoorChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkSettings
    private native int nativeSetARDrone3NetworkSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.NetworkSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkSettingsWifiSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkSettings.WifiSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkSettingsWifiSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkSettingsWifiSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkSettingsWifiSecurityBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkSettings.WifiSecurity.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkSettingsWifiSecurityBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkSettingsWifiSecurityBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkSettingsState
    private native int nativeSetARDrone3NetworkSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.NetworkSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkSettingsStateWifiSelectionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkSettingsState.WifiSelectionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkSettingsStateWifiSelectionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkSettingsStateWifiSelectionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3NetworkSettingsStateWifiSecurityChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.NetworkSettingsState.WifiSecurityChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3NetworkSettingsStateWifiSecurityChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3NetworkSettingsStateWifiSecurityChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Settings
    private native int nativeSetARDrone3SettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.Settings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SettingsState
    private native int nativeSetARDrone3SettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.SettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateProductMotorVersionListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.ProductMotorVersionListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateProductMotorVersionListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateProductMotorVersionListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateProductGPSVersionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.ProductGPSVersionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateProductGPSVersionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateProductGPSVersionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateMotorErrorStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.MotorErrorStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateMotorErrorStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateMotorErrorStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateMotorSoftwareVersionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.MotorSoftwareVersionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateMotorSoftwareVersionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateMotorSoftwareVersionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateMotorFlightsStatusChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.MotorFlightsStatusChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateMotorFlightsStatusChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateMotorFlightsStatusChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateMotorErrorLastErrorChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.MotorErrorLastErrorChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateMotorErrorLastErrorChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateMotorErrorLastErrorChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3SettingsStateP7IDBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.SettingsState.P7ID.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3SettingsStateP7IDBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3SettingsStateP7IDBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class DirectorMode
    private native int nativeSetARDrone3DirectorModeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.DirectorMode.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DirectorModeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DirectorModeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class DirectorModeState
    private native int nativeSetARDrone3DirectorModeStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.DirectorModeState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DirectorModeStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DirectorModeStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PictureSettings
    private native int nativeSetARDrone3PictureSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PictureSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsPictureFormatSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettings.PictureFormatSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsPictureFormatSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsPictureFormatSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsAutoWhiteBalanceSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettings.AutoWhiteBalanceSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsAutoWhiteBalanceSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsAutoWhiteBalanceSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsExpositionSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettings.ExpositionSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsExpositionSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsExpositionSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsSaturationSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettings.SaturationSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsSaturationSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsSaturationSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsTimelapseSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettings.TimelapseSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsTimelapseSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsTimelapseSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsVideoAutorecordSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettings.VideoAutorecordSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsVideoAutorecordSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsVideoAutorecordSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PictureSettingsState
    private native int nativeSetARDrone3PictureSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PictureSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsStatePictureFormatChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettingsState.PictureFormatChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStatePictureFormatChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStatePictureFormatChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsStateAutoWhiteBalanceChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettingsState.AutoWhiteBalanceChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStateAutoWhiteBalanceChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStateAutoWhiteBalanceChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsStateExpositionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettingsState.ExpositionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStateExpositionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStateExpositionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsStateSaturationChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettingsState.SaturationChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStateSaturationChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStateSaturationChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsStateTimelapseChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettingsState.TimelapseChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStateTimelapseChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStateTimelapseChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PictureSettingsStateVideoAutorecordChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PictureSettingsState.VideoAutorecordChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PictureSettingsStateVideoAutorecordChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PictureSettingsStateVideoAutorecordChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaStreaming
    private native int nativeSetARDrone3MediaStreamingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.MediaStreaming.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaStreamingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaStreamingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaStreamingVideoEnableBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaStreaming.VideoEnable.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaStreamingVideoEnableBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaStreamingVideoEnableBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaStreamingState
    private native int nativeSetARDrone3MediaStreamingStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.MediaStreamingState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaStreamingStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaStreamingStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3MediaStreamingStateVideoEnableChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.MediaStreamingState.VideoEnableChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3MediaStreamingStateVideoEnableChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3MediaStreamingStateVideoEnableChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GPSSettings
    private native int nativeSetARDrone3GPSSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.GPSSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsSetHomeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettings.SetHome.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsSetHomeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsSetHomeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsResetHomeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettings.ResetHome.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsResetHomeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsResetHomeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsSendControllerGPSBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettings.SendControllerGPS.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsSendControllerGPSBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsSendControllerGPSBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsHomeTypeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettings.HomeType.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsHomeTypeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsHomeTypeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsReturnHomeDelayBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettings.ReturnHomeDelay.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsReturnHomeDelayBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsReturnHomeDelayBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GPSSettingsState
    private native int nativeSetARDrone3GPSSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.GPSSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsStateHomeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettingsState.HomeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateHomeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateHomeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsStateResetHomeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettingsState.ResetHomeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateResetHomeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateResetHomeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsStateGPSFixStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettingsState.GPSFixStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateGPSFixStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateGPSFixStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsStateGPSUpdateStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettingsState.GPSUpdateStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateGPSUpdateStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateGPSUpdateStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsStateHomeTypeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettingsState.HomeTypeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateHomeTypeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateHomeTypeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSSettingsStateReturnHomeDelayChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSSettingsState.ReturnHomeDelayChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSSettingsStateReturnHomeDelayChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSSettingsStateReturnHomeDelayChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CameraState
    private native int nativeSetARDrone3CameraStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.CameraState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3CameraStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3CameraStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3CameraStateOrientationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.CameraState.Orientation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3CameraStateOrientationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3CameraStateOrientationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3CameraStateDefaultCameraOrientationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.CameraState.DefaultCameraOrientation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3CameraStateDefaultCameraOrientationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3CameraStateDefaultCameraOrientationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Antiflickering
    private native int nativeSetARDrone3AntiflickeringBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.Antiflickering.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AntiflickeringBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AntiflickeringBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3AntiflickeringElectricFrequencyBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Antiflickering.ElectricFrequency.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AntiflickeringElectricFrequencyBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AntiflickeringElectricFrequencyBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3AntiflickeringSetModeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.Antiflickering.SetMode.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AntiflickeringSetModeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AntiflickeringSetModeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AntiflickeringState
    private native int nativeSetARDrone3AntiflickeringStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.AntiflickeringState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AntiflickeringStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AntiflickeringStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3AntiflickeringStateElectricFrequencyChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.AntiflickeringState.ElectricFrequencyChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AntiflickeringStateElectricFrequencyChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AntiflickeringStateElectricFrequencyChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3AntiflickeringStateModeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.AntiflickeringState.ModeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3AntiflickeringStateModeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3AntiflickeringStateModeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GPSState
    private native int nativeSetARDrone3GPSStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.GPSState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSStateNumberOfSatelliteChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSState.NumberOfSatelliteChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSStateNumberOfSatelliteChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSStateNumberOfSatelliteChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSStateHomeTypeAvailabilityChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSState.HomeTypeAvailabilityChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSStateHomeTypeAvailabilityChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSStateHomeTypeAvailabilityChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3GPSStateHomeTypeChosenChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.GPSState.HomeTypeChosenChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3GPSStateHomeTypeChosenChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3GPSStateHomeTypeChosenChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PROState
    private native int nativeSetARDrone3PROStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3.PROState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PROStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PROStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3PROStateFeaturesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3.PROState.Features.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3PROStateFeaturesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3PROStateFeaturesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project ARDrone3Debug
    // - Class PROState
    private native int nativeSetARDrone3DebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3Debug.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Video
    private native int nativeSetARDrone3DebugVideoBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3Debug.Video.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugVideoBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugVideoBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3DebugVideoEnableWobbleCancellationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3Debug.Video.EnableWobbleCancellation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugVideoEnableWobbleCancellationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugVideoEnableWobbleCancellationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3DebugVideoSyncAnglesGyrosBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3Debug.Video.SyncAnglesGyros.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugVideoSyncAnglesGyrosBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugVideoSyncAnglesGyrosBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3DebugVideoManualWhiteBalanceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3Debug.Video.ManualWhiteBalance.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugVideoManualWhiteBalanceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugVideoManualWhiteBalanceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class BatteryDebugSettings
    private native int nativeSetARDrone3DebugBatteryDebugSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3Debug.BatteryDebugSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugBatteryDebugSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugBatteryDebugSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3DebugBatteryDebugSettingsUseDrone2BatteryBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3Debug.BatteryDebugSettings.UseDrone2Battery.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugBatteryDebugSettingsUseDrone2BatteryBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugBatteryDebugSettingsUseDrone2BatteryBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class BatteryDebugSettingsState
    private native int nativeSetARDrone3DebugBatteryDebugSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3Debug.BatteryDebugSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugBatteryDebugSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugBatteryDebugSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3DebugBatteryDebugSettingsStateUseDrone2BatteryChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3Debug.BatteryDebugSettingsState.UseDrone2BatteryChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugBatteryDebugSettingsStateUseDrone2BatteryChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugBatteryDebugSettingsStateUseDrone2BatteryChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GPSDebugState
    private native int nativeSetARDrone3DebugGPSDebugStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands ARDrone3Debug.GPSDebugState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugGPSDebugStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugGPSDebugStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetARDrone3DebugGPSDebugStateNbSatelliteChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command ARDrone3Debug.GPSDebugState.NbSatelliteChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setARDrone3DebugGPSDebugStateNbSatelliteChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetARDrone3DebugGPSDebugStateNbSatelliteChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project JumpingSumo
    // - Class GPSDebugState
    private native int nativeSetJumpingSumoBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Piloting
    private native int nativeSetJumpingSumoPilotingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.Piloting.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoPilotingPCMDBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Piloting.PCMD.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingPCMDBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingPCMDBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoPilotingPostureBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Piloting.Posture.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingPostureBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingPostureBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoPilotingAddCapOffsetBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Piloting.AddCapOffset.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingAddCapOffsetBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingAddCapOffsetBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingState
    private native int nativeSetJumpingSumoPilotingStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.PilotingState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoPilotingStatePostureChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.PilotingState.PostureChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingStatePostureChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingStatePostureChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoPilotingStateAlertStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.PilotingState.AlertStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingStateAlertStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingStateAlertStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoPilotingStateSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.PilotingState.SpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoPilotingStateSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoPilotingStateSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Animations
    private native int nativeSetJumpingSumoAnimationsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.Animations.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsJumpStopBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Animations.JumpStop.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsJumpStopBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsJumpStopBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsJumpCancelBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Animations.JumpCancel.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsJumpCancelBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsJumpCancelBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsJumpLoadBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Animations.JumpLoad.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsJumpLoadBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsJumpLoadBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsJumpBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Animations.Jump.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsJumpBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsJumpBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsSimpleAnimationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Animations.SimpleAnimation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsSimpleAnimationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsSimpleAnimationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AnimationsState
    private native int nativeSetJumpingSumoAnimationsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.AnimationsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsStateJumpLoadChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AnimationsState.JumpLoadChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsStateJumpLoadChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsStateJumpLoadChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsStateJumpTypeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AnimationsState.JumpTypeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsStateJumpTypeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsStateJumpTypeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAnimationsStateJumpMotorProblemChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AnimationsState.JumpMotorProblemChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAnimationsStateJumpMotorProblemChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAnimationsStateJumpMotorProblemChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Settings
    private native int nativeSetJumpingSumoSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.Settings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SettingsState
    private native int nativeSetJumpingSumoSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.SettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoSettingsStateProductGPSVersionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.SettingsState.ProductGPSVersionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSettingsStateProductGPSVersionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSettingsStateProductGPSVersionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecord
    private native int nativeSetJumpingSumoMediaRecordBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.MediaRecord.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordPictureBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecord.Picture.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordPictureBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordPictureBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordVideoBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecord.Video.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordVideoBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordVideoBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordPictureV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecord.PictureV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordPictureV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordPictureV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordVideoV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecord.VideoV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordVideoV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordVideoV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecordState
    private native int nativeSetJumpingSumoMediaRecordStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.MediaRecordState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordStatePictureStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecordState.PictureStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordStatePictureStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordStatePictureStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordStateVideoStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecordState.VideoStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordStateVideoStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordStateVideoStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordStatePictureStateChangedV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecordState.PictureStateChangedV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordStatePictureStateChangedV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordStatePictureStateChangedV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordStateVideoStateChangedV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecordState.VideoStateChangedV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordStateVideoStateChangedV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordStateVideoStateChangedV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecordEvent
    private native int nativeSetJumpingSumoMediaRecordEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.MediaRecordEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordEventPictureEventChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecordEvent.PictureEventChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordEventPictureEventChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordEventPictureEventChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaRecordEventVideoEventChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaRecordEvent.VideoEventChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaRecordEventVideoEventChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaRecordEventVideoEventChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkSettings
    private native int nativeSetJumpingSumoNetworkSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.NetworkSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkSettingsWifiSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkSettings.WifiSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkSettingsWifiSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkSettingsWifiSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkSettingsState
    private native int nativeSetJumpingSumoNetworkSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.NetworkSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkSettingsStateWifiSelectionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkSettingsState.WifiSelectionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkSettingsStateWifiSelectionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkSettingsStateWifiSelectionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Network
    private native int nativeSetJumpingSumoNetworkBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.Network.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkWifiScanBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Network.WifiScan.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkWifiScanBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkWifiScanBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkWifiAuthChannelBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.Network.WifiAuthChannel.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkWifiAuthChannelBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkWifiAuthChannelBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkState
    private native int nativeSetJumpingSumoNetworkStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.NetworkState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkStateWifiScanListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkState.WifiScanListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkStateWifiScanListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkStateWifiScanListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkStateAllWifiScanChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkState.AllWifiScanChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkStateAllWifiScanChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkStateAllWifiScanChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkStateWifiAuthChannelListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkState.WifiAuthChannelListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkStateWifiAuthChannelListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkStateWifiAuthChannelListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkStateAllWifiAuthChannelChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkState.AllWifiAuthChannelChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkStateAllWifiAuthChannelChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkStateAllWifiAuthChannelChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoNetworkStateLinkQualityChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.NetworkState.LinkQualityChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoNetworkStateLinkQualityChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoNetworkStateLinkQualityChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AudioSettings
    private native int nativeSetJumpingSumoAudioSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.AudioSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAudioSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAudioSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAudioSettingsMasterVolumeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AudioSettings.MasterVolume.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAudioSettingsMasterVolumeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAudioSettingsMasterVolumeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAudioSettingsThemeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AudioSettings.Theme.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAudioSettingsThemeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAudioSettingsThemeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AudioSettingsState
    private native int nativeSetJumpingSumoAudioSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.AudioSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAudioSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAudioSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAudioSettingsStateMasterVolumeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AudioSettingsState.MasterVolumeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAudioSettingsStateMasterVolumeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAudioSettingsStateMasterVolumeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoAudioSettingsStateThemeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.AudioSettingsState.ThemeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoAudioSettingsStateThemeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoAudioSettingsStateThemeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class RoadPlan
    private native int nativeSetJumpingSumoRoadPlanBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.RoadPlan.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanAllScriptsMetadataBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlan.AllScriptsMetadata.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanAllScriptsMetadataBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanAllScriptsMetadataBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanScriptUploadedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlan.ScriptUploaded.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanScriptUploadedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanScriptUploadedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanScriptDeleteBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlan.ScriptDelete.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanScriptDeleteBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanScriptDeleteBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanPlayScriptBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlan.PlayScript.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanPlayScriptBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanPlayScriptBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class RoadPlanState
    private native int nativeSetJumpingSumoRoadPlanStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.RoadPlanState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanStateScriptMetadataListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlanState.ScriptMetadataListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanStateScriptMetadataListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanStateScriptMetadataListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanStateAllScriptsMetadataChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlanState.AllScriptsMetadataChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanStateAllScriptsMetadataChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanStateAllScriptsMetadataChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanStateScriptUploadChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlanState.ScriptUploadChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanStateScriptUploadChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanStateScriptUploadChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanStateScriptDeleteChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlanState.ScriptDeleteChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanStateScriptDeleteChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanStateScriptDeleteChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoRoadPlanStatePlayScriptChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.RoadPlanState.PlayScriptChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoRoadPlanStatePlayScriptChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoRoadPlanStatePlayScriptChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SpeedSettings
    private native int nativeSetJumpingSumoSpeedSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.SpeedSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSpeedSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSpeedSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoSpeedSettingsOutdoorBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.SpeedSettings.Outdoor.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSpeedSettingsOutdoorBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSpeedSettingsOutdoorBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SpeedSettingsState
    private native int nativeSetJumpingSumoSpeedSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.SpeedSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSpeedSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSpeedSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoSpeedSettingsStateOutdoorChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.SpeedSettingsState.OutdoorChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoSpeedSettingsStateOutdoorChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoSpeedSettingsStateOutdoorChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaStreaming
    private native int nativeSetJumpingSumoMediaStreamingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.MediaStreaming.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaStreamingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaStreamingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaStreamingVideoEnableBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaStreaming.VideoEnable.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaStreamingVideoEnableBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaStreamingVideoEnableBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaStreamingState
    private native int nativeSetJumpingSumoMediaStreamingStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.MediaStreamingState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaStreamingStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaStreamingStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoMediaStreamingStateVideoEnableChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.MediaStreamingState.VideoEnableChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoMediaStreamingStateVideoEnableChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoMediaStreamingStateVideoEnableChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class VideoSettings
    private native int nativeSetJumpingSumoVideoSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.VideoSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoVideoSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoVideoSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoVideoSettingsAutorecordBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.VideoSettings.Autorecord.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoVideoSettingsAutorecordBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoVideoSettingsAutorecordBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class VideoSettingsState
    private native int nativeSetJumpingSumoVideoSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumo.VideoSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoVideoSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoVideoSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoVideoSettingsStateAutorecordChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumo.VideoSettingsState.AutorecordChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoVideoSettingsStateAutorecordChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoVideoSettingsStateAutorecordChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project JumpingSumoDebug
    // - Class VideoSettingsState
    private native int nativeSetJumpingSumoDebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Jump
    private native int nativeSetJumpingSumoDebugJumpBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.Jump.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugJumpBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugJumpBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugJumpSetJumpMotorBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.Jump.SetJumpMotor.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugJumpSetJumpMotorBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugJumpSetJumpMotorBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugJumpSetCameraOrientationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.Jump.SetCameraOrientation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugJumpSetCameraOrientationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugJumpSetCameraOrientationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Audio
    private native int nativeSetJumpingSumoDebugAudioBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.Audio.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugAudioBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugAudioBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugAudioPlaySoundWithNameBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.Audio.PlaySoundWithName.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugAudioPlaySoundWithNameBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugAudioPlaySoundWithNameBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Misc
    private native int nativeSetJumpingSumoDebugMiscBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.Misc.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugMiscBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugMiscBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugMiscDebugEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.Misc.DebugEvent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugMiscDebugEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugMiscDebugEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Animation
    private native int nativeSetJumpingSumoDebugAnimationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.Animation.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugAnimationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugAnimationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugAnimationPlayAnimationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.Animation.PlayAnimation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugAnimationPlayAnimationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugAnimationPlayAnimationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugAnimationAddCapOffsetBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.Animation.AddCapOffset.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugAnimationAddCapOffsetBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugAnimationAddCapOffsetBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class UserScript
    private native int nativeSetJumpingSumoDebugUserScriptBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.UserScript.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugUserScriptBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugUserScriptBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugUserScriptUserScriptUploadedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.UserScript.UserScriptUploaded.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugUserScriptUserScriptUploadedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugUserScriptUserScriptUploadedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class UserScriptState
    private native int nativeSetJumpingSumoDebugUserScriptStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands JumpingSumoDebug.UserScriptState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugUserScriptStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugUserScriptStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetJumpingSumoDebugUserScriptStateUserScriptParsedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command JumpingSumoDebug.UserScriptState.UserScriptParsed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setJumpingSumoDebugUserScriptStateUserScriptParsedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetJumpingSumoDebugUserScriptStateUserScriptParsedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project MiniDrone
    // - Class UserScriptState
    private native int nativeSetMiniDroneBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Piloting
    private native int nativeSetMiniDronePilotingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.Piloting.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingFlatTrimBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Piloting.FlatTrim.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingFlatTrimBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingFlatTrimBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingTakeOffBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Piloting.TakeOff.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingTakeOffBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingTakeOffBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingPCMDBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Piloting.PCMD.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingPCMDBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingPCMDBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingLandingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Piloting.Landing.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingLandingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingLandingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingEmergencyBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Piloting.Emergency.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingEmergencyBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingEmergencyBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingAutoTakeOffModeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Piloting.AutoTakeOffMode.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingAutoTakeOffModeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingAutoTakeOffModeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingState
    private native int nativeSetMiniDronePilotingStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.PilotingState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingStateFlatTrimChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingState.FlatTrimChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingStateFlatTrimChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingStateFlatTrimChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingStateFlyingStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingState.FlyingStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingStateFlyingStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingStateFlyingStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingStateAlertStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingState.AlertStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingStateAlertStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingStateAlertStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingStateAutoTakeOffModeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingState.AutoTakeOffModeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingStateAutoTakeOffModeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingStateAutoTakeOffModeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Animations
    private native int nativeSetMiniDroneAnimationsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.Animations.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneAnimationsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneAnimationsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneAnimationsFlipBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Animations.Flip.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneAnimationsFlipBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneAnimationsFlipBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneAnimationsCapBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Animations.Cap.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneAnimationsCapBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneAnimationsCapBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecord
    private native int nativeSetMiniDroneMediaRecordBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.MediaRecord.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneMediaRecordPictureBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.MediaRecord.Picture.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordPictureBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordPictureBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneMediaRecordPictureV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.MediaRecord.PictureV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordPictureV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordPictureV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecordState
    private native int nativeSetMiniDroneMediaRecordStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.MediaRecordState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneMediaRecordStatePictureStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.MediaRecordState.PictureStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordStatePictureStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordStatePictureStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneMediaRecordStatePictureStateChangedV2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.MediaRecordState.PictureStateChangedV2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordStatePictureStateChangedV2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordStatePictureStateChangedV2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MediaRecordEvent
    private native int nativeSetMiniDroneMediaRecordEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.MediaRecordEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneMediaRecordEventPictureEventChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.MediaRecordEvent.PictureEventChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneMediaRecordEventPictureEventChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneMediaRecordEventPictureEventChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingSettings
    private native int nativeSetMiniDronePilotingSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.PilotingSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingSettingsMaxAltitudeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingSettings.MaxAltitude.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingSettingsMaxAltitudeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingSettingsMaxAltitudeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingSettingsMaxTiltBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingSettings.MaxTilt.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingSettingsMaxTiltBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingSettingsMaxTiltBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class PilotingSettingsState
    private native int nativeSetMiniDronePilotingSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.PilotingSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingSettingsStateMaxAltitudeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingSettingsState.MaxAltitudeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingSettingsStateMaxAltitudeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingSettingsStateMaxAltitudeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDronePilotingSettingsStateMaxTiltChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.PilotingSettingsState.MaxTiltChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDronePilotingSettingsStateMaxTiltChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDronePilotingSettingsStateMaxTiltChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SpeedSettings
    private native int nativeSetMiniDroneSpeedSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.SpeedSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsMaxVerticalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettings.MaxVerticalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsMaxVerticalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsMaxVerticalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsMaxRotationSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettings.MaxRotationSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsMaxRotationSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsMaxRotationSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsWheelsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettings.Wheels.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsWheelsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsWheelsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsMaxHorizontalSpeedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettings.MaxHorizontalSpeed.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsMaxHorizontalSpeedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsMaxHorizontalSpeedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SpeedSettingsState
    private native int nativeSetMiniDroneSpeedSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.SpeedSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettingsState.MaxVerticalSpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsStateMaxRotationSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettingsState.MaxRotationSpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsStateMaxRotationSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsStateMaxRotationSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsStateWheelsChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettingsState.WheelsChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsStateWheelsChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsStateWheelsChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SpeedSettingsState.MaxHorizontalSpeedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Settings
    private native int nativeSetMiniDroneSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.Settings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSettingsCutOutModeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Settings.CutOutMode.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSettingsCutOutModeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSettingsCutOutModeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SettingsState
    private native int nativeSetMiniDroneSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.SettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSettingsStateProductMotorsVersionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SettingsState.ProductMotorsVersionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSettingsStateProductMotorsVersionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSettingsStateProductMotorsVersionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSettingsStateProductInertialVersionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SettingsState.ProductInertialVersionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSettingsStateProductInertialVersionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSettingsStateProductInertialVersionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneSettingsStateCutOutModeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.SettingsState.CutOutModeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneSettingsStateCutOutModeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneSettingsStateCutOutModeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class FloodControlState
    private native int nativeSetMiniDroneFloodControlStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.FloodControlState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneFloodControlStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneFloodControlStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneFloodControlStateFloodControlChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.FloodControlState.FloodControlChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneFloodControlStateFloodControlChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneFloodControlStateFloodControlChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GPS
    private native int nativeSetMiniDroneGPSBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.GPS.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneGPSBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneGPSBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneGPSControllerLatitudeForRunBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.GPS.ControllerLatitudeForRun.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneGPSControllerLatitudeForRunBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneGPSControllerLatitudeForRunBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneGPSControllerLongitudeForRunBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.GPS.ControllerLongitudeForRun.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneGPSControllerLongitudeForRunBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneGPSControllerLongitudeForRunBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Configuration
    private native int nativeSetMiniDroneConfigurationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDrone.Configuration.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneConfigurationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneConfigurationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneConfigurationControllerTypeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Configuration.ControllerType.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneConfigurationControllerTypeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneConfigurationControllerTypeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneConfigurationControllerNameBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDrone.Configuration.ControllerName.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneConfigurationControllerNameBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneConfigurationControllerNameBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project MiniDroneDebug
    // - Class Configuration
    private native int nativeSetMiniDroneDebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDroneDebug.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneDebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneDebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Debug
    private native int nativeSetMiniDroneDebugDebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands MiniDroneDebug.Debug.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneDebugDebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneDebugDebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneDebugDebugTest1Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDroneDebug.Debug.Test1.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneDebugDebugTest1Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneDebugDebugTest1Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneDebugDebugTest2Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDroneDebug.Debug.Test2.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneDebugDebugTest2Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneDebugDebugTest2Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetMiniDroneDebugDebugTest3Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command MiniDroneDebug.Debug.Test3.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setMiniDroneDebugDebugTest3Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetMiniDroneDebugDebugTest3Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project SkyController
    // - Class Debug
    private native int nativeSetSkyControllerBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class WifiState
    private native int nativeSetSkyControllerWifiStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.WifiState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiStateWifiListBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.WifiState.WifiList.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiStateWifiListBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiStateWifiListBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiStateConnexionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.WifiState.ConnexionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiStateConnexionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiStateConnexionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiStateWifiAuthChannelListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.WifiState.WifiAuthChannelListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiStateWifiAuthChannelListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiStateWifiAuthChannelListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiStateAllWifiAuthChannelChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.WifiState.AllWifiAuthChannelChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiStateAllWifiAuthChannelChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiStateAllWifiAuthChannelChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiStateWifiSignalChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.WifiState.WifiSignalChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiStateWifiSignalChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiStateWifiSignalChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Wifi
    private native int nativeSetSkyControllerWifiBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.Wifi.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiRequestWifiListBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Wifi.RequestWifiList.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiRequestWifiListBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiRequestWifiListBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiRequestCurrentWifiBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Wifi.RequestCurrentWifi.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiRequestCurrentWifiBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiRequestCurrentWifiBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiConnectToWifiBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Wifi.ConnectToWifi.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiConnectToWifiBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiConnectToWifiBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiForgetWifiBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Wifi.ForgetWifi.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiForgetWifiBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiForgetWifiBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerWifiWifiAuthChannelBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Wifi.WifiAuthChannel.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerWifiWifiAuthChannelBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerWifiWifiAuthChannelBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Device
    private native int nativeSetSkyControllerDeviceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.Device.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerDeviceRequestDeviceListBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Device.RequestDeviceList.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceRequestDeviceListBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceRequestDeviceListBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerDeviceRequestCurrentDeviceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Device.RequestCurrentDevice.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceRequestCurrentDeviceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceRequestCurrentDeviceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerDeviceConnectToDeviceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Device.ConnectToDevice.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceConnectToDeviceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceConnectToDeviceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class DeviceState
    private native int nativeSetSkyControllerDeviceStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.DeviceState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerDeviceStateDeviceListBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.DeviceState.DeviceList.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceStateDeviceListBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceStateDeviceListBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerDeviceStateConnexionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.DeviceState.ConnexionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDeviceStateConnexionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDeviceStateConnexionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Settings
    private native int nativeSetSkyControllerSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.Settings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSettingsAllSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Settings.AllSettings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsAllSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsAllSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSettingsResetBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Settings.Reset.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsResetBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsResetBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SettingsState
    private native int nativeSetSkyControllerSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.SettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSettingsStateAllSettingsChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SettingsState.AllSettingsChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsStateAllSettingsChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsStateAllSettingsChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSettingsStateResetChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SettingsState.ResetChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsStateResetChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsStateResetChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSettingsStateProductSerialChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SettingsState.ProductSerialChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsStateProductSerialChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsStateProductSerialChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSettingsStateProductVariantChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SettingsState.ProductVariantChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSettingsStateProductVariantChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSettingsStateProductVariantChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Common
    private native int nativeSetSkyControllerCommonBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.Common.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCommonBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCommonBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCommonAllStatesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Common.AllStates.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCommonAllStatesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCommonAllStatesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CommonState
    private native int nativeSetSkyControllerCommonStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.CommonState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCommonStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCommonStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCommonStateAllStatesChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.CommonState.AllStatesChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCommonStateAllStatesChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCommonStateAllStatesChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SkyControllerState
    private native int nativeSetSkyControllerSkyControllerStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.SkyControllerState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSkyControllerStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSkyControllerStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSkyControllerStateBatteryChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SkyControllerState.BatteryChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSkyControllerStateBatteryChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSkyControllerStateBatteryChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSkyControllerStateGpsFixChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SkyControllerState.GpsFixChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSkyControllerStateGpsFixChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSkyControllerStateGpsFixChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerSkyControllerStateGpsPositionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.SkyControllerState.GpsPositionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerSkyControllerStateGpsPositionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerSkyControllerStateGpsPositionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AccessPointSettings
    private native int nativeSetSkyControllerAccessPointSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.AccessPointSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAccessPointSettingsAccessPointSSIDBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AccessPointSettings.AccessPointSSID.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsAccessPointSSIDBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsAccessPointSSIDBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAccessPointSettingsAccessPointChannelBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AccessPointSettings.AccessPointChannel.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsAccessPointChannelBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsAccessPointChannelBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAccessPointSettingsWifiSelectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AccessPointSettings.WifiSelection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsWifiSelectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsWifiSelectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AccessPointSettingsState
    private native int nativeSetSkyControllerAccessPointSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.AccessPointSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAccessPointSettingsStateAccessPointSSIDChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AccessPointSettingsState.AccessPointSSIDChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsStateAccessPointSSIDChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsStateAccessPointSSIDChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAccessPointSettingsStateAccessPointChannelChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AccessPointSettingsState.AccessPointChannelChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsStateAccessPointChannelChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsStateAccessPointChannelChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAccessPointSettingsStateWifiSelectionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AccessPointSettingsState.WifiSelectionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAccessPointSettingsStateWifiSelectionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAccessPointSettingsStateWifiSelectionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Camera
    private native int nativeSetSkyControllerCameraBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.Camera.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCameraBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCameraBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCameraResetOrientationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Camera.ResetOrientation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCameraResetOrientationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCameraResetOrientationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GamepadInfos
    private native int nativeSetSkyControllerGamepadInfosBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.GamepadInfos.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerGamepadInfosBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerGamepadInfosBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerGamepadInfosGetGamepadControlsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.GamepadInfos.GetGamepadControls.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerGamepadInfosGetGamepadControlsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerGamepadInfosGetGamepadControlsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GamepadInfosState
    private native int nativeSetSkyControllerGamepadInfosStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.GamepadInfosState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerGamepadInfosStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerGamepadInfosStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerGamepadInfosStateGamepadControlBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.GamepadInfosState.GamepadControl.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerGamepadInfosStateGamepadControlBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerGamepadInfosStateGamepadControlBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerGamepadInfosStateAllGamepadControlsSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.GamepadInfosState.AllGamepadControlsSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerGamepadInfosStateAllGamepadControlsSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerGamepadInfosStateAllGamepadControlsSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ButtonMappings
    private native int nativeSetSkyControllerButtonMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.ButtonMappings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsGetCurrentButtonMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappings.GetCurrentButtonMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsGetCurrentButtonMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsGetCurrentButtonMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsGetAvailableButtonMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappings.GetAvailableButtonMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsGetAvailableButtonMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsGetAvailableButtonMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsSetButtonMappingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappings.SetButtonMapping.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsSetButtonMappingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsSetButtonMappingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsDefaultButtonMappingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappings.DefaultButtonMapping.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsDefaultButtonMappingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsDefaultButtonMappingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ButtonMappingsState
    private native int nativeSetSkyControllerButtonMappingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.ButtonMappingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsStateCurrentButtonMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappingsState.CurrentButtonMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsStateCurrentButtonMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsStateCurrentButtonMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappingsState.AllCurrentButtonMappingsSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsStateAvailableButtonMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappingsState.AvailableButtonMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsStateAvailableButtonMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsStateAvailableButtonMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonMappingsState.AllAvailableButtonsMappingsSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AxisMappings
    private native int nativeSetSkyControllerAxisMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.AxisMappings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsGetCurrentAxisMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappings.GetCurrentAxisMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsGetCurrentAxisMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsGetCurrentAxisMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsGetAvailableAxisMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappings.GetAvailableAxisMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsGetAvailableAxisMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsGetAvailableAxisMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsSetAxisMappingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappings.SetAxisMapping.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsSetAxisMappingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsSetAxisMappingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsDefaultAxisMappingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappings.DefaultAxisMapping.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsDefaultAxisMappingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsDefaultAxisMappingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AxisMappingsState
    private native int nativeSetSkyControllerAxisMappingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.AxisMappingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsStateCurrentAxisMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappingsState.CurrentAxisMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsStateCurrentAxisMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsStateCurrentAxisMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappingsState.AllCurrentAxisMappingsSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsStateAvailableAxisMappingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappingsState.AvailableAxisMappings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsStateAvailableAxisMappingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsStateAvailableAxisMappingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisMappingsState.AllAvailableAxisMappingsSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AxisFilters
    private native int nativeSetSkyControllerAxisFiltersBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.AxisFilters.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersGetCurrentAxisFiltersBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFilters.GetCurrentAxisFilters.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersGetCurrentAxisFiltersBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersGetCurrentAxisFiltersBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersGetPresetAxisFiltersBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFilters.GetPresetAxisFilters.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersGetPresetAxisFiltersBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersGetPresetAxisFiltersBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersSetAxisFilterBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFilters.SetAxisFilter.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersSetAxisFilterBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersSetAxisFilterBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersDefaultAxisFiltersBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFilters.DefaultAxisFilters.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersDefaultAxisFiltersBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersDefaultAxisFiltersBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AxisFiltersState
    private native int nativeSetSkyControllerAxisFiltersStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.AxisFiltersState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersStateCurrentAxisFiltersBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFiltersState.CurrentAxisFilters.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersStateCurrentAxisFiltersBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersStateCurrentAxisFiltersBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersStateAllCurrentFiltersSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFiltersState.AllCurrentFiltersSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersStateAllCurrentFiltersSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersStateAllCurrentFiltersSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersStatePresetAxisFiltersBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFiltersState.PresetAxisFilters.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersStatePresetAxisFiltersBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersStatePresetAxisFiltersBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerAxisFiltersStateAllPresetFiltersSentBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.AxisFiltersState.AllPresetFiltersSent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerAxisFiltersStateAllPresetFiltersSentBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerAxisFiltersStateAllPresetFiltersSentBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CoPiloting
    private native int nativeSetSkyControllerCoPilotingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.CoPiloting.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCoPilotingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCoPilotingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCoPilotingSetPilotingSourceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.CoPiloting.SetPilotingSource.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCoPilotingSetPilotingSourceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCoPilotingSetPilotingSourceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CoPilotingState
    private native int nativeSetSkyControllerCoPilotingStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.CoPilotingState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCoPilotingStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCoPilotingStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCoPilotingStatePilotingSourceBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.CoPilotingState.PilotingSource.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCoPilotingStatePilotingSourceBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCoPilotingStatePilotingSourceBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Calibration
    private native int nativeSetSkyControllerCalibrationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.Calibration.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCalibrationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCalibrationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.Calibration.EnableMagnetoCalibrationQualityUpdates.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CalibrationState
    private native int nativeSetSkyControllerCalibrationStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.CalibrationState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCalibrationStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCalibrationStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCalibrationStateMagnetoCalibrationStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.CalibrationState.MagnetoCalibrationState.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCalibrationStateMagnetoCalibrationStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCalibrationStateMagnetoCalibrationStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.CalibrationState.MagnetoCalibrationQualityUpdatesState.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ButtonEvents
    private native int nativeSetSkyControllerButtonEventsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyController.ButtonEvents.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonEventsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonEventsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerButtonEventsSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyController.ButtonEvents.Settings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerButtonEventsSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerButtonEventsSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project SkyControllerDebug
    // - Class ButtonEvents
    private native int nativeSetSkyControllerDebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyControllerDebug.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Debug
    private native int nativeSetSkyControllerDebugDebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands SkyControllerDebug.Debug.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDebugDebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDebugDebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetSkyControllerDebugDebugTest1Behavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command SkyControllerDebug.Debug.Test1.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setSkyControllerDebugDebugTest1Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetSkyControllerDebugDebugTest1Behavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project common
    // - Class Debug
    private native int nativeSetCommonBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Network
    private native int nativeSetCommonNetworkBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Network.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonNetworkBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonNetworkBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonNetworkDisconnectBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Network.Disconnect.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonNetworkDisconnectBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonNetworkDisconnectBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class NetworkEvent
    private native int nativeSetCommonNetworkEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.NetworkEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonNetworkEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonNetworkEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonNetworkEventDisconnectionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.NetworkEvent.Disconnection.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonNetworkEventDisconnectionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonNetworkEventDisconnectionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Settings
    private native int nativeSetCommonSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Settings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsAllSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Settings.AllSettings.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsAllSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsAllSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsResetBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Settings.Reset.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsResetBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsResetBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsProductNameBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Settings.ProductName.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsProductNameBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsProductNameBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsCountryBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Settings.Country.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsCountryBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsCountryBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsAutoCountryBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Settings.AutoCountry.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsAutoCountryBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsAutoCountryBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class SettingsState
    private native int nativeSetCommonSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.SettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateAllSettingsChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.AllSettingsChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateAllSettingsChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateAllSettingsChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateResetChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.ResetChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateResetChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateResetChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateProductNameChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.ProductNameChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateProductNameChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateProductNameChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateProductVersionChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.ProductVersionChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateProductVersionChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateProductVersionChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateProductSerialHighChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.ProductSerialHighChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateProductSerialHighChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateProductSerialHighChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateProductSerialLowChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.ProductSerialLowChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateProductSerialLowChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateProductSerialLowChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateCountryChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.CountryChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateCountryChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateCountryChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonSettingsStateAutoCountryChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.SettingsState.AutoCountryChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonSettingsStateAutoCountryChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonSettingsStateAutoCountryChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Common
    private native int nativeSetCommonCommonBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Common.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonAllStatesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Common.AllStates.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonAllStatesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonAllStatesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonCurrentDateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Common.CurrentDate.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonCurrentDateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonCurrentDateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonCurrentTimeBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Common.CurrentTime.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonCurrentTimeBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonCurrentTimeBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonRebootBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Common.Reboot.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonRebootBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonRebootBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CommonState
    private native int nativeSetCommonCommonStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.CommonState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateAllStatesChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.AllStatesChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateAllStatesChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateAllStatesChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateBatteryStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.BatteryStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateBatteryStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateBatteryStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateMassStorageStateListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.MassStorageStateListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateMassStorageStateListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateMassStorageStateListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateMassStorageInfoStateListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.MassStorageInfoStateListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateMassStorageInfoStateListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateMassStorageInfoStateListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateCurrentDateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.CurrentDateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateCurrentDateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateCurrentDateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateCurrentTimeChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.CurrentTimeChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateCurrentTimeChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateCurrentTimeChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateMassStorageInfoRemainingListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.MassStorageInfoRemainingListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateMassStorageInfoRemainingListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateMassStorageInfoRemainingListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateWifiSignalChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.WifiSignalChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateWifiSignalChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateWifiSignalChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateSensorsStatesListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.SensorsStatesListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateSensorsStatesListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateSensorsStatesListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateProductModelBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.ProductModel.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateProductModelBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateProductModelBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCommonStateCountryListKnownBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CommonState.CountryListKnown.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCommonStateCountryListKnownBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCommonStateCountryListKnownBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class OverHeat
    private native int nativeSetCommonOverHeatBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.OverHeat.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonOverHeatBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonOverHeatBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonOverHeatSwitchOffBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.OverHeat.SwitchOff.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonOverHeatSwitchOffBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonOverHeatSwitchOffBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonOverHeatVentilateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.OverHeat.Ventilate.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonOverHeatVentilateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonOverHeatVentilateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class OverHeatState
    private native int nativeSetCommonOverHeatStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.OverHeatState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonOverHeatStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonOverHeatStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonOverHeatStateOverHeatChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.OverHeatState.OverHeatChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonOverHeatStateOverHeatChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonOverHeatStateOverHeatChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonOverHeatStateOverHeatRegulationChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.OverHeatState.OverHeatRegulationChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonOverHeatStateOverHeatRegulationChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonOverHeatStateOverHeatRegulationChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Controller
    private native int nativeSetCommonControllerBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Controller.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonControllerBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonControllerBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonControllerIsPilotingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Controller.IsPiloting.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonControllerIsPilotingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonControllerIsPilotingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class WifiSettings
    private native int nativeSetCommonWifiSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.WifiSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonWifiSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonWifiSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonWifiSettingsOutdoorSettingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.WifiSettings.OutdoorSetting.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonWifiSettingsOutdoorSettingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonWifiSettingsOutdoorSettingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class WifiSettingsState
    private native int nativeSetCommonWifiSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.WifiSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonWifiSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonWifiSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonWifiSettingsStateOutdoorSettingsChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.WifiSettingsState.OutdoorSettingsChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonWifiSettingsStateOutdoorSettingsChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonWifiSettingsStateOutdoorSettingsChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Mavlink
    private native int nativeSetCommonMavlinkBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Mavlink.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonMavlinkStartBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Mavlink.Start.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkStartBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkStartBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonMavlinkPauseBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Mavlink.Pause.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkPauseBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkPauseBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonMavlinkStopBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Mavlink.Stop.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkStopBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkStopBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class MavlinkState
    private native int nativeSetCommonMavlinkStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.MavlinkState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonMavlinkStateMavlinkFilePlayingStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.MavlinkState.MavlinkFilePlayingStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkStateMavlinkFilePlayingStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkStateMavlinkFilePlayingStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonMavlinkStateMavlinkPlayErrorStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.MavlinkState.MavlinkPlayErrorStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonMavlinkStateMavlinkPlayErrorStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonMavlinkStateMavlinkPlayErrorStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Calibration
    private native int nativeSetCommonCalibrationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Calibration.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCalibrationMagnetoCalibrationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Calibration.MagnetoCalibration.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationMagnetoCalibrationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationMagnetoCalibrationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CalibrationState
    private native int nativeSetCommonCalibrationStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.CalibrationState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCalibrationStateMagnetoCalibrationStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CalibrationState.MagnetoCalibrationStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationStateMagnetoCalibrationStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCalibrationStateMagnetoCalibrationRequiredStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CalibrationState.MagnetoCalibrationRequiredState.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationRequiredStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationStateMagnetoCalibrationRequiredStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CalibrationState.MagnetoCalibrationAxisToCalibrateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCalibrationStateMagnetoCalibrationStartedChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CalibrationState.MagnetoCalibrationStartedChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationStartedChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCalibrationStateMagnetoCalibrationStartedChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class CameraSettingsState
    private native int nativeSetCommonCameraSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.CameraSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCameraSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCameraSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonCameraSettingsStateCameraSettingsChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.CameraSettingsState.CameraSettingsChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonCameraSettingsStateCameraSettingsChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonCameraSettingsStateCameraSettingsChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class GPS
    private native int nativeSetCommonGPSBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.GPS.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonGPSBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonGPSBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonGPSControllerPositionForRunBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.GPS.ControllerPositionForRun.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonGPSControllerPositionForRunBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonGPSControllerPositionForRunBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class FlightPlanState
    private native int nativeSetCommonFlightPlanStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.FlightPlanState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonFlightPlanStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonFlightPlanStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonFlightPlanStateAvailabilityStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.FlightPlanState.AvailabilityStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonFlightPlanStateAvailabilityStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonFlightPlanStateAvailabilityStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonFlightPlanStateComponentStateListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.FlightPlanState.ComponentStateListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonFlightPlanStateComponentStateListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonFlightPlanStateComponentStateListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class FlightPlanEvent
    private native int nativeSetCommonFlightPlanEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.FlightPlanEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonFlightPlanEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonFlightPlanEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonFlightPlanEventStartingErrorEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.FlightPlanEvent.StartingErrorEvent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonFlightPlanEventStartingErrorEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonFlightPlanEventStartingErrorEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonFlightPlanEventSpeedBridleEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.FlightPlanEvent.SpeedBridleEvent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonFlightPlanEventSpeedBridleEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonFlightPlanEventSpeedBridleEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ARLibsVersionsState
    private native int nativeSetCommonARLibsVersionsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.ARLibsVersionsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonARLibsVersionsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonARLibsVersionsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonARLibsVersionsStateControllerLibARCommandsVersionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ARLibsVersionsState.ControllerLibARCommandsVersion.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonARLibsVersionsStateControllerLibARCommandsVersionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonARLibsVersionsStateControllerLibARCommandsVersionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonARLibsVersionsStateSkyControllerLibARCommandsVersionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ARLibsVersionsState.SkyControllerLibARCommandsVersion.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonARLibsVersionsStateSkyControllerLibARCommandsVersionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonARLibsVersionsStateSkyControllerLibARCommandsVersionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonARLibsVersionsStateDeviceLibARCommandsVersionBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ARLibsVersionsState.DeviceLibARCommandsVersion.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonARLibsVersionsStateDeviceLibARCommandsVersionBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonARLibsVersionsStateDeviceLibARCommandsVersionBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Audio
    private native int nativeSetCommonAudioBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Audio.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAudioBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAudioBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAudioControllerReadyForStreamingBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Audio.ControllerReadyForStreaming.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAudioControllerReadyForStreamingBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAudioControllerReadyForStreamingBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AudioState
    private native int nativeSetCommonAudioStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.AudioState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAudioStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAudioStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAudioStateAudioStreamingRunningBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.AudioState.AudioStreamingRunning.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAudioStateAudioStreamingRunningBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAudioStateAudioStreamingRunningBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Headlights
    private native int nativeSetCommonHeadlightsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Headlights.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonHeadlightsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonHeadlightsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonHeadlightsIntensityBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Headlights.Intensity.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonHeadlightsIntensityBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonHeadlightsIntensityBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class HeadlightsState
    private native int nativeSetCommonHeadlightsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.HeadlightsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonHeadlightsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonHeadlightsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonHeadlightsStateIntensityChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.HeadlightsState.IntensityChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonHeadlightsStateIntensityChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonHeadlightsStateIntensityChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Animations
    private native int nativeSetCommonAnimationsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Animations.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAnimationsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAnimationsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAnimationsStartAnimationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Animations.StartAnimation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAnimationsStartAnimationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAnimationsStartAnimationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAnimationsStopAnimationBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Animations.StopAnimation.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAnimationsStopAnimationBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAnimationsStopAnimationBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAnimationsStopAllAnimationsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Animations.StopAllAnimations.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAnimationsStopAllAnimationsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAnimationsStopAllAnimationsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AnimationsState
    private native int nativeSetCommonAnimationsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.AnimationsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAnimationsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAnimationsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAnimationsStateListBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.AnimationsState.List.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAnimationsStateListBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAnimationsStateListBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Accessory
    private native int nativeSetCommonAccessoryBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Accessory.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAccessoryBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAccessoryBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAccessoryConfigBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Accessory.Config.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAccessoryConfigBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAccessoryConfigBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class AccessoryState
    private native int nativeSetCommonAccessoryStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.AccessoryState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAccessoryStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAccessoryStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAccessoryStateSupportedAccessoriesListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.AccessoryState.SupportedAccessoriesListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAccessoryStateSupportedAccessoriesListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAccessoryStateSupportedAccessoriesListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAccessoryStateAccessoryConfigChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.AccessoryState.AccessoryConfigChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAccessoryStateAccessoryConfigChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAccessoryStateAccessoryConfigChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonAccessoryStateAccessoryConfigModificationEnabledBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.AccessoryState.AccessoryConfigModificationEnabled.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonAccessoryStateAccessoryConfigModificationEnabledBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonAccessoryStateAccessoryConfigModificationEnabledBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class Charger
    private native int nativeSetCommonChargerBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.Charger.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonChargerSetMaxChargeRateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.Charger.SetMaxChargeRate.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerSetMaxChargeRateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerSetMaxChargeRateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ChargerState
    private native int nativeSetCommonChargerStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.ChargerState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonChargerStateMaxChargeRateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ChargerState.MaxChargeRateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerStateMaxChargeRateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerStateMaxChargeRateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonChargerStateCurrentChargeStateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ChargerState.CurrentChargeStateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerStateCurrentChargeStateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerStateCurrentChargeStateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonChargerStateLastChargeRateChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ChargerState.LastChargeRateChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerStateLastChargeRateChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerStateLastChargeRateChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonChargerStateChargingInfoBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.ChargerState.ChargingInfo.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonChargerStateChargingInfoBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonChargerStateChargingInfoBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class RunState
    private native int nativeSetCommonRunStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Common.RunState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonRunStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonRunStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonRunStateRunIdChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Common.RunState.RunIdChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonRunStateRunIdChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonRunStateRunIdChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project commonDebug
    // - Class RunState
    private native int nativeSetCommonDebugBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands CommonDebug.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Stats
    private native int nativeSetCommonDebugStatsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands CommonDebug.Stats.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugStatsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugStatsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugStatsSendPacketBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.Stats.SendPacket.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugStatsSendPacketBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugStatsSendPacketBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugStatsStartSendingPacketFromDroneBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.Stats.StartSendingPacketFromDrone.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugStatsStartSendingPacketFromDroneBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugStatsStartSendingPacketFromDroneBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugStatsStopSendingPacketFromDroneBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.Stats.StopSendingPacketFromDrone.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugStatsStopSendingPacketFromDroneBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugStatsStopSendingPacketFromDroneBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class StatsEvent
    private native int nativeSetCommonDebugStatsEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands CommonDebug.StatsEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugStatsEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugStatsEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugStatsEventSendPacketBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.StatsEvent.SendPacket.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugStatsEventSendPacketBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugStatsEventSendPacketBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class DebugSettings
    private native int nativeSetCommonDebugDebugSettingsBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands CommonDebug.DebugSettings.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugDebugSettingsBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugDebugSettingsBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugDebugSettingsGetAllBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.DebugSettings.GetAll.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugDebugSettingsGetAllBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugDebugSettingsGetAllBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugDebugSettingsSetBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.DebugSettings.Set.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugDebugSettingsSetBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugDebugSettingsSetBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class DebugSettingsState
    private native int nativeSetCommonDebugDebugSettingsStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands CommonDebug.DebugSettingsState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugDebugSettingsStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugDebugSettingsStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugDebugSettingsStateInfoBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.DebugSettingsState.Info.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugDebugSettingsStateInfoBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugDebugSettingsStateInfoBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetCommonDebugDebugSettingsStateListChangedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command CommonDebug.DebugSettingsState.ListChanged.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setCommonDebugDebugSettingsStateListChangedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetCommonDebugDebugSettingsStateListChangedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



    // Project pro
    // - Class DebugSettingsState
    private native int nativeSetProBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Pro.XXX.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    // - Class Pro
    private native int nativeSetProProBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Pro.Pro.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetProProBoughtFeaturesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Pro.Pro.BoughtFeatures.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProBoughtFeaturesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProBoughtFeaturesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetProProResponseBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Pro.Pro.Response.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProResponseBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProResponseBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetProProActivateFeaturesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Pro.Pro.ActivateFeatures.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProActivateFeaturesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProActivateFeaturesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ProState
    private native int nativeSetProProStateBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Pro.ProState.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProStateBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProStateBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetProProStateSupportedFeaturesBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Pro.ProState.SupportedFeatures.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProStateSupportedFeaturesBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProStateSupportedFeaturesBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetProProStateFeaturesActivatedBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Pro.ProState.FeaturesActivated.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProStateFeaturesActivatedBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProStateFeaturesActivatedBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }


    // - Class ProEvent
    private native int nativeSetProProEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for all commands Pro.ProEvent.XXX.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }

    private native int nativeSetProProEventChallengeEventBehavior (long cFilter, int behavior);
    /**
     * Sets the behavior for the command Pro.ProEvent.ChallengeEvent.
     * @param behavior The behavior to set.
     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.
     */
    public ARCOMMANDS_FILTER_ERROR_ENUM setProProEventChallengeEventBehavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {
        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }
        int cErr = nativeSetProProEventChallengeEventBehavior (this.cFilter, behavior.getValue());
        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);
    }



}
