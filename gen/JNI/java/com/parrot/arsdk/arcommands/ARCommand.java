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

import com.parrot.arsdk.arsal.ARNativeData;

/**
 * Java representation of a C ARCommand object.<br>
 * This class holds either app-generated objects, that are to be sent
 * to the device, or network-generated objects, that are to be decoded by
 * the application.
 * @author Parrot (c) 2013
 */
public class ARCommand extends ARNativeData {

    public static final int ARCOMMANDS_ARCOMMAND_HEADER_SIZE = 4;
    public static final boolean ARCOMMANDS_ARCOMMAND_HAS_DEBUG_COMMANDS = true;

    public static final int ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_FIRST = (1 << ARCOMMANDS_GENERIC_LIST_FLAGS_ENUM.FIRST.getValue());    ///< indicate it's the first element of the list.
    public static final int ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_LAST = (1 << ARCOMMANDS_GENERIC_LIST_FLAGS_ENUM.LAST.getValue());    ///< indicate it's the last element of the list.
    public static final int ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_EMPTY = (1 << ARCOMMANDS_GENERIC_LIST_FLAGS_ENUM.EMPTY.getValue());    ///< indicate the list is empty (implies First/Last). All other arguments should be ignored.
    public static final int ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_REMOVE = (1 << ARCOMMANDS_GENERIC_LIST_FLAGS_ENUM.REMOVE.getValue());    ///< This value should be removed from the existing list.

    public static final int ARCOMMANDS_FLAG_WIFI_BAND_2_4GHZ = (1 << ARCOMMANDS_WIFI_BAND_ENUM.BAND_2_4GHZ.getValue());    ///< 2.4 GHz band
    public static final int ARCOMMANDS_FLAG_WIFI_BAND_5GHZ = (1 << ARCOMMANDS_WIFI_BAND_ENUM.BAND_5GHZ.getValue());    ///< 5 GHz band

    public static final int ARCOMMANDS_FLAG_WIFI_ENVIRONEMENT_INDOOR = (1 << ARCOMMANDS_WIFI_ENVIRONEMENT_ENUM.INDOOR.getValue());    ///< indoor environement
    public static final int ARCOMMANDS_FLAG_WIFI_ENVIRONEMENT_OUTDOOR = (1 << ARCOMMANDS_WIFI_ENVIRONEMENT_ENUM.OUTDOOR.getValue());    ///< outdoor environement

    /**
     * Creates a new, empty ARCommand with the default size.<br>
     * This is a typical constructor for app-generated ARCommand.<br>
     * To optimize memory, the application can reuse an ARCommand
     * object after it was disposed.
     */
    public ARCommand () {
        super ();
    }


    /**
     * Creates a new, empty ARCommand with an user-specified size.<br>
     * This is a typical constructor for app-generated ARCommand.<br>
     * To optimize memory, the application can reuse an ARCommand
     * object after it was disposed.
     * @param capacity user specified capacity of the command buffer
     */
    public ARCommand (int capacity) {
        super (capacity);
    }

    /**
     * Creates a new ARCommand from another ARNativeData instance.<br>
     * This is a typical constructor for network-generated ARCommand.<br>
     * To optimize memory, the application can reuse an ARCommand
     * object after it was disposed.
     * @param oldData ARNativeData which contains original data
     */
    public ARCommand (ARNativeData oldData) {
        super (oldData);
    }

    /**
     * Creates a new ARCommand from a c pointer and size.<br>
     * To optimize memory, the application can reuse an ARCommand
     * object after it was disposed.
     * @param data The original data buffer to copy
     * @param dataSize The original data buffer size
     */
    public ARCommand (long data, int dataSize) {
        super (data, dataSize);
    }

    /**
     * Creates a new ARCommand from another ARNativeData, with a given minimum capacity.<br>
     * This is a typical constructor for network-generated ARCommand.<br>
     * To optimize memory, the application can reuse an ARCommand
     * object after it was disposed.
     * @param oldData ARNativeData which contains original data
     * @param capacity Minimum capacity of this object
     */
    public ARCommand (ARNativeData oldData, int capacity) {
        super (oldData, capacity);
    }

    /**
     * Describe a ARCommand.<br>
     * @return A String describing the ARCommand, with arguments values included
     */
    public String toString () {
        return nativeToString (pointer, used);
    }

    /**
     * Try to describe an ARNativeData as if it was an ARCommand.<br>
     * @return A String describing the ARNativeData, if possible as an ARCommand.
     */
    public static String arNativeDataToARCommandString (ARNativeData data) {
        if (data == null) { return "null"; }
        String ret = nativeStaticToString(data.getData(), data.getDataSize());
        if (ret == null) { ret = data.toString(); }
        return ret;
    }

    /**
     * Decodes the current ARCommand, calling commands listeners<br>
     * If a listener was set for the Class/Command contained within the ARCommand,
     * its <code>onClassCommandUpdate(...)</code> function will be called in the current thread.
     * @return An ARCOMMANDS_DECODER_ERROR_ENUM error code
     */
    public ARCOMMANDS_DECODER_ERROR_ENUM decode () {
        ARCOMMANDS_DECODER_ERROR_ENUM err = ARCOMMANDS_DECODER_ERROR_ENUM.ARCOMMANDS_DECODER_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeDecode (pointer, used);
        if (ARCOMMANDS_DECODER_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_DECODER_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>Default</code> in feature <code>Generic</code><br>
     * <br>
     * Feature Generic description:<br>
     * All generic messages<br>
     * <br>
     * Command Default description:<br>
     * default<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setGenericDefault () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetGenericDefault (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingFlatTrim</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command FlatTrim description:<br>
     * Do a flat trim<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingFlatTrim () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingFlatTrim (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingTakeOff</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command TakeOff description:<br>
     * Ask the drone to take off<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingTakeOff () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingTakeOff (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingPCMD</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command PCMD description:<br>
     * Ask the drone to move around.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _flag Boolean flag to activate roll/pitch movement
     * @param _roll Roll consign for the drone [-100;100]
     * @param _pitch Pitch consign for the drone [-100;100]
     * @param _yaw Yaw consign for the drone [-100;100]
     * @param _gaz Gaz consign for the drone [-100;100]
     * @param _timestampAndSeqNum Command timestamp in milliseconds (low 24 bits) + command sequence number [0;255] (high 8 bits).
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingPCMD (byte _flag, byte _roll, byte _pitch, byte _yaw, byte _gaz, int _timestampAndSeqNum) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingPCMD (pointer, capacity, _flag, _roll, _pitch, _yaw, _gaz, _timestampAndSeqNum);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingLanding</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command Landing description:<br>
     * Ask the drone to land<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingLanding () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingLanding (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingEmergency</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command Emergency description:<br>
     * Put drone in emergency user state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingEmergency () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingEmergency (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingNavigateHome</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command NavigateHome description:<br>
     * Ask the drone to fly to home<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start the navigate home, 0 to stop it
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingNavigateHome (byte _start) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingNavigateHome (pointer, capacity, _start);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingAutoTakeOffMode</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command AutoTakeOffMode description:<br>
     * @deprecated<br>
     * [NOT USED] Set Drone3 in automatic take off mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of automatic take off mode (1 for autotake off enabled)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingAutoTakeOffMode (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingAutoTakeOffMode (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingMoveBy</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command MoveBy description:<br>
     * [DRAFT] Move the drone to a relative position and rotate heading by a given angle<br>
     * The frame is horizontal and relative to the current drone orientation:<br>
     * - X is front<br>
     * - Y is right<br>
     * - Z is down<br>
     * The movement settings of the device are those set for the autonomous flight.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _dX Wanted displacement along the front axis [m]
     * @param _dY Wanted displacement along the right axis [m]
     * @param _dZ Wanted displacement along the down axis [m]
     * @param _dPsi Wanted rotation of heading  [rad]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingMoveBy (float _dX, float _dY, float _dZ, float _dPsi) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingMoveBy (pointer, capacity, _dX, _dY, _dZ, _dPsi);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingUserTakeOff</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command UserTakeOff description:<br>
     * Set drone in user take off state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of user take off mode
     * @param _state - 1 to enter in user take off.
     * @param _state - 0 to exit from user take off.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingUserTakeOff (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingUserTakeOff (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingCircle</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the totoDrone<br>
     * <br>
     * Command Circle description:<br>
     * Ask Fixed wings to circle<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _direction The circling direction
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingCircle (ARCOMMANDS_ARDRONE3_PILOTING_CIRCLE_DIRECTION_ENUM _direction) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingCircle (pointer, capacity, _direction);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsFlip</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command Flip description:<br>
     * Make a flip<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _direction Direction for the flip
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3AnimationsFlip (ARCOMMANDS_ARDRONE3_ANIMATIONS_FLIP_DIRECTION_ENUM _direction) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3AnimationsFlip (pointer, capacity, _direction);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CameraOrientation</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Camera description:<br>
     * Ask the drone to move camera<br>
     * <br>
     * Command Orientation description:<br>
     * Ask the drone to move camera.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _tilt Tilt camera consign for the drone (in degree)
     * @param _tilt The value is saturated by the drone.
     * @param _tilt Saturation value is sent by thre drone through CameraSettingsChanged command.
     * @param _pan Pan camera consign for the drone (in degree)
     * @param _pan The value is saturated by the drone.
     * @param _pan Saturation value is sent by thre drone through CameraSettingsChanged command.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3CameraOrientation (byte _tilt, byte _pan) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3CameraOrientation (pointer, capacity, _tilt, _pan);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordPicture</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command Picture description:<br>
     * @deprecated<br>
     * Take picture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mass_storage_id Mass storage id to take picture
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordPicture (byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordPicture (pointer, capacity, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordVideo</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command Video description:<br>
     * @deprecated<br>
     * Video record<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _record Command to record video
     * @param _mass_storage_id Mass storage id to record
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordVideo (ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEO_RECORD_ENUM _record, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordVideo (pointer, capacity, _record, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordPictureV2</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command PictureV2 description:<br>
     * Take picture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordPictureV2 () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordPictureV2 (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordVideoV2</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command VideoV2 description:<br>
     * Video record<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _record Command to record video
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordVideoV2 (ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM _record) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordVideoV2 (pointer, capacity, _record);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkWifiScan</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Network description:<br>
     * Network related commands<br>
     * <br>
     * Command WifiScan description:<br>
     * Launches wifi network scan<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band The band(s) : 2.4 Ghz, 5 Ghz, or both
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkWifiScan (ARCOMMANDS_ARDRONE3_NETWORK_WIFISCAN_BAND_ENUM _band) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkWifiScan (pointer, capacity, _band);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkWifiAuthChannel</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Network description:<br>
     * Network related commands<br>
     * <br>
     * Command WifiAuthChannel description:<br>
     * Controller inquire the list of authorized wifi channels.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkWifiAuthChannel () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkWifiAuthChannel (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsMaxAltitude</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command MaxAltitude description:<br>
     * Set Max Altitude<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current altitude max in m
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsMaxAltitude (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsMaxAltitude (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsMaxTilt</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command MaxTilt description:<br>
     * Set Max Tilt<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current tilt max in degree
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsMaxTilt (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsMaxTilt (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsAbsolutControl</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command AbsolutControl description:<br>
     * @deprecated<br>
     * Enable/Disable absolut control<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _on 1 to enable, 0 to disable
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsAbsolutControl (byte _on) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsAbsolutControl (pointer, capacity, _on);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsMaxDistance</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command MaxDistance description:<br>
     * Set the distance max of the drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value Current max distance in meter
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsMaxDistance (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsMaxDistance (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsNoFlyOverMaxDistance</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command NoFlyOverMaxDistance description:<br>
     * Indication about how the product handle flying over the max distance limitation<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _shouldNotFlyOver 1 if the drone can't fly further than max distance, 0 if no limitation on the drone should be done
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsNoFlyOverMaxDistance (byte _shouldNotFlyOver) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsNoFlyOverMaxDistance (pointer, capacity, _shouldNotFlyOver);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsSetAutonomousFlightMaxHorizontalSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command SetAutonomousFlightMaxHorizontalSpeed description:<br>
     * [NOT USED] Set the maximum horizontal speed used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum horizontal speed [m/s]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeed (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeed (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsSetAutonomousFlightMaxVerticalSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command SetAutonomousFlightMaxVerticalSpeed description:<br>
     * [NOT USED] Set the maximum vertical speed used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum vertical speed [m/s]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeed (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeed (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsSetAutonomousFlightMaxHorizontalAcceleration</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command SetAutonomousFlightMaxHorizontalAcceleration description:<br>
     * [NOT USED] Set the maximum horizontal acceleration used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum horizontal acceleration [m/s2]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAcceleration (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAcceleration (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsSetAutonomousFlightMaxVerticalAcceleration</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command SetAutonomousFlightMaxVerticalAcceleration description:<br>
     * [NOT USED] Set the maximum vertical acceleration used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum vertical acceleration [m/s2]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAcceleration (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAcceleration (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsSetAutonomousFlightMaxRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command SetAutonomousFlightMaxRotationSpeed description:<br>
     * [NOT USED] Set the maximum yaw rotation speed used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum yaw rotation speed [rad/s]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeed (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeed (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsBankedTurn</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command BankedTurn description:<br>
     * Enable / Disable Banked Turn mode.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value 1 to enable, 0 to disable
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsBankedTurn (byte _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsBankedTurn (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsMinAltitude</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command MinAltitude description:<br>
     * Set Min Altitude<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current altitude min in m
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsMinAltitude (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsMinAltitude (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsCirclingDirection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command CirclingDirection description:<br>
     * Set Fixed wings circling default direction<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The circling direction
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsCirclingDirection (ARCOMMANDS_ARDRONE3_PILOTINGSETTINGS_CIRCLINGDIRECTION_VALUE_ENUM _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsCirclingDirection (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsCirclingRadius</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command CirclingRadius description:<br>
     * Set Fixed wings circling radius<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The circling radius in meter
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsCirclingRadius (short _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsCirclingRadius (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsCirclingAltitude</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command CirclingAltitude description:<br>
     * Set Fixed wings circling altitude<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The circling altitude in meter
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsCirclingAltitude (short _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsCirclingAltitude (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsPitchMode</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command PitchMode description:<br>
     * Set pitch mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The Pitch mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsPitchMode (ARCOMMANDS_ARDRONE3_PILOTINGSETTINGS_PITCHMODE_VALUE_ENUM _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsPitchMode (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsLandingMode</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command LandingMode description:<br>
     * Set fixed wings Landing Mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The Landing mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsLandingMode (ARCOMMANDS_ARDRONE3_PILOTINGSETTINGS_LANDINGMODE_VALUE_ENUM _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsLandingMode (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsMaxVerticalSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command MaxVerticalSpeed description:<br>
     * Set Max Vertical speed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max vertical speed in m/s
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsMaxVerticalSpeed (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsMaxVerticalSpeed (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsMaxRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command MaxRotationSpeed description:<br>
     * Set Max Yaw Rotation speed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max yaw rotation speed in degree/s
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsMaxRotationSpeed (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsMaxRotationSpeed (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsHullProtection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command HullProtection description:<br>
     * Presence of hull protection<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _present 1 if present, 0 if not present
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsHullProtection (byte _present) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsHullProtection (pointer, capacity, _present);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsOutdoor</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command Outdoor description:<br>
     * @deprecated<br>
     * Outdoor property<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _outdoor 1 if outdoor flight, 0 if indoor flight
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsOutdoor (byte _outdoor) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsOutdoor (pointer, capacity, _outdoor);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsMaxPitchRollRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command MaxPitchRollRotationSpeed description:<br>
     * Set Max Pitch/Rool Rotation speed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max pitch/roll rotation speed in degree/s
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsMaxPitchRollRotationSpeed (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsMaxPitchRollRotationSpeed (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsWifiSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkSettings description:<br>
     * Network settings commands<br>
     * <br>
     * Command WifiSelection description:<br>
     * Auto-select channel of choosen band<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi selection (auto, manual)
     * @param _band The allowed band(s) : 2.4 Ghz, 5 Ghz, or all
     * @param _channel The channel (not used in auto mode)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkSettingsWifiSelection (ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISELECTION_TYPE_ENUM _type, ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISELECTION_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkSettingsWifiSelection (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsWifiSecurity</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkSettings description:<br>
     * Network settings commands<br>
     * <br>
     * Command WifiSecurity description:<br>
     * Sent by the controller to set the wifi security<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi security (open, wpa2)
     * @param _key The key to secure the network (empty if type is open)
     * @param _keyType Type of the key
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkSettingsWifiSecurity (ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_ENUM _type, String _key, ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_ENUM _keyType) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkSettingsWifiSecurity (pointer, capacity, _type, _key, _keyType);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsPictureFormatSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command PictureFormatSelection description:<br>
     * The format of the photo<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of photo format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsPictureFormatSelection (ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsPictureFormatSelection (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsAutoWhiteBalanceSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command AutoWhiteBalanceSelection description:<br>
     * AutoWhiteBalance mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type auto white balance
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsAutoWhiteBalanceSelection (ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsAutoWhiteBalanceSelection (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsExpositionSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command ExpositionSelection description:<br>
     * The exposition of the image<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value Exposition value (bounds given by ExpositionChanged arg min and max, by default [-3:3])
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsExpositionSelection (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsExpositionSelection (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsSaturationSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command SaturationSelection description:<br>
     * The saturation of the image<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value Saturation value (bounds given by SaturationChanged arg min and max, by default [-100:100])
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsSaturationSelection (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsSaturationSelection (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsTimelapseSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command TimelapseSelection description:<br>
     * Picture taken periodically<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 1 if timelapse is enabled, 0 otherwise
     * @param _interval interval in seconds for taking pictures
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsTimelapseSelection (byte _enabled, float _interval) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsTimelapseSelection (pointer, capacity, _enabled, _interval);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsVideoAutorecordSelection</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command VideoAutorecordSelection description:<br>
     * Video autorecord<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 1 if video autorecord is enabled, 0 otherwise
     * @param _mass_storage_id Mass storage id to take video
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsVideoAutorecordSelection (byte _enabled, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsVideoAutorecordSelection (pointer, capacity, _enabled, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsVideoStabilizationMode</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettings description:<br>
     * Photo settings chosen by the user<br>
     * <br>
     * Command VideoStabilizationMode description:<br>
     * Set Video stabilization mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mode Video stabilization mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsVideoStabilizationMode (ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOSTABILIZATIONMODE_MODE_ENUM _mode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsVideoStabilizationMode (pointer, capacity, _mode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaStreamingVideoEnable</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaStreaming description:<br>
     * Control media streaming behavior.<br>
     * <br>
     * Command VideoEnable description:<br>
     * Enable/disable video streaming.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enable 1 to enable, 0 to disable.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaStreamingVideoEnable (byte _enable) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaStreamingVideoEnable (pointer, capacity, _enable);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsSetHome</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettings description:<br>
     * GPS settings<br>
     * <br>
     * Command SetHome description:<br>
     * @deprecated<br>
     * Set home location<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Home latitude in decimal degrees
     * @param _longitude Home longitude in decimal degrees
     * @param _altitude Home altitude in meters
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsSetHome (double _latitude, double _longitude, double _altitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsSetHome (pointer, capacity, _latitude, _longitude, _altitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsResetHome</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettings description:<br>
     * GPS settings<br>
     * <br>
     * Command ResetHome description:<br>
     * Reset home location and let the drone make its own home<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsResetHome () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsResetHome (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsSendControllerGPS</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettings description:<br>
     * GPS settings<br>
     * <br>
     * Command SendControllerGPS description:<br>
     * send controller GPS location<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude GPS latitude in decimal degrees
     * @param _longitude GPS longitude in decimal degrees
     * @param _altitude GPS altitude in meters
     * @param _horizontalAccuracy Horizontal Accuracy in meter ; equal -1 if no horizontal Accuracy
     * @param _verticalAccuracy Vertical Accuracy in meter ; equal -1 if no vertical Accuracy
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsSendControllerGPS (double _latitude, double _longitude, double _altitude, double _horizontalAccuracy, double _verticalAccuracy) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsSendControllerGPS (pointer, capacity, _latitude, _longitude, _altitude, _horizontalAccuracy, _verticalAccuracy);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsHomeType</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettings description:<br>
     * GPS settings<br>
     * <br>
     * Command HomeType description:<br>
     * Set user preference for the type of the home position. Note that this is only a preference<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of the home position
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsHomeType (ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsHomeType (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsReturnHomeDelay</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettings description:<br>
     * GPS settings<br>
     * <br>
     * Command ReturnHomeDelay description:<br>
     * Set the delay after which the drone will automatically try to return home<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _delay Delay in second
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsReturnHomeDelay (short _delay) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsReturnHomeDelay (pointer, capacity, _delay);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AntiflickeringElectricFrequency</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Antiflickering description:<br>
     * Anti-flickering related commands<br>
     * <br>
     * Command ElectricFrequency description:<br>
     * Electric frequency of the country determined by the position of the controller<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _frequency Type of the electric frequency
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3AntiflickeringElectricFrequency (ARCOMMANDS_ARDRONE3_ANTIFLICKERING_ELECTRICFREQUENCY_FREQUENCY_ENUM _frequency) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3AntiflickeringElectricFrequency (pointer, capacity, _frequency);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AntiflickeringSetMode</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class Antiflickering description:<br>
     * Anti-flickering related commands<br>
     * <br>
     * Command SetMode description:<br>
     * Set the anti flickering mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mode Mode of the anti flickering functionnality
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3AntiflickeringSetMode (ARCOMMANDS_ARDRONE3_ANTIFLICKERING_SETMODE_MODE_ENUM _mode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3AntiflickeringSetMode (pointer, capacity, _mode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStatePictureStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command PictureStateChanged description:<br>
     * @deprecated<br>
     * State of picture recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state 1 if picture has been taken, 0 otherwise
     * @param _mass_storage_id Mass storage id where the picture was recorded
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordStatePictureStateChanged (byte _state, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordStatePictureStateChanged (pointer, capacity, _state, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStateVideoStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command VideoStateChanged description:<br>
     * @deprecated<br>
     * State of video recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of video
     * @param _mass_storage_id Mass storage id where the video was recorded
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordStateVideoStateChanged (ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGED_STATE_ENUM _state, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordStateVideoStateChanged (pointer, capacity, _state, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStatePictureStateChangedV2</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command PictureStateChangedV2 description:<br>
     * State of device picture recording changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of device picture recording
     * @param _error Error to explain the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordStatePictureStateChangedV2 (ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM _state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordStatePictureStateChangedV2 (pointer, capacity, _state, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStateVideoStateChangedV2</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command VideoStateChangedV2 description:<br>
     * State of device video recording changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of device video recording
     * @param _error Error to explain the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordStateVideoStateChangedV2 (ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM _state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordStateVideoStateChangedV2 (pointer, capacity, _state, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordEventPictureEventChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecordEvent description:<br>
     * Events of media recording<br>
     * <br>
     * Command PictureEventChanged description:<br>
     * Event of picture recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _event Last event of picture recording
     * @param _error Error to explain the event
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordEventPictureEventChanged (ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM _event, ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordEventPictureEventChanged (pointer, capacity, _event, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordEventVideoEventChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaRecordEvent description:<br>
     * Events of media recording<br>
     * <br>
     * Command VideoEventChanged description:<br>
     * Event of video recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _event Event of video recording
     * @param _error Error to explain the event
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaRecordEventVideoEventChanged (ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_VIDEOEVENTCHANGED_EVENT_ENUM _event, ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_VIDEOEVENTCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaRecordEventVideoEventChanged (pointer, capacity, _event, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateFlatTrimChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command FlatTrimChanged description:<br>
     * Drone acknowledges that flat trim was correctly processed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateFlatTrimChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateFlatTrimChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateFlyingStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command FlyingStateChanged description:<br>
     * Drone flying state changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state Drone flying state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateFlyingStateChanged (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateFlyingStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAlertStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command AlertStateChanged description:<br>
     * Drone alert state changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state Drone alert state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateAlertStateChanged (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateAlertStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateNavigateHomeStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command NavigateHomeStateChanged description:<br>
     * Navigating home state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of navigate home
     * @param _reason Reason of the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateNavigateHomeStateChanged (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM _state, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM _reason) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateNavigateHomeStateChanged (pointer, capacity, _state, _reason);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStatePositionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command PositionChanged description:<br>
     * Drone position changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Latitude position in decimal degrees (500.0 if not available)
     * @param _longitude Longitude position in decimal degrees (500.0 if not available)
     * @param _altitude Altitude in meters (from GPS)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStatePositionChanged (double _latitude, double _longitude, double _altitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStatePositionChanged (pointer, capacity, _latitude, _longitude, _altitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command SpeedChanged description:<br>
     * Drone speed changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speedX Speed on the x axis (when drone moves forward, speed is > 0) (in m/s)
     * @param _speedY Speed on the y axis (when drone moves to right, speed is > 0) (in m/s)
     * @param _speedZ Speed on the z axis (when drone moves down, speed is > 0) (in m/s)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateSpeedChanged (float _speedX, float _speedY, float _speedZ) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateSpeedChanged (pointer, capacity, _speedX, _speedY, _speedZ);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAttitudeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command AttitudeChanged description:<br>
     * Drone attitude changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _roll roll value (in radian)
     * @param _pitch Pitch value (in radian)
     * @param _yaw Yaw value (in radian)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateAttitudeChanged (float _roll, float _pitch, float _yaw) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateAttitudeChanged (pointer, capacity, _roll, _pitch, _yaw);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAutoTakeOffModeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command AutoTakeOffModeChanged description:<br>
     * @deprecated<br>
     * Status of the drone3 automatic take off mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of automatic take off mode (1 if enabled)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateAutoTakeOffModeChanged (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateAutoTakeOffModeChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * State from drone<br>
     * <br>
     * Command AltitudeChanged description:<br>
     * Drone altitude changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _altitude Altitude in meters
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingStateAltitudeChanged (double _altitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingStateAltitudeChanged (pointer, capacity, _altitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingEventMoveByEnd</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingEvent description:<br>
     * Events of Piloting<br>
     * <br>
     * Command MoveByEnd description:<br>
     * [NOT USED] End of relative displacement of the drone<br>
     * The frame is horizontal and relative to the current drone orientation:<br>
     * - X is front<br>
     * - Y is right<br>
     * - Z is down<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _dX Distance traveled along the front axis [m]
     * @param _dY Distance traveled along the right axis [m]
     * @param _dZ Distance traveled along the down axis [m]
     * @param _dPsi Applied angle on heading  [rad]
     * @param _error Error to explain the event
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingEventMoveByEnd (float _dX, float _dY, float _dZ, float _dPsi, ARCOMMANDS_ARDRONE3_PILOTINGEVENT_MOVEBYEND_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingEventMoveByEnd (pointer, capacity, _dX, _dY, _dZ, _dPsi, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateWifiScanListChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command WifiScanListChanged description:<br>
     * One scanning result found<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid SSID of the AP
     * @param _rssi RSSI of the AP in dbm (negative value)
     * @param _band The band : 2.4 GHz or 5 GHz
     * @param _channel Channel of the AP
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkStateWifiScanListChanged (String _ssid, short _rssi, ARCOMMANDS_ARDRONE3_NETWORKSTATE_WIFISCANLISTCHANGED_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkStateWifiScanListChanged (pointer, capacity, _ssid, _rssi, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateAllWifiScanChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command AllWifiScanChanged description:<br>
     * State sent when all scanning result sent<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkStateAllWifiScanChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkStateAllWifiScanChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateWifiAuthChannelListChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command WifiAuthChannelListChanged description:<br>
     * Notify of an Authorized Channel.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band The band of this channel : 2.4 GHz or 5 GHz
     * @param _channel The authorized channel.
     * @param _in_or_out Bit 0 is 1 if channel is authorized outside (0 otherwise) ; Bit 1 is 1 if channel is authorized inside (0 otherwise)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkStateWifiAuthChannelListChanged (ARCOMMANDS_ARDRONE3_NETWORKSTATE_WIFIAUTHCHANNELLISTCHANGED_BAND_ENUM _band, byte _channel, byte _in_or_out) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkStateWifiAuthChannelListChanged (pointer, capacity, _band, _channel, _in_or_out);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateAllWifiAuthChannelChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command AllWifiAuthChannelChanged description:<br>
     * Notify the end of the list of Authorized wifi Channel.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkStateAllWifiAuthChannelChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkStateAllWifiAuthChannelChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateMaxAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command MaxAltitudeChanged description:<br>
     * Max Altitude sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current altitude max
     * @param _min Range min of altitude
     * @param _max Range max of altitude
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateMaxAltitudeChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateMaxAltitudeChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateMaxTiltChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command MaxTiltChanged description:<br>
     * Max tilt sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max tilt
     * @param _min Range min of tilt
     * @param _max Range max of tilt
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateMaxTiltChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateMaxTiltChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateAbsolutControlChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command AbsolutControlChanged description:<br>
     * @deprecated<br>
     * Absolut control boolean sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _on 1 if enabled, 0 if disabled
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateAbsolutControlChanged (byte _on) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateAbsolutControlChanged (pointer, capacity, _on);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateMaxDistanceChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command MaxDistanceChanged description:<br>
     * Max distance sent by the drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max distance in meter
     * @param _min Minimal possible max distance
     * @param _max Maximal possible max distance
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateMaxDistanceChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateMaxDistanceChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateNoFlyOverMaxDistanceChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command NoFlyOverMaxDistanceChanged description:<br>
     * Indication about how the product handle flying over the max distance limitation<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _shouldNotFlyOver 1 if the drone won't fly further than max distance, 0 if no limitation on the drone will be done
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChanged (byte _shouldNotFlyOver) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChanged (pointer, capacity, _shouldNotFlyOver);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateAutonomousFlightMaxHorizontalSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command AutonomousFlightMaxHorizontalSpeed description:<br>
     * [NOT USED] Maximum horizontal speed used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum horizontal speed [m/s]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeed (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeed (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateAutonomousFlightMaxVerticalSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command AutonomousFlightMaxVerticalSpeed description:<br>
     * [NOT USED] Maximum vertical speed used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum vertical speed [m/s]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeed (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeed (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateAutonomousFlightMaxHorizontalAcceleration</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command AutonomousFlightMaxHorizontalAcceleration description:<br>
     * [NOT USED] Maximum horizontal acceleration used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum horizontal acceleration [m/s2]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAcceleration (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAcceleration (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateAutonomousFlightMaxVerticalAcceleration</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command AutonomousFlightMaxVerticalAcceleration description:<br>
     * [NOT USED] Maximum vertical acceleration used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum vertical acceleration [m/s2]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAcceleration (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAcceleration (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateAutonomousFlightMaxRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command AutonomousFlightMaxRotationSpeed description:<br>
     * [NOT USED] Maximum yaw rotation speed used by the autonomous flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value maximum yaw rotation speed [rad/s]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeed (float _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeed (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateBankedTurnChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command BankedTurnChanged description:<br>
     * Banked Turn state.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state 1 if enabled, 0 if disabled
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateBankedTurnChanged (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateBankedTurnChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateMinAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command MinAltitudeChanged description:<br>
     * Min Altitude sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current altitude min
     * @param _min Range min of altitude min
     * @param _max Range max of altitude min
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateMinAltitudeChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateMinAltitudeChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateCirclingDirectionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command CirclingDirectionChanged description:<br>
     * Fixed wings circling default direction changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The circling direction
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateCirclingDirectionChanged (ARCOMMANDS_ARDRONE3_PILOTINGSETTINGSSTATE_CIRCLINGDIRECTIONCHANGED_VALUE_ENUM _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateCirclingDirectionChanged (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateCirclingRadiusChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command CirclingRadiusChanged description:<br>
     * Fixed wings circling radius changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current The current circling radius in meter
     * @param _min Range min of circling radius in meter
     * @param _max Range max of circling radius in meter
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateCirclingRadiusChanged (short _current, short _min, short _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateCirclingRadiusChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateCirclingAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command CirclingAltitudeChanged description:<br>
     * Fixed wings circling altitude changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current The current circling altitude in meter
     * @param _min Range min of circling altitude in meter
     * @param _max Range max of circling altitude in meter
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateCirclingAltitudeChanged (short _current, short _min, short _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateCirclingAltitudeChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStatePitchModeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command PitchModeChanged description:<br>
     * Pitch mode changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The Pitch mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStatePitchModeChanged (ARCOMMANDS_ARDRONE3_PILOTINGSETTINGSSTATE_PITCHMODECHANGED_VALUE_ENUM _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStatePitchModeChanged (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateLandingModeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command LandingModeChanged description:<br>
     * Fixed wings Landing Mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value The Landing mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PilotingSettingsStateLandingModeChanged (ARCOMMANDS_ARDRONE3_PILOTINGSETTINGSSTATE_LANDINGMODECHANGED_VALUE_ENUM _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PilotingSettingsStateLandingModeChanged (pointer, capacity, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateMaxVerticalSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command MaxVerticalSpeedChanged description:<br>
     * Max vertical speed sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max vertical speed in m/s
     * @param _min Range min of vertical speed
     * @param _max Range max of vertical speed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsStateMaxVerticalSpeedChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsStateMaxVerticalSpeedChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateMaxRotationSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command MaxRotationSpeedChanged description:<br>
     * Max yaw rotation speed sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max yaw rotation speed in degree/s
     * @param _min Range min of yaw rotation speed
     * @param _max Range max of yaw rotation speed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsStateMaxRotationSpeedChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsStateMaxRotationSpeedChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateHullProtectionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command HullProtectionChanged description:<br>
     * Presence of hull protection sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _present 1 if present, 0 if not present
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsStateHullProtectionChanged (byte _present) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsStateHullProtectionChanged (pointer, capacity, _present);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateOutdoorChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command OutdoorChanged description:<br>
     * @deprecated<br>
     * Outdoor property sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _outdoor 1 if outdoor flight, 0 if indoor flight
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsStateOutdoorChanged (byte _outdoor) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsStateOutdoorChanged (pointer, capacity, _outdoor);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateMaxPitchRollRotationSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command MaxPitchRollRotationSpeedChanged description:<br>
     * Max pitch/roll rotation speed sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max pitch/roll rotation speed in degree/s
     * @param _min Range min of pitch/roll rotation speed
     * @param _max Range max of pitch/roll rotation speed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsStateWifiSelectionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkSettingsState description:<br>
     * Network settings state from product<br>
     * <br>
     * Command WifiSelectionChanged description:<br>
     * Wifi selection from product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi selection settings
     * @param _band The actual  wifi band state
     * @param _channel The channel (depends of the band)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkSettingsStateWifiSelectionChanged (ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_TYPE_ENUM _type, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkSettingsStateWifiSelectionChanged (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsStateWifiSecurityChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkSettingsState description:<br>
     * Network settings state from product<br>
     * <br>
     * Command WifiSecurityChanged description:<br>
     * @deprecated<br>
     * Sent by the drone when its wifi security changes<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi security (open, wpa2)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkSettingsStateWifiSecurityChanged (ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITYCHANGED_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkSettingsStateWifiSecurityChanged (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsStateWifiSecurity</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class NetworkSettingsState description:<br>
     * Network settings state from product<br>
     * <br>
     * Command WifiSecurity description:<br>
     * Sent by the drone when its wifi security changes<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi security (open, wpa2)
     * @param _key The key used to secure the network (empty if type is open)
     * @param _keyType Type of the key
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3NetworkSettingsStateWifiSecurity (ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITY_TYPE_ENUM _type, String _key, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITY_KEYTYPE_ENUM _keyType) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3NetworkSettingsStateWifiSecurity (pointer, capacity, _type, _key, _keyType);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductMotorVersionListChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductMotorVersionListChanged description:<br>
     * @deprecated<br>
     * Product Motor version (the first argument is the unique identifier for the list)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _motor_number Product Motor number
     * @param _type Product Motor type
     * @param _software Product Motors software version
     * @param _hardware Product Motors hardware version
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateProductMotorVersionListChanged (byte _motor_number, String _type, String _software, String _hardware) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateProductMotorVersionListChanged (pointer, capacity, _motor_number, _type, _software, _hardware);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductGPSVersionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductGPSVersionChanged description:<br>
     * Product GPS versions<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _software Product GPS software version
     * @param _hardware Product GPS hardware version
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateProductGPSVersionChanged (String _software, String _hardware) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateProductGPSVersionChanged (pointer, capacity, _software, _hardware);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateMotorErrorStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command MotorErrorStateChanged description:<br>
     * Motor status changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _motorIds Bit field for concerned motor. If bit 0 = 1, motor 1 is affected by this error. Same with bit 1, 2 and 3.
     * @param _motorError Enumeration of the motor error
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateMotorErrorStateChanged (byte _motorIds, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM _motorError) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateMotorErrorStateChanged (pointer, capacity, _motorIds, _motorError);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateMotorSoftwareVersionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command MotorSoftwareVersionChanged description:<br>
     * @deprecated<br>
     * Motor software version status<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _version name of the version : dot separated fields (major version - minor version - firmware type - nb motors handled). Firmware types : Release, Debug, Alpha, Test-bench
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateMotorSoftwareVersionChanged (String _version) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateMotorSoftwareVersionChanged (pointer, capacity, _version);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateMotorFlightsStatusChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command MotorFlightsStatusChanged description:<br>
     * Motor flights status<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _nbFlights total number of flights
     * @param _lastFlightDuration Duration of the last flight (in seconds)
     * @param _totalFlightDuration Duration of all flights (in seconds)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateMotorFlightsStatusChanged (short _nbFlights, short _lastFlightDuration, int _totalFlightDuration) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateMotorFlightsStatusChanged (pointer, capacity, _nbFlights, _lastFlightDuration, _totalFlightDuration);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateMotorErrorLastErrorChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command MotorErrorLastErrorChanged description:<br>
     * Motor status about last error<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _motorError Enumeration of the motor error
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateMotorErrorLastErrorChanged (ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM _motorError) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateMotorErrorLastErrorChanged (pointer, capacity, _motorError);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateP7ID</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command P7ID description:<br>
     * @deprecated<br>
     * Product P7ID<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _serialID Product P7ID
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3SettingsStateP7ID (String _serialID) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3SettingsStateP7ID (pointer, capacity, _serialID);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStatePictureFormatChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command PictureFormatChanged description:<br>
     * The format of the photo<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of photo format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStatePictureFormatChanged (ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStatePictureFormatChanged (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStateAutoWhiteBalanceChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command AutoWhiteBalanceChanged description:<br>
     * AutoWhiteBalance mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type auto white balance
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStateAutoWhiteBalanceChanged (ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStateAutoWhiteBalanceChanged (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStateExpositionChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command ExpositionChanged description:<br>
     * The exposition of the image<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value Exposition value
     * @param _min Min exposition value
     * @param _max Max exposition value
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStateExpositionChanged (float _value, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStateExpositionChanged (pointer, capacity, _value, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStateSaturationChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command SaturationChanged description:<br>
     * The saturation of the image<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _value Saturation value
     * @param _min Min saturation value
     * @param _max Max saturation value
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStateSaturationChanged (float _value, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStateSaturationChanged (pointer, capacity, _value, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStateTimelapseChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command TimelapseChanged description:<br>
     * Picture taken periodically<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 1 if timelapse is enabled, 0 otherwise
     * @param _interval interval in seconds for taking pictures
     * @param _minInterval Minimal interval for taking pictures
     * @param _maxInterval Maximal interval for taking pictures
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStateTimelapseChanged (byte _enabled, float _interval, float _minInterval, float _maxInterval) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStateTimelapseChanged (pointer, capacity, _enabled, _interval, _minInterval, _maxInterval);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStateVideoAutorecordChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command VideoAutorecordChanged description:<br>
     * Video autorecord<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 1 if video autorecord is enabled, 0 otherwise
     * @param _mass_storage_id Mass storage id for the taken video
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStateVideoAutorecordChanged (byte _enabled, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStateVideoAutorecordChanged (pointer, capacity, _enabled, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PictureSettingsStateVideoStabilizationModeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PictureSettingsState description:<br>
     * Photo settings state from product<br>
     * <br>
     * Command VideoStabilizationModeChanged description:<br>
     * Video stabilization mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mode Video stabilization mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PictureSettingsStateVideoStabilizationModeChanged (ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOSTABILIZATIONMODECHANGED_MODE_ENUM _mode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PictureSettingsStateVideoStabilizationModeChanged (pointer, capacity, _mode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaStreamingStateVideoEnableChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class MediaStreamingState description:<br>
     * Media streaming status.<br>
     * <br>
     * Command VideoEnableChanged description:<br>
     * Return video streaming status.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled Current video streaming status.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3MediaStreamingStateVideoEnableChanged (ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM _enabled) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3MediaStreamingStateVideoEnableChanged (pointer, capacity, _enabled);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsStateHomeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettingsState description:<br>
     * GPS settings state<br>
     * <br>
     * Command HomeChanged description:<br>
     * Return home status<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Home latitude in decimal degrees
     * @param _longitude Home longitude in decimal degrees
     * @param _altitude Home altitude in meters
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsStateHomeChanged (double _latitude, double _longitude, double _altitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsStateHomeChanged (pointer, capacity, _latitude, _longitude, _altitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsStateResetHomeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettingsState description:<br>
     * GPS settings state<br>
     * <br>
     * Command ResetHomeChanged description:<br>
     * Reset home status<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Home latitude in decimal degrees
     * @param _longitude Home longitude in decimal degrees
     * @param _altitude Home altitude in meters
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsStateResetHomeChanged (double _latitude, double _longitude, double _altitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsStateResetHomeChanged (pointer, capacity, _latitude, _longitude, _altitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsStateGPSFixStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettingsState description:<br>
     * GPS settings state<br>
     * <br>
     * Command GPSFixStateChanged description:<br>
     * GPS fix state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _fixed 1 if gps on drone is fixed, 0 otherwise
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsStateGPSFixStateChanged (byte _fixed) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsStateGPSFixStateChanged (pointer, capacity, _fixed);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsStateGPSUpdateStateChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettingsState description:<br>
     * GPS settings state<br>
     * <br>
     * Command GPSUpdateStateChanged description:<br>
     * GPS update state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state The state of the gps update
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsStateGPSUpdateStateChanged (ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_GPSUPDATESTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsStateGPSUpdateStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsStateHomeTypeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettingsState description:<br>
     * GPS settings state<br>
     * <br>
     * Command HomeTypeChanged description:<br>
     * State of the type of the home position. This type is the user preference. The prefered home type may not be available, see HomeTypeStatesChanged to get the drone home type.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of the home position
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsStateHomeTypeChanged (ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsStateHomeTypeChanged (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSSettingsStateReturnHomeDelayChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSSettingsState description:<br>
     * GPS settings state<br>
     * <br>
     * Command ReturnHomeDelayChanged description:<br>
     * State of the delay after which the drone will automatically try to return home<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _delay Delay in second
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSSettingsStateReturnHomeDelayChanged (short _delay) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSSettingsStateReturnHomeDelayChanged (pointer, capacity, _delay);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CameraStateOrientation</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class CameraState description:<br>
     * Camera state<br>
     * <br>
     * Command Orientation description:<br>
     * Camera orientation<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _tilt Tilt camera consign for the drone [-100;100]
     * @param _pan Pan camera consign for the drone [-100;100]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3CameraStateOrientation (byte _tilt, byte _pan) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3CameraStateOrientation (pointer, capacity, _tilt, _pan);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CameraStateDefaultCameraOrientation</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class CameraState description:<br>
     * Camera state<br>
     * <br>
     * Command DefaultCameraOrientation description:<br>
     * Orientation of the camera center.<br>
     * This is the value to send when we want to center the camera.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _tilt Tilt value (in degree)
     * @param _pan Pan value (in degree)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3CameraStateDefaultCameraOrientation (byte _tilt, byte _pan) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3CameraStateDefaultCameraOrientation (pointer, capacity, _tilt, _pan);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AntiflickeringStateElectricFrequencyChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class AntiflickeringState description:<br>
     * Anti-flickering related states<br>
     * <br>
     * Command ElectricFrequencyChanged description:<br>
     * Electric frequency of the country determined by the position of the controller<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _frequency Type of the electric frequency
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3AntiflickeringStateElectricFrequencyChanged (ARCOMMANDS_ARDRONE3_ANTIFLICKERINGSTATE_ELECTRICFREQUENCYCHANGED_FREQUENCY_ENUM _frequency) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3AntiflickeringStateElectricFrequencyChanged (pointer, capacity, _frequency);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AntiflickeringStateModeChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class AntiflickeringState description:<br>
     * Anti-flickering related states<br>
     * <br>
     * Command ModeChanged description:<br>
     * Anti flickering mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mode Mode of the anti flickering functionnality
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3AntiflickeringStateModeChanged (ARCOMMANDS_ARDRONE3_ANTIFLICKERINGSTATE_MODECHANGED_MODE_ENUM _mode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3AntiflickeringStateModeChanged (pointer, capacity, _mode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSStateNumberOfSatelliteChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSState description:<br>
     * GPS related States<br>
     * <br>
     * Command NumberOfSatelliteChanged description:<br>
     * The number of satellite used to compute the gps position<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _numberOfSatellite The number of satellite
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSStateNumberOfSatelliteChanged (byte _numberOfSatellite) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSStateNumberOfSatelliteChanged (pointer, capacity, _numberOfSatellite);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSStateHomeTypeAvailabilityChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSState description:<br>
     * GPS related States<br>
     * <br>
     * Command HomeTypeAvailabilityChanged description:<br>
     * Availability of the return home types in a map : for each type other args will be sent by the drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of the return home
     * @param _available 1 if this type is available, 0 otherwise
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSStateHomeTypeAvailabilityChanged (ARCOMMANDS_ARDRONE3_GPSSTATE_HOMETYPEAVAILABILITYCHANGED_TYPE_ENUM _type, byte _available) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSStateHomeTypeAvailabilityChanged (pointer, capacity, _type, _available);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSStateHomeTypeChosenChanged</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class GPSState description:<br>
     * GPS related States<br>
     * <br>
     * Command HomeTypeChosenChanged description:<br>
     * The return home type chosen<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of the return home chosen
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3GPSStateHomeTypeChosenChanged (ARCOMMANDS_ARDRONE3_GPSSTATE_HOMETYPECHOSENCHANGED_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3GPSStateHomeTypeChosenChanged (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PROStateFeatures</code> in feature <code>ARDrone3</code><br>
     * <br>
     * Feature ARDrone3 description:<br>
     * All ARDrone3-only commands<br>
     * <br>
     * Class PROState description:<br>
     * @deprecated<br>
     * Pro features enabled on the Bebop<br>
     * <br>
     * Command Features description:<br>
     * Features enabled<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _features Bitfield representing enabled features.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setARDrone3PROStateFeatures (long _features) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetARDrone3PROStateFeatures (pointer, capacity, _features);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GeographicRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command GeographicRun description:<br>
     * Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start geographic unknown feature_1 process, 0 to stop it
     * @param _start Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @param _distance_is_default 0 if distance is set by user, 1 if set by current distance (in this case distance value is not used)
     * @param _distance_is_default Not used when arg start is at 0
     * @param _distance_is_default Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @param _distance The distance leader-follower in meter
     * @param _distance Not used when arg start is at 0
     * @param _distance Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @param _elevation_is_default 0 if elevation is set by user, 1 if set by current elevation (in this case elevation value is not used) (not used when arg start is at 0)
     * @param _elevation_is_default Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @param _elevation The elevation leader-follower in rad (not used when arg start is at 0)
     * @param _elevation Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @param _azimuth_is_default 0 if azimuth is set by user, 1 if set by current azimuth (in this case azimuth value is not used) (not used when arg start is at 0)
     * @param _azimuth_is_default Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @param _azimuth The azimuth north-leader-follower in rad (not used when arg start is at 0)
     * @param _azimuth Begin or stop geographic type followMe (follow the leader keeping the same vector). \n\                       Sending this command will stop other running followMe.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1GeographicRun (byte _start, byte _distance_is_default, float _distance, byte _elevation_is_default, float _elevation, byte _azimuth_is_default, float _azimuth) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1GeographicRun (pointer, capacity, _start, _distance_is_default, _distance, _elevation_is_default, _elevation, _azimuth_is_default, _azimuth);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RelativeRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command RelativeRun description:<br>
     * Begin or stop relative type followMe (follow the leader keeping the same orientation to its direction).\n\                       Sending this command will stop other running followMe.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
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
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1RelativeRun (byte _start, byte _distance_is_default, float _distance, byte _elevation_is_default, float _elevation, byte _azimuth_is_default, float _azimuth) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1RelativeRun (pointer, capacity, _start, _distance_is_default, _distance, _elevation_is_default, _elevation, _azimuth_is_default, _azimuth);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>LookAtRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command LookAtRun description:<br>
     * Begin or stop look at type followMe (stare at the leader while hovering).\n\                      Sending this command will stop other running followMe.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start look at process, 0 to stop it
     * @param _start Begin or stop look at type followMe (stare at the leader while hovering).\n\                      Sending this command will stop other running followMe.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1LookAtRun (byte _start) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1LookAtRun (pointer, capacity, _start);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpiralAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command SpiralAnimRun description:<br>
     * Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start the anim, 0 to stop it
     * @param _start Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed should be used
     * @param _speed_is_default Not used when start is 0
     * @param _speed_is_default Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @param _speed The desired speed of the anim in m/s
     * @param _speed Not used when speed_is_default is 1
     * @param _speed Not used when start is 0
     * @param _speed Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @param _revolution_nb_is_default 0 if the number of revolution is set by user, 1 if default revolution nb should be used
     * @param _revolution_nb_is_default Not used when start is 0
     * @param _revolution_nb_is_default Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @param _revolution_number The number of revolution (in turn)
     * @param _revolution_number Negative value is infinite
     * @param _revolution_number Example: 1.5 makes an entire turn plus half of a turn
     * @param _revolution_number Not used when revolutionNb_is_default is 1
     * @param _revolution_number Not used when start is 0
     * @param _revolution_number Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @param _vertical_distance_is_default 0 if the vertical distance is set by user, 1 if default value should be used
     * @param _vertical_distance_is_default Not used when start is 0
     * @param _vertical_distance_is_default Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @param _vertical_distance Distance that should be made by the product to reach the top of the spiral in m
     * @param _vertical_distance Not used when verticalDistance_is_default is 1
     * @param _vertical_distance Not used when start is 0
     * @param _vertical_distance Begin or stop a spiral animation.\n\                      The spiral animation allows the drone to revolve around the target while going up, with a fixed radius.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1SpiralAnimRun (byte _start, byte _speed_is_default, float _speed, byte _revolution_nb_is_default, float _revolution_number, byte _vertical_distance_is_default, float _vertical_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1SpiralAnimRun (pointer, capacity, _start, _speed_is_default, _speed, _revolution_nb_is_default, _revolution_number, _vertical_distance_is_default, _vertical_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SwingAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command SwingAnimRun description:<br>
     * Begin or stop a swing animation.\n\                      The swing animation enables a vertical point of view while the drone passes over the target.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start the anim, 0 to stop it
     * @param _start Begin or stop a swing animation.\n\                      The swing animation enables a vertical point of view while the drone passes over the target.
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed should be used
     * @param _speed_is_default Not used when start is 0
     * @param _speed_is_default Begin or stop a swing animation.\n\                      The swing animation enables a vertical point of view while the drone passes over the target.
     * @param _speed The desired speed of the anim in m/s
     * @param _speed Not used when speed_is_default is 1
     * @param _speed Not used when start is 0
     * @param _speed Begin or stop a swing animation.\n\                      The swing animation enables a vertical point of view while the drone passes over the target.
     * @param _vertical_distance_is_default 0 if the vertical distance is set by user, 1 if default value should be used
     * @param _vertical_distance_is_default Not used when start is 0
     * @param _vertical_distance_is_default Begin or stop a swing animation.\n\                      The swing animation enables a vertical point of view while the drone passes over the target.
     * @param _vertical_distance Distance that should be made by the product to reach the top of the swing in m
     * @param _vertical_distance Not used when verticalDistance_is_default is 1
     * @param _vertical_distance Not used when start is 0
     * @param _vertical_distance Begin or stop a swing animation.\n\                      The swing animation enables a vertical point of view while the drone passes over the target.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1SwingAnimRun (byte _start, byte _speed_is_default, float _speed, byte _vertical_distance_is_default, float _vertical_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1SwingAnimRun (pointer, capacity, _start, _speed_is_default, _speed, _vertical_distance_is_default, _vertical_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>BoomerangAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command BoomerangAnimRun description:<br>
     * Begin or stop a boomerang animation.\n\                      The boomerang animation enables a zoom-out/zoom-in trajectory while preserving the framing chosen by the user.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start the anim, 0 to stop it
     * @param _start Begin or stop a boomerang animation.\n\                      The boomerang animation enables a zoom-out/zoom-in trajectory while preserving the framing chosen by the user.
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed should be used
     * @param _speed_is_default Not used when start is 0
     * @param _speed_is_default Begin or stop a boomerang animation.\n\                      The boomerang animation enables a zoom-out/zoom-in trajectory while preserving the framing chosen by the user.
     * @param _speed The desired speed of the anim in m/s
     * @param _speed Not used when speed_is_default is 1
     * @param _speed Not used when start is 0
     * @param _speed Begin or stop a boomerang animation.\n\                      The boomerang animation enables a zoom-out/zoom-in trajectory while preserving the framing chosen by the user.
     * @param _distance_is_default 0 if the distance is set by user, 1 if default value should be used
     * @param _distance_is_default Not used when start is 0
     * @param _distance_is_default Begin or stop a boomerang animation.\n\                      The boomerang animation enables a zoom-out/zoom-in trajectory while preserving the framing chosen by the user.
     * @param _distance Distance that should be made by the product to reach its return point in m
     * @param _distance Not used when distance_is_default is 1
     * @param _distance Not used when start is 0
     * @param _distance Begin or stop a boomerang animation.\n\                      The boomerang animation enables a zoom-out/zoom-in trajectory while preserving the framing chosen by the user.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1BoomerangAnimRun (byte _start, byte _speed_is_default, float _speed, byte _distance_is_default, float _distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1BoomerangAnimRun (pointer, capacity, _start, _speed_is_default, _speed, _distance_is_default, _distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CandleAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command CandleAnimRun description:<br>
     * Begin or stop a candle animation.\n\                      The candle animation enables a zoom-in directly on the target followed by a vertical zoom-out.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _start 1 to start the anim, 0 to stop it
     * @param _start Begin or stop a candle animation.\n\                      The candle animation enables a zoom-in directly on the target followed by a vertical zoom-out.
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed should be used
     * @param _speed_is_default Not used when start is 0
     * @param _speed_is_default Begin or stop a candle animation.\n\                      The candle animation enables a zoom-in directly on the target followed by a vertical zoom-out.
     * @param _speed The desired speed of the anim in m/s
     * @param _speed Not used when speed_is_default is 1
     * @param _speed Not used when start is 0
     * @param _speed Begin or stop a candle animation.\n\                      The candle animation enables a zoom-in directly on the target followed by a vertical zoom-out.
     * @param _vertical_distance_is_default 0 if the vertical distance is set by user, 1 if default value should be used
     * @param _vertical_distance_is_default Not used when start is 0
     * @param _vertical_distance_is_default Begin or stop a candle animation.\n\                      The candle animation enables a zoom-in directly on the target followed by a vertical zoom-out.
     * @param _vertical_distance Distance that should be made by the product to reach the top of the vertical zoom-out in m
     * @param _vertical_distance Not used when verticalDistance_is_default is 1
     * @param _vertical_distance Not used when start is 0
     * @param _vertical_distance Begin or stop a candle animation.\n\                      The candle animation enables a zoom-in directly on the target followed by a vertical zoom-out.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1CandleAnimRun (byte _start, byte _speed_is_default, float _speed, byte _vertical_distance_is_default, float _vertical_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1CandleAnimRun (pointer, capacity, _start, _speed_is_default, _speed, _vertical_distance_is_default, _vertical_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DollySlideAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command DollySlideAnimRun description:<br>
     * Begin or stop a dolly slide animation.\n\                      Allows the drone to catch up to the target before flying past it, creating a zoom-in/zoom_out effect without a curved path.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
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
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1DollySlideAnimRun (byte _start, byte _speed_is_default, float _speed, byte _angle_is_default, float _angle, byte _horizontal_distance_is_default, float _horizontal_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1DollySlideAnimRun (pointer, capacity, _start, _speed_is_default, _speed, _angle_is_default, _angle, _horizontal_distance_is_default, _horizontal_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>UserFramingPosition</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command UserFramingPosition description:<br>
     * User desired framing in the video.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _horizontal Horizontal position in the video (in %, from left to right)
     * @param _horizontal User desired framing in the video.
     * @param _vertical Vertical position in the video (in %, from bottom to top)
     * @param _vertical User desired framing in the video.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1UserFramingPosition (byte _horizontal, byte _vertical) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1UserFramingPosition (pointer, capacity, _horizontal, _vertical);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>UserGPSData</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command UserGPSData description:<br>
     * User gps data<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Latitude of the user (in deg)
     * @param _longitude Longitude of the user (in deg)
     * @param _altitude Altitude of the user (in meters, according to sea level)
     * @param _horizontal_accuracy Horizontal accuracy (in meter)
     * @param _vertical_accuracy Vertical accuracy (in meter)
     * @param _north_speed North speed (in meter per second)
     * @param _east_speed East speed (in meter per second)
     * @param _down_speed Vertical speed (in meter per second) (down is positive)
     * @param _timestamp Timestamp of the gps data
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1UserGPSData (double _latitude, double _longitude, float _altitude, float _horizontal_accuracy, float _vertical_accuracy, float _north_speed, float _east_speed, float _down_speed, double _timestamp) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1UserGPSData (pointer, capacity, _latitude, _longitude, _altitude, _horizontal_accuracy, _vertical_accuracy, _north_speed, _east_speed, _down_speed, _timestamp);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>UserBaroData</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command UserBaroData description:<br>
     * User barometer data<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _pressure Atmospheric pressure in hPa (millibar)
     * @param _timestamp Timestamp of the barometer data
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1UserBaroData (float _pressure, double _timestamp) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1UserBaroData (pointer, capacity, _pressure, _timestamp);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>LynxDetection</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command LynxDetection description:<br>
     * Send vision detection results.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
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
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1LynxDetection (float _target_pan, float _target_tilt, float _change_of_scale, byte _confidence_index, byte _is_new_selection, long _timestamp) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1LynxDetection (pointer, capacity, _target_pan, _target_tilt, _change_of_scale, _confidence_index, _is_new_selection, _timestamp);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>Availability</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command Availability description:<br>
     * Get the UnknownFeature_1 types you can run.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Get the UnknownFeature_1 types you can run.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1Availability (ARCOMMANDS_FOLLOW_ME_TYPES_AVAILABLE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1Availability (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>Run</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command Run description:<br>
     * State of the UnknownFeature_1 run<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1Run (ARCOMMANDS_FOLLOW_ME_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1Run (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GeographicConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command GeographicConfigChanged description:<br>
     * Geographic configuration changed.\n\                      This event is only valid when [Run type](#134-2) is geographic.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
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
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1GeographicConfigChanged (byte _distance_is_default, float _distance, byte _elevation_is_default, float _elevation, byte _azimuth_is_default, float _azimuth) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1GeographicConfigChanged (pointer, capacity, _distance_is_default, _distance, _elevation_is_default, _elevation, _azimuth_is_default, _azimuth);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RelativeConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command RelativeConfigChanged description:<br>
     * Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _distance_is_default 0 if distance is set by user, 1 if set by current distance
     * @param _distance_is_default Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.
     * @param _distance The distance leader-follower in meter, if distance is default, this value is the current drone distance
     * @param _distance Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.
     * @param _elevation_is_default 0 if elevation is set by user, 1 if set set by current elevation
     * @param _elevation_is_default Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.
     * @param _elevation The elevation leader-follower in rad, if elevation is default, this value is the current leader to drone elevation
     * @param _elevation Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.
     * @param _azimuth_is_default 0 if azimuth is set by user, 1 if set by current azimuth
     * @param _azimuth_is_default Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.
     * @param _azimuth The azimuth course-leader-follower in rad, if azimuth is default, this value is the current leader to drone azimuth
     * @param _azimuth Relative configuration changed.\n\                      This event is only valid when [Run type](#134-2) is relative.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1RelativeConfigChanged (byte _distance_is_default, float _distance, byte _elevation_is_default, float _elevation, byte _azimuth_is_default, float _azimuth) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1RelativeConfigChanged (pointer, capacity, _distance_is_default, _distance, _elevation_is_default, _elevation, _azimuth_is_default, _azimuth);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command AnimRun description:<br>
     * State of the current UnknownFeature_1 animation.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type State of the current UnknownFeature_1 animation.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1AnimRun (ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1AnimRun (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpiralAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command SpiralAnimConfigChanged description:<br>
     * Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed is used
     * @param _speed_is_default Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral
     * @param _speed The speed of the anim in m/s
     * @param _speed Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral
     * @param _revolution_nb_is_default 0 if the number of revolution is set by user, 1 if default revolution nb is used
     * @param _revolution_nb_is_default Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral
     * @param _revolution_nb The number of revolution (in turn)
     * @param _revolution_nb Negative value is infinite
     * @param _revolution_nb Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral
     * @param _vertical_distance_is_default 0 if the vertical distance is set by user, 1 if default value is used
     * @param _vertical_distance_is_default Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral
     * @param _vertical_distance Distance that will be made by the product to reach the top of the spiral in m
     * @param _vertical_distance Spiral animation configuration changed.\n\                 This event is only valid when AnimRun type is spiral
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1SpiralAnimConfigChanged (byte _speed_is_default, float _speed, byte _revolution_nb_is_default, float _revolution_nb, byte _vertical_distance_is_default, float _vertical_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1SpiralAnimConfigChanged (pointer, capacity, _speed_is_default, _speed, _revolution_nb_is_default, _revolution_nb, _vertical_distance_is_default, _vertical_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SwingAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command SwingAnimConfigChanged description:<br>
     * Swing animation configuration changed.\n\                 This event is only valid when AnimRun type is swing<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed is used
     * @param _speed_is_default Swing animation configuration changed.\n\                 This event is only valid when AnimRun type is swing
     * @param _speed The speed of the anim in m/s
     * @param _speed Swing animation configuration changed.\n\                 This event is only valid when AnimRun type is swing
     * @param _vertical_distance_is_default 0 if the vertical distance is set by user, 1 if default value is used
     * @param _vertical_distance_is_default Swing animation configuration changed.\n\                 This event is only valid when AnimRun type is swing
     * @param _vertical_distance Distance that will be made by the product to reach the top of the swing in m
     * @param _vertical_distance Swing animation configuration changed.\n\                 This event is only valid when AnimRun type is swing
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1SwingAnimConfigChanged (byte _speed_is_default, float _speed, byte _vertical_distance_is_default, float _vertical_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1SwingAnimConfigChanged (pointer, capacity, _speed_is_default, _speed, _vertical_distance_is_default, _vertical_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>BoomerangAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command BoomerangAnimConfigChanged description:<br>
     * Boomerang animation configuration changed.\n\                 This event is only valid when AnimRun type is boomerang<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed is used
     * @param _speed_is_default Boomerang animation configuration changed.\n\                 This event is only valid when AnimRun type is boomerang
     * @param _speed The speed of the anim in m/s
     * @param _speed Boomerang animation configuration changed.\n\                 This event is only valid when AnimRun type is boomerang
     * @param _distance_is_default 0 if the distance is set by user, 1 if default value is used
     * @param _distance_is_default Boomerang animation configuration changed.\n\                 This event is only valid when AnimRun type is boomerang
     * @param _distance Distance that will be made by the product to reach its return point in m
     * @param _distance Boomerang animation configuration changed.\n\                 This event is only valid when AnimRun type is boomerang
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1BoomerangAnimConfigChanged (byte _speed_is_default, float _speed, byte _distance_is_default, float _distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1BoomerangAnimConfigChanged (pointer, capacity, _speed_is_default, _speed, _distance_is_default, _distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CandleAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command CandleAnimConfigChanged description:<br>
     * Candle animation configuration changed.\n\                 This event is only valid when AnimRun type is candle<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed is used
     * @param _speed_is_default Candle animation configuration changed.\n\                 This event is only valid when AnimRun type is candle
     * @param _speed The speed of the anim in m/s
     * @param _speed Candle animation configuration changed.\n\                 This event is only valid when AnimRun type is candle
     * @param _vertical_distance_is_default 0 if the vertical distance is set by user, 1 if default value is used
     * @param _vertical_distance_is_default Candle animation configuration changed.\n\                 This event is only valid when AnimRun type is candle
     * @param _vertical_distance Distance that will be made by the product to reach the top of the vertical zoom-out in m
     * @param _vertical_distance Candle animation configuration changed.\n\                 This event is only valid when AnimRun type is candle
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1CandleAnimConfigChanged (byte _speed_is_default, float _speed, byte _vertical_distance_is_default, float _vertical_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1CandleAnimConfigChanged (pointer, capacity, _speed_is_default, _speed, _vertical_distance_is_default, _vertical_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DollySlideAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command DollySlideAnimConfigChanged description:<br>
     * DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speed_is_default 0 if speed is set by user, 1 if default speed is used
     * @param _speed_is_default DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide
     * @param _speed The speed of the anim in m/s
     * @param _speed DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide
     * @param _angle_is_default 0 if the angle is set by user, 1 if default value is used
     * @param _angle_is_default DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide
     * @param _angle Angle Product-User-Target in rad
     * @param _angle DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide
     * @param _horizontal_distance_is_default 0 if the horizontal distance is set by user, 1 if default value is used
     * @param _horizontal_distance_is_default DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide
     * @param _horizontal_distance Distance that will be made by the product to reach its target in m
     * @param _horizontal_distance DollySlide animation configuration changed.\n\                 This event is only valid when AnimRun type is dolly_slide
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1DollySlideAnimConfigChanged (byte _speed_is_default, float _speed, byte _angle_is_default, float _angle, byte _horizontal_distance_is_default, float _horizontal_distance) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1DollySlideAnimConfigChanged (pointer, capacity, _speed_is_default, _speed, _angle_is_default, _angle, _horizontal_distance_is_default, _horizontal_distance);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>UserFramingPositionChanged</code> in feature <code>UnknownFeature_1</code><br>
     * <br>
     * Feature UnknownFeature_1 description:<br>
     * UnknownFeature_1 feature<br>
     * <br>
     * Command UserFramingPositionChanged description:<br>
     * User desired framing in the video changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _horizontal Horizontal position in the video (in %, from left to right)
     * @param _vertical Vertical position in the video (in %, from bottom to top)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setUnknownFeature_1UserFramingPositionChanged (byte _horizontal, byte _vertical) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetUnknownFeature_1UserFramingPositionChanged (pointer, capacity, _horizontal, _vertical);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingPCMD</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the JumpingSumo<br>
     * <br>
     * Command PCMD description:<br>
     * Ask the JS speed and turn ratio.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _flag Boolean for "touch screen".
     * @param _speed Speed value [-100:100].
     * @param _turn Turn value. [-100:100]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingPCMD (byte _flag, byte _speed, byte _turn) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingPCMD (pointer, capacity, _flag, _speed, _turn);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingPosture</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the JumpingSumo<br>
     * <br>
     * Command Posture description:<br>
     * Request a posture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Type of Posture
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingPosture (ARCOMMANDS_JUMPINGSUMO_PILOTING_POSTURE_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingPosture (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingAddCapOffset</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the JumpingSumo<br>
     * <br>
     * Command AddCapOffset description:<br>
     * Add the specified offset to the current cap.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _offset Offset value in radians.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingAddCapOffset (float _offset) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingAddCapOffset (pointer, capacity, _offset);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingUserTakeOff</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the JumpingSumo<br>
     * <br>
     * Command UserTakeOff description:<br>
     * Set drone in user take off state<br>
     * Only used for Unknown_Product_1 product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of user take off mode
     * @param _state - 1 to enter in user take off.
     * @param _state - 0 to exit from user take off.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingUserTakeOff (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingUserTakeOff (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingLand</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the JumpingSumo<br>
     * <br>
     * Command Land description:<br>
     * Ask the Unknown_Product_1 to land<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingLand () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingLand (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsJumpStop</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command JumpStop description:<br>
     * Stop jump, emergency jump stop, stop jump motor and stay there.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsJumpStop () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsJumpStop (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsJumpCancel</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command JumpCancel description:<br>
     * Cancel jump and come back to previous state (if possible).<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsJumpCancel () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsJumpCancel (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsJumpLoad</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command JumpLoad description:<br>
     * Request jump loading<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsJumpLoad () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsJumpLoad (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsJump</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command Jump description:<br>
     * Request a jump<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Type of jump
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsJump (ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsJump (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsSimpleAnimation</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command SimpleAnimation description:<br>
     * Play a parameterless animation.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _id Animation ID.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsSimpleAnimation (ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM _id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsSimpleAnimation (pointer, capacity, _id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordPicture</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command Picture description:<br>
     * @deprecated<br>
     * Take picture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mass_storage_id Mass storage id to take picture
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordPicture (byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordPicture (pointer, capacity, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordVideo</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command Video description:<br>
     * @deprecated<br>
     * Video record<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _record Command to record video
     * @param _mass_storage_id Mass storage id to record
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordVideo (ARCOMMANDS_JUMPINGSUMO_MEDIARECORD_VIDEO_RECORD_ENUM _record, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordVideo (pointer, capacity, _record, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordPictureV2</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command PictureV2 description:<br>
     * Take picture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordPictureV2 () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordPictureV2 (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordVideoV2</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command VideoV2 description:<br>
     * Video record<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _record Command to record video
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordVideoV2 (ARCOMMANDS_JUMPINGSUMO_MEDIARECORD_VIDEOV2_RECORD_ENUM _record) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordVideoV2 (pointer, capacity, _record);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsWifiSelection</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkSettings description:<br>
     * Network settings commands<br>
     * <br>
     * Command WifiSelection description:<br>
     * Auto-select channel of choosen band<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi selection (auto, manual)
     * @param _band The allowed band(s) : 2.4 Ghz, 5 Ghz, or all
     * @param _channel The channel (not used in auto mode)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkSettingsWifiSelection (ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGS_WIFISELECTION_TYPE_ENUM _type, ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGS_WIFISELECTION_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkSettingsWifiSelection (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkWifiScan</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Network description:<br>
     * Network related commands<br>
     * <br>
     * Command WifiScan description:<br>
     * Launches wifi network scan<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band The band(s) : 2.4 Ghz, 5 Ghz, or both
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkWifiScan (ARCOMMANDS_JUMPINGSUMO_NETWORK_WIFISCAN_BAND_ENUM _band) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkWifiScan (pointer, capacity, _band);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkWifiAuthChannel</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class Network description:<br>
     * Network related commands<br>
     * <br>
     * Command WifiAuthChannel description:<br>
     * Controller inquire the list of authorized wifi channels.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkWifiAuthChannel () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkWifiAuthChannel (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AudioSettingsMasterVolume</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AudioSettings description:<br>
     * Audio settings.<br>
     * <br>
     * Command MasterVolume description:<br>
     * Master volume control.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _volume Master audio volume [0:100].
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAudioSettingsMasterVolume (byte _volume) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAudioSettingsMasterVolume (pointer, capacity, _volume);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AudioSettingsTheme</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AudioSettings description:<br>
     * Audio settings.<br>
     * <br>
     * Command Theme description:<br>
     * Audio Theme.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _theme The audio theme to set.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAudioSettingsTheme (ARCOMMANDS_JUMPINGSUMO_AUDIOSETTINGS_THEME_THEME_ENUM _theme) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAudioSettingsTheme (pointer, capacity, _theme);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanAllScriptsMetadata</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlan description:<br>
     * RoadPlan commands.<br>
     * <br>
     * Command AllScriptsMetadata description:<br>
     * Command to ask device all metadata scripts.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanAllScriptsMetadata () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanAllScriptsMetadata (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanScriptUploaded</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlan description:<br>
     * RoadPlan commands.<br>
     * <br>
     * Command ScriptUploaded description:<br>
     * Notify device that a new file has been uploaded.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _uuid UUID of uploaded file.
     * @param _md5Hash MD5 hash code computed over file.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanScriptUploaded (String _uuid, String _md5Hash) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanScriptUploaded (pointer, capacity, _uuid, _md5Hash);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanScriptDelete</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlan description:<br>
     * RoadPlan commands.<br>
     * <br>
     * Command ScriptDelete description:<br>
     * Ask the device to delete a script.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _uuid UUID of the file to delete.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanScriptDelete (String _uuid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanScriptDelete (pointer, capacity, _uuid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanPlayScript</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlan description:<br>
     * RoadPlan commands.<br>
     * <br>
     * Command PlayScript description:<br>
     * Ask the device to play a script.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _uuid UUID of the file to play.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanPlayScript (String _uuid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanPlayScript (pointer, capacity, _uuid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsOutdoor</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command Outdoor description:<br>
     * @deprecated<br>
     * Outdoor property<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _outdoor 1 if outdoor, 0 if indoor
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoSpeedSettingsOutdoor (byte _outdoor) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoSpeedSettingsOutdoor (pointer, capacity, _outdoor);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaStreamingVideoEnable</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaStreaming description:<br>
     * Control media streaming behavior.<br>
     * <br>
     * Command VideoEnable description:<br>
     * Enable/disable video streaming.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enable 1 to enable, 0 to disable.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaStreamingVideoEnable (byte _enable) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaStreamingVideoEnable (pointer, capacity, _enable);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>VideoSettingsAutorecord</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class VideoSettings description:<br>
     * Video settings.<br>
     * <br>
     * Command Autorecord description:<br>
     * Set video automatic recording state.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 0: Disabled 1: Enabled.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoVideoSettingsAutorecord (byte _enabled) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoVideoSettingsAutorecord (pointer, capacity, _enabled);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStatePostureChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class PilotingState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command PostureChanged description:<br>
     * State of posture changed.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of posture
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingStatePostureChanged (ARCOMMANDS_JUMPINGSUMO_PILOTINGSTATE_POSTURECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingStatePostureChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAlertStateChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class PilotingState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command AlertStateChanged description:<br>
     * JS alert state changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state JS alert state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingStateAlertStateChanged (ARCOMMANDS_JUMPINGSUMO_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingStateAlertStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateSpeedChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class PilotingState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command SpeedChanged description:<br>
     * Notification sent when JS speed changes.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _speed Speed command applied to motors in range [-100;100].
     * @param _realSpeed Actual real-world speed in cm/s. Value -32768 returned if not available.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingStateSpeedChanged (byte _speed, short _realSpeed) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingStateSpeedChanged (pointer, capacity, _speed, _realSpeed);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateFlyingStateChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class PilotingState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command FlyingStateChanged description:<br>
     * Drone flying state changed<br>
     * Only used for Unknown_Product_1<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state Drone flying state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoPilotingStateFlyingStateChanged (ARCOMMANDS_JUMPINGSUMO_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoPilotingStateFlyingStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStateJumpLoadChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AnimationsState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command JumpLoadChanged description:<br>
     * State of jump load changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of jump load
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsStateJumpLoadChanged (ARCOMMANDS_JUMPINGSUMO_ANIMATIONSSTATE_JUMPLOADCHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsStateJumpLoadChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStateJumpTypeChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AnimationsState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command JumpTypeChanged description:<br>
     * State of jump type changed.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of jump type.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsStateJumpTypeChanged (ARCOMMANDS_JUMPINGSUMO_ANIMATIONSSTATE_JUMPTYPECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsStateJumpTypeChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStateJumpMotorProblemChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AnimationsState description:<br>
     * Animations state from JS.<br>
     * <br>
     * Command JumpMotorProblemChanged description:<br>
     * State about the jump motor problem<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _error Enum describing the problem of the motor
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAnimationsStateJumpMotorProblemChanged (ARCOMMANDS_JUMPINGSUMO_ANIMATIONSSTATE_JUMPMOTORPROBLEMCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAnimationsStateJumpMotorProblemChanged (pointer, capacity, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductGPSVersionChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductGPSVersionChanged description:<br>
     * @deprecated<br>
     * Product GPS versions<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _software Product GPS software version
     * @param _hardware Product GPS hardware version
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoSettingsStateProductGPSVersionChanged (String _software, String _hardware) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoSettingsStateProductGPSVersionChanged (pointer, capacity, _software, _hardware);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStatePictureStateChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command PictureStateChanged description:<br>
     * @deprecated<br>
     * State of picture recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state 1 if picture has been taken, 0 otherwise
     * @param _mass_storage_id Mass storage id where the picture was recorded
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordStatePictureStateChanged (byte _state, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordStatePictureStateChanged (pointer, capacity, _state, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStateVideoStateChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command VideoStateChanged description:<br>
     * @deprecated<br>
     * State of video recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of video
     * @param _mass_storage_id Mass storage id where the video was recorded
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordStateVideoStateChanged (ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_VIDEOSTATECHANGED_STATE_ENUM _state, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordStateVideoStateChanged (pointer, capacity, _state, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStatePictureStateChangedV2</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command PictureStateChangedV2 description:<br>
     * State of device picture recording changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of device picture recording
     * @param _error Error to explain the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordStatePictureStateChangedV2 (ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM _state, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordStatePictureStateChangedV2 (pointer, capacity, _state, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStateVideoStateChangedV2</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command VideoStateChangedV2 description:<br>
     * State of device video recording changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of device video recording
     * @param _error Error to explain the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordStateVideoStateChangedV2 (ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM _state, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordStateVideoStateChangedV2 (pointer, capacity, _state, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordEventPictureEventChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecordEvent description:<br>
     * Events of media recording<br>
     * <br>
     * Command PictureEventChanged description:<br>
     * Event of picture recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _event Last event of picture recording
     * @param _error Error to explain the event
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordEventPictureEventChanged (ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM _event, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordEventPictureEventChanged (pointer, capacity, _event, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordEventVideoEventChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaRecordEvent description:<br>
     * Events of media recording<br>
     * <br>
     * Command VideoEventChanged description:<br>
     * Event of video recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _event Event of video recording
     * @param _error Error to explain the event
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaRecordEventVideoEventChanged (ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_VIDEOEVENTCHANGED_EVENT_ENUM _event, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_VIDEOEVENTCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaRecordEventVideoEventChanged (pointer, capacity, _event, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkSettingsStateWifiSelectionChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkSettingsState description:<br>
     * Network settings state from product<br>
     * <br>
     * Command WifiSelectionChanged description:<br>
     * Wifi selection from product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi selection settings
     * @param _band The actual  wifi band state
     * @param _channel The channel (depends of the band)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkSettingsStateWifiSelectionChanged (ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_TYPE_ENUM _type, ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkSettingsStateWifiSelectionChanged (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateWifiScanListChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command WifiScanListChanged description:<br>
     * One scanning result found<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid SSID of the AP
     * @param _rssi RSSI of the AP in dbm (negative value)
     * @param _band The band : 2.4 GHz or 5 GHz
     * @param _channel Channel of the AP
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkStateWifiScanListChanged (String _ssid, short _rssi, ARCOMMANDS_JUMPINGSUMO_NETWORKSTATE_WIFISCANLISTCHANGED_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkStateWifiScanListChanged (pointer, capacity, _ssid, _rssi, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateAllWifiScanChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command AllWifiScanChanged description:<br>
     * State sent when all scanning result sent<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkStateAllWifiScanChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkStateAllWifiScanChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateWifiAuthChannelListChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command WifiAuthChannelListChanged description:<br>
     * Notify of an Authorized Channel.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band The band of this channel : 2.4 GHz or 5 GHz
     * @param _channel The authorized channel.
     * @param _in_or_out Bit 0 is 1 if channel is authorized outside (0 otherwise) ; Bit 1 is 1 if channel is authorized inside (0 otherwise)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkStateWifiAuthChannelListChanged (ARCOMMANDS_JUMPINGSUMO_NETWORKSTATE_WIFIAUTHCHANNELLISTCHANGED_BAND_ENUM _band, byte _channel, byte _in_or_out) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkStateWifiAuthChannelListChanged (pointer, capacity, _band, _channel, _in_or_out);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateAllWifiAuthChannelChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command AllWifiAuthChannelChanged description:<br>
     * Notify the end of the list of Authorized wifi Channel.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkStateAllWifiAuthChannelChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkStateAllWifiAuthChannelChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkStateLinkQualityChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class NetworkState description:<br>
     * Network state from Product<br>
     * <br>
     * Command LinkQualityChanged description:<br>
     * Notification sent by the firmware to give an indication of the WiFi link quality.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _quality The WiFi link quality in range 0-6, the higher the value, the higher the link quality.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoNetworkStateLinkQualityChanged (byte _quality) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoNetworkStateLinkQualityChanged (pointer, capacity, _quality);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AudioSettingsStateMasterVolumeChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AudioSettingsState description:<br>
     * Audio settings state.<br>
     * <br>
     * Command MasterVolumeChanged description:<br>
     * Master volume control.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _volume Master audio volume [0:100].
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAudioSettingsStateMasterVolumeChanged (byte _volume) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAudioSettingsStateMasterVolumeChanged (pointer, capacity, _volume);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AudioSettingsStateThemeChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class AudioSettingsState description:<br>
     * Audio settings state.<br>
     * <br>
     * Command ThemeChanged description:<br>
     * Command to notify controller of new Audio Theme.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _theme The audio theme to set.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoAudioSettingsStateThemeChanged (ARCOMMANDS_JUMPINGSUMO_AUDIOSETTINGSSTATE_THEMECHANGED_THEME_ENUM _theme) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoAudioSettingsStateThemeChanged (pointer, capacity, _theme);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanStateScriptMetadataListChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlanState description:<br>
     * RoadPlan command responses.<br>
     * <br>
     * Command ScriptMetadataListChanged description:<br>
     * Update the controller with metadata.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _uuid Script uuid for which metadata changed.
     * @param _version Version number for this script.
     * @param _product Product targeted by script.
     * @param _name Display name of the script.
     * @param _lastModified Timestamp relative to the UNIX epoch of the last time the file was modified.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanStateScriptMetadataListChanged (String _uuid, byte _version, String _product, String _name, long _lastModified) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanStateScriptMetadataListChanged (pointer, capacity, _uuid, _version, _product, _name, _lastModified);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanStateAllScriptsMetadataChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlanState description:<br>
     * RoadPlan command responses.<br>
     * <br>
     * Command AllScriptsMetadataChanged description:<br>
     * Notify controller that all script metadatas are updated.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanStateAllScriptsMetadataChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanStateAllScriptsMetadataChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanStateScriptUploadChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlanState description:<br>
     * RoadPlan command responses.<br>
     * <br>
     * Command ScriptUploadChanged description:<br>
     * Device response to ScriptUploaded command.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _resultCode Error code.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanStateScriptUploadChanged (ARCOMMANDS_JUMPINGSUMO_ROADPLANSTATE_SCRIPTUPLOADCHANGED_RESULTCODE_ENUM _resultCode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanStateScriptUploadChanged (pointer, capacity, _resultCode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanStateScriptDeleteChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlanState description:<br>
     * RoadPlan command responses.<br>
     * <br>
     * Command ScriptDeleteChanged description:<br>
     * Device response to ScriptDelete command.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _resultCode Error code.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanStateScriptDeleteChanged (ARCOMMANDS_JUMPINGSUMO_ROADPLANSTATE_SCRIPTDELETECHANGED_RESULTCODE_ENUM _resultCode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanStateScriptDeleteChanged (pointer, capacity, _resultCode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RoadPlanStatePlayScriptChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class RoadPlanState description:<br>
     * RoadPlan command responses.<br>
     * <br>
     * Command PlayScriptChanged description:<br>
     * Device response to PlayScript command.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _resultCode Error code.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoRoadPlanStatePlayScriptChanged (ARCOMMANDS_JUMPINGSUMO_ROADPLANSTATE_PLAYSCRIPTCHANGED_RESULTCODE_ENUM _resultCode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoRoadPlanStatePlayScriptChanged (pointer, capacity, _resultCode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateOutdoorChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command OutdoorChanged description:<br>
     * @deprecated<br>
     * Outdoor property sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _outdoor 1 if outdoor, 0 if indoor
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoSpeedSettingsStateOutdoorChanged (byte _outdoor) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoSpeedSettingsStateOutdoorChanged (pointer, capacity, _outdoor);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaStreamingStateVideoEnableChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class MediaStreamingState description:<br>
     * Media streaming status.<br>
     * <br>
     * Command VideoEnableChanged description:<br>
     * Return video streaming status.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled Current video streaming status.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoMediaStreamingStateVideoEnableChanged (ARCOMMANDS_JUMPINGSUMO_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM _enabled) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoMediaStreamingStateVideoEnableChanged (pointer, capacity, _enabled);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>VideoSettingsStateAutorecordChanged</code> in feature <code>JumpingSumo</code><br>
     * <br>
     * Feature JumpingSumo description:<br>
     * All commands specific to the Jumping Sumo.<br>
     * <br>
     * Class VideoSettingsState description:<br>
     * Video settings state.<br>
     * <br>
     * Command AutorecordChanged description:<br>
     * Get video automatic recording status.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 0: Disabled 1: Enabled.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setJumpingSumoVideoSettingsStateAutorecordChanged (byte _enabled) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetJumpingSumoVideoSettingsStateAutorecordChanged (pointer, capacity, _enabled);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingFlatTrim</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command FlatTrim description:<br>
     * Do a flat trim<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingFlatTrim () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingFlatTrim (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingTakeOff</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command TakeOff description:<br>
     * Ask the drone to take off<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingTakeOff () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingTakeOff (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingPCMD</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command PCMD description:<br>
     * Ask the drone to move around.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _flag Boolean flag to activate roll/pitch movement
     * @param _roll Roll consign for the MiniDrone [-100;100]
     * @param _pitch Pitch consign for the MiniDrone [-100;100]
     * @param _yaw Yaw consign for the MiniDrone [-100;100]
     * @param _gaz Gaz consign for the MiniDrone [-100;100]
     * @param _timestamp Timestamp in miliseconds. Not an absolute time. (Typically 0 = time of connexion).
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingPCMD (byte _flag, byte _roll, byte _pitch, byte _yaw, byte _gaz, int _timestamp) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingPCMD (pointer, capacity, _flag, _roll, _pitch, _yaw, _gaz, _timestamp);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingLanding</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command Landing description:<br>
     * Ask the MiniDrone to land<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingLanding () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingLanding (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingEmergency</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command Emergency description:<br>
     * Put drone in emergency state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingEmergency () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingEmergency (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingAutoTakeOffMode</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command AutoTakeOffMode description:<br>
     * Set MiniDrone automatic take off mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of automatic take off mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingAutoTakeOffMode (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingAutoTakeOffMode (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingFlyingMode</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Piloting description:<br>
     * All commands related to piloting the MiniDrone<br>
     * <br>
     * Command FlyingMode description:<br>
     * Set drone FlyingMode. Only supported by UnknownProduct_3<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mode Drone Flying Mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingFlyingMode (ARCOMMANDS_MINIDRONE_PILOTING_FLYINGMODE_MODE_ENUM _mode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingFlyingMode (pointer, capacity, _mode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsFlip</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command Flip description:<br>
     * Make a flip<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _direction Direction for the flip
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneAnimationsFlip (ARCOMMANDS_MINIDRONE_ANIMATIONS_FLIP_DIRECTION_ENUM _direction) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneAnimationsFlip (pointer, capacity, _direction);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsCap</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Animations description:<br>
     * Animation commands<br>
     * <br>
     * Command Cap description:<br>
     * Change the product cap<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _offset Change the cap with offset angle [-180;180]
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneAnimationsCap (short _offset) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneAnimationsCap (pointer, capacity, _offset);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordPicture</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command Picture description:<br>
     * @deprecated<br>
     * Take picture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mass_storage_id Mass storage id to take picture
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneMediaRecordPicture (byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneMediaRecordPicture (pointer, capacity, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordPictureV2</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class MediaRecord description:<br>
     * Media recording management<br>
     * <br>
     * Command PictureV2 description:<br>
     * Take picture<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneMediaRecordPictureV2 () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneMediaRecordPictureV2 (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsMaxAltitude</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command MaxAltitude description:<br>
     * Set Max Altitude<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current altitude max in m
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingSettingsMaxAltitude (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingSettingsMaxAltitude (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsMaxTilt</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingSettings description:<br>
     * Piloting Settings commands<br>
     * <br>
     * Command MaxTilt description:<br>
     * Set Max Tilt<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current tilt max in degree
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingSettingsMaxTilt (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingSettingsMaxTilt (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsMaxVerticalSpeed</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command MaxVerticalSpeed description:<br>
     * Set Max Vertical speed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max vertical speed in m/s
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsMaxVerticalSpeed (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsMaxVerticalSpeed (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsMaxRotationSpeed</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command MaxRotationSpeed description:<br>
     * Set Max Rotation speed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max rotation speed in degree/s
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsMaxRotationSpeed (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsMaxRotationSpeed (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsWheels</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command Wheels description:<br>
     * Presence of wheels<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _present 1 if present, 0 if not present
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsWheels (byte _present) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsWheels (pointer, capacity, _present);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsMaxHorizontalSpeed</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettings description:<br>
     * Speed Settings commands<br>
     * <br>
     * Command MaxHorizontalSpeed description:<br>
     * Set Max Horizontal speed (only used in case where PilotingSettings_MaxTilt is not used like in hydrofoil mode)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max Horizontal speed in m/s
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsMaxHorizontalSpeed (float _current) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsMaxHorizontalSpeed (pointer, capacity, _current);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsCutOutMode</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command CutOutMode description:<br>
     * Set MiniDrone cut out mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enable Enable cut out mode (1 if is activate, 0 otherwise)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSettingsCutOutMode (byte _enable) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSettingsCutOutMode (pointer, capacity, _enable);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSControllerLatitudeForRun</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class GPS description:<br>
     * GPS related commands<br>
     * <br>
     * Command ControllerLatitudeForRun description:<br>
     * Set the controller latitude for a run.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Controller latitude in decimal degrees
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneGPSControllerLatitudeForRun (double _latitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneGPSControllerLatitudeForRun (pointer, capacity, _latitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSControllerLongitudeForRun</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class GPS description:<br>
     * GPS related commands<br>
     * <br>
     * Command ControllerLongitudeForRun description:<br>
     * Set the controller longitude for a run.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _longitude Controller longitude in decimal degrees
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneGPSControllerLongitudeForRun (double _longitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneGPSControllerLongitudeForRun (pointer, capacity, _longitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ConfigurationControllerType</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Configuration description:<br>
     * Configuration related commands<br>
     * <br>
     * Command ControllerType description:<br>
     * Set the controller type.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Controller type like iOS or Android
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneConfigurationControllerType (String _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneConfigurationControllerType (pointer, capacity, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ConfigurationControllerName</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class Configuration description:<br>
     * Configuration related commands<br>
     * <br>
     * Command ControllerName description:<br>
     * Set the controller name.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _name Controller name like com.parrot.freeflight3
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneConfigurationControllerName (String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneConfigurationControllerName (pointer, capacity, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateFlatTrimChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * Occasional information<br>
     * <br>
     * Command FlatTrimChanged description:<br>
     * MiniDrone send flat trim was correctly processed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingStateFlatTrimChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingStateFlatTrimChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateFlyingStateChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * Occasional information<br>
     * <br>
     * Command FlyingStateChanged description:<br>
     * Drone flying state changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state Drone flying state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingStateFlyingStateChanged (ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingStateFlyingStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAlertStateChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * Occasional information<br>
     * <br>
     * Command AlertStateChanged description:<br>
     * Drone alert state changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state Drone alert state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingStateAlertStateChanged (ARCOMMANDS_MINIDRONE_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingStateAlertStateChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateAutoTakeOffModeChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * Occasional information<br>
     * <br>
     * Command AutoTakeOffModeChanged description:<br>
     * Set MiniDrone automatic take off mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of automatic take off mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingStateAutoTakeOffModeChanged (byte _state) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingStateAutoTakeOffModeChanged (pointer, capacity, _state);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingStateFlyingModeChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingState description:<br>
     * Occasional information<br>
     * <br>
     * Command FlyingModeChanged description:<br>
     * FlyingMode changed. Only supported by UnknownProduct_3<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mode Drone Flying Mode
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingStateFlyingModeChanged (ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGMODECHANGED_MODE_ENUM _mode) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingStateFlyingModeChanged (pointer, capacity, _mode);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStatePictureStateChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command PictureStateChanged description:<br>
     * @deprecated<br>
     * State of picture recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state 1 if picture has been taken, 0 otherwise
     * @param _mass_storage_id Mass storage id to record
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneMediaRecordStatePictureStateChanged (byte _state, byte _mass_storage_id) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneMediaRecordStatePictureStateChanged (pointer, capacity, _state, _mass_storage_id);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordStatePictureStateChangedV2</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class MediaRecordState description:<br>
     * State of media recording<br>
     * <br>
     * Command PictureStateChangedV2 description:<br>
     * State of device picture recording changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of device picture recording
     * @param _error Error to explain the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneMediaRecordStatePictureStateChangedV2 (ARCOMMANDS_MINIDRONE_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM _state, ARCOMMANDS_MINIDRONE_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneMediaRecordStatePictureStateChangedV2 (pointer, capacity, _state, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MediaRecordEventPictureEventChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class MediaRecordEvent description:<br>
     * Events of media recording<br>
     * <br>
     * Command PictureEventChanged description:<br>
     * Event of picture recording<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _event Last event of picture recording
     * @param _error Error to explain the event
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneMediaRecordEventPictureEventChanged (ARCOMMANDS_MINIDRONE_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM _event, ARCOMMANDS_MINIDRONE_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneMediaRecordEventPictureEventChanged (pointer, capacity, _event, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateMaxAltitudeChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command MaxAltitudeChanged description:<br>
     * Max Altitude sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current altitude max
     * @param _min Range min of altitude
     * @param _max Range max of altitude
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingSettingsStateMaxAltitudeChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingSettingsStateMaxAltitudeChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>PilotingSettingsStateMaxTiltChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class PilotingSettingsState description:<br>
     * Piloting Settings state from product<br>
     * <br>
     * Command MaxTiltChanged description:<br>
     * Max tilt sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max tilt
     * @param _min Range min of tilt
     * @param _max Range max of tilt
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDronePilotingSettingsStateMaxTiltChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDronePilotingSettingsStateMaxTiltChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateMaxVerticalSpeedChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command MaxVerticalSpeedChanged description:<br>
     * Max vertical speed sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max vertical speed in m/s
     * @param _min Range min of vertical speed
     * @param _max Range max of vertical speed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsStateMaxVerticalSpeedChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsStateMaxVerticalSpeedChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateMaxRotationSpeedChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command MaxRotationSpeedChanged description:<br>
     * Max rotation speed sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max rotation speed in degree/s
     * @param _min Range min of rotation speed
     * @param _max Range max of rotation speed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsStateMaxRotationSpeedChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsStateMaxRotationSpeedChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateWheelsChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command WheelsChanged description:<br>
     * Presence of wheels sent by product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _present 1 if present, 0 if not present
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsStateWheelsChanged (byte _present) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsStateWheelsChanged (pointer, capacity, _present);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SpeedSettingsStateMaxHorizontalSpeedChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SpeedSettingsState description:<br>
     * Speed Settings state from product<br>
     * <br>
     * Command MaxHorizontalSpeedChanged description:<br>
     * Max horizontal speed sent by product (only used in case where PilotingSettings_MaxTilt is not used like in hydrofoil mode)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _current Current max horizontal speed in m/s
     * @param _min Range min of horizontal speed
     * @param _max Range max of horizontal speed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSpeedSettingsStateMaxHorizontalSpeedChanged (float _current, float _min, float _max) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSpeedSettingsStateMaxHorizontalSpeedChanged (pointer, capacity, _current, _min, _max);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductMotorsVersionChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductMotorsVersionChanged description:<br>
     * @deprecated<br>
     * Product Motors versions<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _motor Product Motor number [1 - 4]
     * @param _type Product Motor type
     * @param _software Product Motors software version
     * @param _hardware Product Motors hardware version
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSettingsStateProductMotorsVersionChanged (byte _motor, String _type, String _software, String _hardware) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSettingsStateProductMotorsVersionChanged (pointer, capacity, _motor, _type, _software, _hardware);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductInertialVersionChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductInertialVersionChanged description:<br>
     * @deprecated<br>
     * Product Inertial versions<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _software Product Inertial software version
     * @param _hardware Product Inertial hardware version
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSettingsStateProductInertialVersionChanged (String _software, String _hardware) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSettingsStateProductInertialVersionChanged (pointer, capacity, _software, _hardware);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateCutOutModeChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command CutOutModeChanged description:<br>
     * MiniDrone cut out mode<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enable State of cut out mode (1 if is activate, 0 otherwise)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneSettingsStateCutOutModeChanged (byte _enable) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneSettingsStateCutOutModeChanged (pointer, capacity, _enable);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>FloodControlStateFloodControlChanged</code> in feature <code>MiniDrone</code><br>
     * <br>
     * Feature MiniDrone description:<br>
     * All MiniDrone-only commands<br>
     * <br>
     * Class FloodControlState description:<br>
     * Settings state from product<br>
     * <br>
     * Command FloodControlChanged description:<br>
     * @deprecated<br>
     * Flood control regulation<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _delay Delay (in ms) between two PCMD
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setMiniDroneFloodControlStateFloodControlChanged (short _delay) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetMiniDroneFloodControlStateFloodControlChanged (pointer, capacity, _delay);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiRequestWifiList</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Wifi description:<br>
     * Wifi<br>
     * <br>
     * Command RequestWifiList description:<br>
     * Request wifi list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiRequestWifiList () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiRequestWifiList (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiRequestCurrentWifi</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Wifi description:<br>
     * Wifi<br>
     * <br>
     * Command RequestCurrentWifi description:<br>
     * Request current connected wifi<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiRequestCurrentWifi () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiRequestCurrentWifi (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiConnectToWifi</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Wifi description:<br>
     * Wifi<br>
     * <br>
     * Command ConnectToWifi description:<br>
     * Connect to wifi<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _bssid Wifi bssid
     * @param _ssid Wifi ssid
     * @param _passphrase Wifi passphrase
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiConnectToWifi (String _bssid, String _ssid, String _passphrase) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiConnectToWifi (pointer, capacity, _bssid, _ssid, _passphrase);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiForgetWifi</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Wifi description:<br>
     * Wifi<br>
     * <br>
     * Command ForgetWifi description:<br>
     * Forget wifi<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid Wifi ssid
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiForgetWifi (String _ssid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiForgetWifi (pointer, capacity, _ssid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiWifiAuthChannel</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Wifi description:<br>
     * Wifi<br>
     * <br>
     * Command WifiAuthChannel description:<br>
     * Controller inquire the list of authorized wifi channels<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiWifiAuthChannel () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiWifiAuthChannel (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DeviceRequestDeviceList</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Device description:<br>
     * Device Connection commands<br>
     * <br>
     * Command RequestDeviceList description:<br>
     * Request Device list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerDeviceRequestDeviceList () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerDeviceRequestDeviceList (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DeviceRequestCurrentDevice</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Device description:<br>
     * Device Connection commands<br>
     * <br>
     * Command RequestCurrentDevice description:<br>
     * Request current connected Device<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerDeviceRequestCurrentDevice () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerDeviceRequestCurrentDevice (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DeviceConnectToDevice</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Device description:<br>
     * Device Connection commands<br>
     * <br>
     * Command ConnectToDevice description:<br>
     * ask to connect to a device<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _deviceName Device name
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerDeviceConnectToDevice (String _deviceName) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerDeviceConnectToDevice (pointer, capacity, _deviceName);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsAllSettings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command AllSettings description:<br>
     * Get all product settings, the product must send all settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSettingsAllSettings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSettingsAllSettings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsReset</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command Reset description:<br>
     * Reset all settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSettingsReset () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSettingsReset (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonAllStates</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Common description:<br>
     * Common commands<br>
     * <br>
     * Command AllStates description:<br>
     * Get all product states<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCommonAllStates () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCommonAllStates (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessPointSettingsAccessPointSSID</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AccessPointSettings description:<br>
     * AccessPoint settings commands<br>
     * <br>
     * Command AccessPointSSID description:<br>
     * Set AccessPoint SSID<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid AccessPoint SSID
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAccessPointSettingsAccessPointSSID (String _ssid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAccessPointSettingsAccessPointSSID (pointer, capacity, _ssid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessPointSettingsAccessPointChannel</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AccessPointSettings description:<br>
     * AccessPoint settings commands<br>
     * <br>
     * Command AccessPointChannel description:<br>
     * Set AccessPoint Channel<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _channel AccessPoint Channel
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAccessPointSettingsAccessPointChannel (byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAccessPointSettingsAccessPointChannel (pointer, capacity, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessPointSettingsWifiSelection</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AccessPointSettings description:<br>
     * AccessPoint settings commands<br>
     * <br>
     * Command WifiSelection description:<br>
     * Set AccessPoint Band and Channel<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi selection (only manual at the moment)
     * @param _band The allowed band : 2.4 Ghz or 5 Ghz
     * @param _channel The channel
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAccessPointSettingsWifiSelection (ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGS_WIFISELECTION_TYPE_ENUM _type, ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGS_WIFISELECTION_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAccessPointSettingsWifiSelection (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CameraResetOrientation</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Camera description:<br>
     * Ask the drone to move camera<br>
     * <br>
     * Command ResetOrientation description:<br>
     * Reset pan and tilt to center<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCameraResetOrientation () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCameraResetOrientation (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GamepadInfosGetGamepadControls</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class GamepadInfos description:<br>
     * Request infos about the gamepad of the SkyController<br>
     * <br>
     * Command GetGamepadControls description:<br>
     * Asks the SkyController to send the button and axis list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerGamepadInfosGetGamepadControls () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerGamepadInfosGetGamepadControls (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsGetCurrentButtonMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappings description:<br>
     * Controls the button mappings of the SkyController<br>
     * <br>
     * Command GetCurrentButtonMappings description:<br>
     * Asks the SkyController to send its current button mapping<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsGetCurrentButtonMappings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsGetCurrentButtonMappings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsGetAvailableButtonMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappings description:<br>
     * Controls the button mappings of the SkyController<br>
     * <br>
     * Command GetAvailableButtonMappings description:<br>
     * Asks the SkyController to send the possible mappings for each button<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsGetAvailableButtonMappings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsGetAvailableButtonMappings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsSetButtonMapping</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappings description:<br>
     * Controls the button mappings of the SkyController<br>
     * <br>
     * Command SetButtonMapping description:<br>
     * Set a button mapping to the SkyController<br>
     * @note replaces previous mapping for the given key<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _key_id The keycode to map
     * @param _mapping_uid The mapping to associate with the key
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsSetButtonMapping (int _key_id, String _mapping_uid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsSetButtonMapping (pointer, capacity, _key_id, _mapping_uid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsDefaultButtonMapping</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappings description:<br>
     * Controls the button mappings of the SkyController<br>
     * <br>
     * Command DefaultButtonMapping description:<br>
     * Asks the SkyController to reset the button mappings to the default value<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsDefaultButtonMapping () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsDefaultButtonMapping (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsGetCurrentAxisMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappings description:<br>
     * Controls the axis mappings of the SkyController<br>
     * <br>
     * Command GetCurrentAxisMappings description:<br>
     * Asks the SkyController to send its current axis mapping<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsGetCurrentAxisMappings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsGetCurrentAxisMappings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsGetAvailableAxisMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappings description:<br>
     * Controls the axis mappings of the SkyController<br>
     * <br>
     * Command GetAvailableAxisMappings description:<br>
     * Asks the SkyController to send the possible mappings for each axis<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsGetAvailableAxisMappings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsGetAvailableAxisMappings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsSetAxisMapping</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappings description:<br>
     * Controls the axis mappings of the SkyController<br>
     * <br>
     * Command SetAxisMapping description:<br>
     * Set a axis mapping to the SkyController<br>
     * @note replaces previous mapping for the given axis<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _axis_id The axiscode to map
     * @param _mapping_uid The mapping to associate with the axis
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsSetAxisMapping (int _axis_id, String _mapping_uid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsSetAxisMapping (pointer, capacity, _axis_id, _mapping_uid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsDefaultAxisMapping</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappings description:<br>
     * Controls the axis mappings of the SkyController<br>
     * <br>
     * Command DefaultAxisMapping description:<br>
     * Asks the SkyController to reset the axis mappings to the default value<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsDefaultAxisMapping () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsDefaultAxisMapping (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersGetCurrentAxisFilters</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFilters description:<br>
     * Controls the axis filters of the SkyController<br>
     * <br>
     * Command GetCurrentAxisFilters description:<br>
     * Asks the SkyController to send its current axis filters<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersGetCurrentAxisFilters () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersGetCurrentAxisFilters (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersGetPresetAxisFilters</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFilters description:<br>
     * Controls the axis filters of the SkyController<br>
     * <br>
     * Command GetPresetAxisFilters description:<br>
     * Asks the SkyController to send the preset filters<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersGetPresetAxisFilters () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersGetPresetAxisFilters (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersSetAxisFilter</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFilters description:<br>
     * Controls the axis filters of the SkyController<br>
     * <br>
     * Command SetAxisFilter description:<br>
     * Set an axis filter to the SkyController<br>
     * @note replaces previous filter for the given axis<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _axis_id The axiscode to filter
     * @param _filter_uid_or_builder The mapping preset to associate with the axis
     * @param _filter_uid_or_builder (Or a string to build a new one)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersSetAxisFilter (int _axis_id, String _filter_uid_or_builder) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersSetAxisFilter (pointer, capacity, _axis_id, _filter_uid_or_builder);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersDefaultAxisFilters</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFilters description:<br>
     * Controls the axis filters of the SkyController<br>
     * <br>
     * Command DefaultAxisFilters description:<br>
     * Asks the SkyController to reset the axis filters to the default value<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersDefaultAxisFilters () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersDefaultAxisFilters (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CoPilotingSetPilotingSource</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class CoPiloting description:<br>
     * Configuration of the co-piloting feature<br>
     * <br>
     * Command SetPilotingSource description:<br>
     * Set the SkyController piloting source<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _source The source
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCoPilotingSetPilotingSource (ARCOMMANDS_SKYCONTROLLER_COPILOTING_SETPILOTINGSOURCE_SOURCE_ENUM _source) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCoPilotingSetPilotingSource (pointer, capacity, _source);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationEnableMagnetoCalibrationQualityUpdates</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class Calibration description:<br>
     * Commands related to the SkyController sensors calibration<br>
     * <br>
     * Command EnableMagnetoCalibrationQualityUpdates description:<br>
     * Asks the SkyController to send (or not) the magneto calibration quality updates.<br>
     * The MagnetoCalibrationState will always be sent when the status parameters changes,<br>
     * regardless of this setting.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enable Flag to enable the feature.
     * @param _enable 1 = enable quality updates
     * @param _enable 0 = disable quality updates
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdates (byte _enable) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdates (pointer, capacity, _enable);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiStateWifiList</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class WifiState description:<br>
     * Wifi state<br>
     * <br>
     * Command WifiList description:<br>
     * Return the available wifi list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _bssid Wifi bssid
     * @param _ssid Wifi ssid
     * @param _secured Is wifi secured by passphrase
     * @param _saved Is wifi saved in terminal
     * @param _rssi Wifi rssi
     * @param _frequency Wifi frequency
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiStateWifiList (String _bssid, String _ssid, byte _secured, byte _saved, int _rssi, int _frequency) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiStateWifiList (pointer, capacity, _bssid, _ssid, _secured, _saved, _rssi, _frequency);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiStateConnexionChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class WifiState description:<br>
     * Wifi state<br>
     * <br>
     * Command ConnexionChanged description:<br>
     * Return connexion status<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid Wifi ssid
     * @param _status Wifi status
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiStateConnexionChanged (String _ssid, ARCOMMANDS_SKYCONTROLLER_WIFISTATE_CONNEXIONCHANGED_STATUS_ENUM _status) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiStateConnexionChanged (pointer, capacity, _ssid, _status);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiStateWifiAuthChannelListChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class WifiState description:<br>
     * Wifi state<br>
     * <br>
     * Command WifiAuthChannelListChanged description:<br>
     * Notify of an Authorized Channel<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band The band of this channel : 2.4 GHz or 5 GHz
     * @param _channel The authorized channel
     * @param _in_or_out Bit 0 is 1 if channel is authorized outside (0 otherwise) ; Bit 1 is 1 if channel is authorized inside (0 otherwise)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiStateWifiAuthChannelListChanged (ARCOMMANDS_SKYCONTROLLER_WIFISTATE_WIFIAUTHCHANNELLISTCHANGED_BAND_ENUM _band, byte _channel, byte _in_or_out) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiStateWifiAuthChannelListChanged (pointer, capacity, _band, _channel, _in_or_out);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiStateAllWifiAuthChannelChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class WifiState description:<br>
     * Wifi state<br>
     * <br>
     * Command AllWifiAuthChannelChanged description:<br>
     * Notify the end of the list of Authorized wifi Channel<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiStateAllWifiAuthChannelChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiStateAllWifiAuthChannelChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiStateWifiSignalChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class WifiState description:<br>
     * Wifi state<br>
     * <br>
     * Command WifiSignalChanged description:<br>
     * State of the wifi signal<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _level Level of the signal. Levels are from 0 to 5. 0 is an unknown value. 1 is a weak wifi signal, 5 is the best.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerWifiStateWifiSignalChanged (byte _level) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerWifiStateWifiSignalChanged (pointer, capacity, _level);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DeviceStateDeviceList</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class DeviceState description:<br>
     * Device state<br>
     * <br>
     * Command DeviceList description:<br>
     * Return the available Device list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _name Device name
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerDeviceStateDeviceList (String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerDeviceStateDeviceList (pointer, capacity, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DeviceStateConnexionChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class DeviceState description:<br>
     * Device state<br>
     * <br>
     * Command ConnexionChanged description:<br>
     * Return device connexion status<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _status Wifi status to Device
     * @param _deviceName Device name
     * @param _deviceProductID Device name
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerDeviceStateConnexionChanged (ARCOMMANDS_SKYCONTROLLER_DEVICESTATE_CONNEXIONCHANGED_STATUS_ENUM _status, String _deviceName, short _deviceProductID) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerDeviceStateConnexionChanged (pointer, capacity, _status, _deviceName, _deviceProductID);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateAllSettingsChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command AllSettingsChanged description:<br>
     * State sent when all settings has been sent<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSettingsStateAllSettingsChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSettingsStateAllSettingsChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateResetChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ResetChanged description:<br>
     * State sent when all settings has been resetting<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSettingsStateResetChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSettingsStateResetChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductSerialChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductSerialChanged description:<br>
     * Product serial number<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _serialNumber Serial number (hexadecimal value)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSettingsStateProductSerialChanged (String _serialNumber) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSettingsStateProductSerialChanged (pointer, capacity, _serialNumber);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductVariantChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductVariantChanged description:<br>
     * Product variant of SkyController<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _variant Variant of the product
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSettingsStateProductVariantChanged (ARCOMMANDS_SKYCONTROLLER_SETTINGSSTATE_PRODUCTVARIANTCHANGED_VARIANT_ENUM _variant) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSettingsStateProductVariantChanged (pointer, capacity, _variant);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateAllStatesChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command AllStatesChanged description:<br>
     * State sent when all product states has been sent<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCommonStateAllStatesChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCommonStateAllStatesChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SkyControllerStateBatteryChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SkyControllerState description:<br>
     * Sky Controller states<br>
     * <br>
     * Command BatteryChanged description:<br>
     * State sent when SkyController battery has changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _percent SkyController battery
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSkyControllerStateBatteryChanged (byte _percent) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSkyControllerStateBatteryChanged (pointer, capacity, _percent);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SkyControllerStateGpsFixChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SkyControllerState description:<br>
     * Sky Controller states<br>
     * <br>
     * Command GpsFixChanged description:<br>
     * State sent when SkyController gps fix has changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _fixed SkyController fixed
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSkyControllerStateGpsFixChanged (byte _fixed) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSkyControllerStateGpsFixChanged (pointer, capacity, _fixed);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SkyControllerStateGpsPositionChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class SkyControllerState description:<br>
     * Sky Controller states<br>
     * <br>
     * Command GpsPositionChanged description:<br>
     * State sent when the SkyController gps position has changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude SkyController latitude (500. if not available)
     * @param _longitude SkyController longiture (500. if not available)
     * @param _altitude Altitude (in meters) above sea level
     * @param _altitude Only meaningful if latitude and longiture are available
     * @param _heading SkyController heading relative to magnetic north (500.f if not available)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerSkyControllerStateGpsPositionChanged (double _latitude, double _longitude, double _altitude, float _heading) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerSkyControllerStateGpsPositionChanged (pointer, capacity, _latitude, _longitude, _altitude, _heading);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessPointSettingsStateAccessPointSSIDChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AccessPointSettingsState description:<br>
     * AccessPoint settings state from product<br>
     * <br>
     * Command AccessPointSSIDChanged description:<br>
     * State sent when AccessPoint ssid has been sent<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid AccessPoint SSID
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAccessPointSettingsStateAccessPointSSIDChanged (String _ssid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAccessPointSettingsStateAccessPointSSIDChanged (pointer, capacity, _ssid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessPointSettingsStateAccessPointChannelChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AccessPointSettingsState description:<br>
     * AccessPoint settings state from product<br>
     * <br>
     * Command AccessPointChannelChanged description:<br>
     * State sent when AccessPoint channel has been sent<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _channel AccessPoint Channel
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAccessPointSettingsStateAccessPointChannelChanged (byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAccessPointSettingsStateAccessPointChannelChanged (pointer, capacity, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessPointSettingsStateWifiSelectionChanged</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AccessPointSettingsState description:<br>
     * AccessPoint settings state from product<br>
     * <br>
     * Command WifiSelectionChanged description:<br>
     * Wifi selection from product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type of wifi selection (only manual at the moment)
     * @param _band The allowed band : 2.4 Ghz or 5 Ghz
     * @param _channel The channel
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAccessPointSettingsStateWifiSelectionChanged (ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGSSTATE_WIFISELECTIONCHANGED_TYPE_ENUM _type, ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGSSTATE_WIFISELECTIONCHANGED_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAccessPointSettingsStateWifiSelectionChanged (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GamepadInfosStateGamepadControl</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class GamepadInfosState description:<br>
     * Informations about the gamepad of the SkyController<br>
     * <br>
     * Command GamepadControl description:<br>
     * Describe an existing button or axis of the gamepad<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type The type (axis/button) of the control
     * @param _id The button or axis id
     * @param _id @note A button and an axis can have the same ID, but their type is different
     * @param _name Display name for the control
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerGamepadInfosStateGamepadControl (ARCOMMANDS_SKYCONTROLLER_GAMEPADINFOSSTATE_GAMEPADCONTROL_TYPE_ENUM _type, int _id, String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerGamepadInfosStateGamepadControl (pointer, capacity, _type, _id, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GamepadInfosStateAllGamepadControlsSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class GamepadInfosState description:<br>
     * Informations about the gamepad of the SkyController<br>
     * <br>
     * Command AllGamepadControlsSent description:<br>
     * Sent by the SkyController after sending its last 'gamepadControl' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerGamepadInfosStateAllGamepadControlsSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerGamepadInfosStateAllGamepadControlsSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsStateCurrentButtonMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappingsState description:<br>
     * State of the button mappings of the SkyController<br>
     * <br>
     * Command CurrentButtonMappings description:<br>
     * Sent by the SkyController each time a mapping changes<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _key_id The keycode mapped
     * @param _mapping_uid The mapping associated
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsStateCurrentButtonMappings (int _key_id, String _mapping_uid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsStateCurrentButtonMappings (pointer, capacity, _key_id, _mapping_uid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsStateAllCurrentButtonMappingsSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappingsState description:<br>
     * State of the button mappings of the SkyController<br>
     * <br>
     * Command AllCurrentButtonMappingsSent description:<br>
     * Sent by the SkyController after sending its last 'currentButtonMappings' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsStateAllCurrentButtonMappingsSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsStateAllCurrentButtonMappingsSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsStateAvailableButtonMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappingsState description:<br>
     * State of the button mappings of the SkyController<br>
     * <br>
     * Command AvailableButtonMappings description:<br>
     * Sent after a 'getAvailableButtonMappings' request<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mapping_uid The mapping UID (used in communication with the SkyController)
     * @param _name Display name for the user
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsStateAvailableButtonMappings (String _mapping_uid, String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsStateAvailableButtonMappings (pointer, capacity, _mapping_uid, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonMappingsStateAllAvailableButtonsMappingsSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonMappingsState description:<br>
     * State of the button mappings of the SkyController<br>
     * <br>
     * Command AllAvailableButtonsMappingsSent description:<br>
     * Sent by the SkyController after sending its last 'availableButtonMappings' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsStateCurrentAxisMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappingsState description:<br>
     * State of the axis mappings of the SkyController<br>
     * <br>
     * Command CurrentAxisMappings description:<br>
     * Sent by the SkyController each time a mapping changes<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _axis_id The axiscode mapped
     * @param _mapping_uid The mapping associated
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsStateCurrentAxisMappings (int _axis_id, String _mapping_uid) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsStateCurrentAxisMappings (pointer, capacity, _axis_id, _mapping_uid);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsStateAllCurrentAxisMappingsSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappingsState description:<br>
     * State of the axis mappings of the SkyController<br>
     * <br>
     * Command AllCurrentAxisMappingsSent description:<br>
     * Sent by the SkyController after sending its last 'currentAxisMappings' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsStateAllCurrentAxisMappingsSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsStateAllCurrentAxisMappingsSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsStateAvailableAxisMappings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappingsState description:<br>
     * State of the axis mappings of the SkyController<br>
     * <br>
     * Command AvailableAxisMappings description:<br>
     * Sent after a 'getAvailableAxisMappings' request<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mapping_uid The mapping UID (used in communication with the SkyController)
     * @param _name Display name for the user
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsStateAvailableAxisMappings (String _mapping_uid, String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsStateAvailableAxisMappings (pointer, capacity, _mapping_uid, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisMappingsStateAllAvailableAxisMappingsSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisMappingsState description:<br>
     * State of the axis mappings of the SkyController<br>
     * <br>
     * Command AllAvailableAxisMappingsSent description:<br>
     * Sent by the SkyController after sending its last 'availableAxisMappings' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisMappingsStateAllAvailableAxisMappingsSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisMappingsStateAllAvailableAxisMappingsSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersStateCurrentAxisFilters</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFiltersState description:<br>
     * State of the axis filters of the SkyController<br>
     * <br>
     * Command CurrentAxisFilters description:<br>
     * Sent by the SkyController each time a filter changes<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _axis_id The axiscode filtered
     * @param _filter_uid_or_builder The filter associated
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersStateCurrentAxisFilters (int _axis_id, String _filter_uid_or_builder) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersStateCurrentAxisFilters (pointer, capacity, _axis_id, _filter_uid_or_builder);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersStateAllCurrentFiltersSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFiltersState description:<br>
     * State of the axis filters of the SkyController<br>
     * <br>
     * Command AllCurrentFiltersSent description:<br>
     * Sent by the SkyController after sending its last 'currentAxisFilters' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersStateAllCurrentFiltersSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersStateAllCurrentFiltersSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersStatePresetAxisFilters</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFiltersState description:<br>
     * State of the axis filters of the SkyController<br>
     * <br>
     * Command PresetAxisFilters description:<br>
     * Sent after a 'getPresetAxisFilters' request<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _filter_uid The filter UID (used in communication with the SkyController)
     * @param _name Display name for the user
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersStatePresetAxisFilters (String _filter_uid, String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersStatePresetAxisFilters (pointer, capacity, _filter_uid, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AxisFiltersStateAllPresetFiltersSent</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class AxisFiltersState description:<br>
     * State of the axis filters of the SkyController<br>
     * <br>
     * Command AllPresetFiltersSent description:<br>
     * Sent by the SkyController after sending its last 'presetAxisFilters' command<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerAxisFiltersStateAllPresetFiltersSent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerAxisFiltersStateAllPresetFiltersSent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CoPilotingStatePilotingSource</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class CoPilotingState description:<br>
     * State of the co-piloting feature<br>
     * <br>
     * Command PilotingSource description:<br>
     * Source of the piloting commands<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _source The source
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCoPilotingStatePilotingSource (ARCOMMANDS_SKYCONTROLLER_COPILOTINGSTATE_PILOTINGSOURCE_SOURCE_ENUM _source) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCoPilotingStatePilotingSource (pointer, capacity, _source);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationStateMagnetoCalibrationState</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class CalibrationState description:<br>
     * State of the SkyController calibration<br>
     * <br>
     * Command MagnetoCalibrationState description:<br>
     * The current state of the magnetometer calibration<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _status The global status of the calibration
     * @param _X_Quality Calibration quality on X axis.
     * @param _X_Quality 0 is bad, 255 is perfect
     * @param _Y_Quality Calibration quality on Y axis.
     * @param _Y_Quality 0 is bad, 255 is perfect
     * @param _Z_Quality Calibration quality on Z axis.
     * @param _Z_Quality 0 is bad, 255 is perfect
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCalibrationStateMagnetoCalibrationState (ARCOMMANDS_SKYCONTROLLER_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATE_STATUS_ENUM _status, byte _X_Quality, byte _Y_Quality, byte _Z_Quality) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCalibrationStateMagnetoCalibrationState (pointer, capacity, _status, _X_Quality, _Y_Quality, _Z_Quality);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationStateMagnetoCalibrationQualityUpdatesState</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class CalibrationState description:<br>
     * State of the SkyController calibration<br>
     * <br>
     * Command MagnetoCalibrationQualityUpdatesState description:<br>
     * State of the "send calibration state on quality change" setting.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled Flag (is the feature enabled).
     * @param _enabled 1 = The skycontroller sends updated when quality is updated
     * @param _enabled 0 = The skycontroller only sent updated when state is updated
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesState (byte _enabled) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesState (pointer, capacity, _enabled);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ButtonEventsSettings</code> in feature <code>SkyController</code><br>
     * <br>
     * Feature SkyController description:<br>
     * All SkyController-only commands<br>
     * <br>
     * Class ButtonEvents description:<br>
     * Events sent on SkyController button presses.<br>
     * These events are sent under certain conditions only.<br>
     * <br>
     * Command Settings description:<br>
     * Event sent when the settings button is pressed.<br>
     * This event is sent only when the sky controller is connected<br>
     * to a drone.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setSkyControllerButtonEventsSettings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetSkyControllerButtonEventsSettings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkDisconnect</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Network description:<br>
     * Network related commands<br>
     * <br>
     * Command Disconnect description:<br>
     * @deprecated<br>
     * Signals the remote that the host will disconnect and close its<br>
     * libARNetwork instance (and all threads that use libARNetwork)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonNetworkDisconnect () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonNetworkDisconnect (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsAllSettings</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command AllSettings description:<br>
     * Get all product settings, the product must send all settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsAllSettings () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsAllSettings (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsReset</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command Reset description:<br>
     * Reset all settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsReset () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsReset (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsProductName</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command ProductName description:<br>
     * Set Product name<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _name Product name
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsProductName (String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsProductName (pointer, capacity, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsCountry</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command Country description:<br>
     * Set current Country of controller<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _code Country code with ISO 3166 format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsCountry (String _code) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsCountry (pointer, capacity, _code);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsAutoCountry</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Settings description:<br>
     * Settings commands<br>
     * <br>
     * Command AutoCountry description:<br>
     * Set Auto Country Settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _automatic Boolean : 0 : Manual / 1 : Auto
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsAutoCountry (byte _automatic) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsAutoCountry (pointer, capacity, _automatic);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonAllStates</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Common description:<br>
     * Common commands<br>
     * <br>
     * Command AllStates description:<br>
     * Get all product states.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonAllStates () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonAllStates (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonCurrentDate</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Common description:<br>
     * Common commands<br>
     * <br>
     * Command CurrentDate description:<br>
     * Set current date of controller<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _date Date with ISO-8601 format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonCurrentDate (String _date) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonCurrentDate (pointer, capacity, _date);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonCurrentTime</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Common description:<br>
     * Common commands<br>
     * <br>
     * Command CurrentTime description:<br>
     * Set current time of controller<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _time Time with ISO-8601 format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonCurrentTime (String _time) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonCurrentTime (pointer, capacity, _time);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonReboot</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Common description:<br>
     * Common commands<br>
     * <br>
     * Command Reboot description:<br>
     * Command to ask reboot to product<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonReboot () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonReboot (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>OverHeatSwitchOff</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class OverHeat description:<br>
     * Over heat commands<br>
     * <br>
     * Command SwitchOff description:<br>
     * @deprecated<br>
     * Switch off the drone when a overheat appeared<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonOverHeatSwitchOff () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonOverHeatSwitchOff (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>OverHeatVentilate</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class OverHeat description:<br>
     * Over heat commands<br>
     * <br>
     * Command Ventilate description:<br>
     * @deprecated<br>
     * Ventilate the drone when a overheat appeared<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonOverHeatVentilate () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonOverHeatVentilate (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ControllerIsPiloting</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Controller description:<br>
     * Notify the device about the state of the controller application.<br>
     * <br>
     * Command IsPiloting description:<br>
     * Tell the device when the controller application enters/leaves the piloting HUD.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _piloting 0 when the application is not in the piloting HUD, 1 when it enters the HUD.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonControllerIsPiloting (byte _piloting) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonControllerIsPiloting (pointer, capacity, _piloting);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiSettingsOutdoorSetting</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class WifiSettings description:<br>
     * Wifi settings commands<br>
     * <br>
     * Command OutdoorSetting description:<br>
     * Send to product if it should use its outdoor wifi config, or indoor<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _outdoor 1 if it should use outdoor wifi settings, 0 otherwise
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonWifiSettingsOutdoorSetting (byte _outdoor) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonWifiSettingsOutdoorSetting (pointer, capacity, _outdoor);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MavlinkStart</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Mavlink description:<br>
     * Mavlink flight plans commands<br>
     * <br>
     * Command Start description:<br>
     * Start the flight plan<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _filepath flight plan file path from the mavlink ftp root
     * @param _type type of the played mavlink file
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonMavlinkStart (String _filepath, ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonMavlinkStart (pointer, capacity, _filepath, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MavlinkPause</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Mavlink description:<br>
     * Mavlink flight plans commands<br>
     * <br>
     * Command Pause description:<br>
     * Pause the flightplan (can be restarted with a start)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonMavlinkPause () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonMavlinkPause (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MavlinkStop</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Mavlink description:<br>
     * Mavlink flight plans commands<br>
     * <br>
     * Command Stop description:<br>
     * Stop the flightplan<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonMavlinkStop () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonMavlinkStop (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationMagnetoCalibration</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Calibration description:<br>
     * Calibration commands<br>
     * <br>
     * Command MagnetoCalibration description:<br>
     * Sent when a calibration of the magnetometer is asked or is aborted<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _calibrate 1 if the calibration should be started, 0 if it should be aborted
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCalibrationMagnetoCalibration (byte _calibrate) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCalibrationMagnetoCalibration (pointer, capacity, _calibrate);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>GPSControllerPositionForRun</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class GPS description:<br>
     * GPS related commands<br>
     * <br>
     * Command ControllerPositionForRun description:<br>
     * Set the controller position for a run. This command is used by all non gps products. Watch out, this command cannot be used with BLE products<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _latitude Controller latitude in decimal degrees
     * @param _longitude Controller longitude in decimal degrees
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonGPSControllerPositionForRun (double _latitude, double _longitude) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonGPSControllerPositionForRun (pointer, capacity, _latitude, _longitude);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AudioControllerReadyForStreaming</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Audio description:<br>
     * Audio-related commands.<br>
     * <br>
     * Command ControllerReadyForStreaming description:<br>
     * Tell the firmware whether the controller is ready to start audio streaming.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ready Bit field for TX and RX ready.
     * @param _ready bit 0 is 1 if controller is ready and wants to receive sound (Drone TX)
     * @param _ready bit 1 is 1 if controller is ready and wants to send sound (Drone RX)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAudioControllerReadyForStreaming (byte _ready) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAudioControllerReadyForStreaming (pointer, capacity, _ready);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>HeadlightsIntensity</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Headlights description:<br>
     * Controls the headlight LEDs of the Evo variants.<br>
     * <br>
     * Command Intensity description:<br>
     * Set instensity of lighting LEDs.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _left Set the left LED intensity value (0 through 255).
     * @param _right Set the right LED intensity value (0 through 255).
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonHeadlightsIntensity (byte _left, byte _right) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonHeadlightsIntensity (pointer, capacity, _left, _right);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStartAnimation</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Animations description:<br>
     * Animations-related commands.<br>
     * <br>
     * Command StartAnimation description:<br>
     * Start a paramaterless animation.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _anim Animation to start.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAnimationsStartAnimation (ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM _anim) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAnimationsStartAnimation (pointer, capacity, _anim);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStopAnimation</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Animations description:<br>
     * Animations-related commands.<br>
     * <br>
     * Command StopAnimation description:<br>
     * Stop a running animation.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _anim Animation to stop.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAnimationsStopAnimation (ARCOMMANDS_COMMON_ANIMATIONS_STOPANIMATION_ANIM_ENUM _anim) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAnimationsStopAnimation (pointer, capacity, _anim);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStopAllAnimations</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Animations description:<br>
     * Animations-related commands.<br>
     * <br>
     * Command StopAllAnimations description:<br>
     * Stop all running animations.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAnimationsStopAllAnimations () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAnimationsStopAllAnimations (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessoryConfig</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Accessory description:<br>
     * Accessories-related commands.<br>
     * <br>
     * Command Config description:<br>
     * Set the current accessory configuration.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _accessory Accessory configuration to set.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAccessoryConfig (ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM _accessory) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAccessoryConfig (pointer, capacity, _accessory);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ChargerSetMaxChargeRate</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class Charger description:<br>
     * Commands sent by the controller to set charger parameters.<br>
     * <br>
     * Command SetMaxChargeRate description:<br>
     * @deprecated<br>
     * Set the maximum charge rate allowed to charge a battery.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _rate The new maximum charge rate.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonChargerSetMaxChargeRate (ARCOMMANDS_COMMON_CHARGER_SETMAXCHARGERATE_RATE_ENUM _rate) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonChargerSetMaxChargeRate (pointer, capacity, _rate);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>NetworkEventDisconnection</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class NetworkEvent description:<br>
     * Network Event from product<br>
     * <br>
     * Command Disconnection description:<br>
     * Signals the remote that the host will disconnect and close its<br>
     * libARNetwork instance (and all threads that use libARNetwork)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _cause Cause of the disconnection of the product
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonNetworkEventDisconnection (ARCOMMANDS_COMMON_NETWORKEVENT_DISCONNECTION_CAUSE_ENUM _cause) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonNetworkEventDisconnection (pointer, capacity, _cause);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateAllSettingsChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command AllSettingsChanged description:<br>
     * State sent when all settings has been sent.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateAllSettingsChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateAllSettingsChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateResetChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ResetChanged description:<br>
     * State sent when all settings has been resetting.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateResetChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateResetChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductNameChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductNameChanged description:<br>
     * Product name<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _name Product name
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateProductNameChanged (String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateProductNameChanged (pointer, capacity, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductVersionChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductVersionChanged description:<br>
     * Product versions<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _software Product software version
     * @param _hardware Product hardware version
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateProductVersionChanged (String _software, String _hardware) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateProductVersionChanged (pointer, capacity, _software, _hardware);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductSerialHighChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductSerialHighChanged description:<br>
     * Product serial number<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _high Serial high number (hexadecimal value)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateProductSerialHighChanged (String _high) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateProductSerialHighChanged (pointer, capacity, _high);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateProductSerialLowChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command ProductSerialLowChanged description:<br>
     * Product serial number<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _low Serial low number (hexadecimal value)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateProductSerialLowChanged (String _low) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateProductSerialLowChanged (pointer, capacity, _low);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateCountryChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command CountryChanged description:<br>
     * Inform current Country set in product. (Answer to 'Country' command)<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _code Country code with ISO 3166 format, empty string means unknown country.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateCountryChanged (String _code) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateCountryChanged (pointer, capacity, _code);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SettingsStateAutoCountryChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class SettingsState description:<br>
     * Settings state from product<br>
     * <br>
     * Command AutoCountryChanged description:<br>
     * Inform Auto Country Settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _automatic Boolean : 0 : Manual / 1 : Auto
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonSettingsStateAutoCountryChanged (byte _automatic) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonSettingsStateAutoCountryChanged (pointer, capacity, _automatic);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateAllStatesChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command AllStatesChanged description:<br>
     * State sent when all product states has been sent.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateAllStatesChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateAllStatesChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateBatteryStateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command BatteryStateChanged description:<br>
     * Battery state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _percent Battery percentage
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateBatteryStateChanged (byte _percent) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateBatteryStateChanged (pointer, capacity, _percent);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateMassStorageStateListChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command MassStorageStateListChanged description:<br>
     * Mass storage state list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mass_storage_id Mass storage id (unique)
     * @param _name Mass storage name
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateMassStorageStateListChanged (byte _mass_storage_id, String _name) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateMassStorageStateListChanged (pointer, capacity, _mass_storage_id, _name);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateMassStorageInfoStateListChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command MassStorageInfoStateListChanged description:<br>
     * Mass storage info state list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _mass_storage_id Mass storage state id (unique)
     * @param _size Mass storage size in MBytes
     * @param _used_size Mass storage used size in MBytes
     * @param _plugged Mass storage plugged (1 if mass storage is plugged, otherwise 0)
     * @param _full Mass storage full information state (1 if mass storage full, 0 otherwise).
     * @param _internal Mass storage internal type state (1 if mass storage is internal, 0 otherwise)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateMassStorageInfoStateListChanged (byte _mass_storage_id, int _size, int _used_size, byte _plugged, byte _full, byte _internal) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateMassStorageInfoStateListChanged (pointer, capacity, _mass_storage_id, _size, _used_size, _plugged, _full, _internal);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateCurrentDateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command CurrentDateChanged description:<br>
     * Current date state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _date Date with ISO-8601 format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateCurrentDateChanged (String _date) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateCurrentDateChanged (pointer, capacity, _date);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateCurrentTimeChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command CurrentTimeChanged description:<br>
     * Current time state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _time Time with ISO-8601 format
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateCurrentTimeChanged (String _time) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateCurrentTimeChanged (pointer, capacity, _time);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateMassStorageInfoRemainingListChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command MassStorageInfoRemainingListChanged description:<br>
     * @deprecated<br>
     * Mass storage info remaining list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _free_space Mass storage free space in MBytes
     * @param _rec_time Mass storage record time reamining in minute
     * @param _photo_remaining Mass storage photo remaining
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateMassStorageInfoRemainingListChanged (int _free_space, short _rec_time, int _photo_remaining) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateMassStorageInfoRemainingListChanged (pointer, capacity, _free_space, _rec_time, _photo_remaining);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateWifiSignalChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command WifiSignalChanged description:<br>
     * Wifi Signal between controller and product state<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _rssi RSSI of the signal between controller and the product (in dbm)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateWifiSignalChanged (short _rssi) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateWifiSignalChanged (pointer, capacity, _rssi);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateSensorsStatesListChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command SensorsStatesListChanged description:<br>
     * Sensors states list<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _sensorName Sensor name
     * @param _sensorState Sensor state (1 if the sensor is OK, 0 if the sensor is NOT OK)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateSensorsStatesListChanged (ARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME_ENUM _sensorName, byte _sensorState) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateSensorsStatesListChanged (pointer, capacity, _sensorName, _sensorState);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateProductModel</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command ProductModel description:<br>
     * Inform of the product model. This is used to customize the UI depending on the connected product.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _model The Model of the product.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateProductModel (ARCOMMANDS_COMMON_COMMONSTATE_PRODUCTMODEL_MODEL_ENUM _model) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateProductModel (pointer, capacity, _model);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CommonStateCountryListKnown</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CommonState description:<br>
     * Common state from product<br>
     * <br>
     * Command CountryListKnown description:<br>
     * List of the countries known by the device<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _listFlags List entry attribute Bitfield.
     * @param _listFlags 0x01: First: indicate it's the first element of the list.
     * @param _listFlags 0x02: Last:  indicate it's the last element of the list.
     * @param _listFlags 0x04: Empty: indicate the list is empty (implies First/Last). All other arguments should be ignored.
     * @param _countryCodes Following of country code with ISO 3166 format, separated by ";". Be careful of the command size allowed by the network used. If necessary, split the list in several commands.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCommonStateCountryListKnown (byte _listFlags, String _countryCodes) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCommonStateCountryListKnown (pointer, capacity, _listFlags, _countryCodes);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>OverHeatStateOverHeatChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class OverHeatState description:<br>
     * Overheat state from product<br>
     * <br>
     * Command OverHeatChanged description:<br>
     * @deprecated<br>
     * Overheat temperature reached<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonOverHeatStateOverHeatChanged () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonOverHeatStateOverHeatChanged (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>OverHeatStateOverHeatRegulationChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class OverHeatState description:<br>
     * Overheat state from product<br>
     * <br>
     * Command OverHeatRegulationChanged description:<br>
     * @deprecated<br>
     * Overheat regulation state changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _regulationType Type of overheat regulation : 0 for ventilation, 1 for switch off
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonOverHeatStateOverHeatRegulationChanged (byte _regulationType) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonOverHeatStateOverHeatRegulationChanged (pointer, capacity, _regulationType);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>WifiSettingsStateOutdoorSettingsChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class WifiSettingsState description:<br>
     * Wifi settings state from product<br>
     * <br>
     * Command OutdoorSettingsChanged description:<br>
     * Status of the wifi config : either indoor or outdoor<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _outdoor 1 if it should use outdoor wifi settings, 0 otherwise
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonWifiSettingsStateOutdoorSettingsChanged (byte _outdoor) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonWifiSettingsStateOutdoorSettingsChanged (pointer, capacity, _outdoor);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MavlinkStateMavlinkFilePlayingStateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class MavlinkState description:<br>
     * Mavlink flight plans states commands<br>
     * <br>
     * Command MavlinkFilePlayingStateChanged description:<br>
     * Playing state of a mavlink flight plan<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _state State of the mavlink
     * @param _filepath flight plan file path from the mavlink ftp root
     * @param _type type of the played mavlink file
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonMavlinkStateMavlinkFilePlayingStateChanged (ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM _state, String _filepath, ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM _type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonMavlinkStateMavlinkFilePlayingStateChanged (pointer, capacity, _state, _filepath, _type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>MavlinkStateMavlinkPlayErrorStateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class MavlinkState description:<br>
     * Mavlink flight plans states commands<br>
     * <br>
     * Command MavlinkPlayErrorStateChanged description:<br>
     * FlightPlan play state error<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _error State of play error
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonMavlinkStateMavlinkPlayErrorStateChanged (ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonMavlinkStateMavlinkPlayErrorStateChanged (pointer, capacity, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationStateMagnetoCalibrationStateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CalibrationState description:<br>
     * Status of the calibration<br>
     * <br>
     * Command MagnetoCalibrationStateChanged description:<br>
     * Sent when the state of the magneto calibration has changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _xAxisCalibration State of the x axis (roll) calibration : 1 if calibration is done, 0 otherwise
     * @param _yAxisCalibration State of the y axis (pitch) calibration : 1 if calibration is done, 0 otherwise
     * @param _zAxisCalibration State of the z axis (yaw) calibration : 1 if calibration is done, 0 otherwise
     * @param _calibrationFailed 1 if calibration has failed, 0 otherwise. If this arg is 1, consider all previous arg as 0
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationStateChanged (byte _xAxisCalibration, byte _yAxisCalibration, byte _zAxisCalibration, byte _calibrationFailed) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCalibrationStateMagnetoCalibrationStateChanged (pointer, capacity, _xAxisCalibration, _yAxisCalibration, _zAxisCalibration, _calibrationFailed);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationStateMagnetoCalibrationRequiredState</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CalibrationState description:<br>
     * Status of the calibration<br>
     * <br>
     * Command MagnetoCalibrationRequiredState description:<br>
     * Status of the calibration requirement<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _required 1 if calibration is required, 0 if current calibration is still valid
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationRequiredState (byte _required) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCalibrationStateMagnetoCalibrationRequiredState (pointer, capacity, _required);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationStateMagnetoCalibrationAxisToCalibrateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CalibrationState description:<br>
     * Status of the calibration<br>
     * <br>
     * Command MagnetoCalibrationAxisToCalibrateChanged description:<br>
     * Event sent by a product to inform about the axis to calibrate<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _axis The axis to calibrate
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChanged (ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM _axis) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChanged (pointer, capacity, _axis);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CalibrationStateMagnetoCalibrationStartedChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CalibrationState description:<br>
     * Status of the calibration<br>
     * <br>
     * Command MagnetoCalibrationStartedChanged description:<br>
     * Status of the calibration process<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _started 1 if calibration has started, 0 otherwise
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCalibrationStateMagnetoCalibrationStartedChanged (byte _started) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCalibrationStateMagnetoCalibrationStartedChanged (pointer, capacity, _started);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CameraSettingsStateCameraSettingsChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class CameraSettingsState description:<br>
     * Status of the camera settings<br>
     * <br>
     * Command CameraSettingsChanged description:<br>
     * Status of the camera settings<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _fov Value of the camera horizontal fov (in degree)
     * @param _panMax Value of max pan (right pan) (in degree)
     * @param _panMin Value of min pan (left pan) (in degree)
     * @param _tiltMax Value of max tilt (top tilt) (in degree)
     * @param _tiltMin Value of min tilt (bottom tilt) (in degree)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonCameraSettingsStateCameraSettingsChanged (float _fov, float _panMax, float _panMin, float _tiltMax, float _tiltMin) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonCameraSettingsStateCameraSettingsChanged (pointer, capacity, _fov, _panMax, _panMin, _tiltMax, _tiltMin);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>FlightPlanStateAvailabilityStateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class FlightPlanState description:<br>
     * FlightPlan state commands<br>
     * <br>
     * Command AvailabilityStateChanged description:<br>
     * State of availability to run a flight plan file<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _AvailabilityState Running a flightPlan file is available (1 running a flightPlan file is available, otherwise 0)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonFlightPlanStateAvailabilityStateChanged (byte _AvailabilityState) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonFlightPlanStateAvailabilityStateChanged (pointer, capacity, _AvailabilityState);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>FlightPlanStateComponentStateListChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class FlightPlanState description:<br>
     * FlightPlan state commands<br>
     * <br>
     * Command ComponentStateListChanged description:<br>
     * List of state of drone flightPlan components<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _component Drone FlightPlan component id (unique)
     * @param _State State of the FlightPlan component (1 FlightPlan component OK, otherwise 0)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonFlightPlanStateComponentStateListChanged (ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM _component, byte _State) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonFlightPlanStateComponentStateListChanged (pointer, capacity, _component, _State);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>FlightPlanEventStartingErrorEvent</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class FlightPlanEvent description:<br>
     * FlightPlan Event commands<br>
     * <br>
     * Command StartingErrorEvent description:<br>
     * Event of flight plan start error<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonFlightPlanEventStartingErrorEvent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonFlightPlanEventStartingErrorEvent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>FlightPlanEventSpeedBridleEvent</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class FlightPlanEvent description:<br>
     * FlightPlan Event commands<br>
     * <br>
     * Command SpeedBridleEvent description:<br>
     * Bridle speed of the drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonFlightPlanEventSpeedBridleEvent () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonFlightPlanEventSpeedBridleEvent (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ARLibsVersionsStateControllerLibARCommandsVersion</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ARLibsVersionsState description:<br>
     * ARlibs Versions Commands<br>
     * <br>
     * Command ControllerLibARCommandsVersion description:<br>
     * Controller libARCommands version<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _version version of libARCommands ("1.2.3.4" format)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonARLibsVersionsStateControllerLibARCommandsVersion (String _version) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonARLibsVersionsStateControllerLibARCommandsVersion (pointer, capacity, _version);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ARLibsVersionsStateSkyControllerLibARCommandsVersion</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ARLibsVersionsState description:<br>
     * ARlibs Versions Commands<br>
     * <br>
     * Command SkyControllerLibARCommandsVersion description:<br>
     * SkyController libARCommands version<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _version version of libARCommands ("1.2.3.4" format)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonARLibsVersionsStateSkyControllerLibARCommandsVersion (String _version) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonARLibsVersionsStateSkyControllerLibARCommandsVersion (pointer, capacity, _version);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ARLibsVersionsStateDeviceLibARCommandsVersion</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ARLibsVersionsState description:<br>
     * ARlibs Versions Commands<br>
     * <br>
     * Command DeviceLibARCommandsVersion description:<br>
     * Device libARCommands version<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _version version of libARCommands ("1.2.3.4" format)
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonARLibsVersionsStateDeviceLibARCommandsVersion (String _version) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonARLibsVersionsStateDeviceLibARCommandsVersion (pointer, capacity, _version);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AudioStateAudioStreamingRunning</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class AudioState description:<br>
     * Audio-related state updates.<br>
     * <br>
     * Command AudioStreamingRunning description:<br>
     * Notify the controller whether the audio streaming is running.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _running Bit field for TX and RX running
     * @param _running bit 0 is 1 if Drone TX is running
     * @param _running bit 1 is 1 if Drone RX is running
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAudioStateAudioStreamingRunning (byte _running) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAudioStateAudioStreamingRunning (pointer, capacity, _running);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>HeadlightsStateIntensityChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class HeadlightsState description:<br>
     * Get information about the state of the Evo variants' LEDs.<br>
     * <br>
     * Command IntensityChanged description:<br>
     * Notify the instensity values for headlight LEDs.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _left The intensity value for the left LED (0 through 255).
     * @param _right The intensity value for the right LED (0 through 255).
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonHeadlightsStateIntensityChanged (byte _left, byte _right) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonHeadlightsStateIntensityChanged (pointer, capacity, _left, _right);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AnimationsStateList</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class AnimationsState description:<br>
     * Animations-related notification/feedback commands.<br>
     * <br>
     * Command List description:<br>
     * List of animations state.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _anim Animation type.
     * @param _state State of the animation
     * @param _error Error to explain the state
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAnimationsStateList (ARCOMMANDS_COMMON_ANIMATIONSSTATE_LIST_ANIM_ENUM _anim, ARCOMMANDS_COMMON_ANIMATIONSSTATE_LIST_STATE_ENUM _state, ARCOMMANDS_COMMON_ANIMATIONSSTATE_LIST_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAnimationsStateList (pointer, capacity, _anim, _state, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessoryStateSupportedAccessoriesListChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class AccessoryState description:<br>
     * Accessories-related commands.<br>
     * <br>
     * Command SupportedAccessoriesListChanged description:<br>
     * List of supported accessories<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _accessory Accessory configurations supported by the product.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAccessoryStateSupportedAccessoriesListChanged (ARCOMMANDS_COMMON_ACCESSORYSTATE_SUPPORTEDACCESSORIESLISTCHANGED_ACCESSORY_ENUM _accessory) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAccessoryStateSupportedAccessoriesListChanged (pointer, capacity, _accessory);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessoryStateAccessoryConfigChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class AccessoryState description:<br>
     * Accessories-related commands.<br>
     * <br>
     * Command AccessoryConfigChanged description:<br>
     * Accessory config response.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _newAccessory Accessory configuration reported by firmware.
     * @param _error Error code.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAccessoryStateAccessoryConfigChanged (ARCOMMANDS_COMMON_ACCESSORYSTATE_ACCESSORYCONFIGCHANGED_NEWACCESSORY_ENUM _newAccessory, ARCOMMANDS_COMMON_ACCESSORYSTATE_ACCESSORYCONFIGCHANGED_ERROR_ENUM _error) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAccessoryStateAccessoryConfigChanged (pointer, capacity, _newAccessory, _error);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AccessoryStateAccessoryConfigModificationEnabled</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class AccessoryState description:<br>
     * Accessories-related commands.<br>
     * <br>
     * Command AccessoryConfigModificationEnabled description:<br>
     * Possibility to modify the accessory configuration.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _enabled 1 if the modification of the accessory Config is enabled, 0 otherwise
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonAccessoryStateAccessoryConfigModificationEnabled (byte _enabled) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonAccessoryStateAccessoryConfigModificationEnabled (pointer, capacity, _enabled);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ChargerStateMaxChargeRateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ChargerState description:<br>
     * Commands sent by the firmware to advertise the charger status.<br>
     * <br>
     * Command MaxChargeRateChanged description:<br>
     * @deprecated<br>
     * The maximum charge rate reported by the firmware.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _rate The current maximum charge rate.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonChargerStateMaxChargeRateChanged (ARCOMMANDS_COMMON_CHARGERSTATE_MAXCHARGERATECHANGED_RATE_ENUM _rate) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonChargerStateMaxChargeRateChanged (pointer, capacity, _rate);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ChargerStateCurrentChargeStateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ChargerState description:<br>
     * Commands sent by the firmware to advertise the charger status.<br>
     * <br>
     * Command CurrentChargeStateChanged description:<br>
     * @deprecated<br>
     * The charge status of the battery changed.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _status Charger status.
     * @param _phase The current charging phase.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonChargerStateCurrentChargeStateChanged (ARCOMMANDS_COMMON_CHARGERSTATE_CURRENTCHARGESTATECHANGED_STATUS_ENUM _status, ARCOMMANDS_COMMON_CHARGERSTATE_CURRENTCHARGESTATECHANGED_PHASE_ENUM _phase) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonChargerStateCurrentChargeStateChanged (pointer, capacity, _status, _phase);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ChargerStateLastChargeRateChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ChargerState description:<br>
     * Commands sent by the firmware to advertise the charger status.<br>
     * <br>
     * Command LastChargeRateChanged description:<br>
     * @deprecated<br>
     * The charge rate of the last charge sent by the firmware.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _rate The charge rate recorded by the firmware for the last charge.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonChargerStateLastChargeRateChanged (ARCOMMANDS_COMMON_CHARGERSTATE_LASTCHARGERATECHANGED_RATE_ENUM _rate) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonChargerStateLastChargeRateChanged (pointer, capacity, _rate);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ChargerStateChargingInfo</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class ChargerState description:<br>
     * Commands sent by the firmware to advertise the charger status.<br>
     * <br>
     * Command ChargingInfo description:<br>
     * Information of the charge.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _phase The current charging phase.
     * @param _rate The charge rate. If phase is DISCHARGING, refers to the last charge.
     * @param _intensity The charging intensity, in dA. (12dA = 1,2A) ; If phase is DISCHARGING, refers to the last charge. Equals to 0 if not known.
     * @param _fullChargingTime The full charging time estimated, in minute. If phase is DISCHARGING, refers to the last charge. Equals to 0 if not known.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonChargerStateChargingInfo (ARCOMMANDS_COMMON_CHARGERSTATE_CHARGINGINFO_PHASE_ENUM _phase, ARCOMMANDS_COMMON_CHARGERSTATE_CHARGINGINFO_RATE_ENUM _rate, byte _intensity, byte _fullChargingTime) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonChargerStateChargingInfo (pointer, capacity, _phase, _rate, _intensity, _fullChargingTime);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RunStateRunIdChanged</code> in feature <code>Common</code><br>
     * <br>
     * Feature Common description:<br>
     * All common commands shared between all projects<br>
     * <br>
     * Class RunState description:<br>
     * Commands sent by the drone to inform about the run or flight state<br>
     * <br>
     * Command RunIdChanged description:<br>
     * Sent when a run id has changed<br>
     * Run ids are uniquely identifying a run or a flight<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _runId Id of the run
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonRunStateRunIdChanged (String _runId) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonRunStateRunIdChanged (pointer, capacity, _runId);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>StatsSendPacket</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class Stats description:<br>
     * Commands to make stats<br>
     * <br>
     * Command SendPacket description:<br>
     * Send a packet to the drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _packet Packet to send to the drone
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugStatsSendPacket (String _packet) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugStatsSendPacket (pointer, capacity, _packet);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>StatsStartSendingPacketFromDrone</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class Stats description:<br>
     * Commands to make stats<br>
     * <br>
     * Command StartSendingPacketFromDrone description:<br>
     * Ask drone to start sending packets<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _frequency Frequency of the packet
     * @param _packetSize Size of the the packet
     * @param _date time of day in sec
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugStatsStartSendingPacketFromDrone (byte _frequency, byte _packetSize, int _date) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugStatsStartSendingPacketFromDrone (pointer, capacity, _frequency, _packetSize, _date);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>StatsStopSendingPacketFromDrone</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class Stats description:<br>
     * Commands to make stats<br>
     * <br>
     * Command StopSendingPacketFromDrone description:<br>
     * Ask drone to stop sending packets<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugStatsStopSendingPacketFromDrone () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugStatsStopSendingPacketFromDrone (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DebugSettingsGetAll</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class DebugSettings description:<br>
     * Debug custom commands sent to the drone<br>
     * <br>
     * Command GetAll description:<br>
     * Cmd sent by controller to get all settings info (generate "SettingInfo" events).<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugDebugSettingsGetAll () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugDebugSettingsGetAll (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DebugSettingsSet</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class DebugSettings description:<br>
     * Debug custom commands sent to the drone<br>
     * <br>
     * Command Set description:<br>
     * Change setting value.<br>
     * Cmd sent by controller to change a writable setting.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _id Setting Id.
     * @param _value New setting value (string encoded).
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugDebugSettingsSet (short _id, String _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugDebugSettingsSet (pointer, capacity, _id, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>StatsEventSendPacket</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class StatsEvent description:<br>
     * Stats Event from drone<br>
     * <br>
     * Command SendPacket description:<br>
     * Send a packet from drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _packet packet from drone
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugStatsEventSendPacket (String _packet) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugStatsEventSendPacket (pointer, capacity, _packet);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DebugSettingsStateInfo</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class DebugSettingsState description:<br>
     * Debug custom commands sent by the drone<br>
     * <br>
     * Command Info description:<br>
     * Sent by the drone as answer to GetSettingsInfo<br>
     * Describe a debug setting and give the current value.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
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
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugDebugSettingsStateInfo (byte _listFlags, short _id, String _label, ARCOMMANDS_COMMONDEBUG_DEBUGSETTINGSSTATE_INFO_TYPE_ENUM _type, ARCOMMANDS_COMMONDEBUG_DEBUGSETTINGSSTATE_INFO_MODE_ENUM _mode, String _range_min, String _range_max, String _range_step, String _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugDebugSettingsStateInfo (pointer, capacity, _listFlags, _id, _label, _type, _mode, _range_min, _range_max, _range_step, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>DebugSettingsStateListChanged</code> in feature <code>CommonDebug</code><br>
     * <br>
     * Feature CommonDebug description:<br>
     * All debug commands shared between all projects<br>
     * <br>
     * Class DebugSettingsState description:<br>
     * Debug custom commands sent by the drone<br>
     * <br>
     * Command ListChanged description:<br>
     * Setting value changed.<br>
     * Cmd sent by drone when setting changed occurred.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _id Setting Id.
     * @param _value New setting value (string encoded).
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setCommonDebugDebugSettingsStateListChanged (short _id, String _value) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetCommonDebugDebugSettingsStateListChanged (pointer, capacity, _id, _value);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ProBoughtFeatures</code> in feature <code>Pro</code><br>
     * <br>
     * Feature Pro description:<br>
     * Pro Feature<br>
     * <br>
     * Class Pro description:<br>
     * Pro related commands from the controller to the product<br>
     * <br>
     * Command BoughtFeatures description:<br>
     * Bought features on this pro version of the controller (features that have been bought)<br>
     * This command starts the pro authentification process<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _features Bought features
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setProProBoughtFeatures (long _features) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetProProBoughtFeatures (pointer, capacity, _features);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ProResponse</code> in feature <code>Pro</code><br>
     * <br>
     * Feature Pro description:<br>
     * Pro Feature<br>
     * <br>
     * Class Pro description:<br>
     * Pro related commands from the controller to the product<br>
     * <br>
     * Command Response description:<br>
     * Response to the challenge string sent by the controller<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _listFlags List entry attribute Bitfield.
     * @param _listFlags 0x01: First: indicate it's the first element of the list.
     * @param _listFlags 0x02: Last:  indicate it's the last element of the list.
     * @param _listFlags 0x04: Empty: indicate the list is empty (implies First/Last). All other arguments should be ignored.
     * @param _signedChallenge the signed challenge
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setProProResponse (byte _listFlags, String _signedChallenge) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetProProResponse (pointer, capacity, _listFlags, _signedChallenge);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ProActivateFeatures</code> in feature <code>Pro</code><br>
     * <br>
     * Feature Pro description:<br>
     * Pro Feature<br>
     * <br>
     * Class Pro description:<br>
     * Pro related commands from the controller to the product<br>
     * <br>
     * Command ActivateFeatures description:<br>
     * Activate some pro features<br>
     * Pro features activated should be part of the list returned by ProState->FeaturesSupported<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _features Pro features to activate
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setProProActivateFeatures (long _features) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetProProActivateFeatures (pointer, capacity, _features);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ProStateSupportedFeatures</code> in feature <code>Pro</code><br>
     * <br>
     * Feature Pro description:<br>
     * Pro Feature<br>
     * <br>
     * Class ProState description:<br>
     * Pro related commands from the product to the controller<br>
     * <br>
     * Command SupportedFeatures description:<br>
     * Features supported. This command ends the pro authentification<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _status Status of the supported features
     * @param _features Supported pro features.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setProProStateSupportedFeatures (ARCOMMANDS_PRO_PROSTATE_SUPPORTEDFEATURES_STATUS_ENUM _status, long _features) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetProProStateSupportedFeatures (pointer, capacity, _status, _features);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ProStateFeaturesActivated</code> in feature <code>Pro</code><br>
     * <br>
     * Feature Pro description:<br>
     * Pro Feature<br>
     * <br>
     * Class ProState description:<br>
     * Pro related commands from the product to the controller<br>
     * <br>
     * Command FeaturesActivated description:<br>
     * Pro features that are currently activated<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _features Activated pro features.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setProProStateFeaturesActivated (long _features) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetProProStateFeaturesActivated (pointer, capacity, _features);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ProEventChallengeEvent</code> in feature <code>Pro</code><br>
     * <br>
     * Feature Pro description:<br>
     * Pro Feature<br>
     * <br>
     * Class ProEvent description:<br>
     * Pro related events from the product to the controller<br>
     * <br>
     * Command ChallengeEvent description:<br>
     * Challenge event sent from the drone<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _challenge the challenge that the receiver will have to sign
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setProProEventChallengeEvent (String _challenge) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetProProEventChallengeEvent (pointer, capacity, _challenge);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>Scan</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command Scan description:<br>
     * Launches wifi network scan for a given band to get a list of all wifi networks found by the drone.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band Launches wifi network scan for a given band to get a list of all wifi networks found by the drone.
     * @param _band a combination of ; ARCOMMANDS_FLAG_WIFI_BAND_2_4GHZ ; ARCOMMANDS_FLAG_WIFI_BAND_5GHZ
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiScan (byte _band) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiScan (pointer, capacity, _band);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>UpdateAuthorizedChannels</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command UpdateAuthorizedChannels description:<br>
     * Get all available Wifi channels.\nThe list of available Wifi channels is related to the country of the drone. You can get this country with the event [WifiCountryChanged](#wifi-CountryChanged).<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiUpdateAuthorizedChannels () {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiUpdateAuthorizedChannels (pointer, capacity);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SetApChannel</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command SetApChannel description:<br>
     * Select channel of choosen band to put the drone's access point on this channel.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Select channel of choosen band to put the drone's access point on this channel.
     * @param _band Select channel of choosen band to put the drone's access point on this channel.
     * @param _channel The channel you want to select. Used only when type is manual.
     * @param _channel Select channel of choosen band to put the drone's access point on this channel.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiSetApChannel (ARCOMMANDS_WIFI_SELECTION_TYPE_ENUM _type, ARCOMMANDS_WIFI_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiSetApChannel (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SetSecurity</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command SetSecurity description:<br>
     * Set the wifi security.\nThe security is changed on the next boot.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Set the wifi security.\nThe security is changed on the next boot.
     * @param _key The key to secure the network. Not used if type is open
     * @param _key Set the wifi security.\nThe security is changed on the next boot.
     * @param _key_type Set the wifi security.\nThe security is changed on the next boot.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiSetSecurity (ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM _type, String _key, ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM _key_type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiSetSecurity (pointer, capacity, _type, _key, _key_type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SetCountry</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command SetCountry description:<br>
     * Set the wifi country.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _selection_mode Set the wifi country.
     * @param _code Country code with ISO 3166 format. Not used if automatic is 1.
     * @param _code Set the wifi country.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiSetCountry (ARCOMMANDS_WIFI_COUNTRY_SELECTION_ENUM _selection_mode, String _code) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiSetCountry (pointer, capacity, _selection_mode, _code);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SetEnvironement</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command SetEnvironement description:<br>
     * Set indoor or outdoor wifi settings.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _environement Set indoor or outdoor wifi settings.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiSetEnvironement (ARCOMMANDS_WIFI_ENVIRONEMENT_ENUM _environement) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiSetEnvironement (pointer, capacity, _environement);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ScannedItem</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command ScannedItem description:<br>
     * Wifi scan results.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _ssid SSID of the AP
     * @param _ssid Wifi scan results.
     * @param _rssi RSSI of the AP.
     * @param _rssi Wifi scan results.
     * @param _band Wifi scan results.
     * @param _channel Channel of the AP
     * @param _channel Wifi scan results.
     * @param _list_flags Wifi scan results.
     * @param _list_flags a combination of ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_FIRST ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_LAST ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_EMPTY ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_REMOVE
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiScannedItem (String _ssid, short _rssi, ARCOMMANDS_WIFI_BAND_ENUM _band, byte _channel, byte _list_flags) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiScannedItem (pointer, capacity, _ssid, _rssi, _band, _channel, _list_flags);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>AuthorizedChannel</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command AuthorizedChannel description:<br>
     * Available channel results.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _band Available channel results.
     * @param _channel The channel number
     * @param _channel Available channel results.
     * @param _environement Available channel results.
     * @param _environement a combination of ; ARCOMMANDS_FLAG_WIFI_ENVIRONEMENT_INDOOR ; ARCOMMANDS_FLAG_WIFI_ENVIRONEMENT_OUTDOOR
     * @param _list_flags Available channel results.
     * @param _list_flags a combination of ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_FIRST ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_LAST ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_EMPTY ; ARCOMMANDS_FLAG_GENERIC_LIST_FLAGS_REMOVE
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiAuthorizedChannel (ARCOMMANDS_WIFI_BAND_ENUM _band, byte _channel, byte _environement, byte _list_flags) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiAuthorizedChannel (pointer, capacity, _band, _channel, _environement, _list_flags);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>ApChannelChanged</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command ApChannelChanged description:<br>
     * Wifi selection changed.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _type Wifi selection changed.
     * @param _band Wifi selection changed.
     * @param _channel The channel of the drone's access point
     * @param _channel Wifi selection changed.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiApChannelChanged (ARCOMMANDS_WIFI_SELECTION_TYPE_ENUM _type, ARCOMMANDS_WIFI_BAND_ENUM _band, byte _channel) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiApChannelChanged (pointer, capacity, _type, _band, _channel);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>SecurityChanged</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command SecurityChanged description:<br>
     * Wifi security changed<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _key The key to secure the network. Not used if type is open
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiSecurityChanged (String _key, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM _key_type) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiSecurityChanged (pointer, capacity, _key, _key_type);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>CountryChanged</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command CountryChanged description:<br>
     * Wifi country changed.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _selection_mode Wifi country changed.
     * @param _code Country code with ISO 3166 format, empty string means unknown country.
     * @param _code Wifi country changed.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiCountryChanged (ARCOMMANDS_WIFI_COUNTRY_SELECTION_ENUM _selection_mode, String _code) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiCountryChanged (pointer, capacity, _selection_mode, _code);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>EnvironementChanged</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command EnvironementChanged description:<br>
     * Status of the wifi config : either indoor or outdoor.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _environement 1 if it uses outdoor wifi settings, 0 otherwise
     * @param _environement Status of the wifi config : either indoor or outdoor.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiEnvironementChanged (ARCOMMANDS_WIFI_ENVIRONEMENT_ENUM _environement) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiEnvironementChanged (pointer, capacity, _environement);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    /**
     * Set an ARCommand to hold the command <code>RssiChanged</code> in feature <code>Wifi</code><br>
     * <br>
     * Feature Wifi description:<br>
     * All commands/events related to the Wifi<br>
     * <br>
     * Command RssiChanged description:<br>
     * Rssi Changed. This is an information about the Wifi link quality.<br>
     * <br>
     * This function reuses the current ARCommand, replacing its content with a
     * new command created from the current params
     * @param _rssi Rssi on the connected wifi network. Rssi values are generally between -30 and -120dBm. The nearest of 0 is the better.
     * @param _rssi Rssi Changed. This is an information about the Wifi link quality.
     * @return An ARCOMMANDS_GENERATOR_ERROR_ENUM error code.
     */
    public ARCOMMANDS_GENERATOR_ERROR_ENUM setWifiRssiChanged (short _rssi) {
        ARCOMMANDS_GENERATOR_ERROR_ENUM err = ARCOMMANDS_GENERATOR_ERROR_ENUM.ARCOMMANDS_GENERATOR_ERROR;
        if (!valid) {
            return err;
        }
        int errInt = nativeSetWifiRssiChanged (pointer, capacity, _rssi);
        if (ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt) != null) {
            err = ARCOMMANDS_GENERATOR_ERROR_ENUM.getFromValue (errInt);
        }
        return err;
    }

    private static ARCommandGenericDefaultListener _ARCommandGenericDefaultListener = null;

    /**
     * Set the listener for the command <code>Default</code> in feature <code>Generic</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandGenericDefaultListener_PARAM New listener for the command
     */
    public static void setGenericDefaultListener (ARCommandGenericDefaultListener _ARCommandGenericDefaultListener_PARAM) {
        _ARCommandGenericDefaultListener = _ARCommandGenericDefaultListener_PARAM;
    }


    private static ARCommandARDrone3PilotingFlatTrimListener _ARCommandARDrone3PilotingFlatTrimListener = null;

    /**
     * Set the listener for the command <code>PilotingFlatTrim</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingFlatTrimListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingFlatTrimListener (ARCommandARDrone3PilotingFlatTrimListener _ARCommandARDrone3PilotingFlatTrimListener_PARAM) {
        _ARCommandARDrone3PilotingFlatTrimListener = _ARCommandARDrone3PilotingFlatTrimListener_PARAM;
    }

    private static ARCommandARDrone3PilotingTakeOffListener _ARCommandARDrone3PilotingTakeOffListener = null;

    /**
     * Set the listener for the command <code>PilotingTakeOff</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingTakeOffListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingTakeOffListener (ARCommandARDrone3PilotingTakeOffListener _ARCommandARDrone3PilotingTakeOffListener_PARAM) {
        _ARCommandARDrone3PilotingTakeOffListener = _ARCommandARDrone3PilotingTakeOffListener_PARAM;
    }

    private static ARCommandARDrone3PilotingPCMDListener _ARCommandARDrone3PilotingPCMDListener = null;

    /**
     * Set the listener for the command <code>PilotingPCMD</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingPCMDListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingPCMDListener (ARCommandARDrone3PilotingPCMDListener _ARCommandARDrone3PilotingPCMDListener_PARAM) {
        _ARCommandARDrone3PilotingPCMDListener = _ARCommandARDrone3PilotingPCMDListener_PARAM;
    }

    private static ARCommandARDrone3PilotingLandingListener _ARCommandARDrone3PilotingLandingListener = null;

    /**
     * Set the listener for the command <code>PilotingLanding</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingLandingListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingLandingListener (ARCommandARDrone3PilotingLandingListener _ARCommandARDrone3PilotingLandingListener_PARAM) {
        _ARCommandARDrone3PilotingLandingListener = _ARCommandARDrone3PilotingLandingListener_PARAM;
    }

    private static ARCommandARDrone3PilotingEmergencyListener _ARCommandARDrone3PilotingEmergencyListener = null;

    /**
     * Set the listener for the command <code>PilotingEmergency</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingEmergencyListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingEmergencyListener (ARCommandARDrone3PilotingEmergencyListener _ARCommandARDrone3PilotingEmergencyListener_PARAM) {
        _ARCommandARDrone3PilotingEmergencyListener = _ARCommandARDrone3PilotingEmergencyListener_PARAM;
    }

    private static ARCommandARDrone3PilotingNavigateHomeListener _ARCommandARDrone3PilotingNavigateHomeListener = null;

    /**
     * Set the listener for the command <code>PilotingNavigateHome</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingNavigateHomeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingNavigateHomeListener (ARCommandARDrone3PilotingNavigateHomeListener _ARCommandARDrone3PilotingNavigateHomeListener_PARAM) {
        _ARCommandARDrone3PilotingNavigateHomeListener = _ARCommandARDrone3PilotingNavigateHomeListener_PARAM;
    }

    private static ARCommandARDrone3PilotingAutoTakeOffModeListener _ARCommandARDrone3PilotingAutoTakeOffModeListener = null;

    /**
     * Set the listener for the command <code>PilotingAutoTakeOffMode</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingAutoTakeOffModeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingAutoTakeOffModeListener (ARCommandARDrone3PilotingAutoTakeOffModeListener _ARCommandARDrone3PilotingAutoTakeOffModeListener_PARAM) {
        _ARCommandARDrone3PilotingAutoTakeOffModeListener = _ARCommandARDrone3PilotingAutoTakeOffModeListener_PARAM;
    }

    private static ARCommandARDrone3PilotingMoveByListener _ARCommandARDrone3PilotingMoveByListener = null;

    /**
     * Set the listener for the command <code>PilotingMoveBy</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingMoveByListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingMoveByListener (ARCommandARDrone3PilotingMoveByListener _ARCommandARDrone3PilotingMoveByListener_PARAM) {
        _ARCommandARDrone3PilotingMoveByListener = _ARCommandARDrone3PilotingMoveByListener_PARAM;
    }

    private static ARCommandARDrone3PilotingUserTakeOffListener _ARCommandARDrone3PilotingUserTakeOffListener = null;

    /**
     * Set the listener for the command <code>PilotingUserTakeOff</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingUserTakeOffListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingUserTakeOffListener (ARCommandARDrone3PilotingUserTakeOffListener _ARCommandARDrone3PilotingUserTakeOffListener_PARAM) {
        _ARCommandARDrone3PilotingUserTakeOffListener = _ARCommandARDrone3PilotingUserTakeOffListener_PARAM;
    }

    private static ARCommandARDrone3PilotingCircleListener _ARCommandARDrone3PilotingCircleListener = null;

    /**
     * Set the listener for the command <code>PilotingCircle</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingCircleListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingCircleListener (ARCommandARDrone3PilotingCircleListener _ARCommandARDrone3PilotingCircleListener_PARAM) {
        _ARCommandARDrone3PilotingCircleListener = _ARCommandARDrone3PilotingCircleListener_PARAM;
    }

    private static ARCommandARDrone3AnimationsFlipListener _ARCommandARDrone3AnimationsFlipListener = null;

    /**
     * Set the listener for the command <code>AnimationsFlip</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3AnimationsFlipListener_PARAM New listener for the command
     */
    public static void setARDrone3AnimationsFlipListener (ARCommandARDrone3AnimationsFlipListener _ARCommandARDrone3AnimationsFlipListener_PARAM) {
        _ARCommandARDrone3AnimationsFlipListener = _ARCommandARDrone3AnimationsFlipListener_PARAM;
    }

    private static ARCommandARDrone3CameraOrientationListener _ARCommandARDrone3CameraOrientationListener = null;

    /**
     * Set the listener for the command <code>CameraOrientation</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3CameraOrientationListener_PARAM New listener for the command
     */
    public static void setARDrone3CameraOrientationListener (ARCommandARDrone3CameraOrientationListener _ARCommandARDrone3CameraOrientationListener_PARAM) {
        _ARCommandARDrone3CameraOrientationListener = _ARCommandARDrone3CameraOrientationListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordPictureListener _ARCommandARDrone3MediaRecordPictureListener = null;

    /**
     * Set the listener for the command <code>MediaRecordPicture</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordPictureListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordPictureListener (ARCommandARDrone3MediaRecordPictureListener _ARCommandARDrone3MediaRecordPictureListener_PARAM) {
        _ARCommandARDrone3MediaRecordPictureListener = _ARCommandARDrone3MediaRecordPictureListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordVideoListener _ARCommandARDrone3MediaRecordVideoListener = null;

    /**
     * Set the listener for the command <code>MediaRecordVideo</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordVideoListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordVideoListener (ARCommandARDrone3MediaRecordVideoListener _ARCommandARDrone3MediaRecordVideoListener_PARAM) {
        _ARCommandARDrone3MediaRecordVideoListener = _ARCommandARDrone3MediaRecordVideoListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordPictureV2Listener _ARCommandARDrone3MediaRecordPictureV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordPictureV2</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordPictureV2Listener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordPictureV2Listener (ARCommandARDrone3MediaRecordPictureV2Listener _ARCommandARDrone3MediaRecordPictureV2Listener_PARAM) {
        _ARCommandARDrone3MediaRecordPictureV2Listener = _ARCommandARDrone3MediaRecordPictureV2Listener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordVideoV2Listener _ARCommandARDrone3MediaRecordVideoV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordVideoV2</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordVideoV2Listener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordVideoV2Listener (ARCommandARDrone3MediaRecordVideoV2Listener _ARCommandARDrone3MediaRecordVideoV2Listener_PARAM) {
        _ARCommandARDrone3MediaRecordVideoV2Listener = _ARCommandARDrone3MediaRecordVideoV2Listener_PARAM;
    }

    private static ARCommandARDrone3NetworkWifiScanListener _ARCommandARDrone3NetworkWifiScanListener = null;

    /**
     * Set the listener for the command <code>NetworkWifiScan</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkWifiScanListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkWifiScanListener (ARCommandARDrone3NetworkWifiScanListener _ARCommandARDrone3NetworkWifiScanListener_PARAM) {
        _ARCommandARDrone3NetworkWifiScanListener = _ARCommandARDrone3NetworkWifiScanListener_PARAM;
    }

    private static ARCommandARDrone3NetworkWifiAuthChannelListener _ARCommandARDrone3NetworkWifiAuthChannelListener = null;

    /**
     * Set the listener for the command <code>NetworkWifiAuthChannel</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkWifiAuthChannelListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkWifiAuthChannelListener (ARCommandARDrone3NetworkWifiAuthChannelListener _ARCommandARDrone3NetworkWifiAuthChannelListener_PARAM) {
        _ARCommandARDrone3NetworkWifiAuthChannelListener = _ARCommandARDrone3NetworkWifiAuthChannelListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsMaxAltitudeListener _ARCommandARDrone3PilotingSettingsMaxAltitudeListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsMaxAltitude</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsMaxAltitudeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsMaxAltitudeListener (ARCommandARDrone3PilotingSettingsMaxAltitudeListener _ARCommandARDrone3PilotingSettingsMaxAltitudeListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsMaxAltitudeListener = _ARCommandARDrone3PilotingSettingsMaxAltitudeListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsMaxTiltListener _ARCommandARDrone3PilotingSettingsMaxTiltListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsMaxTilt</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsMaxTiltListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsMaxTiltListener (ARCommandARDrone3PilotingSettingsMaxTiltListener _ARCommandARDrone3PilotingSettingsMaxTiltListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsMaxTiltListener = _ARCommandARDrone3PilotingSettingsMaxTiltListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsAbsolutControlListener _ARCommandARDrone3PilotingSettingsAbsolutControlListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsAbsolutControl</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsAbsolutControlListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsAbsolutControlListener (ARCommandARDrone3PilotingSettingsAbsolutControlListener _ARCommandARDrone3PilotingSettingsAbsolutControlListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsAbsolutControlListener = _ARCommandARDrone3PilotingSettingsAbsolutControlListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsMaxDistanceListener _ARCommandARDrone3PilotingSettingsMaxDistanceListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsMaxDistance</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsMaxDistanceListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsMaxDistanceListener (ARCommandARDrone3PilotingSettingsMaxDistanceListener _ARCommandARDrone3PilotingSettingsMaxDistanceListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsMaxDistanceListener = _ARCommandARDrone3PilotingSettingsMaxDistanceListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener _ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsNoFlyOverMaxDistance</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsNoFlyOverMaxDistanceListener (ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener _ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener = _ARCommandARDrone3PilotingSettingsNoFlyOverMaxDistanceListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsSetAutonomousFlightMaxHorizontalSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener (ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener = _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsSetAutonomousFlightMaxVerticalSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener (ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener = _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsSetAutonomousFlightMaxHorizontalAcceleration</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener (ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener = _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAccelerationListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsSetAutonomousFlightMaxVerticalAcceleration</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener (ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener = _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAccelerationListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsSetAutonomousFlightMaxRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener (ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener = _ARCommandARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsBankedTurnListener _ARCommandARDrone3PilotingSettingsBankedTurnListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsBankedTurn</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsBankedTurnListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsBankedTurnListener (ARCommandARDrone3PilotingSettingsBankedTurnListener _ARCommandARDrone3PilotingSettingsBankedTurnListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsBankedTurnListener = _ARCommandARDrone3PilotingSettingsBankedTurnListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsMinAltitudeListener _ARCommandARDrone3PilotingSettingsMinAltitudeListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsMinAltitude</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsMinAltitudeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsMinAltitudeListener (ARCommandARDrone3PilotingSettingsMinAltitudeListener _ARCommandARDrone3PilotingSettingsMinAltitudeListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsMinAltitudeListener = _ARCommandARDrone3PilotingSettingsMinAltitudeListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsCirclingDirectionListener _ARCommandARDrone3PilotingSettingsCirclingDirectionListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsCirclingDirection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsCirclingDirectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsCirclingDirectionListener (ARCommandARDrone3PilotingSettingsCirclingDirectionListener _ARCommandARDrone3PilotingSettingsCirclingDirectionListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsCirclingDirectionListener = _ARCommandARDrone3PilotingSettingsCirclingDirectionListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsCirclingRadiusListener _ARCommandARDrone3PilotingSettingsCirclingRadiusListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsCirclingRadius</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsCirclingRadiusListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsCirclingRadiusListener (ARCommandARDrone3PilotingSettingsCirclingRadiusListener _ARCommandARDrone3PilotingSettingsCirclingRadiusListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsCirclingRadiusListener = _ARCommandARDrone3PilotingSettingsCirclingRadiusListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsCirclingAltitudeListener _ARCommandARDrone3PilotingSettingsCirclingAltitudeListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsCirclingAltitude</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsCirclingAltitudeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsCirclingAltitudeListener (ARCommandARDrone3PilotingSettingsCirclingAltitudeListener _ARCommandARDrone3PilotingSettingsCirclingAltitudeListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsCirclingAltitudeListener = _ARCommandARDrone3PilotingSettingsCirclingAltitudeListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsPitchModeListener _ARCommandARDrone3PilotingSettingsPitchModeListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsPitchMode</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsPitchModeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsPitchModeListener (ARCommandARDrone3PilotingSettingsPitchModeListener _ARCommandARDrone3PilotingSettingsPitchModeListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsPitchModeListener = _ARCommandARDrone3PilotingSettingsPitchModeListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsLandingModeListener _ARCommandARDrone3PilotingSettingsLandingModeListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsLandingMode</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsLandingModeListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsLandingModeListener (ARCommandARDrone3PilotingSettingsLandingModeListener _ARCommandARDrone3PilotingSettingsLandingModeListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsLandingModeListener = _ARCommandARDrone3PilotingSettingsLandingModeListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener _ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsMaxVerticalSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsMaxVerticalSpeedListener (ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener _ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener = _ARCommandARDrone3SpeedSettingsMaxVerticalSpeedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener _ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsMaxRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsMaxRotationSpeedListener (ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener _ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener = _ARCommandARDrone3SpeedSettingsMaxRotationSpeedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsHullProtectionListener _ARCommandARDrone3SpeedSettingsHullProtectionListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsHullProtection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsHullProtectionListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsHullProtectionListener (ARCommandARDrone3SpeedSettingsHullProtectionListener _ARCommandARDrone3SpeedSettingsHullProtectionListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsHullProtectionListener = _ARCommandARDrone3SpeedSettingsHullProtectionListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsOutdoorListener _ARCommandARDrone3SpeedSettingsOutdoorListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsOutdoor</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsOutdoorListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsOutdoorListener (ARCommandARDrone3SpeedSettingsOutdoorListener _ARCommandARDrone3SpeedSettingsOutdoorListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsOutdoorListener = _ARCommandARDrone3SpeedSettingsOutdoorListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener _ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsMaxPitchRollRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener (ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener _ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener = _ARCommandARDrone3SpeedSettingsMaxPitchRollRotationSpeedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkSettingsWifiSelectionListener _ARCommandARDrone3NetworkSettingsWifiSelectionListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsWifiSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkSettingsWifiSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkSettingsWifiSelectionListener (ARCommandARDrone3NetworkSettingsWifiSelectionListener _ARCommandARDrone3NetworkSettingsWifiSelectionListener_PARAM) {
        _ARCommandARDrone3NetworkSettingsWifiSelectionListener = _ARCommandARDrone3NetworkSettingsWifiSelectionListener_PARAM;
    }

    private static ARCommandARDrone3NetworkSettingsWifiSecurityListener _ARCommandARDrone3NetworkSettingsWifiSecurityListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsWifiSecurity</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkSettingsWifiSecurityListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkSettingsWifiSecurityListener (ARCommandARDrone3NetworkSettingsWifiSecurityListener _ARCommandARDrone3NetworkSettingsWifiSecurityListener_PARAM) {
        _ARCommandARDrone3NetworkSettingsWifiSecurityListener = _ARCommandARDrone3NetworkSettingsWifiSecurityListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsPictureFormatSelectionListener _ARCommandARDrone3PictureSettingsPictureFormatSelectionListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsPictureFormatSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsPictureFormatSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsPictureFormatSelectionListener (ARCommandARDrone3PictureSettingsPictureFormatSelectionListener _ARCommandARDrone3PictureSettingsPictureFormatSelectionListener_PARAM) {
        _ARCommandARDrone3PictureSettingsPictureFormatSelectionListener = _ARCommandARDrone3PictureSettingsPictureFormatSelectionListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener _ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsAutoWhiteBalanceSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsAutoWhiteBalanceSelectionListener (ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener _ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener_PARAM) {
        _ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener = _ARCommandARDrone3PictureSettingsAutoWhiteBalanceSelectionListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsExpositionSelectionListener _ARCommandARDrone3PictureSettingsExpositionSelectionListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsExpositionSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsExpositionSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsExpositionSelectionListener (ARCommandARDrone3PictureSettingsExpositionSelectionListener _ARCommandARDrone3PictureSettingsExpositionSelectionListener_PARAM) {
        _ARCommandARDrone3PictureSettingsExpositionSelectionListener = _ARCommandARDrone3PictureSettingsExpositionSelectionListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsSaturationSelectionListener _ARCommandARDrone3PictureSettingsSaturationSelectionListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsSaturationSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsSaturationSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsSaturationSelectionListener (ARCommandARDrone3PictureSettingsSaturationSelectionListener _ARCommandARDrone3PictureSettingsSaturationSelectionListener_PARAM) {
        _ARCommandARDrone3PictureSettingsSaturationSelectionListener = _ARCommandARDrone3PictureSettingsSaturationSelectionListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsTimelapseSelectionListener _ARCommandARDrone3PictureSettingsTimelapseSelectionListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsTimelapseSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsTimelapseSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsTimelapseSelectionListener (ARCommandARDrone3PictureSettingsTimelapseSelectionListener _ARCommandARDrone3PictureSettingsTimelapseSelectionListener_PARAM) {
        _ARCommandARDrone3PictureSettingsTimelapseSelectionListener = _ARCommandARDrone3PictureSettingsTimelapseSelectionListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener _ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsVideoAutorecordSelection</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsVideoAutorecordSelectionListener (ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener _ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener_PARAM) {
        _ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener = _ARCommandARDrone3PictureSettingsVideoAutorecordSelectionListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsVideoStabilizationModeListener _ARCommandARDrone3PictureSettingsVideoStabilizationModeListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsVideoStabilizationMode</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsVideoStabilizationModeListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsVideoStabilizationModeListener (ARCommandARDrone3PictureSettingsVideoStabilizationModeListener _ARCommandARDrone3PictureSettingsVideoStabilizationModeListener_PARAM) {
        _ARCommandARDrone3PictureSettingsVideoStabilizationModeListener = _ARCommandARDrone3PictureSettingsVideoStabilizationModeListener_PARAM;
    }

    private static ARCommandARDrone3MediaStreamingVideoEnableListener _ARCommandARDrone3MediaStreamingVideoEnableListener = null;

    /**
     * Set the listener for the command <code>MediaStreamingVideoEnable</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaStreamingVideoEnableListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaStreamingVideoEnableListener (ARCommandARDrone3MediaStreamingVideoEnableListener _ARCommandARDrone3MediaStreamingVideoEnableListener_PARAM) {
        _ARCommandARDrone3MediaStreamingVideoEnableListener = _ARCommandARDrone3MediaStreamingVideoEnableListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsSetHomeListener _ARCommandARDrone3GPSSettingsSetHomeListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsSetHome</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsSetHomeListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsSetHomeListener (ARCommandARDrone3GPSSettingsSetHomeListener _ARCommandARDrone3GPSSettingsSetHomeListener_PARAM) {
        _ARCommandARDrone3GPSSettingsSetHomeListener = _ARCommandARDrone3GPSSettingsSetHomeListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsResetHomeListener _ARCommandARDrone3GPSSettingsResetHomeListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsResetHome</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsResetHomeListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsResetHomeListener (ARCommandARDrone3GPSSettingsResetHomeListener _ARCommandARDrone3GPSSettingsResetHomeListener_PARAM) {
        _ARCommandARDrone3GPSSettingsResetHomeListener = _ARCommandARDrone3GPSSettingsResetHomeListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsSendControllerGPSListener _ARCommandARDrone3GPSSettingsSendControllerGPSListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsSendControllerGPS</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsSendControllerGPSListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsSendControllerGPSListener (ARCommandARDrone3GPSSettingsSendControllerGPSListener _ARCommandARDrone3GPSSettingsSendControllerGPSListener_PARAM) {
        _ARCommandARDrone3GPSSettingsSendControllerGPSListener = _ARCommandARDrone3GPSSettingsSendControllerGPSListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsHomeTypeListener _ARCommandARDrone3GPSSettingsHomeTypeListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsHomeType</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsHomeTypeListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsHomeTypeListener (ARCommandARDrone3GPSSettingsHomeTypeListener _ARCommandARDrone3GPSSettingsHomeTypeListener_PARAM) {
        _ARCommandARDrone3GPSSettingsHomeTypeListener = _ARCommandARDrone3GPSSettingsHomeTypeListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsReturnHomeDelayListener _ARCommandARDrone3GPSSettingsReturnHomeDelayListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsReturnHomeDelay</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsReturnHomeDelayListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsReturnHomeDelayListener (ARCommandARDrone3GPSSettingsReturnHomeDelayListener _ARCommandARDrone3GPSSettingsReturnHomeDelayListener_PARAM) {
        _ARCommandARDrone3GPSSettingsReturnHomeDelayListener = _ARCommandARDrone3GPSSettingsReturnHomeDelayListener_PARAM;
    }

    private static ARCommandARDrone3AntiflickeringElectricFrequencyListener _ARCommandARDrone3AntiflickeringElectricFrequencyListener = null;

    /**
     * Set the listener for the command <code>AntiflickeringElectricFrequency</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3AntiflickeringElectricFrequencyListener_PARAM New listener for the command
     */
    public static void setARDrone3AntiflickeringElectricFrequencyListener (ARCommandARDrone3AntiflickeringElectricFrequencyListener _ARCommandARDrone3AntiflickeringElectricFrequencyListener_PARAM) {
        _ARCommandARDrone3AntiflickeringElectricFrequencyListener = _ARCommandARDrone3AntiflickeringElectricFrequencyListener_PARAM;
    }

    private static ARCommandARDrone3AntiflickeringSetModeListener _ARCommandARDrone3AntiflickeringSetModeListener = null;

    /**
     * Set the listener for the command <code>AntiflickeringSetMode</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3AntiflickeringSetModeListener_PARAM New listener for the command
     */
    public static void setARDrone3AntiflickeringSetModeListener (ARCommandARDrone3AntiflickeringSetModeListener _ARCommandARDrone3AntiflickeringSetModeListener_PARAM) {
        _ARCommandARDrone3AntiflickeringSetModeListener = _ARCommandARDrone3AntiflickeringSetModeListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordStatePictureStateChangedListener _ARCommandARDrone3MediaRecordStatePictureStateChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordStatePictureStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordStatePictureStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordStatePictureStateChangedListener (ARCommandARDrone3MediaRecordStatePictureStateChangedListener _ARCommandARDrone3MediaRecordStatePictureStateChangedListener_PARAM) {
        _ARCommandARDrone3MediaRecordStatePictureStateChangedListener = _ARCommandARDrone3MediaRecordStatePictureStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordStateVideoStateChangedListener _ARCommandARDrone3MediaRecordStateVideoStateChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordStateVideoStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordStateVideoStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordStateVideoStateChangedListener (ARCommandARDrone3MediaRecordStateVideoStateChangedListener _ARCommandARDrone3MediaRecordStateVideoStateChangedListener_PARAM) {
        _ARCommandARDrone3MediaRecordStateVideoStateChangedListener = _ARCommandARDrone3MediaRecordStateVideoStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener _ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordStatePictureStateChangedV2</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordStatePictureStateChangedV2Listener (ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener _ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener_PARAM) {
        _ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener = _ARCommandARDrone3MediaRecordStatePictureStateChangedV2Listener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener _ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordStateVideoStateChangedV2</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordStateVideoStateChangedV2Listener (ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener _ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener_PARAM) {
        _ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener = _ARCommandARDrone3MediaRecordStateVideoStateChangedV2Listener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordEventPictureEventChangedListener _ARCommandARDrone3MediaRecordEventPictureEventChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordEventPictureEventChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordEventPictureEventChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordEventPictureEventChangedListener (ARCommandARDrone3MediaRecordEventPictureEventChangedListener _ARCommandARDrone3MediaRecordEventPictureEventChangedListener_PARAM) {
        _ARCommandARDrone3MediaRecordEventPictureEventChangedListener = _ARCommandARDrone3MediaRecordEventPictureEventChangedListener_PARAM;
    }

    private static ARCommandARDrone3MediaRecordEventVideoEventChangedListener _ARCommandARDrone3MediaRecordEventVideoEventChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordEventVideoEventChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaRecordEventVideoEventChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaRecordEventVideoEventChangedListener (ARCommandARDrone3MediaRecordEventVideoEventChangedListener _ARCommandARDrone3MediaRecordEventVideoEventChangedListener_PARAM) {
        _ARCommandARDrone3MediaRecordEventVideoEventChangedListener = _ARCommandARDrone3MediaRecordEventVideoEventChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateFlatTrimChangedListener _ARCommandARDrone3PilotingStateFlatTrimChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateFlatTrimChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateFlatTrimChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateFlatTrimChangedListener (ARCommandARDrone3PilotingStateFlatTrimChangedListener _ARCommandARDrone3PilotingStateFlatTrimChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateFlatTrimChangedListener = _ARCommandARDrone3PilotingStateFlatTrimChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateFlyingStateChangedListener _ARCommandARDrone3PilotingStateFlyingStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateFlyingStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateFlyingStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateFlyingStateChangedListener (ARCommandARDrone3PilotingStateFlyingStateChangedListener _ARCommandARDrone3PilotingStateFlyingStateChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateFlyingStateChangedListener = _ARCommandARDrone3PilotingStateFlyingStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateAlertStateChangedListener _ARCommandARDrone3PilotingStateAlertStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAlertStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateAlertStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateAlertStateChangedListener (ARCommandARDrone3PilotingStateAlertStateChangedListener _ARCommandARDrone3PilotingStateAlertStateChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateAlertStateChangedListener = _ARCommandARDrone3PilotingStateAlertStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener _ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateNavigateHomeStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateNavigateHomeStateChangedListener (ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener _ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener = _ARCommandARDrone3PilotingStateNavigateHomeStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStatePositionChangedListener _ARCommandARDrone3PilotingStatePositionChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStatePositionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStatePositionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStatePositionChangedListener (ARCommandARDrone3PilotingStatePositionChangedListener _ARCommandARDrone3PilotingStatePositionChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStatePositionChangedListener = _ARCommandARDrone3PilotingStatePositionChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateSpeedChangedListener _ARCommandARDrone3PilotingStateSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateSpeedChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateSpeedChangedListener (ARCommandARDrone3PilotingStateSpeedChangedListener _ARCommandARDrone3PilotingStateSpeedChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateSpeedChangedListener = _ARCommandARDrone3PilotingStateSpeedChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateAttitudeChangedListener _ARCommandARDrone3PilotingStateAttitudeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAttitudeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateAttitudeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateAttitudeChangedListener (ARCommandARDrone3PilotingStateAttitudeChangedListener _ARCommandARDrone3PilotingStateAttitudeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateAttitudeChangedListener = _ARCommandARDrone3PilotingStateAttitudeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener _ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAutoTakeOffModeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateAutoTakeOffModeChangedListener (ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener _ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener = _ARCommandARDrone3PilotingStateAutoTakeOffModeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingStateAltitudeChangedListener _ARCommandARDrone3PilotingStateAltitudeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingStateAltitudeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingStateAltitudeChangedListener (ARCommandARDrone3PilotingStateAltitudeChangedListener _ARCommandARDrone3PilotingStateAltitudeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingStateAltitudeChangedListener = _ARCommandARDrone3PilotingStateAltitudeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingEventMoveByEndListener _ARCommandARDrone3PilotingEventMoveByEndListener = null;

    /**
     * Set the listener for the command <code>PilotingEventMoveByEnd</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingEventMoveByEndListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingEventMoveByEndListener (ARCommandARDrone3PilotingEventMoveByEndListener _ARCommandARDrone3PilotingEventMoveByEndListener_PARAM) {
        _ARCommandARDrone3PilotingEventMoveByEndListener = _ARCommandARDrone3PilotingEventMoveByEndListener_PARAM;
    }

    private static ARCommandARDrone3NetworkStateWifiScanListChangedListener _ARCommandARDrone3NetworkStateWifiScanListChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateWifiScanListChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkStateWifiScanListChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkStateWifiScanListChangedListener (ARCommandARDrone3NetworkStateWifiScanListChangedListener _ARCommandARDrone3NetworkStateWifiScanListChangedListener_PARAM) {
        _ARCommandARDrone3NetworkStateWifiScanListChangedListener = _ARCommandARDrone3NetworkStateWifiScanListChangedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkStateAllWifiScanChangedListener _ARCommandARDrone3NetworkStateAllWifiScanChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateAllWifiScanChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkStateAllWifiScanChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkStateAllWifiScanChangedListener (ARCommandARDrone3NetworkStateAllWifiScanChangedListener _ARCommandARDrone3NetworkStateAllWifiScanChangedListener_PARAM) {
        _ARCommandARDrone3NetworkStateAllWifiScanChangedListener = _ARCommandARDrone3NetworkStateAllWifiScanChangedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener _ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateWifiAuthChannelListChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkStateWifiAuthChannelListChangedListener (ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener _ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener_PARAM) {
        _ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener = _ARCommandARDrone3NetworkStateWifiAuthChannelListChangedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener _ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateAllWifiAuthChannelChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkStateAllWifiAuthChannelChangedListener (ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener _ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener_PARAM) {
        _ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener = _ARCommandARDrone3NetworkStateAllWifiAuthChannelChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener _ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateMaxAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateMaxAltitudeChangedListener (ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener _ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener = _ARCommandARDrone3PilotingSettingsStateMaxAltitudeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener _ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateMaxTiltChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateMaxTiltChangedListener (ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener _ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener = _ARCommandARDrone3PilotingSettingsStateMaxTiltChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener _ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateAbsolutControlChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateAbsolutControlChangedListener (ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener _ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener = _ARCommandARDrone3PilotingSettingsStateAbsolutControlChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener _ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateMaxDistanceChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateMaxDistanceChangedListener (ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener _ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener = _ARCommandARDrone3PilotingSettingsStateMaxDistanceChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener _ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateNoFlyOverMaxDistanceChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener (ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener _ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener = _ARCommandARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateAutonomousFlightMaxHorizontalSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener (ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener = _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateAutonomousFlightMaxVerticalSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener (ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener = _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateAutonomousFlightMaxHorizontalAcceleration</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener (ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener = _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAccelerationListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateAutonomousFlightMaxVerticalAcceleration</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener (ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener = _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAccelerationListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateAutonomousFlightMaxRotationSpeed</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener (ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener = _ARCommandARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener _ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateBankedTurnChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateBankedTurnChangedListener (ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener _ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener = _ARCommandARDrone3PilotingSettingsStateBankedTurnChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener _ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateMinAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateMinAltitudeChangedListener (ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener _ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener = _ARCommandARDrone3PilotingSettingsStateMinAltitudeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener _ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateCirclingDirectionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateCirclingDirectionChangedListener (ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener _ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener = _ARCommandARDrone3PilotingSettingsStateCirclingDirectionChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener _ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateCirclingRadiusChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateCirclingRadiusChangedListener (ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener _ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener = _ARCommandARDrone3PilotingSettingsStateCirclingRadiusChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener _ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateCirclingAltitudeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateCirclingAltitudeChangedListener (ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener _ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener = _ARCommandARDrone3PilotingSettingsStateCirclingAltitudeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener _ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStatePitchModeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStatePitchModeChangedListener (ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener _ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener = _ARCommandARDrone3PilotingSettingsStatePitchModeChangedListener_PARAM;
    }

    private static ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener _ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateLandingModeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PilotingSettingsStateLandingModeChangedListener (ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener _ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener_PARAM) {
        _ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener = _ARCommandARDrone3PilotingSettingsStateLandingModeChangedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener _ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateMaxVerticalSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener (ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener _ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener = _ARCommandARDrone3SpeedSettingsStateMaxVerticalSpeedChangedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener _ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateMaxRotationSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener (ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener _ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener = _ARCommandARDrone3SpeedSettingsStateMaxRotationSpeedChangedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener _ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateHullProtectionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsStateHullProtectionChangedListener (ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener _ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener = _ARCommandARDrone3SpeedSettingsStateHullProtectionChangedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener _ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateOutdoorChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsStateOutdoorChangedListener (ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener _ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener = _ARCommandARDrone3SpeedSettingsStateOutdoorChangedListener_PARAM;
    }

    private static ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener _ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateMaxPitchRollRotationSpeedChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener (ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener _ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener_PARAM) {
        _ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener = _ARCommandARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChangedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener _ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsStateWifiSelectionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkSettingsStateWifiSelectionChangedListener (ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener _ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener_PARAM) {
        _ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener = _ARCommandARDrone3NetworkSettingsStateWifiSelectionChangedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener _ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsStateWifiSecurityChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkSettingsStateWifiSecurityChangedListener (ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener _ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener_PARAM) {
        _ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener = _ARCommandARDrone3NetworkSettingsStateWifiSecurityChangedListener_PARAM;
    }

    private static ARCommandARDrone3NetworkSettingsStateWifiSecurityListener _ARCommandARDrone3NetworkSettingsStateWifiSecurityListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsStateWifiSecurity</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3NetworkSettingsStateWifiSecurityListener_PARAM New listener for the command
     */
    public static void setARDrone3NetworkSettingsStateWifiSecurityListener (ARCommandARDrone3NetworkSettingsStateWifiSecurityListener _ARCommandARDrone3NetworkSettingsStateWifiSecurityListener_PARAM) {
        _ARCommandARDrone3NetworkSettingsStateWifiSecurityListener = _ARCommandARDrone3NetworkSettingsStateWifiSecurityListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener _ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductMotorVersionListChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateProductMotorVersionListChangedListener (ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener _ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener_PARAM) {
        _ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener = _ARCommandARDrone3SettingsStateProductMotorVersionListChangedListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateProductGPSVersionChangedListener _ARCommandARDrone3SettingsStateProductGPSVersionChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductGPSVersionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateProductGPSVersionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateProductGPSVersionChangedListener (ARCommandARDrone3SettingsStateProductGPSVersionChangedListener _ARCommandARDrone3SettingsStateProductGPSVersionChangedListener_PARAM) {
        _ARCommandARDrone3SettingsStateProductGPSVersionChangedListener = _ARCommandARDrone3SettingsStateProductGPSVersionChangedListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateMotorErrorStateChangedListener _ARCommandARDrone3SettingsStateMotorErrorStateChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateMotorErrorStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateMotorErrorStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateMotorErrorStateChangedListener (ARCommandARDrone3SettingsStateMotorErrorStateChangedListener _ARCommandARDrone3SettingsStateMotorErrorStateChangedListener_PARAM) {
        _ARCommandARDrone3SettingsStateMotorErrorStateChangedListener = _ARCommandARDrone3SettingsStateMotorErrorStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener _ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateMotorSoftwareVersionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateMotorSoftwareVersionChangedListener (ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener _ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener_PARAM) {
        _ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener = _ARCommandARDrone3SettingsStateMotorSoftwareVersionChangedListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener _ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateMotorFlightsStatusChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateMotorFlightsStatusChangedListener (ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener _ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener_PARAM) {
        _ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener = _ARCommandARDrone3SettingsStateMotorFlightsStatusChangedListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener _ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateMotorErrorLastErrorChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateMotorErrorLastErrorChangedListener (ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener _ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener_PARAM) {
        _ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener = _ARCommandARDrone3SettingsStateMotorErrorLastErrorChangedListener_PARAM;
    }

    private static ARCommandARDrone3SettingsStateP7IDListener _ARCommandARDrone3SettingsStateP7IDListener = null;

    /**
     * Set the listener for the command <code>SettingsStateP7ID</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3SettingsStateP7IDListener_PARAM New listener for the command
     */
    public static void setARDrone3SettingsStateP7IDListener (ARCommandARDrone3SettingsStateP7IDListener _ARCommandARDrone3SettingsStateP7IDListener_PARAM) {
        _ARCommandARDrone3SettingsStateP7IDListener = _ARCommandARDrone3SettingsStateP7IDListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener _ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStatePictureFormatChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStatePictureFormatChangedListener (ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener _ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener = _ARCommandARDrone3PictureSettingsStatePictureFormatChangedListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener _ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStateAutoWhiteBalanceChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener (ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener _ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener = _ARCommandARDrone3PictureSettingsStateAutoWhiteBalanceChangedListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStateExpositionChangedListener _ARCommandARDrone3PictureSettingsStateExpositionChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStateExpositionChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStateExpositionChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStateExpositionChangedListener (ARCommandARDrone3PictureSettingsStateExpositionChangedListener _ARCommandARDrone3PictureSettingsStateExpositionChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStateExpositionChangedListener = _ARCommandARDrone3PictureSettingsStateExpositionChangedListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStateSaturationChangedListener _ARCommandARDrone3PictureSettingsStateSaturationChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStateSaturationChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStateSaturationChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStateSaturationChangedListener (ARCommandARDrone3PictureSettingsStateSaturationChangedListener _ARCommandARDrone3PictureSettingsStateSaturationChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStateSaturationChangedListener = _ARCommandARDrone3PictureSettingsStateSaturationChangedListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStateTimelapseChangedListener _ARCommandARDrone3PictureSettingsStateTimelapseChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStateTimelapseChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStateTimelapseChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStateTimelapseChangedListener (ARCommandARDrone3PictureSettingsStateTimelapseChangedListener _ARCommandARDrone3PictureSettingsStateTimelapseChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStateTimelapseChangedListener = _ARCommandARDrone3PictureSettingsStateTimelapseChangedListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener _ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStateVideoAutorecordChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStateVideoAutorecordChangedListener (ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener _ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener = _ARCommandARDrone3PictureSettingsStateVideoAutorecordChangedListener_PARAM;
    }

    private static ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener _ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener = null;

    /**
     * Set the listener for the command <code>PictureSettingsStateVideoStabilizationModeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3PictureSettingsStateVideoStabilizationModeChangedListener (ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener _ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener_PARAM) {
        _ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener = _ARCommandARDrone3PictureSettingsStateVideoStabilizationModeChangedListener_PARAM;
    }

    private static ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener _ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener = null;

    /**
     * Set the listener for the command <code>MediaStreamingStateVideoEnableChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3MediaStreamingStateVideoEnableChangedListener (ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener _ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener_PARAM) {
        _ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener = _ARCommandARDrone3MediaStreamingStateVideoEnableChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsStateHomeChangedListener _ARCommandARDrone3GPSSettingsStateHomeChangedListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsStateHomeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsStateHomeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsStateHomeChangedListener (ARCommandARDrone3GPSSettingsStateHomeChangedListener _ARCommandARDrone3GPSSettingsStateHomeChangedListener_PARAM) {
        _ARCommandARDrone3GPSSettingsStateHomeChangedListener = _ARCommandARDrone3GPSSettingsStateHomeChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsStateResetHomeChangedListener _ARCommandARDrone3GPSSettingsStateResetHomeChangedListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsStateResetHomeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsStateResetHomeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsStateResetHomeChangedListener (ARCommandARDrone3GPSSettingsStateResetHomeChangedListener _ARCommandARDrone3GPSSettingsStateResetHomeChangedListener_PARAM) {
        _ARCommandARDrone3GPSSettingsStateResetHomeChangedListener = _ARCommandARDrone3GPSSettingsStateResetHomeChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener _ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsStateGPSFixStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsStateGPSFixStateChangedListener (ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener _ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener_PARAM) {
        _ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener = _ARCommandARDrone3GPSSettingsStateGPSFixStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener _ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsStateGPSUpdateStateChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsStateGPSUpdateStateChangedListener (ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener _ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener_PARAM) {
        _ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener = _ARCommandARDrone3GPSSettingsStateGPSUpdateStateChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener _ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsStateHomeTypeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsStateHomeTypeChangedListener (ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener _ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener_PARAM) {
        _ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener = _ARCommandARDrone3GPSSettingsStateHomeTypeChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener _ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener = null;

    /**
     * Set the listener for the command <code>GPSSettingsStateReturnHomeDelayChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSSettingsStateReturnHomeDelayChangedListener (ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener _ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener_PARAM) {
        _ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener = _ARCommandARDrone3GPSSettingsStateReturnHomeDelayChangedListener_PARAM;
    }

    private static ARCommandARDrone3CameraStateOrientationListener _ARCommandARDrone3CameraStateOrientationListener = null;

    /**
     * Set the listener for the command <code>CameraStateOrientation</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3CameraStateOrientationListener_PARAM New listener for the command
     */
    public static void setARDrone3CameraStateOrientationListener (ARCommandARDrone3CameraStateOrientationListener _ARCommandARDrone3CameraStateOrientationListener_PARAM) {
        _ARCommandARDrone3CameraStateOrientationListener = _ARCommandARDrone3CameraStateOrientationListener_PARAM;
    }

    private static ARCommandARDrone3CameraStateDefaultCameraOrientationListener _ARCommandARDrone3CameraStateDefaultCameraOrientationListener = null;

    /**
     * Set the listener for the command <code>CameraStateDefaultCameraOrientation</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3CameraStateDefaultCameraOrientationListener_PARAM New listener for the command
     */
    public static void setARDrone3CameraStateDefaultCameraOrientationListener (ARCommandARDrone3CameraStateDefaultCameraOrientationListener _ARCommandARDrone3CameraStateDefaultCameraOrientationListener_PARAM) {
        _ARCommandARDrone3CameraStateDefaultCameraOrientationListener = _ARCommandARDrone3CameraStateDefaultCameraOrientationListener_PARAM;
    }

    private static ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener _ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener = null;

    /**
     * Set the listener for the command <code>AntiflickeringStateElectricFrequencyChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3AntiflickeringStateElectricFrequencyChangedListener (ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener _ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener_PARAM) {
        _ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener = _ARCommandARDrone3AntiflickeringStateElectricFrequencyChangedListener_PARAM;
    }

    private static ARCommandARDrone3AntiflickeringStateModeChangedListener _ARCommandARDrone3AntiflickeringStateModeChangedListener = null;

    /**
     * Set the listener for the command <code>AntiflickeringStateModeChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3AntiflickeringStateModeChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3AntiflickeringStateModeChangedListener (ARCommandARDrone3AntiflickeringStateModeChangedListener _ARCommandARDrone3AntiflickeringStateModeChangedListener_PARAM) {
        _ARCommandARDrone3AntiflickeringStateModeChangedListener = _ARCommandARDrone3AntiflickeringStateModeChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener _ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener = null;

    /**
     * Set the listener for the command <code>GPSStateNumberOfSatelliteChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSStateNumberOfSatelliteChangedListener (ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener _ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener_PARAM) {
        _ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener = _ARCommandARDrone3GPSStateNumberOfSatelliteChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener _ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener = null;

    /**
     * Set the listener for the command <code>GPSStateHomeTypeAvailabilityChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSStateHomeTypeAvailabilityChangedListener (ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener _ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener_PARAM) {
        _ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener = _ARCommandARDrone3GPSStateHomeTypeAvailabilityChangedListener_PARAM;
    }

    private static ARCommandARDrone3GPSStateHomeTypeChosenChangedListener _ARCommandARDrone3GPSStateHomeTypeChosenChangedListener = null;

    /**
     * Set the listener for the command <code>GPSStateHomeTypeChosenChanged</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3GPSStateHomeTypeChosenChangedListener_PARAM New listener for the command
     */
    public static void setARDrone3GPSStateHomeTypeChosenChangedListener (ARCommandARDrone3GPSStateHomeTypeChosenChangedListener _ARCommandARDrone3GPSStateHomeTypeChosenChangedListener_PARAM) {
        _ARCommandARDrone3GPSStateHomeTypeChosenChangedListener = _ARCommandARDrone3GPSStateHomeTypeChosenChangedListener_PARAM;
    }

    private static ARCommandARDrone3PROStateFeaturesListener _ARCommandARDrone3PROStateFeaturesListener = null;

    /**
     * Set the listener for the command <code>PROStateFeatures</code> in feature <code>ARDrone3</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandARDrone3PROStateFeaturesListener_PARAM New listener for the command
     */
    public static void setARDrone3PROStateFeaturesListener (ARCommandARDrone3PROStateFeaturesListener _ARCommandARDrone3PROStateFeaturesListener_PARAM) {
        _ARCommandARDrone3PROStateFeaturesListener = _ARCommandARDrone3PROStateFeaturesListener_PARAM;
    }


    private static ARCommandUnknownFeature_1GeographicRunListener _ARCommandUnknownFeature_1GeographicRunListener = null;

    /**
     * Set the listener for the command <code>GeographicRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1GeographicRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1GeographicRunListener (ARCommandUnknownFeature_1GeographicRunListener _ARCommandUnknownFeature_1GeographicRunListener_PARAM) {
        _ARCommandUnknownFeature_1GeographicRunListener = _ARCommandUnknownFeature_1GeographicRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1RelativeRunListener _ARCommandUnknownFeature_1RelativeRunListener = null;

    /**
     * Set the listener for the command <code>RelativeRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1RelativeRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1RelativeRunListener (ARCommandUnknownFeature_1RelativeRunListener _ARCommandUnknownFeature_1RelativeRunListener_PARAM) {
        _ARCommandUnknownFeature_1RelativeRunListener = _ARCommandUnknownFeature_1RelativeRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1LookAtRunListener _ARCommandUnknownFeature_1LookAtRunListener = null;

    /**
     * Set the listener for the command <code>LookAtRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1LookAtRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1LookAtRunListener (ARCommandUnknownFeature_1LookAtRunListener _ARCommandUnknownFeature_1LookAtRunListener_PARAM) {
        _ARCommandUnknownFeature_1LookAtRunListener = _ARCommandUnknownFeature_1LookAtRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1SpiralAnimRunListener _ARCommandUnknownFeature_1SpiralAnimRunListener = null;

    /**
     * Set the listener for the command <code>SpiralAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1SpiralAnimRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1SpiralAnimRunListener (ARCommandUnknownFeature_1SpiralAnimRunListener _ARCommandUnknownFeature_1SpiralAnimRunListener_PARAM) {
        _ARCommandUnknownFeature_1SpiralAnimRunListener = _ARCommandUnknownFeature_1SpiralAnimRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1SwingAnimRunListener _ARCommandUnknownFeature_1SwingAnimRunListener = null;

    /**
     * Set the listener for the command <code>SwingAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1SwingAnimRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1SwingAnimRunListener (ARCommandUnknownFeature_1SwingAnimRunListener _ARCommandUnknownFeature_1SwingAnimRunListener_PARAM) {
        _ARCommandUnknownFeature_1SwingAnimRunListener = _ARCommandUnknownFeature_1SwingAnimRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1BoomerangAnimRunListener _ARCommandUnknownFeature_1BoomerangAnimRunListener = null;

    /**
     * Set the listener for the command <code>BoomerangAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1BoomerangAnimRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1BoomerangAnimRunListener (ARCommandUnknownFeature_1BoomerangAnimRunListener _ARCommandUnknownFeature_1BoomerangAnimRunListener_PARAM) {
        _ARCommandUnknownFeature_1BoomerangAnimRunListener = _ARCommandUnknownFeature_1BoomerangAnimRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1CandleAnimRunListener _ARCommandUnknownFeature_1CandleAnimRunListener = null;

    /**
     * Set the listener for the command <code>CandleAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1CandleAnimRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1CandleAnimRunListener (ARCommandUnknownFeature_1CandleAnimRunListener _ARCommandUnknownFeature_1CandleAnimRunListener_PARAM) {
        _ARCommandUnknownFeature_1CandleAnimRunListener = _ARCommandUnknownFeature_1CandleAnimRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1DollySlideAnimRunListener _ARCommandUnknownFeature_1DollySlideAnimRunListener = null;

    /**
     * Set the listener for the command <code>DollySlideAnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1DollySlideAnimRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1DollySlideAnimRunListener (ARCommandUnknownFeature_1DollySlideAnimRunListener _ARCommandUnknownFeature_1DollySlideAnimRunListener_PARAM) {
        _ARCommandUnknownFeature_1DollySlideAnimRunListener = _ARCommandUnknownFeature_1DollySlideAnimRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1UserFramingPositionListener _ARCommandUnknownFeature_1UserFramingPositionListener = null;

    /**
     * Set the listener for the command <code>UserFramingPosition</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1UserFramingPositionListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1UserFramingPositionListener (ARCommandUnknownFeature_1UserFramingPositionListener _ARCommandUnknownFeature_1UserFramingPositionListener_PARAM) {
        _ARCommandUnknownFeature_1UserFramingPositionListener = _ARCommandUnknownFeature_1UserFramingPositionListener_PARAM;
    }

    private static ARCommandUnknownFeature_1UserGPSDataListener _ARCommandUnknownFeature_1UserGPSDataListener = null;

    /**
     * Set the listener for the command <code>UserGPSData</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1UserGPSDataListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1UserGPSDataListener (ARCommandUnknownFeature_1UserGPSDataListener _ARCommandUnknownFeature_1UserGPSDataListener_PARAM) {
        _ARCommandUnknownFeature_1UserGPSDataListener = _ARCommandUnknownFeature_1UserGPSDataListener_PARAM;
    }

    private static ARCommandUnknownFeature_1UserBaroDataListener _ARCommandUnknownFeature_1UserBaroDataListener = null;

    /**
     * Set the listener for the command <code>UserBaroData</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1UserBaroDataListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1UserBaroDataListener (ARCommandUnknownFeature_1UserBaroDataListener _ARCommandUnknownFeature_1UserBaroDataListener_PARAM) {
        _ARCommandUnknownFeature_1UserBaroDataListener = _ARCommandUnknownFeature_1UserBaroDataListener_PARAM;
    }

    private static ARCommandUnknownFeature_1LynxDetectionListener _ARCommandUnknownFeature_1LynxDetectionListener = null;

    /**
     * Set the listener for the command <code>LynxDetection</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1LynxDetectionListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1LynxDetectionListener (ARCommandUnknownFeature_1LynxDetectionListener _ARCommandUnknownFeature_1LynxDetectionListener_PARAM) {
        _ARCommandUnknownFeature_1LynxDetectionListener = _ARCommandUnknownFeature_1LynxDetectionListener_PARAM;
    }

    private static ARCommandUnknownFeature_1AvailabilityListener _ARCommandUnknownFeature_1AvailabilityListener = null;

    /**
     * Set the listener for the command <code>Availability</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1AvailabilityListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1AvailabilityListener (ARCommandUnknownFeature_1AvailabilityListener _ARCommandUnknownFeature_1AvailabilityListener_PARAM) {
        _ARCommandUnknownFeature_1AvailabilityListener = _ARCommandUnknownFeature_1AvailabilityListener_PARAM;
    }

    private static ARCommandUnknownFeature_1RunListener _ARCommandUnknownFeature_1RunListener = null;

    /**
     * Set the listener for the command <code>Run</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1RunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1RunListener (ARCommandUnknownFeature_1RunListener _ARCommandUnknownFeature_1RunListener_PARAM) {
        _ARCommandUnknownFeature_1RunListener = _ARCommandUnknownFeature_1RunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1GeographicConfigChangedListener _ARCommandUnknownFeature_1GeographicConfigChangedListener = null;

    /**
     * Set the listener for the command <code>GeographicConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1GeographicConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1GeographicConfigChangedListener (ARCommandUnknownFeature_1GeographicConfigChangedListener _ARCommandUnknownFeature_1GeographicConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1GeographicConfigChangedListener = _ARCommandUnknownFeature_1GeographicConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1RelativeConfigChangedListener _ARCommandUnknownFeature_1RelativeConfigChangedListener = null;

    /**
     * Set the listener for the command <code>RelativeConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1RelativeConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1RelativeConfigChangedListener (ARCommandUnknownFeature_1RelativeConfigChangedListener _ARCommandUnknownFeature_1RelativeConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1RelativeConfigChangedListener = _ARCommandUnknownFeature_1RelativeConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1AnimRunListener _ARCommandUnknownFeature_1AnimRunListener = null;

    /**
     * Set the listener for the command <code>AnimRun</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1AnimRunListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1AnimRunListener (ARCommandUnknownFeature_1AnimRunListener _ARCommandUnknownFeature_1AnimRunListener_PARAM) {
        _ARCommandUnknownFeature_1AnimRunListener = _ARCommandUnknownFeature_1AnimRunListener_PARAM;
    }

    private static ARCommandUnknownFeature_1SpiralAnimConfigChangedListener _ARCommandUnknownFeature_1SpiralAnimConfigChangedListener = null;

    /**
     * Set the listener for the command <code>SpiralAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1SpiralAnimConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1SpiralAnimConfigChangedListener (ARCommandUnknownFeature_1SpiralAnimConfigChangedListener _ARCommandUnknownFeature_1SpiralAnimConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1SpiralAnimConfigChangedListener = _ARCommandUnknownFeature_1SpiralAnimConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1SwingAnimConfigChangedListener _ARCommandUnknownFeature_1SwingAnimConfigChangedListener = null;

    /**
     * Set the listener for the command <code>SwingAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1SwingAnimConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1SwingAnimConfigChangedListener (ARCommandUnknownFeature_1SwingAnimConfigChangedListener _ARCommandUnknownFeature_1SwingAnimConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1SwingAnimConfigChangedListener = _ARCommandUnknownFeature_1SwingAnimConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener _ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener = null;

    /**
     * Set the listener for the command <code>BoomerangAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1BoomerangAnimConfigChangedListener (ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener _ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener = _ARCommandUnknownFeature_1BoomerangAnimConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1CandleAnimConfigChangedListener _ARCommandUnknownFeature_1CandleAnimConfigChangedListener = null;

    /**
     * Set the listener for the command <code>CandleAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1CandleAnimConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1CandleAnimConfigChangedListener (ARCommandUnknownFeature_1CandleAnimConfigChangedListener _ARCommandUnknownFeature_1CandleAnimConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1CandleAnimConfigChangedListener = _ARCommandUnknownFeature_1CandleAnimConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener _ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener = null;

    /**
     * Set the listener for the command <code>DollySlideAnimConfigChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1DollySlideAnimConfigChangedListener (ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener _ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener_PARAM) {
        _ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener = _ARCommandUnknownFeature_1DollySlideAnimConfigChangedListener_PARAM;
    }

    private static ARCommandUnknownFeature_1UserFramingPositionChangedListener _ARCommandUnknownFeature_1UserFramingPositionChangedListener = null;

    /**
     * Set the listener for the command <code>UserFramingPositionChanged</code> in feature <code>UnknownFeature_1</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandUnknownFeature_1UserFramingPositionChangedListener_PARAM New listener for the command
     */
    public static void setUnknownFeature_1UserFramingPositionChangedListener (ARCommandUnknownFeature_1UserFramingPositionChangedListener _ARCommandUnknownFeature_1UserFramingPositionChangedListener_PARAM) {
        _ARCommandUnknownFeature_1UserFramingPositionChangedListener = _ARCommandUnknownFeature_1UserFramingPositionChangedListener_PARAM;
    }


    private static ARCommandJumpingSumoPilotingPCMDListener _ARCommandJumpingSumoPilotingPCMDListener = null;

    /**
     * Set the listener for the command <code>PilotingPCMD</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingPCMDListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingPCMDListener (ARCommandJumpingSumoPilotingPCMDListener _ARCommandJumpingSumoPilotingPCMDListener_PARAM) {
        _ARCommandJumpingSumoPilotingPCMDListener = _ARCommandJumpingSumoPilotingPCMDListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingPostureListener _ARCommandJumpingSumoPilotingPostureListener = null;

    /**
     * Set the listener for the command <code>PilotingPosture</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingPostureListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingPostureListener (ARCommandJumpingSumoPilotingPostureListener _ARCommandJumpingSumoPilotingPostureListener_PARAM) {
        _ARCommandJumpingSumoPilotingPostureListener = _ARCommandJumpingSumoPilotingPostureListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingAddCapOffsetListener _ARCommandJumpingSumoPilotingAddCapOffsetListener = null;

    /**
     * Set the listener for the command <code>PilotingAddCapOffset</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingAddCapOffsetListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingAddCapOffsetListener (ARCommandJumpingSumoPilotingAddCapOffsetListener _ARCommandJumpingSumoPilotingAddCapOffsetListener_PARAM) {
        _ARCommandJumpingSumoPilotingAddCapOffsetListener = _ARCommandJumpingSumoPilotingAddCapOffsetListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingUserTakeOffListener _ARCommandJumpingSumoPilotingUserTakeOffListener = null;

    /**
     * Set the listener for the command <code>PilotingUserTakeOff</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingUserTakeOffListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingUserTakeOffListener (ARCommandJumpingSumoPilotingUserTakeOffListener _ARCommandJumpingSumoPilotingUserTakeOffListener_PARAM) {
        _ARCommandJumpingSumoPilotingUserTakeOffListener = _ARCommandJumpingSumoPilotingUserTakeOffListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingLandListener _ARCommandJumpingSumoPilotingLandListener = null;

    /**
     * Set the listener for the command <code>PilotingLand</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingLandListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingLandListener (ARCommandJumpingSumoPilotingLandListener _ARCommandJumpingSumoPilotingLandListener_PARAM) {
        _ARCommandJumpingSumoPilotingLandListener = _ARCommandJumpingSumoPilotingLandListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsJumpStopListener _ARCommandJumpingSumoAnimationsJumpStopListener = null;

    /**
     * Set the listener for the command <code>AnimationsJumpStop</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsJumpStopListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsJumpStopListener (ARCommandJumpingSumoAnimationsJumpStopListener _ARCommandJumpingSumoAnimationsJumpStopListener_PARAM) {
        _ARCommandJumpingSumoAnimationsJumpStopListener = _ARCommandJumpingSumoAnimationsJumpStopListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsJumpCancelListener _ARCommandJumpingSumoAnimationsJumpCancelListener = null;

    /**
     * Set the listener for the command <code>AnimationsJumpCancel</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsJumpCancelListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsJumpCancelListener (ARCommandJumpingSumoAnimationsJumpCancelListener _ARCommandJumpingSumoAnimationsJumpCancelListener_PARAM) {
        _ARCommandJumpingSumoAnimationsJumpCancelListener = _ARCommandJumpingSumoAnimationsJumpCancelListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsJumpLoadListener _ARCommandJumpingSumoAnimationsJumpLoadListener = null;

    /**
     * Set the listener for the command <code>AnimationsJumpLoad</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsJumpLoadListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsJumpLoadListener (ARCommandJumpingSumoAnimationsJumpLoadListener _ARCommandJumpingSumoAnimationsJumpLoadListener_PARAM) {
        _ARCommandJumpingSumoAnimationsJumpLoadListener = _ARCommandJumpingSumoAnimationsJumpLoadListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsJumpListener _ARCommandJumpingSumoAnimationsJumpListener = null;

    /**
     * Set the listener for the command <code>AnimationsJump</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsJumpListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsJumpListener (ARCommandJumpingSumoAnimationsJumpListener _ARCommandJumpingSumoAnimationsJumpListener_PARAM) {
        _ARCommandJumpingSumoAnimationsJumpListener = _ARCommandJumpingSumoAnimationsJumpListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsSimpleAnimationListener _ARCommandJumpingSumoAnimationsSimpleAnimationListener = null;

    /**
     * Set the listener for the command <code>AnimationsSimpleAnimation</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsSimpleAnimationListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsSimpleAnimationListener (ARCommandJumpingSumoAnimationsSimpleAnimationListener _ARCommandJumpingSumoAnimationsSimpleAnimationListener_PARAM) {
        _ARCommandJumpingSumoAnimationsSimpleAnimationListener = _ARCommandJumpingSumoAnimationsSimpleAnimationListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordPictureListener _ARCommandJumpingSumoMediaRecordPictureListener = null;

    /**
     * Set the listener for the command <code>MediaRecordPicture</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordPictureListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordPictureListener (ARCommandJumpingSumoMediaRecordPictureListener _ARCommandJumpingSumoMediaRecordPictureListener_PARAM) {
        _ARCommandJumpingSumoMediaRecordPictureListener = _ARCommandJumpingSumoMediaRecordPictureListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordVideoListener _ARCommandJumpingSumoMediaRecordVideoListener = null;

    /**
     * Set the listener for the command <code>MediaRecordVideo</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordVideoListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordVideoListener (ARCommandJumpingSumoMediaRecordVideoListener _ARCommandJumpingSumoMediaRecordVideoListener_PARAM) {
        _ARCommandJumpingSumoMediaRecordVideoListener = _ARCommandJumpingSumoMediaRecordVideoListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordPictureV2Listener _ARCommandJumpingSumoMediaRecordPictureV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordPictureV2</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordPictureV2Listener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordPictureV2Listener (ARCommandJumpingSumoMediaRecordPictureV2Listener _ARCommandJumpingSumoMediaRecordPictureV2Listener_PARAM) {
        _ARCommandJumpingSumoMediaRecordPictureV2Listener = _ARCommandJumpingSumoMediaRecordPictureV2Listener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordVideoV2Listener _ARCommandJumpingSumoMediaRecordVideoV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordVideoV2</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordVideoV2Listener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordVideoV2Listener (ARCommandJumpingSumoMediaRecordVideoV2Listener _ARCommandJumpingSumoMediaRecordVideoV2Listener_PARAM) {
        _ARCommandJumpingSumoMediaRecordVideoV2Listener = _ARCommandJumpingSumoMediaRecordVideoV2Listener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkSettingsWifiSelectionListener _ARCommandJumpingSumoNetworkSettingsWifiSelectionListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsWifiSelection</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkSettingsWifiSelectionListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkSettingsWifiSelectionListener (ARCommandJumpingSumoNetworkSettingsWifiSelectionListener _ARCommandJumpingSumoNetworkSettingsWifiSelectionListener_PARAM) {
        _ARCommandJumpingSumoNetworkSettingsWifiSelectionListener = _ARCommandJumpingSumoNetworkSettingsWifiSelectionListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkWifiScanListener _ARCommandJumpingSumoNetworkWifiScanListener = null;

    /**
     * Set the listener for the command <code>NetworkWifiScan</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkWifiScanListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkWifiScanListener (ARCommandJumpingSumoNetworkWifiScanListener _ARCommandJumpingSumoNetworkWifiScanListener_PARAM) {
        _ARCommandJumpingSumoNetworkWifiScanListener = _ARCommandJumpingSumoNetworkWifiScanListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkWifiAuthChannelListener _ARCommandJumpingSumoNetworkWifiAuthChannelListener = null;

    /**
     * Set the listener for the command <code>NetworkWifiAuthChannel</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkWifiAuthChannelListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkWifiAuthChannelListener (ARCommandJumpingSumoNetworkWifiAuthChannelListener _ARCommandJumpingSumoNetworkWifiAuthChannelListener_PARAM) {
        _ARCommandJumpingSumoNetworkWifiAuthChannelListener = _ARCommandJumpingSumoNetworkWifiAuthChannelListener_PARAM;
    }

    private static ARCommandJumpingSumoAudioSettingsMasterVolumeListener _ARCommandJumpingSumoAudioSettingsMasterVolumeListener = null;

    /**
     * Set the listener for the command <code>AudioSettingsMasterVolume</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAudioSettingsMasterVolumeListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAudioSettingsMasterVolumeListener (ARCommandJumpingSumoAudioSettingsMasterVolumeListener _ARCommandJumpingSumoAudioSettingsMasterVolumeListener_PARAM) {
        _ARCommandJumpingSumoAudioSettingsMasterVolumeListener = _ARCommandJumpingSumoAudioSettingsMasterVolumeListener_PARAM;
    }

    private static ARCommandJumpingSumoAudioSettingsThemeListener _ARCommandJumpingSumoAudioSettingsThemeListener = null;

    /**
     * Set the listener for the command <code>AudioSettingsTheme</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAudioSettingsThemeListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAudioSettingsThemeListener (ARCommandJumpingSumoAudioSettingsThemeListener _ARCommandJumpingSumoAudioSettingsThemeListener_PARAM) {
        _ARCommandJumpingSumoAudioSettingsThemeListener = _ARCommandJumpingSumoAudioSettingsThemeListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener _ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener = null;

    /**
     * Set the listener for the command <code>RoadPlanAllScriptsMetadata</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanAllScriptsMetadataListener (ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener _ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener = _ARCommandJumpingSumoRoadPlanAllScriptsMetadataListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanScriptUploadedListener _ARCommandJumpingSumoRoadPlanScriptUploadedListener = null;

    /**
     * Set the listener for the command <code>RoadPlanScriptUploaded</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanScriptUploadedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanScriptUploadedListener (ARCommandJumpingSumoRoadPlanScriptUploadedListener _ARCommandJumpingSumoRoadPlanScriptUploadedListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanScriptUploadedListener = _ARCommandJumpingSumoRoadPlanScriptUploadedListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanScriptDeleteListener _ARCommandJumpingSumoRoadPlanScriptDeleteListener = null;

    /**
     * Set the listener for the command <code>RoadPlanScriptDelete</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanScriptDeleteListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanScriptDeleteListener (ARCommandJumpingSumoRoadPlanScriptDeleteListener _ARCommandJumpingSumoRoadPlanScriptDeleteListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanScriptDeleteListener = _ARCommandJumpingSumoRoadPlanScriptDeleteListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanPlayScriptListener _ARCommandJumpingSumoRoadPlanPlayScriptListener = null;

    /**
     * Set the listener for the command <code>RoadPlanPlayScript</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanPlayScriptListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanPlayScriptListener (ARCommandJumpingSumoRoadPlanPlayScriptListener _ARCommandJumpingSumoRoadPlanPlayScriptListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanPlayScriptListener = _ARCommandJumpingSumoRoadPlanPlayScriptListener_PARAM;
    }

    private static ARCommandJumpingSumoSpeedSettingsOutdoorListener _ARCommandJumpingSumoSpeedSettingsOutdoorListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsOutdoor</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoSpeedSettingsOutdoorListener_PARAM New listener for the command
     */
    public static void setJumpingSumoSpeedSettingsOutdoorListener (ARCommandJumpingSumoSpeedSettingsOutdoorListener _ARCommandJumpingSumoSpeedSettingsOutdoorListener_PARAM) {
        _ARCommandJumpingSumoSpeedSettingsOutdoorListener = _ARCommandJumpingSumoSpeedSettingsOutdoorListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaStreamingVideoEnableListener _ARCommandJumpingSumoMediaStreamingVideoEnableListener = null;

    /**
     * Set the listener for the command <code>MediaStreamingVideoEnable</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaStreamingVideoEnableListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaStreamingVideoEnableListener (ARCommandJumpingSumoMediaStreamingVideoEnableListener _ARCommandJumpingSumoMediaStreamingVideoEnableListener_PARAM) {
        _ARCommandJumpingSumoMediaStreamingVideoEnableListener = _ARCommandJumpingSumoMediaStreamingVideoEnableListener_PARAM;
    }

    private static ARCommandJumpingSumoVideoSettingsAutorecordListener _ARCommandJumpingSumoVideoSettingsAutorecordListener = null;

    /**
     * Set the listener for the command <code>VideoSettingsAutorecord</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoVideoSettingsAutorecordListener_PARAM New listener for the command
     */
    public static void setJumpingSumoVideoSettingsAutorecordListener (ARCommandJumpingSumoVideoSettingsAutorecordListener _ARCommandJumpingSumoVideoSettingsAutorecordListener_PARAM) {
        _ARCommandJumpingSumoVideoSettingsAutorecordListener = _ARCommandJumpingSumoVideoSettingsAutorecordListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingStatePostureChangedListener _ARCommandJumpingSumoPilotingStatePostureChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStatePostureChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingStatePostureChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingStatePostureChangedListener (ARCommandJumpingSumoPilotingStatePostureChangedListener _ARCommandJumpingSumoPilotingStatePostureChangedListener_PARAM) {
        _ARCommandJumpingSumoPilotingStatePostureChangedListener = _ARCommandJumpingSumoPilotingStatePostureChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingStateAlertStateChangedListener _ARCommandJumpingSumoPilotingStateAlertStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAlertStateChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingStateAlertStateChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingStateAlertStateChangedListener (ARCommandJumpingSumoPilotingStateAlertStateChangedListener _ARCommandJumpingSumoPilotingStateAlertStateChangedListener_PARAM) {
        _ARCommandJumpingSumoPilotingStateAlertStateChangedListener = _ARCommandJumpingSumoPilotingStateAlertStateChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingStateSpeedChangedListener _ARCommandJumpingSumoPilotingStateSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateSpeedChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingStateSpeedChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingStateSpeedChangedListener (ARCommandJumpingSumoPilotingStateSpeedChangedListener _ARCommandJumpingSumoPilotingStateSpeedChangedListener_PARAM) {
        _ARCommandJumpingSumoPilotingStateSpeedChangedListener = _ARCommandJumpingSumoPilotingStateSpeedChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoPilotingStateFlyingStateChangedListener _ARCommandJumpingSumoPilotingStateFlyingStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateFlyingStateChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoPilotingStateFlyingStateChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoPilotingStateFlyingStateChangedListener (ARCommandJumpingSumoPilotingStateFlyingStateChangedListener _ARCommandJumpingSumoPilotingStateFlyingStateChangedListener_PARAM) {
        _ARCommandJumpingSumoPilotingStateFlyingStateChangedListener = _ARCommandJumpingSumoPilotingStateFlyingStateChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener _ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener = null;

    /**
     * Set the listener for the command <code>AnimationsStateJumpLoadChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsStateJumpLoadChangedListener (ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener _ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener_PARAM) {
        _ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener = _ARCommandJumpingSumoAnimationsStateJumpLoadChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener _ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener = null;

    /**
     * Set the listener for the command <code>AnimationsStateJumpTypeChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsStateJumpTypeChangedListener (ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener _ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener_PARAM) {
        _ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener = _ARCommandJumpingSumoAnimationsStateJumpTypeChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener _ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener = null;

    /**
     * Set the listener for the command <code>AnimationsStateJumpMotorProblemChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAnimationsStateJumpMotorProblemChangedListener (ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener _ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener_PARAM) {
        _ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener = _ARCommandJumpingSumoAnimationsStateJumpMotorProblemChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener _ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductGPSVersionChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoSettingsStateProductGPSVersionChangedListener (ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener _ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener_PARAM) {
        _ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener = _ARCommandJumpingSumoSettingsStateProductGPSVersionChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener _ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordStatePictureStateChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordStatePictureStateChangedListener (ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener _ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener_PARAM) {
        _ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener = _ARCommandJumpingSumoMediaRecordStatePictureStateChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener _ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordStateVideoStateChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordStateVideoStateChangedListener (ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener _ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener_PARAM) {
        _ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener = _ARCommandJumpingSumoMediaRecordStateVideoStateChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener _ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordStatePictureStateChangedV2</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordStatePictureStateChangedV2Listener (ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener _ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener_PARAM) {
        _ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener = _ARCommandJumpingSumoMediaRecordStatePictureStateChangedV2Listener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener _ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordStateVideoStateChangedV2</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordStateVideoStateChangedV2Listener (ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener _ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener_PARAM) {
        _ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener = _ARCommandJumpingSumoMediaRecordStateVideoStateChangedV2Listener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener _ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordEventPictureEventChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordEventPictureEventChangedListener (ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener _ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener_PARAM) {
        _ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener = _ARCommandJumpingSumoMediaRecordEventPictureEventChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener _ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordEventVideoEventChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaRecordEventVideoEventChangedListener (ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener _ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener_PARAM) {
        _ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener = _ARCommandJumpingSumoMediaRecordEventVideoEventChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener _ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkSettingsStateWifiSelectionChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkSettingsStateWifiSelectionChangedListener (ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener _ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener_PARAM) {
        _ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener = _ARCommandJumpingSumoNetworkSettingsStateWifiSelectionChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkStateWifiScanListChangedListener _ARCommandJumpingSumoNetworkStateWifiScanListChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateWifiScanListChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkStateWifiScanListChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkStateWifiScanListChangedListener (ARCommandJumpingSumoNetworkStateWifiScanListChangedListener _ARCommandJumpingSumoNetworkStateWifiScanListChangedListener_PARAM) {
        _ARCommandJumpingSumoNetworkStateWifiScanListChangedListener = _ARCommandJumpingSumoNetworkStateWifiScanListChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener _ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateAllWifiScanChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkStateAllWifiScanChangedListener (ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener _ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener_PARAM) {
        _ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener = _ARCommandJumpingSumoNetworkStateAllWifiScanChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener _ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateWifiAuthChannelListChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkStateWifiAuthChannelListChangedListener (ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener _ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener_PARAM) {
        _ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener = _ARCommandJumpingSumoNetworkStateWifiAuthChannelListChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener _ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateAllWifiAuthChannelChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkStateAllWifiAuthChannelChangedListener (ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener _ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener_PARAM) {
        _ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener = _ARCommandJumpingSumoNetworkStateAllWifiAuthChannelChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoNetworkStateLinkQualityChangedListener _ARCommandJumpingSumoNetworkStateLinkQualityChangedListener = null;

    /**
     * Set the listener for the command <code>NetworkStateLinkQualityChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoNetworkStateLinkQualityChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoNetworkStateLinkQualityChangedListener (ARCommandJumpingSumoNetworkStateLinkQualityChangedListener _ARCommandJumpingSumoNetworkStateLinkQualityChangedListener_PARAM) {
        _ARCommandJumpingSumoNetworkStateLinkQualityChangedListener = _ARCommandJumpingSumoNetworkStateLinkQualityChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener _ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener = null;

    /**
     * Set the listener for the command <code>AudioSettingsStateMasterVolumeChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAudioSettingsStateMasterVolumeChangedListener (ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener _ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener_PARAM) {
        _ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener = _ARCommandJumpingSumoAudioSettingsStateMasterVolumeChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoAudioSettingsStateThemeChangedListener _ARCommandJumpingSumoAudioSettingsStateThemeChangedListener = null;

    /**
     * Set the listener for the command <code>AudioSettingsStateThemeChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoAudioSettingsStateThemeChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoAudioSettingsStateThemeChangedListener (ARCommandJumpingSumoAudioSettingsStateThemeChangedListener _ARCommandJumpingSumoAudioSettingsStateThemeChangedListener_PARAM) {
        _ARCommandJumpingSumoAudioSettingsStateThemeChangedListener = _ARCommandJumpingSumoAudioSettingsStateThemeChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener _ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener = null;

    /**
     * Set the listener for the command <code>RoadPlanStateScriptMetadataListChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanStateScriptMetadataListChangedListener (ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener _ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener = _ARCommandJumpingSumoRoadPlanStateScriptMetadataListChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener _ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener = null;

    /**
     * Set the listener for the command <code>RoadPlanStateAllScriptsMetadataChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener (ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener _ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener = _ARCommandJumpingSumoRoadPlanStateAllScriptsMetadataChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener _ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener = null;

    /**
     * Set the listener for the command <code>RoadPlanStateScriptUploadChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanStateScriptUploadChangedListener (ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener _ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener = _ARCommandJumpingSumoRoadPlanStateScriptUploadChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener _ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener = null;

    /**
     * Set the listener for the command <code>RoadPlanStateScriptDeleteChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanStateScriptDeleteChangedListener (ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener _ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener = _ARCommandJumpingSumoRoadPlanStateScriptDeleteChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener _ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener = null;

    /**
     * Set the listener for the command <code>RoadPlanStatePlayScriptChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoRoadPlanStatePlayScriptChangedListener (ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener _ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener_PARAM) {
        _ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener = _ARCommandJumpingSumoRoadPlanStatePlayScriptChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener _ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateOutdoorChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoSpeedSettingsStateOutdoorChangedListener (ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener _ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener_PARAM) {
        _ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener = _ARCommandJumpingSumoSpeedSettingsStateOutdoorChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener _ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener = null;

    /**
     * Set the listener for the command <code>MediaStreamingStateVideoEnableChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoMediaStreamingStateVideoEnableChangedListener (ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener _ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener_PARAM) {
        _ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener = _ARCommandJumpingSumoMediaStreamingStateVideoEnableChangedListener_PARAM;
    }

    private static ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener _ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener = null;

    /**
     * Set the listener for the command <code>VideoSettingsStateAutorecordChanged</code> in feature <code>JumpingSumo</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener_PARAM New listener for the command
     */
    public static void setJumpingSumoVideoSettingsStateAutorecordChangedListener (ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener _ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener_PARAM) {
        _ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener = _ARCommandJumpingSumoVideoSettingsStateAutorecordChangedListener_PARAM;
    }


    private static ARCommandMiniDronePilotingFlatTrimListener _ARCommandMiniDronePilotingFlatTrimListener = null;

    /**
     * Set the listener for the command <code>PilotingFlatTrim</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingFlatTrimListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingFlatTrimListener (ARCommandMiniDronePilotingFlatTrimListener _ARCommandMiniDronePilotingFlatTrimListener_PARAM) {
        _ARCommandMiniDronePilotingFlatTrimListener = _ARCommandMiniDronePilotingFlatTrimListener_PARAM;
    }

    private static ARCommandMiniDronePilotingTakeOffListener _ARCommandMiniDronePilotingTakeOffListener = null;

    /**
     * Set the listener for the command <code>PilotingTakeOff</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingTakeOffListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingTakeOffListener (ARCommandMiniDronePilotingTakeOffListener _ARCommandMiniDronePilotingTakeOffListener_PARAM) {
        _ARCommandMiniDronePilotingTakeOffListener = _ARCommandMiniDronePilotingTakeOffListener_PARAM;
    }

    private static ARCommandMiniDronePilotingPCMDListener _ARCommandMiniDronePilotingPCMDListener = null;

    /**
     * Set the listener for the command <code>PilotingPCMD</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingPCMDListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingPCMDListener (ARCommandMiniDronePilotingPCMDListener _ARCommandMiniDronePilotingPCMDListener_PARAM) {
        _ARCommandMiniDronePilotingPCMDListener = _ARCommandMiniDronePilotingPCMDListener_PARAM;
    }

    private static ARCommandMiniDronePilotingLandingListener _ARCommandMiniDronePilotingLandingListener = null;

    /**
     * Set the listener for the command <code>PilotingLanding</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingLandingListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingLandingListener (ARCommandMiniDronePilotingLandingListener _ARCommandMiniDronePilotingLandingListener_PARAM) {
        _ARCommandMiniDronePilotingLandingListener = _ARCommandMiniDronePilotingLandingListener_PARAM;
    }

    private static ARCommandMiniDronePilotingEmergencyListener _ARCommandMiniDronePilotingEmergencyListener = null;

    /**
     * Set the listener for the command <code>PilotingEmergency</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingEmergencyListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingEmergencyListener (ARCommandMiniDronePilotingEmergencyListener _ARCommandMiniDronePilotingEmergencyListener_PARAM) {
        _ARCommandMiniDronePilotingEmergencyListener = _ARCommandMiniDronePilotingEmergencyListener_PARAM;
    }

    private static ARCommandMiniDronePilotingAutoTakeOffModeListener _ARCommandMiniDronePilotingAutoTakeOffModeListener = null;

    /**
     * Set the listener for the command <code>PilotingAutoTakeOffMode</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingAutoTakeOffModeListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingAutoTakeOffModeListener (ARCommandMiniDronePilotingAutoTakeOffModeListener _ARCommandMiniDronePilotingAutoTakeOffModeListener_PARAM) {
        _ARCommandMiniDronePilotingAutoTakeOffModeListener = _ARCommandMiniDronePilotingAutoTakeOffModeListener_PARAM;
    }

    private static ARCommandMiniDronePilotingFlyingModeListener _ARCommandMiniDronePilotingFlyingModeListener = null;

    /**
     * Set the listener for the command <code>PilotingFlyingMode</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingFlyingModeListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingFlyingModeListener (ARCommandMiniDronePilotingFlyingModeListener _ARCommandMiniDronePilotingFlyingModeListener_PARAM) {
        _ARCommandMiniDronePilotingFlyingModeListener = _ARCommandMiniDronePilotingFlyingModeListener_PARAM;
    }

    private static ARCommandMiniDroneAnimationsFlipListener _ARCommandMiniDroneAnimationsFlipListener = null;

    /**
     * Set the listener for the command <code>AnimationsFlip</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneAnimationsFlipListener_PARAM New listener for the command
     */
    public static void setMiniDroneAnimationsFlipListener (ARCommandMiniDroneAnimationsFlipListener _ARCommandMiniDroneAnimationsFlipListener_PARAM) {
        _ARCommandMiniDroneAnimationsFlipListener = _ARCommandMiniDroneAnimationsFlipListener_PARAM;
    }

    private static ARCommandMiniDroneAnimationsCapListener _ARCommandMiniDroneAnimationsCapListener = null;

    /**
     * Set the listener for the command <code>AnimationsCap</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneAnimationsCapListener_PARAM New listener for the command
     */
    public static void setMiniDroneAnimationsCapListener (ARCommandMiniDroneAnimationsCapListener _ARCommandMiniDroneAnimationsCapListener_PARAM) {
        _ARCommandMiniDroneAnimationsCapListener = _ARCommandMiniDroneAnimationsCapListener_PARAM;
    }

    private static ARCommandMiniDroneMediaRecordPictureListener _ARCommandMiniDroneMediaRecordPictureListener = null;

    /**
     * Set the listener for the command <code>MediaRecordPicture</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneMediaRecordPictureListener_PARAM New listener for the command
     */
    public static void setMiniDroneMediaRecordPictureListener (ARCommandMiniDroneMediaRecordPictureListener _ARCommandMiniDroneMediaRecordPictureListener_PARAM) {
        _ARCommandMiniDroneMediaRecordPictureListener = _ARCommandMiniDroneMediaRecordPictureListener_PARAM;
    }

    private static ARCommandMiniDroneMediaRecordPictureV2Listener _ARCommandMiniDroneMediaRecordPictureV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordPictureV2</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneMediaRecordPictureV2Listener_PARAM New listener for the command
     */
    public static void setMiniDroneMediaRecordPictureV2Listener (ARCommandMiniDroneMediaRecordPictureV2Listener _ARCommandMiniDroneMediaRecordPictureV2Listener_PARAM) {
        _ARCommandMiniDroneMediaRecordPictureV2Listener = _ARCommandMiniDroneMediaRecordPictureV2Listener_PARAM;
    }

    private static ARCommandMiniDronePilotingSettingsMaxAltitudeListener _ARCommandMiniDronePilotingSettingsMaxAltitudeListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsMaxAltitude</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingSettingsMaxAltitudeListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingSettingsMaxAltitudeListener (ARCommandMiniDronePilotingSettingsMaxAltitudeListener _ARCommandMiniDronePilotingSettingsMaxAltitudeListener_PARAM) {
        _ARCommandMiniDronePilotingSettingsMaxAltitudeListener = _ARCommandMiniDronePilotingSettingsMaxAltitudeListener_PARAM;
    }

    private static ARCommandMiniDronePilotingSettingsMaxTiltListener _ARCommandMiniDronePilotingSettingsMaxTiltListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsMaxTilt</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingSettingsMaxTiltListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingSettingsMaxTiltListener (ARCommandMiniDronePilotingSettingsMaxTiltListener _ARCommandMiniDronePilotingSettingsMaxTiltListener_PARAM) {
        _ARCommandMiniDronePilotingSettingsMaxTiltListener = _ARCommandMiniDronePilotingSettingsMaxTiltListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener _ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsMaxVerticalSpeed</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsMaxVerticalSpeedListener (ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener _ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener = _ARCommandMiniDroneSpeedSettingsMaxVerticalSpeedListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener _ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsMaxRotationSpeed</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsMaxRotationSpeedListener (ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener _ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener = _ARCommandMiniDroneSpeedSettingsMaxRotationSpeedListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsWheelsListener _ARCommandMiniDroneSpeedSettingsWheelsListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsWheels</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsWheelsListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsWheelsListener (ARCommandMiniDroneSpeedSettingsWheelsListener _ARCommandMiniDroneSpeedSettingsWheelsListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsWheelsListener = _ARCommandMiniDroneSpeedSettingsWheelsListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener _ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsMaxHorizontalSpeed</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsMaxHorizontalSpeedListener (ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener _ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener = _ARCommandMiniDroneSpeedSettingsMaxHorizontalSpeedListener_PARAM;
    }

    private static ARCommandMiniDroneSettingsCutOutModeListener _ARCommandMiniDroneSettingsCutOutModeListener = null;

    /**
     * Set the listener for the command <code>SettingsCutOutMode</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSettingsCutOutModeListener_PARAM New listener for the command
     */
    public static void setMiniDroneSettingsCutOutModeListener (ARCommandMiniDroneSettingsCutOutModeListener _ARCommandMiniDroneSettingsCutOutModeListener_PARAM) {
        _ARCommandMiniDroneSettingsCutOutModeListener = _ARCommandMiniDroneSettingsCutOutModeListener_PARAM;
    }

    private static ARCommandMiniDroneGPSControllerLatitudeForRunListener _ARCommandMiniDroneGPSControllerLatitudeForRunListener = null;

    /**
     * Set the listener for the command <code>GPSControllerLatitudeForRun</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneGPSControllerLatitudeForRunListener_PARAM New listener for the command
     */
    public static void setMiniDroneGPSControllerLatitudeForRunListener (ARCommandMiniDroneGPSControllerLatitudeForRunListener _ARCommandMiniDroneGPSControllerLatitudeForRunListener_PARAM) {
        _ARCommandMiniDroneGPSControllerLatitudeForRunListener = _ARCommandMiniDroneGPSControllerLatitudeForRunListener_PARAM;
    }

    private static ARCommandMiniDroneGPSControllerLongitudeForRunListener _ARCommandMiniDroneGPSControllerLongitudeForRunListener = null;

    /**
     * Set the listener for the command <code>GPSControllerLongitudeForRun</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneGPSControllerLongitudeForRunListener_PARAM New listener for the command
     */
    public static void setMiniDroneGPSControllerLongitudeForRunListener (ARCommandMiniDroneGPSControllerLongitudeForRunListener _ARCommandMiniDroneGPSControllerLongitudeForRunListener_PARAM) {
        _ARCommandMiniDroneGPSControllerLongitudeForRunListener = _ARCommandMiniDroneGPSControllerLongitudeForRunListener_PARAM;
    }

    private static ARCommandMiniDroneConfigurationControllerTypeListener _ARCommandMiniDroneConfigurationControllerTypeListener = null;

    /**
     * Set the listener for the command <code>ConfigurationControllerType</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneConfigurationControllerTypeListener_PARAM New listener for the command
     */
    public static void setMiniDroneConfigurationControllerTypeListener (ARCommandMiniDroneConfigurationControllerTypeListener _ARCommandMiniDroneConfigurationControllerTypeListener_PARAM) {
        _ARCommandMiniDroneConfigurationControllerTypeListener = _ARCommandMiniDroneConfigurationControllerTypeListener_PARAM;
    }

    private static ARCommandMiniDroneConfigurationControllerNameListener _ARCommandMiniDroneConfigurationControllerNameListener = null;

    /**
     * Set the listener for the command <code>ConfigurationControllerName</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneConfigurationControllerNameListener_PARAM New listener for the command
     */
    public static void setMiniDroneConfigurationControllerNameListener (ARCommandMiniDroneConfigurationControllerNameListener _ARCommandMiniDroneConfigurationControllerNameListener_PARAM) {
        _ARCommandMiniDroneConfigurationControllerNameListener = _ARCommandMiniDroneConfigurationControllerNameListener_PARAM;
    }

    private static ARCommandMiniDronePilotingStateFlatTrimChangedListener _ARCommandMiniDronePilotingStateFlatTrimChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateFlatTrimChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingStateFlatTrimChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingStateFlatTrimChangedListener (ARCommandMiniDronePilotingStateFlatTrimChangedListener _ARCommandMiniDronePilotingStateFlatTrimChangedListener_PARAM) {
        _ARCommandMiniDronePilotingStateFlatTrimChangedListener = _ARCommandMiniDronePilotingStateFlatTrimChangedListener_PARAM;
    }

    private static ARCommandMiniDronePilotingStateFlyingStateChangedListener _ARCommandMiniDronePilotingStateFlyingStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateFlyingStateChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingStateFlyingStateChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingStateFlyingStateChangedListener (ARCommandMiniDronePilotingStateFlyingStateChangedListener _ARCommandMiniDronePilotingStateFlyingStateChangedListener_PARAM) {
        _ARCommandMiniDronePilotingStateFlyingStateChangedListener = _ARCommandMiniDronePilotingStateFlyingStateChangedListener_PARAM;
    }

    private static ARCommandMiniDronePilotingStateAlertStateChangedListener _ARCommandMiniDronePilotingStateAlertStateChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAlertStateChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingStateAlertStateChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingStateAlertStateChangedListener (ARCommandMiniDronePilotingStateAlertStateChangedListener _ARCommandMiniDronePilotingStateAlertStateChangedListener_PARAM) {
        _ARCommandMiniDronePilotingStateAlertStateChangedListener = _ARCommandMiniDronePilotingStateAlertStateChangedListener_PARAM;
    }

    private static ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener _ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateAutoTakeOffModeChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingStateAutoTakeOffModeChangedListener (ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener _ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener_PARAM) {
        _ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener = _ARCommandMiniDronePilotingStateAutoTakeOffModeChangedListener_PARAM;
    }

    private static ARCommandMiniDronePilotingStateFlyingModeChangedListener _ARCommandMiniDronePilotingStateFlyingModeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingStateFlyingModeChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingStateFlyingModeChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingStateFlyingModeChangedListener (ARCommandMiniDronePilotingStateFlyingModeChangedListener _ARCommandMiniDronePilotingStateFlyingModeChangedListener_PARAM) {
        _ARCommandMiniDronePilotingStateFlyingModeChangedListener = _ARCommandMiniDronePilotingStateFlyingModeChangedListener_PARAM;
    }

    private static ARCommandMiniDroneMediaRecordStatePictureStateChangedListener _ARCommandMiniDroneMediaRecordStatePictureStateChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordStatePictureStateChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneMediaRecordStatePictureStateChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneMediaRecordStatePictureStateChangedListener (ARCommandMiniDroneMediaRecordStatePictureStateChangedListener _ARCommandMiniDroneMediaRecordStatePictureStateChangedListener_PARAM) {
        _ARCommandMiniDroneMediaRecordStatePictureStateChangedListener = _ARCommandMiniDroneMediaRecordStatePictureStateChangedListener_PARAM;
    }

    private static ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener _ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener = null;

    /**
     * Set the listener for the command <code>MediaRecordStatePictureStateChangedV2</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener_PARAM New listener for the command
     */
    public static void setMiniDroneMediaRecordStatePictureStateChangedV2Listener (ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener _ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener_PARAM) {
        _ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener = _ARCommandMiniDroneMediaRecordStatePictureStateChangedV2Listener_PARAM;
    }

    private static ARCommandMiniDroneMediaRecordEventPictureEventChangedListener _ARCommandMiniDroneMediaRecordEventPictureEventChangedListener = null;

    /**
     * Set the listener for the command <code>MediaRecordEventPictureEventChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneMediaRecordEventPictureEventChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneMediaRecordEventPictureEventChangedListener (ARCommandMiniDroneMediaRecordEventPictureEventChangedListener _ARCommandMiniDroneMediaRecordEventPictureEventChangedListener_PARAM) {
        _ARCommandMiniDroneMediaRecordEventPictureEventChangedListener = _ARCommandMiniDroneMediaRecordEventPictureEventChangedListener_PARAM;
    }

    private static ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener _ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateMaxAltitudeChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingSettingsStateMaxAltitudeChangedListener (ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener _ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener_PARAM) {
        _ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener = _ARCommandMiniDronePilotingSettingsStateMaxAltitudeChangedListener_PARAM;
    }

    private static ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener _ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener = null;

    /**
     * Set the listener for the command <code>PilotingSettingsStateMaxTiltChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener_PARAM New listener for the command
     */
    public static void setMiniDronePilotingSettingsStateMaxTiltChangedListener (ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener _ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener_PARAM) {
        _ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener = _ARCommandMiniDronePilotingSettingsStateMaxTiltChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener _ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateMaxVerticalSpeedChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener (ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener _ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener = _ARCommandMiniDroneSpeedSettingsStateMaxVerticalSpeedChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener _ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateMaxRotationSpeedChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener (ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener _ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener = _ARCommandMiniDroneSpeedSettingsStateMaxRotationSpeedChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener _ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateWheelsChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsStateWheelsChangedListener (ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener _ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener = _ARCommandMiniDroneSpeedSettingsStateWheelsChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener _ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener = null;

    /**
     * Set the listener for the command <code>SpeedSettingsStateMaxHorizontalSpeedChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener (ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener _ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener_PARAM) {
        _ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener = _ARCommandMiniDroneSpeedSettingsStateMaxHorizontalSpeedChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener _ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductMotorsVersionChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSettingsStateProductMotorsVersionChangedListener (ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener _ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener_PARAM) {
        _ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener = _ARCommandMiniDroneSettingsStateProductMotorsVersionChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener _ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductInertialVersionChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSettingsStateProductInertialVersionChangedListener (ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener _ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener_PARAM) {
        _ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener = _ARCommandMiniDroneSettingsStateProductInertialVersionChangedListener_PARAM;
    }

    private static ARCommandMiniDroneSettingsStateCutOutModeChangedListener _ARCommandMiniDroneSettingsStateCutOutModeChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateCutOutModeChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneSettingsStateCutOutModeChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneSettingsStateCutOutModeChangedListener (ARCommandMiniDroneSettingsStateCutOutModeChangedListener _ARCommandMiniDroneSettingsStateCutOutModeChangedListener_PARAM) {
        _ARCommandMiniDroneSettingsStateCutOutModeChangedListener = _ARCommandMiniDroneSettingsStateCutOutModeChangedListener_PARAM;
    }

    private static ARCommandMiniDroneFloodControlStateFloodControlChangedListener _ARCommandMiniDroneFloodControlStateFloodControlChangedListener = null;

    /**
     * Set the listener for the command <code>FloodControlStateFloodControlChanged</code> in feature <code>MiniDrone</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandMiniDroneFloodControlStateFloodControlChangedListener_PARAM New listener for the command
     */
    public static void setMiniDroneFloodControlStateFloodControlChangedListener (ARCommandMiniDroneFloodControlStateFloodControlChangedListener _ARCommandMiniDroneFloodControlStateFloodControlChangedListener_PARAM) {
        _ARCommandMiniDroneFloodControlStateFloodControlChangedListener = _ARCommandMiniDroneFloodControlStateFloodControlChangedListener_PARAM;
    }


    private static ARCommandSkyControllerWifiRequestWifiListListener _ARCommandSkyControllerWifiRequestWifiListListener = null;

    /**
     * Set the listener for the command <code>WifiRequestWifiList</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiRequestWifiListListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiRequestWifiListListener (ARCommandSkyControllerWifiRequestWifiListListener _ARCommandSkyControllerWifiRequestWifiListListener_PARAM) {
        _ARCommandSkyControllerWifiRequestWifiListListener = _ARCommandSkyControllerWifiRequestWifiListListener_PARAM;
    }

    private static ARCommandSkyControllerWifiRequestCurrentWifiListener _ARCommandSkyControllerWifiRequestCurrentWifiListener = null;

    /**
     * Set the listener for the command <code>WifiRequestCurrentWifi</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiRequestCurrentWifiListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiRequestCurrentWifiListener (ARCommandSkyControllerWifiRequestCurrentWifiListener _ARCommandSkyControllerWifiRequestCurrentWifiListener_PARAM) {
        _ARCommandSkyControllerWifiRequestCurrentWifiListener = _ARCommandSkyControllerWifiRequestCurrentWifiListener_PARAM;
    }

    private static ARCommandSkyControllerWifiConnectToWifiListener _ARCommandSkyControllerWifiConnectToWifiListener = null;

    /**
     * Set the listener for the command <code>WifiConnectToWifi</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiConnectToWifiListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiConnectToWifiListener (ARCommandSkyControllerWifiConnectToWifiListener _ARCommandSkyControllerWifiConnectToWifiListener_PARAM) {
        _ARCommandSkyControllerWifiConnectToWifiListener = _ARCommandSkyControllerWifiConnectToWifiListener_PARAM;
    }

    private static ARCommandSkyControllerWifiForgetWifiListener _ARCommandSkyControllerWifiForgetWifiListener = null;

    /**
     * Set the listener for the command <code>WifiForgetWifi</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiForgetWifiListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiForgetWifiListener (ARCommandSkyControllerWifiForgetWifiListener _ARCommandSkyControllerWifiForgetWifiListener_PARAM) {
        _ARCommandSkyControllerWifiForgetWifiListener = _ARCommandSkyControllerWifiForgetWifiListener_PARAM;
    }

    private static ARCommandSkyControllerWifiWifiAuthChannelListener _ARCommandSkyControllerWifiWifiAuthChannelListener = null;

    /**
     * Set the listener for the command <code>WifiWifiAuthChannel</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiWifiAuthChannelListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiWifiAuthChannelListener (ARCommandSkyControllerWifiWifiAuthChannelListener _ARCommandSkyControllerWifiWifiAuthChannelListener_PARAM) {
        _ARCommandSkyControllerWifiWifiAuthChannelListener = _ARCommandSkyControllerWifiWifiAuthChannelListener_PARAM;
    }

    private static ARCommandSkyControllerDeviceRequestDeviceListListener _ARCommandSkyControllerDeviceRequestDeviceListListener = null;

    /**
     * Set the listener for the command <code>DeviceRequestDeviceList</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerDeviceRequestDeviceListListener_PARAM New listener for the command
     */
    public static void setSkyControllerDeviceRequestDeviceListListener (ARCommandSkyControllerDeviceRequestDeviceListListener _ARCommandSkyControllerDeviceRequestDeviceListListener_PARAM) {
        _ARCommandSkyControllerDeviceRequestDeviceListListener = _ARCommandSkyControllerDeviceRequestDeviceListListener_PARAM;
    }

    private static ARCommandSkyControllerDeviceRequestCurrentDeviceListener _ARCommandSkyControllerDeviceRequestCurrentDeviceListener = null;

    /**
     * Set the listener for the command <code>DeviceRequestCurrentDevice</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerDeviceRequestCurrentDeviceListener_PARAM New listener for the command
     */
    public static void setSkyControllerDeviceRequestCurrentDeviceListener (ARCommandSkyControllerDeviceRequestCurrentDeviceListener _ARCommandSkyControllerDeviceRequestCurrentDeviceListener_PARAM) {
        _ARCommandSkyControllerDeviceRequestCurrentDeviceListener = _ARCommandSkyControllerDeviceRequestCurrentDeviceListener_PARAM;
    }

    private static ARCommandSkyControllerDeviceConnectToDeviceListener _ARCommandSkyControllerDeviceConnectToDeviceListener = null;

    /**
     * Set the listener for the command <code>DeviceConnectToDevice</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerDeviceConnectToDeviceListener_PARAM New listener for the command
     */
    public static void setSkyControllerDeviceConnectToDeviceListener (ARCommandSkyControllerDeviceConnectToDeviceListener _ARCommandSkyControllerDeviceConnectToDeviceListener_PARAM) {
        _ARCommandSkyControllerDeviceConnectToDeviceListener = _ARCommandSkyControllerDeviceConnectToDeviceListener_PARAM;
    }

    private static ARCommandSkyControllerSettingsAllSettingsListener _ARCommandSkyControllerSettingsAllSettingsListener = null;

    /**
     * Set the listener for the command <code>SettingsAllSettings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSettingsAllSettingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerSettingsAllSettingsListener (ARCommandSkyControllerSettingsAllSettingsListener _ARCommandSkyControllerSettingsAllSettingsListener_PARAM) {
        _ARCommandSkyControllerSettingsAllSettingsListener = _ARCommandSkyControllerSettingsAllSettingsListener_PARAM;
    }

    private static ARCommandSkyControllerSettingsResetListener _ARCommandSkyControllerSettingsResetListener = null;

    /**
     * Set the listener for the command <code>SettingsReset</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSettingsResetListener_PARAM New listener for the command
     */
    public static void setSkyControllerSettingsResetListener (ARCommandSkyControllerSettingsResetListener _ARCommandSkyControllerSettingsResetListener_PARAM) {
        _ARCommandSkyControllerSettingsResetListener = _ARCommandSkyControllerSettingsResetListener_PARAM;
    }

    private static ARCommandSkyControllerCommonAllStatesListener _ARCommandSkyControllerCommonAllStatesListener = null;

    /**
     * Set the listener for the command <code>CommonAllStates</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCommonAllStatesListener_PARAM New listener for the command
     */
    public static void setSkyControllerCommonAllStatesListener (ARCommandSkyControllerCommonAllStatesListener _ARCommandSkyControllerCommonAllStatesListener_PARAM) {
        _ARCommandSkyControllerCommonAllStatesListener = _ARCommandSkyControllerCommonAllStatesListener_PARAM;
    }

    private static ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener _ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener = null;

    /**
     * Set the listener for the command <code>AccessPointSettingsAccessPointSSID</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener_PARAM New listener for the command
     */
    public static void setSkyControllerAccessPointSettingsAccessPointSSIDListener (ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener _ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener_PARAM) {
        _ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener = _ARCommandSkyControllerAccessPointSettingsAccessPointSSIDListener_PARAM;
    }

    private static ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener _ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener = null;

    /**
     * Set the listener for the command <code>AccessPointSettingsAccessPointChannel</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener_PARAM New listener for the command
     */
    public static void setSkyControllerAccessPointSettingsAccessPointChannelListener (ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener _ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener_PARAM) {
        _ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener = _ARCommandSkyControllerAccessPointSettingsAccessPointChannelListener_PARAM;
    }

    private static ARCommandSkyControllerAccessPointSettingsWifiSelectionListener _ARCommandSkyControllerAccessPointSettingsWifiSelectionListener = null;

    /**
     * Set the listener for the command <code>AccessPointSettingsWifiSelection</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAccessPointSettingsWifiSelectionListener_PARAM New listener for the command
     */
    public static void setSkyControllerAccessPointSettingsWifiSelectionListener (ARCommandSkyControllerAccessPointSettingsWifiSelectionListener _ARCommandSkyControllerAccessPointSettingsWifiSelectionListener_PARAM) {
        _ARCommandSkyControllerAccessPointSettingsWifiSelectionListener = _ARCommandSkyControllerAccessPointSettingsWifiSelectionListener_PARAM;
    }

    private static ARCommandSkyControllerCameraResetOrientationListener _ARCommandSkyControllerCameraResetOrientationListener = null;

    /**
     * Set the listener for the command <code>CameraResetOrientation</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCameraResetOrientationListener_PARAM New listener for the command
     */
    public static void setSkyControllerCameraResetOrientationListener (ARCommandSkyControllerCameraResetOrientationListener _ARCommandSkyControllerCameraResetOrientationListener_PARAM) {
        _ARCommandSkyControllerCameraResetOrientationListener = _ARCommandSkyControllerCameraResetOrientationListener_PARAM;
    }

    private static ARCommandSkyControllerGamepadInfosGetGamepadControlsListener _ARCommandSkyControllerGamepadInfosGetGamepadControlsListener = null;

    /**
     * Set the listener for the command <code>GamepadInfosGetGamepadControls</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerGamepadInfosGetGamepadControlsListener_PARAM New listener for the command
     */
    public static void setSkyControllerGamepadInfosGetGamepadControlsListener (ARCommandSkyControllerGamepadInfosGetGamepadControlsListener _ARCommandSkyControllerGamepadInfosGetGamepadControlsListener_PARAM) {
        _ARCommandSkyControllerGamepadInfosGetGamepadControlsListener = _ARCommandSkyControllerGamepadInfosGetGamepadControlsListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener _ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsGetCurrentButtonMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsGetCurrentButtonMappingsListener (ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener _ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener = _ARCommandSkyControllerButtonMappingsGetCurrentButtonMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener _ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsGetAvailableButtonMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsGetAvailableButtonMappingsListener (ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener _ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener = _ARCommandSkyControllerButtonMappingsGetAvailableButtonMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsSetButtonMappingListener _ARCommandSkyControllerButtonMappingsSetButtonMappingListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsSetButtonMapping</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsSetButtonMappingListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsSetButtonMappingListener (ARCommandSkyControllerButtonMappingsSetButtonMappingListener _ARCommandSkyControllerButtonMappingsSetButtonMappingListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsSetButtonMappingListener = _ARCommandSkyControllerButtonMappingsSetButtonMappingListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener _ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsDefaultButtonMapping</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsDefaultButtonMappingListener (ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener _ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener = _ARCommandSkyControllerButtonMappingsDefaultButtonMappingListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener _ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsGetCurrentAxisMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsGetCurrentAxisMappingsListener (ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener _ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener = _ARCommandSkyControllerAxisMappingsGetCurrentAxisMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener _ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsGetAvailableAxisMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsGetAvailableAxisMappingsListener (ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener _ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener = _ARCommandSkyControllerAxisMappingsGetAvailableAxisMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsSetAxisMappingListener _ARCommandSkyControllerAxisMappingsSetAxisMappingListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsSetAxisMapping</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsSetAxisMappingListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsSetAxisMappingListener (ARCommandSkyControllerAxisMappingsSetAxisMappingListener _ARCommandSkyControllerAxisMappingsSetAxisMappingListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsSetAxisMappingListener = _ARCommandSkyControllerAxisMappingsSetAxisMappingListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener _ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsDefaultAxisMapping</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsDefaultAxisMappingListener (ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener _ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener = _ARCommandSkyControllerAxisMappingsDefaultAxisMappingListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener _ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersGetCurrentAxisFilters</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersGetCurrentAxisFiltersListener (ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener _ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener = _ARCommandSkyControllerAxisFiltersGetCurrentAxisFiltersListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener _ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersGetPresetAxisFilters</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersGetPresetAxisFiltersListener (ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener _ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener = _ARCommandSkyControllerAxisFiltersGetPresetAxisFiltersListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersSetAxisFilterListener _ARCommandSkyControllerAxisFiltersSetAxisFilterListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersSetAxisFilter</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersSetAxisFilterListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersSetAxisFilterListener (ARCommandSkyControllerAxisFiltersSetAxisFilterListener _ARCommandSkyControllerAxisFiltersSetAxisFilterListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersSetAxisFilterListener = _ARCommandSkyControllerAxisFiltersSetAxisFilterListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener _ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersDefaultAxisFilters</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersDefaultAxisFiltersListener (ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener _ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener = _ARCommandSkyControllerAxisFiltersDefaultAxisFiltersListener_PARAM;
    }

    private static ARCommandSkyControllerCoPilotingSetPilotingSourceListener _ARCommandSkyControllerCoPilotingSetPilotingSourceListener = null;

    /**
     * Set the listener for the command <code>CoPilotingSetPilotingSource</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCoPilotingSetPilotingSourceListener_PARAM New listener for the command
     */
    public static void setSkyControllerCoPilotingSetPilotingSourceListener (ARCommandSkyControllerCoPilotingSetPilotingSourceListener _ARCommandSkyControllerCoPilotingSetPilotingSourceListener_PARAM) {
        _ARCommandSkyControllerCoPilotingSetPilotingSourceListener = _ARCommandSkyControllerCoPilotingSetPilotingSourceListener_PARAM;
    }

    private static ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener _ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener = null;

    /**
     * Set the listener for the command <code>CalibrationEnableMagnetoCalibrationQualityUpdates</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener_PARAM New listener for the command
     */
    public static void setSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener (ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener _ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener_PARAM) {
        _ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener = _ARCommandSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdatesListener_PARAM;
    }

    private static ARCommandSkyControllerWifiStateWifiListListener _ARCommandSkyControllerWifiStateWifiListListener = null;

    /**
     * Set the listener for the command <code>WifiStateWifiList</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiStateWifiListListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiStateWifiListListener (ARCommandSkyControllerWifiStateWifiListListener _ARCommandSkyControllerWifiStateWifiListListener_PARAM) {
        _ARCommandSkyControllerWifiStateWifiListListener = _ARCommandSkyControllerWifiStateWifiListListener_PARAM;
    }

    private static ARCommandSkyControllerWifiStateConnexionChangedListener _ARCommandSkyControllerWifiStateConnexionChangedListener = null;

    /**
     * Set the listener for the command <code>WifiStateConnexionChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiStateConnexionChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiStateConnexionChangedListener (ARCommandSkyControllerWifiStateConnexionChangedListener _ARCommandSkyControllerWifiStateConnexionChangedListener_PARAM) {
        _ARCommandSkyControllerWifiStateConnexionChangedListener = _ARCommandSkyControllerWifiStateConnexionChangedListener_PARAM;
    }

    private static ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener _ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener = null;

    /**
     * Set the listener for the command <code>WifiStateWifiAuthChannelListChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiStateWifiAuthChannelListChangedListener (ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener _ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener_PARAM) {
        _ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener = _ARCommandSkyControllerWifiStateWifiAuthChannelListChangedListener_PARAM;
    }

    private static ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener _ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener = null;

    /**
     * Set the listener for the command <code>WifiStateAllWifiAuthChannelChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiStateAllWifiAuthChannelChangedListener (ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener _ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener_PARAM) {
        _ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener = _ARCommandSkyControllerWifiStateAllWifiAuthChannelChangedListener_PARAM;
    }

    private static ARCommandSkyControllerWifiStateWifiSignalChangedListener _ARCommandSkyControllerWifiStateWifiSignalChangedListener = null;

    /**
     * Set the listener for the command <code>WifiStateWifiSignalChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerWifiStateWifiSignalChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerWifiStateWifiSignalChangedListener (ARCommandSkyControllerWifiStateWifiSignalChangedListener _ARCommandSkyControllerWifiStateWifiSignalChangedListener_PARAM) {
        _ARCommandSkyControllerWifiStateWifiSignalChangedListener = _ARCommandSkyControllerWifiStateWifiSignalChangedListener_PARAM;
    }

    private static ARCommandSkyControllerDeviceStateDeviceListListener _ARCommandSkyControllerDeviceStateDeviceListListener = null;

    /**
     * Set the listener for the command <code>DeviceStateDeviceList</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerDeviceStateDeviceListListener_PARAM New listener for the command
     */
    public static void setSkyControllerDeviceStateDeviceListListener (ARCommandSkyControllerDeviceStateDeviceListListener _ARCommandSkyControllerDeviceStateDeviceListListener_PARAM) {
        _ARCommandSkyControllerDeviceStateDeviceListListener = _ARCommandSkyControllerDeviceStateDeviceListListener_PARAM;
    }

    private static ARCommandSkyControllerDeviceStateConnexionChangedListener _ARCommandSkyControllerDeviceStateConnexionChangedListener = null;

    /**
     * Set the listener for the command <code>DeviceStateConnexionChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerDeviceStateConnexionChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerDeviceStateConnexionChangedListener (ARCommandSkyControllerDeviceStateConnexionChangedListener _ARCommandSkyControllerDeviceStateConnexionChangedListener_PARAM) {
        _ARCommandSkyControllerDeviceStateConnexionChangedListener = _ARCommandSkyControllerDeviceStateConnexionChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSettingsStateAllSettingsChangedListener _ARCommandSkyControllerSettingsStateAllSettingsChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateAllSettingsChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSettingsStateAllSettingsChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSettingsStateAllSettingsChangedListener (ARCommandSkyControllerSettingsStateAllSettingsChangedListener _ARCommandSkyControllerSettingsStateAllSettingsChangedListener_PARAM) {
        _ARCommandSkyControllerSettingsStateAllSettingsChangedListener = _ARCommandSkyControllerSettingsStateAllSettingsChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSettingsStateResetChangedListener _ARCommandSkyControllerSettingsStateResetChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateResetChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSettingsStateResetChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSettingsStateResetChangedListener (ARCommandSkyControllerSettingsStateResetChangedListener _ARCommandSkyControllerSettingsStateResetChangedListener_PARAM) {
        _ARCommandSkyControllerSettingsStateResetChangedListener = _ARCommandSkyControllerSettingsStateResetChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSettingsStateProductSerialChangedListener _ARCommandSkyControllerSettingsStateProductSerialChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductSerialChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSettingsStateProductSerialChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSettingsStateProductSerialChangedListener (ARCommandSkyControllerSettingsStateProductSerialChangedListener _ARCommandSkyControllerSettingsStateProductSerialChangedListener_PARAM) {
        _ARCommandSkyControllerSettingsStateProductSerialChangedListener = _ARCommandSkyControllerSettingsStateProductSerialChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSettingsStateProductVariantChangedListener _ARCommandSkyControllerSettingsStateProductVariantChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductVariantChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSettingsStateProductVariantChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSettingsStateProductVariantChangedListener (ARCommandSkyControllerSettingsStateProductVariantChangedListener _ARCommandSkyControllerSettingsStateProductVariantChangedListener_PARAM) {
        _ARCommandSkyControllerSettingsStateProductVariantChangedListener = _ARCommandSkyControllerSettingsStateProductVariantChangedListener_PARAM;
    }

    private static ARCommandSkyControllerCommonStateAllStatesChangedListener _ARCommandSkyControllerCommonStateAllStatesChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateAllStatesChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCommonStateAllStatesChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerCommonStateAllStatesChangedListener (ARCommandSkyControllerCommonStateAllStatesChangedListener _ARCommandSkyControllerCommonStateAllStatesChangedListener_PARAM) {
        _ARCommandSkyControllerCommonStateAllStatesChangedListener = _ARCommandSkyControllerCommonStateAllStatesChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSkyControllerStateBatteryChangedListener _ARCommandSkyControllerSkyControllerStateBatteryChangedListener = null;

    /**
     * Set the listener for the command <code>SkyControllerStateBatteryChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSkyControllerStateBatteryChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSkyControllerStateBatteryChangedListener (ARCommandSkyControllerSkyControllerStateBatteryChangedListener _ARCommandSkyControllerSkyControllerStateBatteryChangedListener_PARAM) {
        _ARCommandSkyControllerSkyControllerStateBatteryChangedListener = _ARCommandSkyControllerSkyControllerStateBatteryChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSkyControllerStateGpsFixChangedListener _ARCommandSkyControllerSkyControllerStateGpsFixChangedListener = null;

    /**
     * Set the listener for the command <code>SkyControllerStateGpsFixChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSkyControllerStateGpsFixChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSkyControllerStateGpsFixChangedListener (ARCommandSkyControllerSkyControllerStateGpsFixChangedListener _ARCommandSkyControllerSkyControllerStateGpsFixChangedListener_PARAM) {
        _ARCommandSkyControllerSkyControllerStateGpsFixChangedListener = _ARCommandSkyControllerSkyControllerStateGpsFixChangedListener_PARAM;
    }

    private static ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener _ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener = null;

    /**
     * Set the listener for the command <code>SkyControllerStateGpsPositionChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerSkyControllerStateGpsPositionChangedListener (ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener _ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener_PARAM) {
        _ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener = _ARCommandSkyControllerSkyControllerStateGpsPositionChangedListener_PARAM;
    }

    private static ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener _ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener = null;

    /**
     * Set the listener for the command <code>AccessPointSettingsStateAccessPointSSIDChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener (ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener _ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener_PARAM) {
        _ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener = _ARCommandSkyControllerAccessPointSettingsStateAccessPointSSIDChangedListener_PARAM;
    }

    private static ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener _ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener = null;

    /**
     * Set the listener for the command <code>AccessPointSettingsStateAccessPointChannelChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener (ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener _ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener_PARAM) {
        _ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener = _ARCommandSkyControllerAccessPointSettingsStateAccessPointChannelChangedListener_PARAM;
    }

    private static ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener _ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener = null;

    /**
     * Set the listener for the command <code>AccessPointSettingsStateWifiSelectionChanged</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener_PARAM New listener for the command
     */
    public static void setSkyControllerAccessPointSettingsStateWifiSelectionChangedListener (ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener _ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener_PARAM) {
        _ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener = _ARCommandSkyControllerAccessPointSettingsStateWifiSelectionChangedListener_PARAM;
    }

    private static ARCommandSkyControllerGamepadInfosStateGamepadControlListener _ARCommandSkyControllerGamepadInfosStateGamepadControlListener = null;

    /**
     * Set the listener for the command <code>GamepadInfosStateGamepadControl</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerGamepadInfosStateGamepadControlListener_PARAM New listener for the command
     */
    public static void setSkyControllerGamepadInfosStateGamepadControlListener (ARCommandSkyControllerGamepadInfosStateGamepadControlListener _ARCommandSkyControllerGamepadInfosStateGamepadControlListener_PARAM) {
        _ARCommandSkyControllerGamepadInfosStateGamepadControlListener = _ARCommandSkyControllerGamepadInfosStateGamepadControlListener_PARAM;
    }

    private static ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener _ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener = null;

    /**
     * Set the listener for the command <code>GamepadInfosStateAllGamepadControlsSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerGamepadInfosStateAllGamepadControlsSentListener (ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener _ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener_PARAM) {
        _ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener = _ARCommandSkyControllerGamepadInfosStateAllGamepadControlsSentListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener _ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsStateCurrentButtonMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsStateCurrentButtonMappingsListener (ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener _ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener = _ARCommandSkyControllerButtonMappingsStateCurrentButtonMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener _ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsStateAllCurrentButtonMappingsSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener (ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener _ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener = _ARCommandSkyControllerButtonMappingsStateAllCurrentButtonMappingsSentListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener _ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsStateAvailableButtonMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsStateAvailableButtonMappingsListener (ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener _ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener = _ARCommandSkyControllerButtonMappingsStateAvailableButtonMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener _ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener = null;

    /**
     * Set the listener for the command <code>ButtonMappingsStateAllAvailableButtonsMappingsSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener (ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener _ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener_PARAM) {
        _ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener = _ARCommandSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSentListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener _ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsStateCurrentAxisMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsStateCurrentAxisMappingsListener (ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener _ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener = _ARCommandSkyControllerAxisMappingsStateCurrentAxisMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener _ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsStateAllCurrentAxisMappingsSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener (ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener _ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener = _ARCommandSkyControllerAxisMappingsStateAllCurrentAxisMappingsSentListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener _ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsStateAvailableAxisMappings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsStateAvailableAxisMappingsListener (ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener _ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener = _ARCommandSkyControllerAxisMappingsStateAvailableAxisMappingsListener_PARAM;
    }

    private static ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener _ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener = null;

    /**
     * Set the listener for the command <code>AxisMappingsStateAllAvailableAxisMappingsSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener (ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener _ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener_PARAM) {
        _ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener = _ARCommandSkyControllerAxisMappingsStateAllAvailableAxisMappingsSentListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener _ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersStateCurrentAxisFilters</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersStateCurrentAxisFiltersListener (ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener _ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener = _ARCommandSkyControllerAxisFiltersStateCurrentAxisFiltersListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener _ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersStateAllCurrentFiltersSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersStateAllCurrentFiltersSentListener (ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener _ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener = _ARCommandSkyControllerAxisFiltersStateAllCurrentFiltersSentListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener _ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersStatePresetAxisFilters</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersStatePresetAxisFiltersListener (ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener _ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener = _ARCommandSkyControllerAxisFiltersStatePresetAxisFiltersListener_PARAM;
    }

    private static ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener _ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener = null;

    /**
     * Set the listener for the command <code>AxisFiltersStateAllPresetFiltersSent</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener_PARAM New listener for the command
     */
    public static void setSkyControllerAxisFiltersStateAllPresetFiltersSentListener (ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener _ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener_PARAM) {
        _ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener = _ARCommandSkyControllerAxisFiltersStateAllPresetFiltersSentListener_PARAM;
    }

    private static ARCommandSkyControllerCoPilotingStatePilotingSourceListener _ARCommandSkyControllerCoPilotingStatePilotingSourceListener = null;

    /**
     * Set the listener for the command <code>CoPilotingStatePilotingSource</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCoPilotingStatePilotingSourceListener_PARAM New listener for the command
     */
    public static void setSkyControllerCoPilotingStatePilotingSourceListener (ARCommandSkyControllerCoPilotingStatePilotingSourceListener _ARCommandSkyControllerCoPilotingStatePilotingSourceListener_PARAM) {
        _ARCommandSkyControllerCoPilotingStatePilotingSourceListener = _ARCommandSkyControllerCoPilotingStatePilotingSourceListener_PARAM;
    }

    private static ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener _ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener = null;

    /**
     * Set the listener for the command <code>CalibrationStateMagnetoCalibrationState</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener_PARAM New listener for the command
     */
    public static void setSkyControllerCalibrationStateMagnetoCalibrationStateListener (ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener _ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener_PARAM) {
        _ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener = _ARCommandSkyControllerCalibrationStateMagnetoCalibrationStateListener_PARAM;
    }

    private static ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener _ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener = null;

    /**
     * Set the listener for the command <code>CalibrationStateMagnetoCalibrationQualityUpdatesState</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener_PARAM New listener for the command
     */
    public static void setSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener (ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener _ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener_PARAM) {
        _ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener = _ARCommandSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesStateListener_PARAM;
    }

    private static ARCommandSkyControllerButtonEventsSettingsListener _ARCommandSkyControllerButtonEventsSettingsListener = null;

    /**
     * Set the listener for the command <code>ButtonEventsSettings</code> in feature <code>SkyController</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandSkyControllerButtonEventsSettingsListener_PARAM New listener for the command
     */
    public static void setSkyControllerButtonEventsSettingsListener (ARCommandSkyControllerButtonEventsSettingsListener _ARCommandSkyControllerButtonEventsSettingsListener_PARAM) {
        _ARCommandSkyControllerButtonEventsSettingsListener = _ARCommandSkyControllerButtonEventsSettingsListener_PARAM;
    }


    private static ARCommandCommonNetworkDisconnectListener _ARCommandCommonNetworkDisconnectListener = null;

    /**
     * Set the listener for the command <code>NetworkDisconnect</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonNetworkDisconnectListener_PARAM New listener for the command
     */
    public static void setCommonNetworkDisconnectListener (ARCommandCommonNetworkDisconnectListener _ARCommandCommonNetworkDisconnectListener_PARAM) {
        _ARCommandCommonNetworkDisconnectListener = _ARCommandCommonNetworkDisconnectListener_PARAM;
    }

    private static ARCommandCommonSettingsAllSettingsListener _ARCommandCommonSettingsAllSettingsListener = null;

    /**
     * Set the listener for the command <code>SettingsAllSettings</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsAllSettingsListener_PARAM New listener for the command
     */
    public static void setCommonSettingsAllSettingsListener (ARCommandCommonSettingsAllSettingsListener _ARCommandCommonSettingsAllSettingsListener_PARAM) {
        _ARCommandCommonSettingsAllSettingsListener = _ARCommandCommonSettingsAllSettingsListener_PARAM;
    }

    private static ARCommandCommonSettingsResetListener _ARCommandCommonSettingsResetListener = null;

    /**
     * Set the listener for the command <code>SettingsReset</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsResetListener_PARAM New listener for the command
     */
    public static void setCommonSettingsResetListener (ARCommandCommonSettingsResetListener _ARCommandCommonSettingsResetListener_PARAM) {
        _ARCommandCommonSettingsResetListener = _ARCommandCommonSettingsResetListener_PARAM;
    }

    private static ARCommandCommonSettingsProductNameListener _ARCommandCommonSettingsProductNameListener = null;

    /**
     * Set the listener for the command <code>SettingsProductName</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsProductNameListener_PARAM New listener for the command
     */
    public static void setCommonSettingsProductNameListener (ARCommandCommonSettingsProductNameListener _ARCommandCommonSettingsProductNameListener_PARAM) {
        _ARCommandCommonSettingsProductNameListener = _ARCommandCommonSettingsProductNameListener_PARAM;
    }

    private static ARCommandCommonSettingsCountryListener _ARCommandCommonSettingsCountryListener = null;

    /**
     * Set the listener for the command <code>SettingsCountry</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsCountryListener_PARAM New listener for the command
     */
    public static void setCommonSettingsCountryListener (ARCommandCommonSettingsCountryListener _ARCommandCommonSettingsCountryListener_PARAM) {
        _ARCommandCommonSettingsCountryListener = _ARCommandCommonSettingsCountryListener_PARAM;
    }

    private static ARCommandCommonSettingsAutoCountryListener _ARCommandCommonSettingsAutoCountryListener = null;

    /**
     * Set the listener for the command <code>SettingsAutoCountry</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsAutoCountryListener_PARAM New listener for the command
     */
    public static void setCommonSettingsAutoCountryListener (ARCommandCommonSettingsAutoCountryListener _ARCommandCommonSettingsAutoCountryListener_PARAM) {
        _ARCommandCommonSettingsAutoCountryListener = _ARCommandCommonSettingsAutoCountryListener_PARAM;
    }

    private static ARCommandCommonCommonAllStatesListener _ARCommandCommonCommonAllStatesListener = null;

    /**
     * Set the listener for the command <code>CommonAllStates</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonAllStatesListener_PARAM New listener for the command
     */
    public static void setCommonCommonAllStatesListener (ARCommandCommonCommonAllStatesListener _ARCommandCommonCommonAllStatesListener_PARAM) {
        _ARCommandCommonCommonAllStatesListener = _ARCommandCommonCommonAllStatesListener_PARAM;
    }

    private static ARCommandCommonCommonCurrentDateListener _ARCommandCommonCommonCurrentDateListener = null;

    /**
     * Set the listener for the command <code>CommonCurrentDate</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonCurrentDateListener_PARAM New listener for the command
     */
    public static void setCommonCommonCurrentDateListener (ARCommandCommonCommonCurrentDateListener _ARCommandCommonCommonCurrentDateListener_PARAM) {
        _ARCommandCommonCommonCurrentDateListener = _ARCommandCommonCommonCurrentDateListener_PARAM;
    }

    private static ARCommandCommonCommonCurrentTimeListener _ARCommandCommonCommonCurrentTimeListener = null;

    /**
     * Set the listener for the command <code>CommonCurrentTime</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonCurrentTimeListener_PARAM New listener for the command
     */
    public static void setCommonCommonCurrentTimeListener (ARCommandCommonCommonCurrentTimeListener _ARCommandCommonCommonCurrentTimeListener_PARAM) {
        _ARCommandCommonCommonCurrentTimeListener = _ARCommandCommonCommonCurrentTimeListener_PARAM;
    }

    private static ARCommandCommonCommonRebootListener _ARCommandCommonCommonRebootListener = null;

    /**
     * Set the listener for the command <code>CommonReboot</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonRebootListener_PARAM New listener for the command
     */
    public static void setCommonCommonRebootListener (ARCommandCommonCommonRebootListener _ARCommandCommonCommonRebootListener_PARAM) {
        _ARCommandCommonCommonRebootListener = _ARCommandCommonCommonRebootListener_PARAM;
    }

    private static ARCommandCommonOverHeatSwitchOffListener _ARCommandCommonOverHeatSwitchOffListener = null;

    /**
     * Set the listener for the command <code>OverHeatSwitchOff</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonOverHeatSwitchOffListener_PARAM New listener for the command
     */
    public static void setCommonOverHeatSwitchOffListener (ARCommandCommonOverHeatSwitchOffListener _ARCommandCommonOverHeatSwitchOffListener_PARAM) {
        _ARCommandCommonOverHeatSwitchOffListener = _ARCommandCommonOverHeatSwitchOffListener_PARAM;
    }

    private static ARCommandCommonOverHeatVentilateListener _ARCommandCommonOverHeatVentilateListener = null;

    /**
     * Set the listener for the command <code>OverHeatVentilate</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonOverHeatVentilateListener_PARAM New listener for the command
     */
    public static void setCommonOverHeatVentilateListener (ARCommandCommonOverHeatVentilateListener _ARCommandCommonOverHeatVentilateListener_PARAM) {
        _ARCommandCommonOverHeatVentilateListener = _ARCommandCommonOverHeatVentilateListener_PARAM;
    }

    private static ARCommandCommonControllerIsPilotingListener _ARCommandCommonControllerIsPilotingListener = null;

    /**
     * Set the listener for the command <code>ControllerIsPiloting</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonControllerIsPilotingListener_PARAM New listener for the command
     */
    public static void setCommonControllerIsPilotingListener (ARCommandCommonControllerIsPilotingListener _ARCommandCommonControllerIsPilotingListener_PARAM) {
        _ARCommandCommonControllerIsPilotingListener = _ARCommandCommonControllerIsPilotingListener_PARAM;
    }

    private static ARCommandCommonWifiSettingsOutdoorSettingListener _ARCommandCommonWifiSettingsOutdoorSettingListener = null;

    /**
     * Set the listener for the command <code>WifiSettingsOutdoorSetting</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonWifiSettingsOutdoorSettingListener_PARAM New listener for the command
     */
    public static void setCommonWifiSettingsOutdoorSettingListener (ARCommandCommonWifiSettingsOutdoorSettingListener _ARCommandCommonWifiSettingsOutdoorSettingListener_PARAM) {
        _ARCommandCommonWifiSettingsOutdoorSettingListener = _ARCommandCommonWifiSettingsOutdoorSettingListener_PARAM;
    }

    private static ARCommandCommonMavlinkStartListener _ARCommandCommonMavlinkStartListener = null;

    /**
     * Set the listener for the command <code>MavlinkStart</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonMavlinkStartListener_PARAM New listener for the command
     */
    public static void setCommonMavlinkStartListener (ARCommandCommonMavlinkStartListener _ARCommandCommonMavlinkStartListener_PARAM) {
        _ARCommandCommonMavlinkStartListener = _ARCommandCommonMavlinkStartListener_PARAM;
    }

    private static ARCommandCommonMavlinkPauseListener _ARCommandCommonMavlinkPauseListener = null;

    /**
     * Set the listener for the command <code>MavlinkPause</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonMavlinkPauseListener_PARAM New listener for the command
     */
    public static void setCommonMavlinkPauseListener (ARCommandCommonMavlinkPauseListener _ARCommandCommonMavlinkPauseListener_PARAM) {
        _ARCommandCommonMavlinkPauseListener = _ARCommandCommonMavlinkPauseListener_PARAM;
    }

    private static ARCommandCommonMavlinkStopListener _ARCommandCommonMavlinkStopListener = null;

    /**
     * Set the listener for the command <code>MavlinkStop</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonMavlinkStopListener_PARAM New listener for the command
     */
    public static void setCommonMavlinkStopListener (ARCommandCommonMavlinkStopListener _ARCommandCommonMavlinkStopListener_PARAM) {
        _ARCommandCommonMavlinkStopListener = _ARCommandCommonMavlinkStopListener_PARAM;
    }

    private static ARCommandCommonCalibrationMagnetoCalibrationListener _ARCommandCommonCalibrationMagnetoCalibrationListener = null;

    /**
     * Set the listener for the command <code>CalibrationMagnetoCalibration</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCalibrationMagnetoCalibrationListener_PARAM New listener for the command
     */
    public static void setCommonCalibrationMagnetoCalibrationListener (ARCommandCommonCalibrationMagnetoCalibrationListener _ARCommandCommonCalibrationMagnetoCalibrationListener_PARAM) {
        _ARCommandCommonCalibrationMagnetoCalibrationListener = _ARCommandCommonCalibrationMagnetoCalibrationListener_PARAM;
    }

    private static ARCommandCommonGPSControllerPositionForRunListener _ARCommandCommonGPSControllerPositionForRunListener = null;

    /**
     * Set the listener for the command <code>GPSControllerPositionForRun</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonGPSControllerPositionForRunListener_PARAM New listener for the command
     */
    public static void setCommonGPSControllerPositionForRunListener (ARCommandCommonGPSControllerPositionForRunListener _ARCommandCommonGPSControllerPositionForRunListener_PARAM) {
        _ARCommandCommonGPSControllerPositionForRunListener = _ARCommandCommonGPSControllerPositionForRunListener_PARAM;
    }

    private static ARCommandCommonAudioControllerReadyForStreamingListener _ARCommandCommonAudioControllerReadyForStreamingListener = null;

    /**
     * Set the listener for the command <code>AudioControllerReadyForStreaming</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAudioControllerReadyForStreamingListener_PARAM New listener for the command
     */
    public static void setCommonAudioControllerReadyForStreamingListener (ARCommandCommonAudioControllerReadyForStreamingListener _ARCommandCommonAudioControllerReadyForStreamingListener_PARAM) {
        _ARCommandCommonAudioControllerReadyForStreamingListener = _ARCommandCommonAudioControllerReadyForStreamingListener_PARAM;
    }

    private static ARCommandCommonHeadlightsIntensityListener _ARCommandCommonHeadlightsIntensityListener = null;

    /**
     * Set the listener for the command <code>HeadlightsIntensity</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonHeadlightsIntensityListener_PARAM New listener for the command
     */
    public static void setCommonHeadlightsIntensityListener (ARCommandCommonHeadlightsIntensityListener _ARCommandCommonHeadlightsIntensityListener_PARAM) {
        _ARCommandCommonHeadlightsIntensityListener = _ARCommandCommonHeadlightsIntensityListener_PARAM;
    }

    private static ARCommandCommonAnimationsStartAnimationListener _ARCommandCommonAnimationsStartAnimationListener = null;

    /**
     * Set the listener for the command <code>AnimationsStartAnimation</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAnimationsStartAnimationListener_PARAM New listener for the command
     */
    public static void setCommonAnimationsStartAnimationListener (ARCommandCommonAnimationsStartAnimationListener _ARCommandCommonAnimationsStartAnimationListener_PARAM) {
        _ARCommandCommonAnimationsStartAnimationListener = _ARCommandCommonAnimationsStartAnimationListener_PARAM;
    }

    private static ARCommandCommonAnimationsStopAnimationListener _ARCommandCommonAnimationsStopAnimationListener = null;

    /**
     * Set the listener for the command <code>AnimationsStopAnimation</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAnimationsStopAnimationListener_PARAM New listener for the command
     */
    public static void setCommonAnimationsStopAnimationListener (ARCommandCommonAnimationsStopAnimationListener _ARCommandCommonAnimationsStopAnimationListener_PARAM) {
        _ARCommandCommonAnimationsStopAnimationListener = _ARCommandCommonAnimationsStopAnimationListener_PARAM;
    }

    private static ARCommandCommonAnimationsStopAllAnimationsListener _ARCommandCommonAnimationsStopAllAnimationsListener = null;

    /**
     * Set the listener for the command <code>AnimationsStopAllAnimations</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAnimationsStopAllAnimationsListener_PARAM New listener for the command
     */
    public static void setCommonAnimationsStopAllAnimationsListener (ARCommandCommonAnimationsStopAllAnimationsListener _ARCommandCommonAnimationsStopAllAnimationsListener_PARAM) {
        _ARCommandCommonAnimationsStopAllAnimationsListener = _ARCommandCommonAnimationsStopAllAnimationsListener_PARAM;
    }

    private static ARCommandCommonAccessoryConfigListener _ARCommandCommonAccessoryConfigListener = null;

    /**
     * Set the listener for the command <code>AccessoryConfig</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAccessoryConfigListener_PARAM New listener for the command
     */
    public static void setCommonAccessoryConfigListener (ARCommandCommonAccessoryConfigListener _ARCommandCommonAccessoryConfigListener_PARAM) {
        _ARCommandCommonAccessoryConfigListener = _ARCommandCommonAccessoryConfigListener_PARAM;
    }

    private static ARCommandCommonChargerSetMaxChargeRateListener _ARCommandCommonChargerSetMaxChargeRateListener = null;

    /**
     * Set the listener for the command <code>ChargerSetMaxChargeRate</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonChargerSetMaxChargeRateListener_PARAM New listener for the command
     */
    public static void setCommonChargerSetMaxChargeRateListener (ARCommandCommonChargerSetMaxChargeRateListener _ARCommandCommonChargerSetMaxChargeRateListener_PARAM) {
        _ARCommandCommonChargerSetMaxChargeRateListener = _ARCommandCommonChargerSetMaxChargeRateListener_PARAM;
    }

    private static ARCommandCommonNetworkEventDisconnectionListener _ARCommandCommonNetworkEventDisconnectionListener = null;

    /**
     * Set the listener for the command <code>NetworkEventDisconnection</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonNetworkEventDisconnectionListener_PARAM New listener for the command
     */
    public static void setCommonNetworkEventDisconnectionListener (ARCommandCommonNetworkEventDisconnectionListener _ARCommandCommonNetworkEventDisconnectionListener_PARAM) {
        _ARCommandCommonNetworkEventDisconnectionListener = _ARCommandCommonNetworkEventDisconnectionListener_PARAM;
    }

    private static ARCommandCommonSettingsStateAllSettingsChangedListener _ARCommandCommonSettingsStateAllSettingsChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateAllSettingsChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateAllSettingsChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateAllSettingsChangedListener (ARCommandCommonSettingsStateAllSettingsChangedListener _ARCommandCommonSettingsStateAllSettingsChangedListener_PARAM) {
        _ARCommandCommonSettingsStateAllSettingsChangedListener = _ARCommandCommonSettingsStateAllSettingsChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateResetChangedListener _ARCommandCommonSettingsStateResetChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateResetChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateResetChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateResetChangedListener (ARCommandCommonSettingsStateResetChangedListener _ARCommandCommonSettingsStateResetChangedListener_PARAM) {
        _ARCommandCommonSettingsStateResetChangedListener = _ARCommandCommonSettingsStateResetChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateProductNameChangedListener _ARCommandCommonSettingsStateProductNameChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductNameChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateProductNameChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateProductNameChangedListener (ARCommandCommonSettingsStateProductNameChangedListener _ARCommandCommonSettingsStateProductNameChangedListener_PARAM) {
        _ARCommandCommonSettingsStateProductNameChangedListener = _ARCommandCommonSettingsStateProductNameChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateProductVersionChangedListener _ARCommandCommonSettingsStateProductVersionChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductVersionChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateProductVersionChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateProductVersionChangedListener (ARCommandCommonSettingsStateProductVersionChangedListener _ARCommandCommonSettingsStateProductVersionChangedListener_PARAM) {
        _ARCommandCommonSettingsStateProductVersionChangedListener = _ARCommandCommonSettingsStateProductVersionChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateProductSerialHighChangedListener _ARCommandCommonSettingsStateProductSerialHighChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductSerialHighChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateProductSerialHighChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateProductSerialHighChangedListener (ARCommandCommonSettingsStateProductSerialHighChangedListener _ARCommandCommonSettingsStateProductSerialHighChangedListener_PARAM) {
        _ARCommandCommonSettingsStateProductSerialHighChangedListener = _ARCommandCommonSettingsStateProductSerialHighChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateProductSerialLowChangedListener _ARCommandCommonSettingsStateProductSerialLowChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateProductSerialLowChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateProductSerialLowChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateProductSerialLowChangedListener (ARCommandCommonSettingsStateProductSerialLowChangedListener _ARCommandCommonSettingsStateProductSerialLowChangedListener_PARAM) {
        _ARCommandCommonSettingsStateProductSerialLowChangedListener = _ARCommandCommonSettingsStateProductSerialLowChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateCountryChangedListener _ARCommandCommonSettingsStateCountryChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateCountryChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateCountryChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateCountryChangedListener (ARCommandCommonSettingsStateCountryChangedListener _ARCommandCommonSettingsStateCountryChangedListener_PARAM) {
        _ARCommandCommonSettingsStateCountryChangedListener = _ARCommandCommonSettingsStateCountryChangedListener_PARAM;
    }

    private static ARCommandCommonSettingsStateAutoCountryChangedListener _ARCommandCommonSettingsStateAutoCountryChangedListener = null;

    /**
     * Set the listener for the command <code>SettingsStateAutoCountryChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonSettingsStateAutoCountryChangedListener_PARAM New listener for the command
     */
    public static void setCommonSettingsStateAutoCountryChangedListener (ARCommandCommonSettingsStateAutoCountryChangedListener _ARCommandCommonSettingsStateAutoCountryChangedListener_PARAM) {
        _ARCommandCommonSettingsStateAutoCountryChangedListener = _ARCommandCommonSettingsStateAutoCountryChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateAllStatesChangedListener _ARCommandCommonCommonStateAllStatesChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateAllStatesChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateAllStatesChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateAllStatesChangedListener (ARCommandCommonCommonStateAllStatesChangedListener _ARCommandCommonCommonStateAllStatesChangedListener_PARAM) {
        _ARCommandCommonCommonStateAllStatesChangedListener = _ARCommandCommonCommonStateAllStatesChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateBatteryStateChangedListener _ARCommandCommonCommonStateBatteryStateChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateBatteryStateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateBatteryStateChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateBatteryStateChangedListener (ARCommandCommonCommonStateBatteryStateChangedListener _ARCommandCommonCommonStateBatteryStateChangedListener_PARAM) {
        _ARCommandCommonCommonStateBatteryStateChangedListener = _ARCommandCommonCommonStateBatteryStateChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateMassStorageStateListChangedListener _ARCommandCommonCommonStateMassStorageStateListChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateMassStorageStateListChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateMassStorageStateListChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateMassStorageStateListChangedListener (ARCommandCommonCommonStateMassStorageStateListChangedListener _ARCommandCommonCommonStateMassStorageStateListChangedListener_PARAM) {
        _ARCommandCommonCommonStateMassStorageStateListChangedListener = _ARCommandCommonCommonStateMassStorageStateListChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateMassStorageInfoStateListChangedListener _ARCommandCommonCommonStateMassStorageInfoStateListChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateMassStorageInfoStateListChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateMassStorageInfoStateListChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateMassStorageInfoStateListChangedListener (ARCommandCommonCommonStateMassStorageInfoStateListChangedListener _ARCommandCommonCommonStateMassStorageInfoStateListChangedListener_PARAM) {
        _ARCommandCommonCommonStateMassStorageInfoStateListChangedListener = _ARCommandCommonCommonStateMassStorageInfoStateListChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateCurrentDateChangedListener _ARCommandCommonCommonStateCurrentDateChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateCurrentDateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateCurrentDateChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateCurrentDateChangedListener (ARCommandCommonCommonStateCurrentDateChangedListener _ARCommandCommonCommonStateCurrentDateChangedListener_PARAM) {
        _ARCommandCommonCommonStateCurrentDateChangedListener = _ARCommandCommonCommonStateCurrentDateChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateCurrentTimeChangedListener _ARCommandCommonCommonStateCurrentTimeChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateCurrentTimeChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateCurrentTimeChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateCurrentTimeChangedListener (ARCommandCommonCommonStateCurrentTimeChangedListener _ARCommandCommonCommonStateCurrentTimeChangedListener_PARAM) {
        _ARCommandCommonCommonStateCurrentTimeChangedListener = _ARCommandCommonCommonStateCurrentTimeChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener _ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateMassStorageInfoRemainingListChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateMassStorageInfoRemainingListChangedListener (ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener _ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener_PARAM) {
        _ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener = _ARCommandCommonCommonStateMassStorageInfoRemainingListChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateWifiSignalChangedListener _ARCommandCommonCommonStateWifiSignalChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateWifiSignalChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateWifiSignalChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateWifiSignalChangedListener (ARCommandCommonCommonStateWifiSignalChangedListener _ARCommandCommonCommonStateWifiSignalChangedListener_PARAM) {
        _ARCommandCommonCommonStateWifiSignalChangedListener = _ARCommandCommonCommonStateWifiSignalChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateSensorsStatesListChangedListener _ARCommandCommonCommonStateSensorsStatesListChangedListener = null;

    /**
     * Set the listener for the command <code>CommonStateSensorsStatesListChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateSensorsStatesListChangedListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateSensorsStatesListChangedListener (ARCommandCommonCommonStateSensorsStatesListChangedListener _ARCommandCommonCommonStateSensorsStatesListChangedListener_PARAM) {
        _ARCommandCommonCommonStateSensorsStatesListChangedListener = _ARCommandCommonCommonStateSensorsStatesListChangedListener_PARAM;
    }

    private static ARCommandCommonCommonStateProductModelListener _ARCommandCommonCommonStateProductModelListener = null;

    /**
     * Set the listener for the command <code>CommonStateProductModel</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateProductModelListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateProductModelListener (ARCommandCommonCommonStateProductModelListener _ARCommandCommonCommonStateProductModelListener_PARAM) {
        _ARCommandCommonCommonStateProductModelListener = _ARCommandCommonCommonStateProductModelListener_PARAM;
    }

    private static ARCommandCommonCommonStateCountryListKnownListener _ARCommandCommonCommonStateCountryListKnownListener = null;

    /**
     * Set the listener for the command <code>CommonStateCountryListKnown</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCommonStateCountryListKnownListener_PARAM New listener for the command
     */
    public static void setCommonCommonStateCountryListKnownListener (ARCommandCommonCommonStateCountryListKnownListener _ARCommandCommonCommonStateCountryListKnownListener_PARAM) {
        _ARCommandCommonCommonStateCountryListKnownListener = _ARCommandCommonCommonStateCountryListKnownListener_PARAM;
    }

    private static ARCommandCommonOverHeatStateOverHeatChangedListener _ARCommandCommonOverHeatStateOverHeatChangedListener = null;

    /**
     * Set the listener for the command <code>OverHeatStateOverHeatChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonOverHeatStateOverHeatChangedListener_PARAM New listener for the command
     */
    public static void setCommonOverHeatStateOverHeatChangedListener (ARCommandCommonOverHeatStateOverHeatChangedListener _ARCommandCommonOverHeatStateOverHeatChangedListener_PARAM) {
        _ARCommandCommonOverHeatStateOverHeatChangedListener = _ARCommandCommonOverHeatStateOverHeatChangedListener_PARAM;
    }

    private static ARCommandCommonOverHeatStateOverHeatRegulationChangedListener _ARCommandCommonOverHeatStateOverHeatRegulationChangedListener = null;

    /**
     * Set the listener for the command <code>OverHeatStateOverHeatRegulationChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonOverHeatStateOverHeatRegulationChangedListener_PARAM New listener for the command
     */
    public static void setCommonOverHeatStateOverHeatRegulationChangedListener (ARCommandCommonOverHeatStateOverHeatRegulationChangedListener _ARCommandCommonOverHeatStateOverHeatRegulationChangedListener_PARAM) {
        _ARCommandCommonOverHeatStateOverHeatRegulationChangedListener = _ARCommandCommonOverHeatStateOverHeatRegulationChangedListener_PARAM;
    }

    private static ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener _ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener = null;

    /**
     * Set the listener for the command <code>WifiSettingsStateOutdoorSettingsChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener_PARAM New listener for the command
     */
    public static void setCommonWifiSettingsStateOutdoorSettingsChangedListener (ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener _ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener_PARAM) {
        _ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener = _ARCommandCommonWifiSettingsStateOutdoorSettingsChangedListener_PARAM;
    }

    private static ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener _ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener = null;

    /**
     * Set the listener for the command <code>MavlinkStateMavlinkFilePlayingStateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener_PARAM New listener for the command
     */
    public static void setCommonMavlinkStateMavlinkFilePlayingStateChangedListener (ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener _ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener_PARAM) {
        _ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener = _ARCommandCommonMavlinkStateMavlinkFilePlayingStateChangedListener_PARAM;
    }

    private static ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener _ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener = null;

    /**
     * Set the listener for the command <code>MavlinkStateMavlinkPlayErrorStateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener_PARAM New listener for the command
     */
    public static void setCommonMavlinkStateMavlinkPlayErrorStateChangedListener (ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener _ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener_PARAM) {
        _ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener = _ARCommandCommonMavlinkStateMavlinkPlayErrorStateChangedListener_PARAM;
    }

    private static ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener _ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener = null;

    /**
     * Set the listener for the command <code>CalibrationStateMagnetoCalibrationStateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener_PARAM New listener for the command
     */
    public static void setCommonCalibrationStateMagnetoCalibrationStateChangedListener (ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener _ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener_PARAM) {
        _ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener = _ARCommandCommonCalibrationStateMagnetoCalibrationStateChangedListener_PARAM;
    }

    private static ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener _ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener = null;

    /**
     * Set the listener for the command <code>CalibrationStateMagnetoCalibrationRequiredState</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener_PARAM New listener for the command
     */
    public static void setCommonCalibrationStateMagnetoCalibrationRequiredStateListener (ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener _ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener_PARAM) {
        _ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener = _ARCommandCommonCalibrationStateMagnetoCalibrationRequiredStateListener_PARAM;
    }

    private static ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener _ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener = null;

    /**
     * Set the listener for the command <code>CalibrationStateMagnetoCalibrationAxisToCalibrateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener_PARAM New listener for the command
     */
    public static void setCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener (ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener _ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener_PARAM) {
        _ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener = _ARCommandCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChangedListener_PARAM;
    }

    private static ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener _ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener = null;

    /**
     * Set the listener for the command <code>CalibrationStateMagnetoCalibrationStartedChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener_PARAM New listener for the command
     */
    public static void setCommonCalibrationStateMagnetoCalibrationStartedChangedListener (ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener _ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener_PARAM) {
        _ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener = _ARCommandCommonCalibrationStateMagnetoCalibrationStartedChangedListener_PARAM;
    }

    private static ARCommandCommonCameraSettingsStateCameraSettingsChangedListener _ARCommandCommonCameraSettingsStateCameraSettingsChangedListener = null;

    /**
     * Set the listener for the command <code>CameraSettingsStateCameraSettingsChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonCameraSettingsStateCameraSettingsChangedListener_PARAM New listener for the command
     */
    public static void setCommonCameraSettingsStateCameraSettingsChangedListener (ARCommandCommonCameraSettingsStateCameraSettingsChangedListener _ARCommandCommonCameraSettingsStateCameraSettingsChangedListener_PARAM) {
        _ARCommandCommonCameraSettingsStateCameraSettingsChangedListener = _ARCommandCommonCameraSettingsStateCameraSettingsChangedListener_PARAM;
    }

    private static ARCommandCommonFlightPlanStateAvailabilityStateChangedListener _ARCommandCommonFlightPlanStateAvailabilityStateChangedListener = null;

    /**
     * Set the listener for the command <code>FlightPlanStateAvailabilityStateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonFlightPlanStateAvailabilityStateChangedListener_PARAM New listener for the command
     */
    public static void setCommonFlightPlanStateAvailabilityStateChangedListener (ARCommandCommonFlightPlanStateAvailabilityStateChangedListener _ARCommandCommonFlightPlanStateAvailabilityStateChangedListener_PARAM) {
        _ARCommandCommonFlightPlanStateAvailabilityStateChangedListener = _ARCommandCommonFlightPlanStateAvailabilityStateChangedListener_PARAM;
    }

    private static ARCommandCommonFlightPlanStateComponentStateListChangedListener _ARCommandCommonFlightPlanStateComponentStateListChangedListener = null;

    /**
     * Set the listener for the command <code>FlightPlanStateComponentStateListChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonFlightPlanStateComponentStateListChangedListener_PARAM New listener for the command
     */
    public static void setCommonFlightPlanStateComponentStateListChangedListener (ARCommandCommonFlightPlanStateComponentStateListChangedListener _ARCommandCommonFlightPlanStateComponentStateListChangedListener_PARAM) {
        _ARCommandCommonFlightPlanStateComponentStateListChangedListener = _ARCommandCommonFlightPlanStateComponentStateListChangedListener_PARAM;
    }

    private static ARCommandCommonFlightPlanEventStartingErrorEventListener _ARCommandCommonFlightPlanEventStartingErrorEventListener = null;

    /**
     * Set the listener for the command <code>FlightPlanEventStartingErrorEvent</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonFlightPlanEventStartingErrorEventListener_PARAM New listener for the command
     */
    public static void setCommonFlightPlanEventStartingErrorEventListener (ARCommandCommonFlightPlanEventStartingErrorEventListener _ARCommandCommonFlightPlanEventStartingErrorEventListener_PARAM) {
        _ARCommandCommonFlightPlanEventStartingErrorEventListener = _ARCommandCommonFlightPlanEventStartingErrorEventListener_PARAM;
    }

    private static ARCommandCommonFlightPlanEventSpeedBridleEventListener _ARCommandCommonFlightPlanEventSpeedBridleEventListener = null;

    /**
     * Set the listener for the command <code>FlightPlanEventSpeedBridleEvent</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonFlightPlanEventSpeedBridleEventListener_PARAM New listener for the command
     */
    public static void setCommonFlightPlanEventSpeedBridleEventListener (ARCommandCommonFlightPlanEventSpeedBridleEventListener _ARCommandCommonFlightPlanEventSpeedBridleEventListener_PARAM) {
        _ARCommandCommonFlightPlanEventSpeedBridleEventListener = _ARCommandCommonFlightPlanEventSpeedBridleEventListener_PARAM;
    }

    private static ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener _ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener = null;

    /**
     * Set the listener for the command <code>ARLibsVersionsStateControllerLibARCommandsVersion</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener_PARAM New listener for the command
     */
    public static void setCommonARLibsVersionsStateControllerLibARCommandsVersionListener (ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener _ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener_PARAM) {
        _ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener = _ARCommandCommonARLibsVersionsStateControllerLibARCommandsVersionListener_PARAM;
    }

    private static ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener _ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener = null;

    /**
     * Set the listener for the command <code>ARLibsVersionsStateSkyControllerLibARCommandsVersion</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener_PARAM New listener for the command
     */
    public static void setCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener (ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener _ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener_PARAM) {
        _ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener = _ARCommandCommonARLibsVersionsStateSkyControllerLibARCommandsVersionListener_PARAM;
    }

    private static ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener _ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener = null;

    /**
     * Set the listener for the command <code>ARLibsVersionsStateDeviceLibARCommandsVersion</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener_PARAM New listener for the command
     */
    public static void setCommonARLibsVersionsStateDeviceLibARCommandsVersionListener (ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener _ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener_PARAM) {
        _ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener = _ARCommandCommonARLibsVersionsStateDeviceLibARCommandsVersionListener_PARAM;
    }

    private static ARCommandCommonAudioStateAudioStreamingRunningListener _ARCommandCommonAudioStateAudioStreamingRunningListener = null;

    /**
     * Set the listener for the command <code>AudioStateAudioStreamingRunning</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAudioStateAudioStreamingRunningListener_PARAM New listener for the command
     */
    public static void setCommonAudioStateAudioStreamingRunningListener (ARCommandCommonAudioStateAudioStreamingRunningListener _ARCommandCommonAudioStateAudioStreamingRunningListener_PARAM) {
        _ARCommandCommonAudioStateAudioStreamingRunningListener = _ARCommandCommonAudioStateAudioStreamingRunningListener_PARAM;
    }

    private static ARCommandCommonHeadlightsStateIntensityChangedListener _ARCommandCommonHeadlightsStateIntensityChangedListener = null;

    /**
     * Set the listener for the command <code>HeadlightsStateIntensityChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonHeadlightsStateIntensityChangedListener_PARAM New listener for the command
     */
    public static void setCommonHeadlightsStateIntensityChangedListener (ARCommandCommonHeadlightsStateIntensityChangedListener _ARCommandCommonHeadlightsStateIntensityChangedListener_PARAM) {
        _ARCommandCommonHeadlightsStateIntensityChangedListener = _ARCommandCommonHeadlightsStateIntensityChangedListener_PARAM;
    }

    private static ARCommandCommonAnimationsStateListListener _ARCommandCommonAnimationsStateListListener = null;

    /**
     * Set the listener for the command <code>AnimationsStateList</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAnimationsStateListListener_PARAM New listener for the command
     */
    public static void setCommonAnimationsStateListListener (ARCommandCommonAnimationsStateListListener _ARCommandCommonAnimationsStateListListener_PARAM) {
        _ARCommandCommonAnimationsStateListListener = _ARCommandCommonAnimationsStateListListener_PARAM;
    }

    private static ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener _ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener = null;

    /**
     * Set the listener for the command <code>AccessoryStateSupportedAccessoriesListChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener_PARAM New listener for the command
     */
    public static void setCommonAccessoryStateSupportedAccessoriesListChangedListener (ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener _ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener_PARAM) {
        _ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener = _ARCommandCommonAccessoryStateSupportedAccessoriesListChangedListener_PARAM;
    }

    private static ARCommandCommonAccessoryStateAccessoryConfigChangedListener _ARCommandCommonAccessoryStateAccessoryConfigChangedListener = null;

    /**
     * Set the listener for the command <code>AccessoryStateAccessoryConfigChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAccessoryStateAccessoryConfigChangedListener_PARAM New listener for the command
     */
    public static void setCommonAccessoryStateAccessoryConfigChangedListener (ARCommandCommonAccessoryStateAccessoryConfigChangedListener _ARCommandCommonAccessoryStateAccessoryConfigChangedListener_PARAM) {
        _ARCommandCommonAccessoryStateAccessoryConfigChangedListener = _ARCommandCommonAccessoryStateAccessoryConfigChangedListener_PARAM;
    }

    private static ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener _ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener = null;

    /**
     * Set the listener for the command <code>AccessoryStateAccessoryConfigModificationEnabled</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener_PARAM New listener for the command
     */
    public static void setCommonAccessoryStateAccessoryConfigModificationEnabledListener (ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener _ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener_PARAM) {
        _ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener = _ARCommandCommonAccessoryStateAccessoryConfigModificationEnabledListener_PARAM;
    }

    private static ARCommandCommonChargerStateMaxChargeRateChangedListener _ARCommandCommonChargerStateMaxChargeRateChangedListener = null;

    /**
     * Set the listener for the command <code>ChargerStateMaxChargeRateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonChargerStateMaxChargeRateChangedListener_PARAM New listener for the command
     */
    public static void setCommonChargerStateMaxChargeRateChangedListener (ARCommandCommonChargerStateMaxChargeRateChangedListener _ARCommandCommonChargerStateMaxChargeRateChangedListener_PARAM) {
        _ARCommandCommonChargerStateMaxChargeRateChangedListener = _ARCommandCommonChargerStateMaxChargeRateChangedListener_PARAM;
    }

    private static ARCommandCommonChargerStateCurrentChargeStateChangedListener _ARCommandCommonChargerStateCurrentChargeStateChangedListener = null;

    /**
     * Set the listener for the command <code>ChargerStateCurrentChargeStateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonChargerStateCurrentChargeStateChangedListener_PARAM New listener for the command
     */
    public static void setCommonChargerStateCurrentChargeStateChangedListener (ARCommandCommonChargerStateCurrentChargeStateChangedListener _ARCommandCommonChargerStateCurrentChargeStateChangedListener_PARAM) {
        _ARCommandCommonChargerStateCurrentChargeStateChangedListener = _ARCommandCommonChargerStateCurrentChargeStateChangedListener_PARAM;
    }

    private static ARCommandCommonChargerStateLastChargeRateChangedListener _ARCommandCommonChargerStateLastChargeRateChangedListener = null;

    /**
     * Set the listener for the command <code>ChargerStateLastChargeRateChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonChargerStateLastChargeRateChangedListener_PARAM New listener for the command
     */
    public static void setCommonChargerStateLastChargeRateChangedListener (ARCommandCommonChargerStateLastChargeRateChangedListener _ARCommandCommonChargerStateLastChargeRateChangedListener_PARAM) {
        _ARCommandCommonChargerStateLastChargeRateChangedListener = _ARCommandCommonChargerStateLastChargeRateChangedListener_PARAM;
    }

    private static ARCommandCommonChargerStateChargingInfoListener _ARCommandCommonChargerStateChargingInfoListener = null;

    /**
     * Set the listener for the command <code>ChargerStateChargingInfo</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonChargerStateChargingInfoListener_PARAM New listener for the command
     */
    public static void setCommonChargerStateChargingInfoListener (ARCommandCommonChargerStateChargingInfoListener _ARCommandCommonChargerStateChargingInfoListener_PARAM) {
        _ARCommandCommonChargerStateChargingInfoListener = _ARCommandCommonChargerStateChargingInfoListener_PARAM;
    }

    private static ARCommandCommonRunStateRunIdChangedListener _ARCommandCommonRunStateRunIdChangedListener = null;

    /**
     * Set the listener for the command <code>RunStateRunIdChanged</code> in feature <code>Common</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonRunStateRunIdChangedListener_PARAM New listener for the command
     */
    public static void setCommonRunStateRunIdChangedListener (ARCommandCommonRunStateRunIdChangedListener _ARCommandCommonRunStateRunIdChangedListener_PARAM) {
        _ARCommandCommonRunStateRunIdChangedListener = _ARCommandCommonRunStateRunIdChangedListener_PARAM;
    }


    private static ARCommandCommonDebugStatsSendPacketListener _ARCommandCommonDebugStatsSendPacketListener = null;

    /**
     * Set the listener for the command <code>StatsSendPacket</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugStatsSendPacketListener_PARAM New listener for the command
     */
    public static void setCommonDebugStatsSendPacketListener (ARCommandCommonDebugStatsSendPacketListener _ARCommandCommonDebugStatsSendPacketListener_PARAM) {
        _ARCommandCommonDebugStatsSendPacketListener = _ARCommandCommonDebugStatsSendPacketListener_PARAM;
    }

    private static ARCommandCommonDebugStatsStartSendingPacketFromDroneListener _ARCommandCommonDebugStatsStartSendingPacketFromDroneListener = null;

    /**
     * Set the listener for the command <code>StatsStartSendingPacketFromDrone</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugStatsStartSendingPacketFromDroneListener_PARAM New listener for the command
     */
    public static void setCommonDebugStatsStartSendingPacketFromDroneListener (ARCommandCommonDebugStatsStartSendingPacketFromDroneListener _ARCommandCommonDebugStatsStartSendingPacketFromDroneListener_PARAM) {
        _ARCommandCommonDebugStatsStartSendingPacketFromDroneListener = _ARCommandCommonDebugStatsStartSendingPacketFromDroneListener_PARAM;
    }

    private static ARCommandCommonDebugStatsStopSendingPacketFromDroneListener _ARCommandCommonDebugStatsStopSendingPacketFromDroneListener = null;

    /**
     * Set the listener for the command <code>StatsStopSendingPacketFromDrone</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugStatsStopSendingPacketFromDroneListener_PARAM New listener for the command
     */
    public static void setCommonDebugStatsStopSendingPacketFromDroneListener (ARCommandCommonDebugStatsStopSendingPacketFromDroneListener _ARCommandCommonDebugStatsStopSendingPacketFromDroneListener_PARAM) {
        _ARCommandCommonDebugStatsStopSendingPacketFromDroneListener = _ARCommandCommonDebugStatsStopSendingPacketFromDroneListener_PARAM;
    }

    private static ARCommandCommonDebugDebugSettingsGetAllListener _ARCommandCommonDebugDebugSettingsGetAllListener = null;

    /**
     * Set the listener for the command <code>DebugSettingsGetAll</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugDebugSettingsGetAllListener_PARAM New listener for the command
     */
    public static void setCommonDebugDebugSettingsGetAllListener (ARCommandCommonDebugDebugSettingsGetAllListener _ARCommandCommonDebugDebugSettingsGetAllListener_PARAM) {
        _ARCommandCommonDebugDebugSettingsGetAllListener = _ARCommandCommonDebugDebugSettingsGetAllListener_PARAM;
    }

    private static ARCommandCommonDebugDebugSettingsSetListener _ARCommandCommonDebugDebugSettingsSetListener = null;

    /**
     * Set the listener for the command <code>DebugSettingsSet</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugDebugSettingsSetListener_PARAM New listener for the command
     */
    public static void setCommonDebugDebugSettingsSetListener (ARCommandCommonDebugDebugSettingsSetListener _ARCommandCommonDebugDebugSettingsSetListener_PARAM) {
        _ARCommandCommonDebugDebugSettingsSetListener = _ARCommandCommonDebugDebugSettingsSetListener_PARAM;
    }

    private static ARCommandCommonDebugStatsEventSendPacketListener _ARCommandCommonDebugStatsEventSendPacketListener = null;

    /**
     * Set the listener for the command <code>StatsEventSendPacket</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugStatsEventSendPacketListener_PARAM New listener for the command
     */
    public static void setCommonDebugStatsEventSendPacketListener (ARCommandCommonDebugStatsEventSendPacketListener _ARCommandCommonDebugStatsEventSendPacketListener_PARAM) {
        _ARCommandCommonDebugStatsEventSendPacketListener = _ARCommandCommonDebugStatsEventSendPacketListener_PARAM;
    }

    private static ARCommandCommonDebugDebugSettingsStateInfoListener _ARCommandCommonDebugDebugSettingsStateInfoListener = null;

    /**
     * Set the listener for the command <code>DebugSettingsStateInfo</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugDebugSettingsStateInfoListener_PARAM New listener for the command
     */
    public static void setCommonDebugDebugSettingsStateInfoListener (ARCommandCommonDebugDebugSettingsStateInfoListener _ARCommandCommonDebugDebugSettingsStateInfoListener_PARAM) {
        _ARCommandCommonDebugDebugSettingsStateInfoListener = _ARCommandCommonDebugDebugSettingsStateInfoListener_PARAM;
    }

    private static ARCommandCommonDebugDebugSettingsStateListChangedListener _ARCommandCommonDebugDebugSettingsStateListChangedListener = null;

    /**
     * Set the listener for the command <code>DebugSettingsStateListChanged</code> in feature <code>CommonDebug</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandCommonDebugDebugSettingsStateListChangedListener_PARAM New listener for the command
     */
    public static void setCommonDebugDebugSettingsStateListChangedListener (ARCommandCommonDebugDebugSettingsStateListChangedListener _ARCommandCommonDebugDebugSettingsStateListChangedListener_PARAM) {
        _ARCommandCommonDebugDebugSettingsStateListChangedListener = _ARCommandCommonDebugDebugSettingsStateListChangedListener_PARAM;
    }


    private static ARCommandProProBoughtFeaturesListener _ARCommandProProBoughtFeaturesListener = null;

    /**
     * Set the listener for the command <code>ProBoughtFeatures</code> in feature <code>Pro</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandProProBoughtFeaturesListener_PARAM New listener for the command
     */
    public static void setProProBoughtFeaturesListener (ARCommandProProBoughtFeaturesListener _ARCommandProProBoughtFeaturesListener_PARAM) {
        _ARCommandProProBoughtFeaturesListener = _ARCommandProProBoughtFeaturesListener_PARAM;
    }

    private static ARCommandProProResponseListener _ARCommandProProResponseListener = null;

    /**
     * Set the listener for the command <code>ProResponse</code> in feature <code>Pro</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandProProResponseListener_PARAM New listener for the command
     */
    public static void setProProResponseListener (ARCommandProProResponseListener _ARCommandProProResponseListener_PARAM) {
        _ARCommandProProResponseListener = _ARCommandProProResponseListener_PARAM;
    }

    private static ARCommandProProActivateFeaturesListener _ARCommandProProActivateFeaturesListener = null;

    /**
     * Set the listener for the command <code>ProActivateFeatures</code> in feature <code>Pro</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandProProActivateFeaturesListener_PARAM New listener for the command
     */
    public static void setProProActivateFeaturesListener (ARCommandProProActivateFeaturesListener _ARCommandProProActivateFeaturesListener_PARAM) {
        _ARCommandProProActivateFeaturesListener = _ARCommandProProActivateFeaturesListener_PARAM;
    }

    private static ARCommandProProStateSupportedFeaturesListener _ARCommandProProStateSupportedFeaturesListener = null;

    /**
     * Set the listener for the command <code>ProStateSupportedFeatures</code> in feature <code>Pro</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandProProStateSupportedFeaturesListener_PARAM New listener for the command
     */
    public static void setProProStateSupportedFeaturesListener (ARCommandProProStateSupportedFeaturesListener _ARCommandProProStateSupportedFeaturesListener_PARAM) {
        _ARCommandProProStateSupportedFeaturesListener = _ARCommandProProStateSupportedFeaturesListener_PARAM;
    }

    private static ARCommandProProStateFeaturesActivatedListener _ARCommandProProStateFeaturesActivatedListener = null;

    /**
     * Set the listener for the command <code>ProStateFeaturesActivated</code> in feature <code>Pro</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandProProStateFeaturesActivatedListener_PARAM New listener for the command
     */
    public static void setProProStateFeaturesActivatedListener (ARCommandProProStateFeaturesActivatedListener _ARCommandProProStateFeaturesActivatedListener_PARAM) {
        _ARCommandProProStateFeaturesActivatedListener = _ARCommandProProStateFeaturesActivatedListener_PARAM;
    }

    private static ARCommandProProEventChallengeEventListener _ARCommandProProEventChallengeEventListener = null;

    /**
     * Set the listener for the command <code>ProEventChallengeEvent</code> in feature <code>Pro</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandProProEventChallengeEventListener_PARAM New listener for the command
     */
    public static void setProProEventChallengeEventListener (ARCommandProProEventChallengeEventListener _ARCommandProProEventChallengeEventListener_PARAM) {
        _ARCommandProProEventChallengeEventListener = _ARCommandProProEventChallengeEventListener_PARAM;
    }


    private static ARCommandWifiScanListener _ARCommandWifiScanListener = null;

    /**
     * Set the listener for the command <code>Scan</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiScanListener_PARAM New listener for the command
     */
    public static void setWifiScanListener (ARCommandWifiScanListener _ARCommandWifiScanListener_PARAM) {
        _ARCommandWifiScanListener = _ARCommandWifiScanListener_PARAM;
    }

    private static ARCommandWifiUpdateAuthorizedChannelsListener _ARCommandWifiUpdateAuthorizedChannelsListener = null;

    /**
     * Set the listener for the command <code>UpdateAuthorizedChannels</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiUpdateAuthorizedChannelsListener_PARAM New listener for the command
     */
    public static void setWifiUpdateAuthorizedChannelsListener (ARCommandWifiUpdateAuthorizedChannelsListener _ARCommandWifiUpdateAuthorizedChannelsListener_PARAM) {
        _ARCommandWifiUpdateAuthorizedChannelsListener = _ARCommandWifiUpdateAuthorizedChannelsListener_PARAM;
    }

    private static ARCommandWifiSetApChannelListener _ARCommandWifiSetApChannelListener = null;

    /**
     * Set the listener for the command <code>SetApChannel</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiSetApChannelListener_PARAM New listener for the command
     */
    public static void setWifiSetApChannelListener (ARCommandWifiSetApChannelListener _ARCommandWifiSetApChannelListener_PARAM) {
        _ARCommandWifiSetApChannelListener = _ARCommandWifiSetApChannelListener_PARAM;
    }

    private static ARCommandWifiSetSecurityListener _ARCommandWifiSetSecurityListener = null;

    /**
     * Set the listener for the command <code>SetSecurity</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiSetSecurityListener_PARAM New listener for the command
     */
    public static void setWifiSetSecurityListener (ARCommandWifiSetSecurityListener _ARCommandWifiSetSecurityListener_PARAM) {
        _ARCommandWifiSetSecurityListener = _ARCommandWifiSetSecurityListener_PARAM;
    }

    private static ARCommandWifiSetCountryListener _ARCommandWifiSetCountryListener = null;

    /**
     * Set the listener for the command <code>SetCountry</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiSetCountryListener_PARAM New listener for the command
     */
    public static void setWifiSetCountryListener (ARCommandWifiSetCountryListener _ARCommandWifiSetCountryListener_PARAM) {
        _ARCommandWifiSetCountryListener = _ARCommandWifiSetCountryListener_PARAM;
    }

    private static ARCommandWifiSetEnvironementListener _ARCommandWifiSetEnvironementListener = null;

    /**
     * Set the listener for the command <code>SetEnvironement</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiSetEnvironementListener_PARAM New listener for the command
     */
    public static void setWifiSetEnvironementListener (ARCommandWifiSetEnvironementListener _ARCommandWifiSetEnvironementListener_PARAM) {
        _ARCommandWifiSetEnvironementListener = _ARCommandWifiSetEnvironementListener_PARAM;
    }

    private static ARCommandWifiScannedItemListener _ARCommandWifiScannedItemListener = null;

    /**
     * Set the listener for the command <code>ScannedItem</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiScannedItemListener_PARAM New listener for the command
     */
    public static void setWifiScannedItemListener (ARCommandWifiScannedItemListener _ARCommandWifiScannedItemListener_PARAM) {
        _ARCommandWifiScannedItemListener = _ARCommandWifiScannedItemListener_PARAM;
    }

    private static ARCommandWifiAuthorizedChannelListener _ARCommandWifiAuthorizedChannelListener = null;

    /**
     * Set the listener for the command <code>AuthorizedChannel</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiAuthorizedChannelListener_PARAM New listener for the command
     */
    public static void setWifiAuthorizedChannelListener (ARCommandWifiAuthorizedChannelListener _ARCommandWifiAuthorizedChannelListener_PARAM) {
        _ARCommandWifiAuthorizedChannelListener = _ARCommandWifiAuthorizedChannelListener_PARAM;
    }

    private static ARCommandWifiApChannelChangedListener _ARCommandWifiApChannelChangedListener = null;

    /**
     * Set the listener for the command <code>ApChannelChanged</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiApChannelChangedListener_PARAM New listener for the command
     */
    public static void setWifiApChannelChangedListener (ARCommandWifiApChannelChangedListener _ARCommandWifiApChannelChangedListener_PARAM) {
        _ARCommandWifiApChannelChangedListener = _ARCommandWifiApChannelChangedListener_PARAM;
    }

    private static ARCommandWifiSecurityChangedListener _ARCommandWifiSecurityChangedListener = null;

    /**
     * Set the listener for the command <code>SecurityChanged</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiSecurityChangedListener_PARAM New listener for the command
     */
    public static void setWifiSecurityChangedListener (ARCommandWifiSecurityChangedListener _ARCommandWifiSecurityChangedListener_PARAM) {
        _ARCommandWifiSecurityChangedListener = _ARCommandWifiSecurityChangedListener_PARAM;
    }

    private static ARCommandWifiCountryChangedListener _ARCommandWifiCountryChangedListener = null;

    /**
     * Set the listener for the command <code>CountryChanged</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiCountryChangedListener_PARAM New listener for the command
     */
    public static void setWifiCountryChangedListener (ARCommandWifiCountryChangedListener _ARCommandWifiCountryChangedListener_PARAM) {
        _ARCommandWifiCountryChangedListener = _ARCommandWifiCountryChangedListener_PARAM;
    }

    private static ARCommandWifiEnvironementChangedListener _ARCommandWifiEnvironementChangedListener = null;

    /**
     * Set the listener for the command <code>EnvironementChanged</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiEnvironementChangedListener_PARAM New listener for the command
     */
    public static void setWifiEnvironementChangedListener (ARCommandWifiEnvironementChangedListener _ARCommandWifiEnvironementChangedListener_PARAM) {
        _ARCommandWifiEnvironementChangedListener = _ARCommandWifiEnvironementChangedListener_PARAM;
    }

    private static ARCommandWifiRssiChangedListener _ARCommandWifiRssiChangedListener = null;

    /**
     * Set the listener for the command <code>RssiChanged</code> in feature <code>Wifi</code><br>
     * Listeners are static to the class, and are not to be set on every object
     * @param _ARCommandWifiRssiChangedListener_PARAM New listener for the command
     */
    public static void setWifiRssiChangedListener (ARCommandWifiRssiChangedListener _ARCommandWifiRssiChangedListener_PARAM) {
        _ARCommandWifiRssiChangedListener = _ARCommandWifiRssiChangedListener_PARAM;
    }



    private native String  nativeToString (long jpdata, int jdataSize);
    private static native String  nativeStaticToString (long jpdata, int jdataSize);
    private native int     nativeDecode (long jpdata, int jdataSize);

    private native int     nativeSetGenericDefault (long pdata, int dataTotalLength);


    private native int     nativeSetARDrone3PilotingFlatTrim (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingTakeOff (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingPCMD (long pdata, int dataTotalLength, byte flag, byte roll, byte pitch, byte yaw, byte gaz, int timestampAndSeqNum);

    private native int     nativeSetARDrone3PilotingLanding (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingEmergency (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingNavigateHome (long pdata, int dataTotalLength, byte start);

    private native int     nativeSetARDrone3PilotingAutoTakeOffMode (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetARDrone3PilotingMoveBy (long pdata, int dataTotalLength, float dX, float dY, float dZ, float dPsi);

    private native int     nativeSetARDrone3PilotingUserTakeOff (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetARDrone3PilotingCircle (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTING_CIRCLE_DIRECTION_ENUM direction);

    private native int     nativeSetARDrone3AnimationsFlip (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_ANIMATIONS_FLIP_DIRECTION_ENUM direction);

    private native int     nativeSetARDrone3CameraOrientation (long pdata, int dataTotalLength, byte tilt, byte pan);

    private native int     nativeSetARDrone3MediaRecordPicture (long pdata, int dataTotalLength, byte mass_storage_id);

    private native int     nativeSetARDrone3MediaRecordVideo (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEO_RECORD_ENUM record, byte mass_storage_id);

    private native int     nativeSetARDrone3MediaRecordPictureV2 (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3MediaRecordVideoV2 (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORD_VIDEOV2_RECORD_ENUM record);

    private native int     nativeSetARDrone3NetworkWifiScan (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORK_WIFISCAN_BAND_ENUM band);

    private native int     nativeSetARDrone3NetworkWifiAuthChannel (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingSettingsMaxAltitude (long pdata, int dataTotalLength, float current);

    private native int     nativeSetARDrone3PilotingSettingsMaxTilt (long pdata, int dataTotalLength, float current);

    private native int     nativeSetARDrone3PilotingSettingsAbsolutControl (long pdata, int dataTotalLength, byte on);

    private native int     nativeSetARDrone3PilotingSettingsMaxDistance (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsNoFlyOverMaxDistance (long pdata, int dataTotalLength, byte shouldNotFlyOver);

    private native int     nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalSpeed (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalSpeed (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxHorizontalAcceleration (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxVerticalAcceleration (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsSetAutonomousFlightMaxRotationSpeed (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsBankedTurn (long pdata, int dataTotalLength, byte value);

    private native int     nativeSetARDrone3PilotingSettingsMinAltitude (long pdata, int dataTotalLength, float current);

    private native int     nativeSetARDrone3PilotingSettingsCirclingDirection (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSETTINGS_CIRCLINGDIRECTION_VALUE_ENUM value);

    private native int     nativeSetARDrone3PilotingSettingsCirclingRadius (long pdata, int dataTotalLength, short value);

    private native int     nativeSetARDrone3PilotingSettingsCirclingAltitude (long pdata, int dataTotalLength, short value);

    private native int     nativeSetARDrone3PilotingSettingsPitchMode (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSETTINGS_PITCHMODE_VALUE_ENUM value);

    private native int     nativeSetARDrone3PilotingSettingsLandingMode (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSETTINGS_LANDINGMODE_VALUE_ENUM value);

    private native int     nativeSetARDrone3SpeedSettingsMaxVerticalSpeed (long pdata, int dataTotalLength, float current);

    private native int     nativeSetARDrone3SpeedSettingsMaxRotationSpeed (long pdata, int dataTotalLength, float current);

    private native int     nativeSetARDrone3SpeedSettingsHullProtection (long pdata, int dataTotalLength, byte present);

    private native int     nativeSetARDrone3SpeedSettingsOutdoor (long pdata, int dataTotalLength, byte outdoor);

    private native int     nativeSetARDrone3SpeedSettingsMaxPitchRollRotationSpeed (long pdata, int dataTotalLength, float current);

    private native int     nativeSetARDrone3NetworkSettingsWifiSelection (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISELECTION_TYPE_ENUM type, ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISELECTION_BAND_ENUM band, byte channel);

    private native int     nativeSetARDrone3NetworkSettingsWifiSecurity (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_ENUM type, String key, ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_ENUM keyType);

    private native int     nativeSetARDrone3PictureSettingsPictureFormatSelection (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PICTURESETTINGS_PICTUREFORMATSELECTION_TYPE_ENUM type);

    private native int     nativeSetARDrone3PictureSettingsAutoWhiteBalanceSelection (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PICTURESETTINGS_AUTOWHITEBALANCESELECTION_TYPE_ENUM type);

    private native int     nativeSetARDrone3PictureSettingsExpositionSelection (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PictureSettingsSaturationSelection (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PictureSettingsTimelapseSelection (long pdata, int dataTotalLength, byte enabled, float interval);

    private native int     nativeSetARDrone3PictureSettingsVideoAutorecordSelection (long pdata, int dataTotalLength, byte enabled, byte mass_storage_id);

    private native int     nativeSetARDrone3PictureSettingsVideoStabilizationMode (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PICTURESETTINGS_VIDEOSTABILIZATIONMODE_MODE_ENUM mode);

    private native int     nativeSetARDrone3MediaStreamingVideoEnable (long pdata, int dataTotalLength, byte enable);

    private native int     nativeSetARDrone3GPSSettingsSetHome (long pdata, int dataTotalLength, double latitude, double longitude, double altitude);

    private native int     nativeSetARDrone3GPSSettingsResetHome (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3GPSSettingsSendControllerGPS (long pdata, int dataTotalLength, double latitude, double longitude, double altitude, double horizontalAccuracy, double verticalAccuracy);

    private native int     nativeSetARDrone3GPSSettingsHomeType (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_GPSSETTINGS_HOMETYPE_TYPE_ENUM type);

    private native int     nativeSetARDrone3GPSSettingsReturnHomeDelay (long pdata, int dataTotalLength, short delay);

    private native int     nativeSetARDrone3AntiflickeringElectricFrequency (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_ANTIFLICKERING_ELECTRICFREQUENCY_FREQUENCY_ENUM frequency);

    private native int     nativeSetARDrone3AntiflickeringSetMode (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_ANTIFLICKERING_SETMODE_MODE_ENUM mode);

    private native int     nativeSetARDrone3MediaRecordStatePictureStateChanged (long pdata, int dataTotalLength, byte state, byte mass_storage_id);

    private native int     nativeSetARDrone3MediaRecordStateVideoStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGED_STATE_ENUM state, byte mass_storage_id);

    private native int     nativeSetARDrone3MediaRecordStatePictureStateChangedV2 (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error);

    private native int     nativeSetARDrone3MediaRecordStateVideoStateChangedV2 (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_ARDRONE3_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error);

    private native int     nativeSetARDrone3MediaRecordEventPictureEventChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM event, ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error);

    private native int     nativeSetARDrone3MediaRecordEventVideoEventChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_VIDEOEVENTCHANGED_EVENT_ENUM event, ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_VIDEOEVENTCHANGED_ERROR_ENUM error);

    private native int     nativeSetARDrone3PilotingStateFlatTrimChanged (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingStateFlyingStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state);

    private native int     nativeSetARDrone3PilotingStateAlertStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM state);

    private native int     nativeSetARDrone3PilotingStateNavigateHomeStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_STATE_ENUM state, ARCOMMANDS_ARDRONE3_PILOTINGSTATE_NAVIGATEHOMESTATECHANGED_REASON_ENUM reason);

    private native int     nativeSetARDrone3PilotingStatePositionChanged (long pdata, int dataTotalLength, double latitude, double longitude, double altitude);

    private native int     nativeSetARDrone3PilotingStateSpeedChanged (long pdata, int dataTotalLength, float speedX, float speedY, float speedZ);

    private native int     nativeSetARDrone3PilotingStateAttitudeChanged (long pdata, int dataTotalLength, float roll, float pitch, float yaw);

    private native int     nativeSetARDrone3PilotingStateAutoTakeOffModeChanged (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetARDrone3PilotingStateAltitudeChanged (long pdata, int dataTotalLength, double altitude);

    private native int     nativeSetARDrone3PilotingEventMoveByEnd (long pdata, int dataTotalLength, float dX, float dY, float dZ, float dPsi, ARCOMMANDS_ARDRONE3_PILOTINGEVENT_MOVEBYEND_ERROR_ENUM error);

    private native int     nativeSetARDrone3NetworkStateWifiScanListChanged (long pdata, int dataTotalLength, String ssid, short rssi, ARCOMMANDS_ARDRONE3_NETWORKSTATE_WIFISCANLISTCHANGED_BAND_ENUM band, byte channel);

    private native int     nativeSetARDrone3NetworkStateAllWifiScanChanged (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3NetworkStateWifiAuthChannelListChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORKSTATE_WIFIAUTHCHANNELLISTCHANGED_BAND_ENUM band, byte channel, byte in_or_out);

    private native int     nativeSetARDrone3NetworkStateAllWifiAuthChannelChanged (long pdata, int dataTotalLength);

    private native int     nativeSetARDrone3PilotingSettingsStateMaxAltitudeChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3PilotingSettingsStateMaxTiltChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3PilotingSettingsStateAbsolutControlChanged (long pdata, int dataTotalLength, byte on);

    private native int     nativeSetARDrone3PilotingSettingsStateMaxDistanceChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3PilotingSettingsStateNoFlyOverMaxDistanceChanged (long pdata, int dataTotalLength, byte shouldNotFlyOver);

    private native int     nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalSpeed (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalSpeed (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxHorizontalAcceleration (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxVerticalAcceleration (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsStateAutonomousFlightMaxRotationSpeed (long pdata, int dataTotalLength, float value);

    private native int     nativeSetARDrone3PilotingSettingsStateBankedTurnChanged (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetARDrone3PilotingSettingsStateMinAltitudeChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3PilotingSettingsStateCirclingDirectionChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSETTINGSSTATE_CIRCLINGDIRECTIONCHANGED_VALUE_ENUM value);

    private native int     nativeSetARDrone3PilotingSettingsStateCirclingRadiusChanged (long pdata, int dataTotalLength, short current, short min, short max);

    private native int     nativeSetARDrone3PilotingSettingsStateCirclingAltitudeChanged (long pdata, int dataTotalLength, short current, short min, short max);

    private native int     nativeSetARDrone3PilotingSettingsStatePitchModeChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSETTINGSSTATE_PITCHMODECHANGED_VALUE_ENUM value);

    private native int     nativeSetARDrone3PilotingSettingsStateLandingModeChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PILOTINGSETTINGSSTATE_LANDINGMODECHANGED_VALUE_ENUM value);

    private native int     nativeSetARDrone3SpeedSettingsStateMaxVerticalSpeedChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3SpeedSettingsStateMaxRotationSpeedChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3SpeedSettingsStateHullProtectionChanged (long pdata, int dataTotalLength, byte present);

    private native int     nativeSetARDrone3SpeedSettingsStateOutdoorChanged (long pdata, int dataTotalLength, byte outdoor);

    private native int     nativeSetARDrone3SpeedSettingsStateMaxPitchRollRotationSpeedChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetARDrone3NetworkSettingsStateWifiSelectionChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_TYPE_ENUM type, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_BAND_ENUM band, byte channel);

    private native int     nativeSetARDrone3NetworkSettingsStateWifiSecurityChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITYCHANGED_TYPE_ENUM type);

    private native int     nativeSetARDrone3NetworkSettingsStateWifiSecurity (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITY_TYPE_ENUM type, String key, ARCOMMANDS_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITY_KEYTYPE_ENUM keyType);

    private native int     nativeSetARDrone3SettingsStateProductMotorVersionListChanged (long pdata, int dataTotalLength, byte motor_number, String type, String software, String hardware);

    private native int     nativeSetARDrone3SettingsStateProductGPSVersionChanged (long pdata, int dataTotalLength, String software, String hardware);

    private native int     nativeSetARDrone3SettingsStateMotorErrorStateChanged (long pdata, int dataTotalLength, byte motorIds, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORSTATECHANGED_MOTORERROR_ENUM motorError);

    private native int     nativeSetARDrone3SettingsStateMotorSoftwareVersionChanged (long pdata, int dataTotalLength, String version);

    private native int     nativeSetARDrone3SettingsStateMotorFlightsStatusChanged (long pdata, int dataTotalLength, short nbFlights, short lastFlightDuration, int totalFlightDuration);

    private native int     nativeSetARDrone3SettingsStateMotorErrorLastErrorChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_SETTINGSSTATE_MOTORERRORLASTERRORCHANGED_MOTORERROR_ENUM motorError);

    private native int     nativeSetARDrone3SettingsStateP7ID (long pdata, int dataTotalLength, String serialID);

    private native int     nativeSetARDrone3PictureSettingsStatePictureFormatChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_PICTUREFORMATCHANGED_TYPE_ENUM type);

    private native int     nativeSetARDrone3PictureSettingsStateAutoWhiteBalanceChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_AUTOWHITEBALANCECHANGED_TYPE_ENUM type);

    private native int     nativeSetARDrone3PictureSettingsStateExpositionChanged (long pdata, int dataTotalLength, float value, float min, float max);

    private native int     nativeSetARDrone3PictureSettingsStateSaturationChanged (long pdata, int dataTotalLength, float value, float min, float max);

    private native int     nativeSetARDrone3PictureSettingsStateTimelapseChanged (long pdata, int dataTotalLength, byte enabled, float interval, float minInterval, float maxInterval);

    private native int     nativeSetARDrone3PictureSettingsStateVideoAutorecordChanged (long pdata, int dataTotalLength, byte enabled, byte mass_storage_id);

    private native int     nativeSetARDrone3PictureSettingsStateVideoStabilizationModeChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_PICTURESETTINGSSTATE_VIDEOSTABILIZATIONMODECHANGED_MODE_ENUM mode);

    private native int     nativeSetARDrone3MediaStreamingStateVideoEnableChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabled);

    private native int     nativeSetARDrone3GPSSettingsStateHomeChanged (long pdata, int dataTotalLength, double latitude, double longitude, double altitude);

    private native int     nativeSetARDrone3GPSSettingsStateResetHomeChanged (long pdata, int dataTotalLength, double latitude, double longitude, double altitude);

    private native int     nativeSetARDrone3GPSSettingsStateGPSFixStateChanged (long pdata, int dataTotalLength, byte fixed);

    private native int     nativeSetARDrone3GPSSettingsStateGPSUpdateStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_GPSUPDATESTATECHANGED_STATE_ENUM state);

    private native int     nativeSetARDrone3GPSSettingsStateHomeTypeChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_GPSSETTINGSSTATE_HOMETYPECHANGED_TYPE_ENUM type);

    private native int     nativeSetARDrone3GPSSettingsStateReturnHomeDelayChanged (long pdata, int dataTotalLength, short delay);

    private native int     nativeSetARDrone3CameraStateOrientation (long pdata, int dataTotalLength, byte tilt, byte pan);

    private native int     nativeSetARDrone3CameraStateDefaultCameraOrientation (long pdata, int dataTotalLength, byte tilt, byte pan);

    private native int     nativeSetARDrone3AntiflickeringStateElectricFrequencyChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_ANTIFLICKERINGSTATE_ELECTRICFREQUENCYCHANGED_FREQUENCY_ENUM frequency);

    private native int     nativeSetARDrone3AntiflickeringStateModeChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_ANTIFLICKERINGSTATE_MODECHANGED_MODE_ENUM mode);

    private native int     nativeSetARDrone3GPSStateNumberOfSatelliteChanged (long pdata, int dataTotalLength, byte numberOfSatellite);

    private native int     nativeSetARDrone3GPSStateHomeTypeAvailabilityChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_GPSSTATE_HOMETYPEAVAILABILITYCHANGED_TYPE_ENUM type, byte available);

    private native int     nativeSetARDrone3GPSStateHomeTypeChosenChanged (long pdata, int dataTotalLength, ARCOMMANDS_ARDRONE3_GPSSTATE_HOMETYPECHOSENCHANGED_TYPE_ENUM type);

    private native int     nativeSetARDrone3PROStateFeatures (long pdata, int dataTotalLength, long features);


    private native int     nativeSetUnknownFeature_1GeographicRun (long pdata, int dataTotalLength, byte start, byte distance_is_default, float distance, byte elevation_is_default, float elevation, byte azimuth_is_default, float azimuth);

    private native int     nativeSetUnknownFeature_1RelativeRun (long pdata, int dataTotalLength, byte start, byte distance_is_default, float distance, byte elevation_is_default, float elevation, byte azimuth_is_default, float azimuth);

    private native int     nativeSetUnknownFeature_1LookAtRun (long pdata, int dataTotalLength, byte start);

    private native int     nativeSetUnknownFeature_1SpiralAnimRun (long pdata, int dataTotalLength, byte start, byte speed_is_default, float speed, byte revolution_nb_is_default, float revolution_number, byte vertical_distance_is_default, float vertical_distance);

    private native int     nativeSetUnknownFeature_1SwingAnimRun (long pdata, int dataTotalLength, byte start, byte speed_is_default, float speed, byte vertical_distance_is_default, float vertical_distance);

    private native int     nativeSetUnknownFeature_1BoomerangAnimRun (long pdata, int dataTotalLength, byte start, byte speed_is_default, float speed, byte distance_is_default, float distance);

    private native int     nativeSetUnknownFeature_1CandleAnimRun (long pdata, int dataTotalLength, byte start, byte speed_is_default, float speed, byte vertical_distance_is_default, float vertical_distance);

    private native int     nativeSetUnknownFeature_1DollySlideAnimRun (long pdata, int dataTotalLength, byte start, byte speed_is_default, float speed, byte angle_is_default, float angle, byte horizontal_distance_is_default, float horizontal_distance);

    private native int     nativeSetUnknownFeature_1UserFramingPosition (long pdata, int dataTotalLength, byte horizontal, byte vertical);

    private native int     nativeSetUnknownFeature_1UserGPSData (long pdata, int dataTotalLength, double latitude, double longitude, float altitude, float horizontal_accuracy, float vertical_accuracy, float north_speed, float east_speed, float down_speed, double timestamp);

    private native int     nativeSetUnknownFeature_1UserBaroData (long pdata, int dataTotalLength, float pressure, double timestamp);

    private native int     nativeSetUnknownFeature_1LynxDetection (long pdata, int dataTotalLength, float target_pan, float target_tilt, float change_of_scale, byte confidence_index, byte is_new_selection, long timestamp);

    private native int     nativeSetUnknownFeature_1Availability (long pdata, int dataTotalLength, ARCOMMANDS_FOLLOW_ME_TYPES_AVAILABLE_ENUM type);

    private native int     nativeSetUnknownFeature_1Run (long pdata, int dataTotalLength, ARCOMMANDS_FOLLOW_ME_TYPE_ENUM type);

    private native int     nativeSetUnknownFeature_1GeographicConfigChanged (long pdata, int dataTotalLength, byte distance_is_default, float distance, byte elevation_is_default, float elevation, byte azimuth_is_default, float azimuth);

    private native int     nativeSetUnknownFeature_1RelativeConfigChanged (long pdata, int dataTotalLength, byte distance_is_default, float distance, byte elevation_is_default, float elevation, byte azimuth_is_default, float azimuth);

    private native int     nativeSetUnknownFeature_1AnimRun (long pdata, int dataTotalLength, ARCOMMANDS_FOLLOW_ME_ANIM_TYPE_ENUM type);

    private native int     nativeSetUnknownFeature_1SpiralAnimConfigChanged (long pdata, int dataTotalLength, byte speed_is_default, float speed, byte revolution_nb_is_default, float revolution_nb, byte vertical_distance_is_default, float vertical_distance);

    private native int     nativeSetUnknownFeature_1SwingAnimConfigChanged (long pdata, int dataTotalLength, byte speed_is_default, float speed, byte vertical_distance_is_default, float vertical_distance);

    private native int     nativeSetUnknownFeature_1BoomerangAnimConfigChanged (long pdata, int dataTotalLength, byte speed_is_default, float speed, byte distance_is_default, float distance);

    private native int     nativeSetUnknownFeature_1CandleAnimConfigChanged (long pdata, int dataTotalLength, byte speed_is_default, float speed, byte vertical_distance_is_default, float vertical_distance);

    private native int     nativeSetUnknownFeature_1DollySlideAnimConfigChanged (long pdata, int dataTotalLength, byte speed_is_default, float speed, byte angle_is_default, float angle, byte horizontal_distance_is_default, float horizontal_distance);

    private native int     nativeSetUnknownFeature_1UserFramingPositionChanged (long pdata, int dataTotalLength, byte horizontal, byte vertical);


    private native int     nativeSetJumpingSumoPilotingPCMD (long pdata, int dataTotalLength, byte flag, byte speed, byte turn);

    private native int     nativeSetJumpingSumoPilotingPosture (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_PILOTING_POSTURE_TYPE_ENUM type);

    private native int     nativeSetJumpingSumoPilotingAddCapOffset (long pdata, int dataTotalLength, float offset);

    private native int     nativeSetJumpingSumoPilotingUserTakeOff (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetJumpingSumoPilotingLand (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoAnimationsJumpStop (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoAnimationsJumpCancel (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoAnimationsJumpLoad (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoAnimationsJump (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM type);

    private native int     nativeSetJumpingSumoAnimationsSimpleAnimation (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM id);

    private native int     nativeSetJumpingSumoMediaRecordPicture (long pdata, int dataTotalLength, byte mass_storage_id);

    private native int     nativeSetJumpingSumoMediaRecordVideo (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORD_VIDEO_RECORD_ENUM record, byte mass_storage_id);

    private native int     nativeSetJumpingSumoMediaRecordPictureV2 (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoMediaRecordVideoV2 (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORD_VIDEOV2_RECORD_ENUM record);

    private native int     nativeSetJumpingSumoNetworkSettingsWifiSelection (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGS_WIFISELECTION_TYPE_ENUM type, ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGS_WIFISELECTION_BAND_ENUM band, byte channel);

    private native int     nativeSetJumpingSumoNetworkWifiScan (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_NETWORK_WIFISCAN_BAND_ENUM band);

    private native int     nativeSetJumpingSumoNetworkWifiAuthChannel (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoAudioSettingsMasterVolume (long pdata, int dataTotalLength, byte volume);

    private native int     nativeSetJumpingSumoAudioSettingsTheme (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_AUDIOSETTINGS_THEME_THEME_ENUM theme);

    private native int     nativeSetJumpingSumoRoadPlanAllScriptsMetadata (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoRoadPlanScriptUploaded (long pdata, int dataTotalLength, String uuid, String md5Hash);

    private native int     nativeSetJumpingSumoRoadPlanScriptDelete (long pdata, int dataTotalLength, String uuid);

    private native int     nativeSetJumpingSumoRoadPlanPlayScript (long pdata, int dataTotalLength, String uuid);

    private native int     nativeSetJumpingSumoSpeedSettingsOutdoor (long pdata, int dataTotalLength, byte outdoor);

    private native int     nativeSetJumpingSumoMediaStreamingVideoEnable (long pdata, int dataTotalLength, byte enable);

    private native int     nativeSetJumpingSumoVideoSettingsAutorecord (long pdata, int dataTotalLength, byte enabled);

    private native int     nativeSetJumpingSumoPilotingStatePostureChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_PILOTINGSTATE_POSTURECHANGED_STATE_ENUM state);

    private native int     nativeSetJumpingSumoPilotingStateAlertStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM state);

    private native int     nativeSetJumpingSumoPilotingStateSpeedChanged (long pdata, int dataTotalLength, byte speed, short realSpeed);

    private native int     nativeSetJumpingSumoPilotingStateFlyingStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state);

    private native int     nativeSetJumpingSumoAnimationsStateJumpLoadChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ANIMATIONSSTATE_JUMPLOADCHANGED_STATE_ENUM state);

    private native int     nativeSetJumpingSumoAnimationsStateJumpTypeChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ANIMATIONSSTATE_JUMPTYPECHANGED_STATE_ENUM state);

    private native int     nativeSetJumpingSumoAnimationsStateJumpMotorProblemChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ANIMATIONSSTATE_JUMPMOTORPROBLEMCHANGED_ERROR_ENUM error);

    private native int     nativeSetJumpingSumoSettingsStateProductGPSVersionChanged (long pdata, int dataTotalLength, String software, String hardware);

    private native int     nativeSetJumpingSumoMediaRecordStatePictureStateChanged (long pdata, int dataTotalLength, byte state, byte mass_storage_id);

    private native int     nativeSetJumpingSumoMediaRecordStateVideoStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_VIDEOSTATECHANGED_STATE_ENUM state, byte mass_storage_id);

    private native int     nativeSetJumpingSumoMediaRecordStatePictureStateChangedV2 (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error);

    private native int     nativeSetJumpingSumoMediaRecordStateVideoStateChangedV2 (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDSTATE_VIDEOSTATECHANGEDV2_ERROR_ENUM error);

    private native int     nativeSetJumpingSumoMediaRecordEventPictureEventChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM event, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error);

    private native int     nativeSetJumpingSumoMediaRecordEventVideoEventChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_VIDEOEVENTCHANGED_EVENT_ENUM event, ARCOMMANDS_JUMPINGSUMO_MEDIARECORDEVENT_VIDEOEVENTCHANGED_ERROR_ENUM error);

    private native int     nativeSetJumpingSumoNetworkSettingsStateWifiSelectionChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_TYPE_ENUM type, ARCOMMANDS_JUMPINGSUMO_NETWORKSETTINGSSTATE_WIFISELECTIONCHANGED_BAND_ENUM band, byte channel);

    private native int     nativeSetJumpingSumoNetworkStateWifiScanListChanged (long pdata, int dataTotalLength, String ssid, short rssi, ARCOMMANDS_JUMPINGSUMO_NETWORKSTATE_WIFISCANLISTCHANGED_BAND_ENUM band, byte channel);

    private native int     nativeSetJumpingSumoNetworkStateAllWifiScanChanged (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoNetworkStateWifiAuthChannelListChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_NETWORKSTATE_WIFIAUTHCHANNELLISTCHANGED_BAND_ENUM band, byte channel, byte in_or_out);

    private native int     nativeSetJumpingSumoNetworkStateAllWifiAuthChannelChanged (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoNetworkStateLinkQualityChanged (long pdata, int dataTotalLength, byte quality);

    private native int     nativeSetJumpingSumoAudioSettingsStateMasterVolumeChanged (long pdata, int dataTotalLength, byte volume);

    private native int     nativeSetJumpingSumoAudioSettingsStateThemeChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_AUDIOSETTINGSSTATE_THEMECHANGED_THEME_ENUM theme);

    private native int     nativeSetJumpingSumoRoadPlanStateScriptMetadataListChanged (long pdata, int dataTotalLength, String uuid, byte version, String product, String name, long lastModified);

    private native int     nativeSetJumpingSumoRoadPlanStateAllScriptsMetadataChanged (long pdata, int dataTotalLength);

    private native int     nativeSetJumpingSumoRoadPlanStateScriptUploadChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ROADPLANSTATE_SCRIPTUPLOADCHANGED_RESULTCODE_ENUM resultCode);

    private native int     nativeSetJumpingSumoRoadPlanStateScriptDeleteChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ROADPLANSTATE_SCRIPTDELETECHANGED_RESULTCODE_ENUM resultCode);

    private native int     nativeSetJumpingSumoRoadPlanStatePlayScriptChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_ROADPLANSTATE_PLAYSCRIPTCHANGED_RESULTCODE_ENUM resultCode);

    private native int     nativeSetJumpingSumoSpeedSettingsStateOutdoorChanged (long pdata, int dataTotalLength, byte outdoor);

    private native int     nativeSetJumpingSumoMediaStreamingStateVideoEnableChanged (long pdata, int dataTotalLength, ARCOMMANDS_JUMPINGSUMO_MEDIASTREAMINGSTATE_VIDEOENABLECHANGED_ENABLED_ENUM enabled);

    private native int     nativeSetJumpingSumoVideoSettingsStateAutorecordChanged (long pdata, int dataTotalLength, byte enabled);


    private native int     nativeSetMiniDronePilotingFlatTrim (long pdata, int dataTotalLength);

    private native int     nativeSetMiniDronePilotingTakeOff (long pdata, int dataTotalLength);

    private native int     nativeSetMiniDronePilotingPCMD (long pdata, int dataTotalLength, byte flag, byte roll, byte pitch, byte yaw, byte gaz, int timestamp);

    private native int     nativeSetMiniDronePilotingLanding (long pdata, int dataTotalLength);

    private native int     nativeSetMiniDronePilotingEmergency (long pdata, int dataTotalLength);

    private native int     nativeSetMiniDronePilotingAutoTakeOffMode (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetMiniDronePilotingFlyingMode (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_PILOTING_FLYINGMODE_MODE_ENUM mode);

    private native int     nativeSetMiniDroneAnimationsFlip (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_ANIMATIONS_FLIP_DIRECTION_ENUM direction);

    private native int     nativeSetMiniDroneAnimationsCap (long pdata, int dataTotalLength, short offset);

    private native int     nativeSetMiniDroneMediaRecordPicture (long pdata, int dataTotalLength, byte mass_storage_id);

    private native int     nativeSetMiniDroneMediaRecordPictureV2 (long pdata, int dataTotalLength);

    private native int     nativeSetMiniDronePilotingSettingsMaxAltitude (long pdata, int dataTotalLength, float current);

    private native int     nativeSetMiniDronePilotingSettingsMaxTilt (long pdata, int dataTotalLength, float current);

    private native int     nativeSetMiniDroneSpeedSettingsMaxVerticalSpeed (long pdata, int dataTotalLength, float current);

    private native int     nativeSetMiniDroneSpeedSettingsMaxRotationSpeed (long pdata, int dataTotalLength, float current);

    private native int     nativeSetMiniDroneSpeedSettingsWheels (long pdata, int dataTotalLength, byte present);

    private native int     nativeSetMiniDroneSpeedSettingsMaxHorizontalSpeed (long pdata, int dataTotalLength, float current);

    private native int     nativeSetMiniDroneSettingsCutOutMode (long pdata, int dataTotalLength, byte enable);

    private native int     nativeSetMiniDroneGPSControllerLatitudeForRun (long pdata, int dataTotalLength, double latitude);

    private native int     nativeSetMiniDroneGPSControllerLongitudeForRun (long pdata, int dataTotalLength, double longitude);

    private native int     nativeSetMiniDroneConfigurationControllerType (long pdata, int dataTotalLength, String type);

    private native int     nativeSetMiniDroneConfigurationControllerName (long pdata, int dataTotalLength, String name);

    private native int     nativeSetMiniDronePilotingStateFlatTrimChanged (long pdata, int dataTotalLength);

    private native int     nativeSetMiniDronePilotingStateFlyingStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state);

    private native int     nativeSetMiniDronePilotingStateAlertStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_PILOTINGSTATE_ALERTSTATECHANGED_STATE_ENUM state);

    private native int     nativeSetMiniDronePilotingStateAutoTakeOffModeChanged (long pdata, int dataTotalLength, byte state);

    private native int     nativeSetMiniDronePilotingStateFlyingModeChanged (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGMODECHANGED_MODE_ENUM mode);

    private native int     nativeSetMiniDroneMediaRecordStatePictureStateChanged (long pdata, int dataTotalLength, byte state, byte mass_storage_id);

    private native int     nativeSetMiniDroneMediaRecordStatePictureStateChangedV2 (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_STATE_ENUM state, ARCOMMANDS_MINIDRONE_MEDIARECORDSTATE_PICTURESTATECHANGEDV2_ERROR_ENUM error);

    private native int     nativeSetMiniDroneMediaRecordEventPictureEventChanged (long pdata, int dataTotalLength, ARCOMMANDS_MINIDRONE_MEDIARECORDEVENT_PICTUREEVENTCHANGED_EVENT_ENUM event, ARCOMMANDS_MINIDRONE_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error);

    private native int     nativeSetMiniDronePilotingSettingsStateMaxAltitudeChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetMiniDronePilotingSettingsStateMaxTiltChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetMiniDroneSpeedSettingsStateMaxVerticalSpeedChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetMiniDroneSpeedSettingsStateMaxRotationSpeedChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetMiniDroneSpeedSettingsStateWheelsChanged (long pdata, int dataTotalLength, byte present);

    private native int     nativeSetMiniDroneSpeedSettingsStateMaxHorizontalSpeedChanged (long pdata, int dataTotalLength, float current, float min, float max);

    private native int     nativeSetMiniDroneSettingsStateProductMotorsVersionChanged (long pdata, int dataTotalLength, byte motor, String type, String software, String hardware);

    private native int     nativeSetMiniDroneSettingsStateProductInertialVersionChanged (long pdata, int dataTotalLength, String software, String hardware);

    private native int     nativeSetMiniDroneSettingsStateCutOutModeChanged (long pdata, int dataTotalLength, byte enable);

    private native int     nativeSetMiniDroneFloodControlStateFloodControlChanged (long pdata, int dataTotalLength, short delay);


    private native int     nativeSetSkyControllerWifiRequestWifiList (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerWifiRequestCurrentWifi (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerWifiConnectToWifi (long pdata, int dataTotalLength, String bssid, String ssid, String passphrase);

    private native int     nativeSetSkyControllerWifiForgetWifi (long pdata, int dataTotalLength, String ssid);

    private native int     nativeSetSkyControllerWifiWifiAuthChannel (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerDeviceRequestDeviceList (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerDeviceRequestCurrentDevice (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerDeviceConnectToDevice (long pdata, int dataTotalLength, String deviceName);

    private native int     nativeSetSkyControllerSettingsAllSettings (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerSettingsReset (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerCommonAllStates (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAccessPointSettingsAccessPointSSID (long pdata, int dataTotalLength, String ssid);

    private native int     nativeSetSkyControllerAccessPointSettingsAccessPointChannel (long pdata, int dataTotalLength, byte channel);

    private native int     nativeSetSkyControllerAccessPointSettingsWifiSelection (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGS_WIFISELECTION_TYPE_ENUM type, ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGS_WIFISELECTION_BAND_ENUM band, byte channel);

    private native int     nativeSetSkyControllerCameraResetOrientation (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerGamepadInfosGetGamepadControls (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerButtonMappingsGetCurrentButtonMappings (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerButtonMappingsGetAvailableButtonMappings (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerButtonMappingsSetButtonMapping (long pdata, int dataTotalLength, int key_id, String mapping_uid);

    private native int     nativeSetSkyControllerButtonMappingsDefaultButtonMapping (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisMappingsGetCurrentAxisMappings (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisMappingsGetAvailableAxisMappings (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisMappingsSetAxisMapping (long pdata, int dataTotalLength, int axis_id, String mapping_uid);

    private native int     nativeSetSkyControllerAxisMappingsDefaultAxisMapping (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisFiltersGetCurrentAxisFilters (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisFiltersGetPresetAxisFilters (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisFiltersSetAxisFilter (long pdata, int dataTotalLength, int axis_id, String filter_uid_or_builder);

    private native int     nativeSetSkyControllerAxisFiltersDefaultAxisFilters (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerCoPilotingSetPilotingSource (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_COPILOTING_SETPILOTINGSOURCE_SOURCE_ENUM source);

    private native int     nativeSetSkyControllerCalibrationEnableMagnetoCalibrationQualityUpdates (long pdata, int dataTotalLength, byte enable);

    private native int     nativeSetSkyControllerWifiStateWifiList (long pdata, int dataTotalLength, String bssid, String ssid, byte secured, byte saved, int rssi, int frequency);

    private native int     nativeSetSkyControllerWifiStateConnexionChanged (long pdata, int dataTotalLength, String ssid, ARCOMMANDS_SKYCONTROLLER_WIFISTATE_CONNEXIONCHANGED_STATUS_ENUM status);

    private native int     nativeSetSkyControllerWifiStateWifiAuthChannelListChanged (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_WIFISTATE_WIFIAUTHCHANNELLISTCHANGED_BAND_ENUM band, byte channel, byte in_or_out);

    private native int     nativeSetSkyControllerWifiStateAllWifiAuthChannelChanged (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerWifiStateWifiSignalChanged (long pdata, int dataTotalLength, byte level);

    private native int     nativeSetSkyControllerDeviceStateDeviceList (long pdata, int dataTotalLength, String name);

    private native int     nativeSetSkyControllerDeviceStateConnexionChanged (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_DEVICESTATE_CONNEXIONCHANGED_STATUS_ENUM status, String deviceName, short deviceProductID);

    private native int     nativeSetSkyControllerSettingsStateAllSettingsChanged (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerSettingsStateResetChanged (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerSettingsStateProductSerialChanged (long pdata, int dataTotalLength, String serialNumber);

    private native int     nativeSetSkyControllerSettingsStateProductVariantChanged (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_SETTINGSSTATE_PRODUCTVARIANTCHANGED_VARIANT_ENUM variant);

    private native int     nativeSetSkyControllerCommonStateAllStatesChanged (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerSkyControllerStateBatteryChanged (long pdata, int dataTotalLength, byte percent);

    private native int     nativeSetSkyControllerSkyControllerStateGpsFixChanged (long pdata, int dataTotalLength, byte fixed);

    private native int     nativeSetSkyControllerSkyControllerStateGpsPositionChanged (long pdata, int dataTotalLength, double latitude, double longitude, double altitude, float heading);

    private native int     nativeSetSkyControllerAccessPointSettingsStateAccessPointSSIDChanged (long pdata, int dataTotalLength, String ssid);

    private native int     nativeSetSkyControllerAccessPointSettingsStateAccessPointChannelChanged (long pdata, int dataTotalLength, byte channel);

    private native int     nativeSetSkyControllerAccessPointSettingsStateWifiSelectionChanged (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGSSTATE_WIFISELECTIONCHANGED_TYPE_ENUM type, ARCOMMANDS_SKYCONTROLLER_ACCESSPOINTSETTINGSSTATE_WIFISELECTIONCHANGED_BAND_ENUM band, byte channel);

    private native int     nativeSetSkyControllerGamepadInfosStateGamepadControl (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_GAMEPADINFOSSTATE_GAMEPADCONTROL_TYPE_ENUM type, int id, String name);

    private native int     nativeSetSkyControllerGamepadInfosStateAllGamepadControlsSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerButtonMappingsStateCurrentButtonMappings (long pdata, int dataTotalLength, int key_id, String mapping_uid);

    private native int     nativeSetSkyControllerButtonMappingsStateAllCurrentButtonMappingsSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerButtonMappingsStateAvailableButtonMappings (long pdata, int dataTotalLength, String mapping_uid, String name);

    private native int     nativeSetSkyControllerButtonMappingsStateAllAvailableButtonsMappingsSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisMappingsStateCurrentAxisMappings (long pdata, int dataTotalLength, int axis_id, String mapping_uid);

    private native int     nativeSetSkyControllerAxisMappingsStateAllCurrentAxisMappingsSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisMappingsStateAvailableAxisMappings (long pdata, int dataTotalLength, String mapping_uid, String name);

    private native int     nativeSetSkyControllerAxisMappingsStateAllAvailableAxisMappingsSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisFiltersStateCurrentAxisFilters (long pdata, int dataTotalLength, int axis_id, String filter_uid_or_builder);

    private native int     nativeSetSkyControllerAxisFiltersStateAllCurrentFiltersSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerAxisFiltersStatePresetAxisFilters (long pdata, int dataTotalLength, String filter_uid, String name);

    private native int     nativeSetSkyControllerAxisFiltersStateAllPresetFiltersSent (long pdata, int dataTotalLength);

    private native int     nativeSetSkyControllerCoPilotingStatePilotingSource (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_COPILOTINGSTATE_PILOTINGSOURCE_SOURCE_ENUM source);

    private native int     nativeSetSkyControllerCalibrationStateMagnetoCalibrationState (long pdata, int dataTotalLength, ARCOMMANDS_SKYCONTROLLER_CALIBRATIONSTATE_MAGNETOCALIBRATIONSTATE_STATUS_ENUM status, byte X_Quality, byte Y_Quality, byte Z_Quality);

    private native int     nativeSetSkyControllerCalibrationStateMagnetoCalibrationQualityUpdatesState (long pdata, int dataTotalLength, byte enabled);

    private native int     nativeSetSkyControllerButtonEventsSettings (long pdata, int dataTotalLength);


    private native int     nativeSetCommonNetworkDisconnect (long pdata, int dataTotalLength);

    private native int     nativeSetCommonSettingsAllSettings (long pdata, int dataTotalLength);

    private native int     nativeSetCommonSettingsReset (long pdata, int dataTotalLength);

    private native int     nativeSetCommonSettingsProductName (long pdata, int dataTotalLength, String name);

    private native int     nativeSetCommonSettingsCountry (long pdata, int dataTotalLength, String code);

    private native int     nativeSetCommonSettingsAutoCountry (long pdata, int dataTotalLength, byte automatic);

    private native int     nativeSetCommonCommonAllStates (long pdata, int dataTotalLength);

    private native int     nativeSetCommonCommonCurrentDate (long pdata, int dataTotalLength, String date);

    private native int     nativeSetCommonCommonCurrentTime (long pdata, int dataTotalLength, String time);

    private native int     nativeSetCommonCommonReboot (long pdata, int dataTotalLength);

    private native int     nativeSetCommonOverHeatSwitchOff (long pdata, int dataTotalLength);

    private native int     nativeSetCommonOverHeatVentilate (long pdata, int dataTotalLength);

    private native int     nativeSetCommonControllerIsPiloting (long pdata, int dataTotalLength, byte piloting);

    private native int     nativeSetCommonWifiSettingsOutdoorSetting (long pdata, int dataTotalLength, byte outdoor);

    private native int     nativeSetCommonMavlinkStart (long pdata, int dataTotalLength, String filepath, ARCOMMANDS_COMMON_MAVLINK_START_TYPE_ENUM type);

    private native int     nativeSetCommonMavlinkPause (long pdata, int dataTotalLength);

    private native int     nativeSetCommonMavlinkStop (long pdata, int dataTotalLength);

    private native int     nativeSetCommonCalibrationMagnetoCalibration (long pdata, int dataTotalLength, byte calibrate);

    private native int     nativeSetCommonGPSControllerPositionForRun (long pdata, int dataTotalLength, double latitude, double longitude);

    private native int     nativeSetCommonAudioControllerReadyForStreaming (long pdata, int dataTotalLength, byte ready);

    private native int     nativeSetCommonHeadlightsIntensity (long pdata, int dataTotalLength, byte left, byte right);

    private native int     nativeSetCommonAnimationsStartAnimation (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM anim);

    private native int     nativeSetCommonAnimationsStopAnimation (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_ANIMATIONS_STOPANIMATION_ANIM_ENUM anim);

    private native int     nativeSetCommonAnimationsStopAllAnimations (long pdata, int dataTotalLength);

    private native int     nativeSetCommonAccessoryConfig (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_ACCESSORY_CONFIG_ACCESSORY_ENUM accessory);

    private native int     nativeSetCommonChargerSetMaxChargeRate (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_CHARGER_SETMAXCHARGERATE_RATE_ENUM rate);

    private native int     nativeSetCommonNetworkEventDisconnection (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_NETWORKEVENT_DISCONNECTION_CAUSE_ENUM cause);

    private native int     nativeSetCommonSettingsStateAllSettingsChanged (long pdata, int dataTotalLength);

    private native int     nativeSetCommonSettingsStateResetChanged (long pdata, int dataTotalLength);

    private native int     nativeSetCommonSettingsStateProductNameChanged (long pdata, int dataTotalLength, String name);

    private native int     nativeSetCommonSettingsStateProductVersionChanged (long pdata, int dataTotalLength, String software, String hardware);

    private native int     nativeSetCommonSettingsStateProductSerialHighChanged (long pdata, int dataTotalLength, String high);

    private native int     nativeSetCommonSettingsStateProductSerialLowChanged (long pdata, int dataTotalLength, String low);

    private native int     nativeSetCommonSettingsStateCountryChanged (long pdata, int dataTotalLength, String code);

    private native int     nativeSetCommonSettingsStateAutoCountryChanged (long pdata, int dataTotalLength, byte automatic);

    private native int     nativeSetCommonCommonStateAllStatesChanged (long pdata, int dataTotalLength);

    private native int     nativeSetCommonCommonStateBatteryStateChanged (long pdata, int dataTotalLength, byte percent);

    private native int     nativeSetCommonCommonStateMassStorageStateListChanged (long pdata, int dataTotalLength, byte mass_storage_id, String name);

    private native int     nativeSetCommonCommonStateMassStorageInfoStateListChanged (long pdata, int dataTotalLength, byte mass_storage_id, int size, int used_size, byte plugged, byte full, byte internal);

    private native int     nativeSetCommonCommonStateCurrentDateChanged (long pdata, int dataTotalLength, String date);

    private native int     nativeSetCommonCommonStateCurrentTimeChanged (long pdata, int dataTotalLength, String time);

    private native int     nativeSetCommonCommonStateMassStorageInfoRemainingListChanged (long pdata, int dataTotalLength, int free_space, short rec_time, int photo_remaining);

    private native int     nativeSetCommonCommonStateWifiSignalChanged (long pdata, int dataTotalLength, short rssi);

    private native int     nativeSetCommonCommonStateSensorsStatesListChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME_ENUM sensorName, byte sensorState);

    private native int     nativeSetCommonCommonStateProductModel (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_COMMONSTATE_PRODUCTMODEL_MODEL_ENUM model);

    private native int     nativeSetCommonCommonStateCountryListKnown (long pdata, int dataTotalLength, byte listFlags, String countryCodes);

    private native int     nativeSetCommonOverHeatStateOverHeatChanged (long pdata, int dataTotalLength);

    private native int     nativeSetCommonOverHeatStateOverHeatRegulationChanged (long pdata, int dataTotalLength, byte regulationType);

    private native int     nativeSetCommonWifiSettingsStateOutdoorSettingsChanged (long pdata, int dataTotalLength, byte outdoor);

    private native int     nativeSetCommonMavlinkStateMavlinkFilePlayingStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_STATE_ENUM state, String filepath, ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKFILEPLAYINGSTATECHANGED_TYPE_ENUM type);

    private native int     nativeSetCommonMavlinkStateMavlinkPlayErrorStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_MAVLINKSTATE_MAVLINKPLAYERRORSTATECHANGED_ERROR_ENUM error);

    private native int     nativeSetCommonCalibrationStateMagnetoCalibrationStateChanged (long pdata, int dataTotalLength, byte xAxisCalibration, byte yAxisCalibration, byte zAxisCalibration, byte calibrationFailed);

    private native int     nativeSetCommonCalibrationStateMagnetoCalibrationRequiredState (long pdata, int dataTotalLength, byte required);

    private native int     nativeSetCommonCalibrationStateMagnetoCalibrationAxisToCalibrateChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_CALIBRATIONSTATE_MAGNETOCALIBRATIONAXISTOCALIBRATECHANGED_AXIS_ENUM axis);

    private native int     nativeSetCommonCalibrationStateMagnetoCalibrationStartedChanged (long pdata, int dataTotalLength, byte started);

    private native int     nativeSetCommonCameraSettingsStateCameraSettingsChanged (long pdata, int dataTotalLength, float fov, float panMax, float panMin, float tiltMax, float tiltMin);

    private native int     nativeSetCommonFlightPlanStateAvailabilityStateChanged (long pdata, int dataTotalLength, byte AvailabilityState);

    private native int     nativeSetCommonFlightPlanStateComponentStateListChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_FLIGHTPLANSTATE_COMPONENTSTATELISTCHANGED_COMPONENT_ENUM component, byte State);

    private native int     nativeSetCommonFlightPlanEventStartingErrorEvent (long pdata, int dataTotalLength);

    private native int     nativeSetCommonFlightPlanEventSpeedBridleEvent (long pdata, int dataTotalLength);

    private native int     nativeSetCommonARLibsVersionsStateControllerLibARCommandsVersion (long pdata, int dataTotalLength, String version);

    private native int     nativeSetCommonARLibsVersionsStateSkyControllerLibARCommandsVersion (long pdata, int dataTotalLength, String version);

    private native int     nativeSetCommonARLibsVersionsStateDeviceLibARCommandsVersion (long pdata, int dataTotalLength, String version);

    private native int     nativeSetCommonAudioStateAudioStreamingRunning (long pdata, int dataTotalLength, byte running);

    private native int     nativeSetCommonHeadlightsStateIntensityChanged (long pdata, int dataTotalLength, byte left, byte right);

    private native int     nativeSetCommonAnimationsStateList (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_ANIMATIONSSTATE_LIST_ANIM_ENUM anim, ARCOMMANDS_COMMON_ANIMATIONSSTATE_LIST_STATE_ENUM state, ARCOMMANDS_COMMON_ANIMATIONSSTATE_LIST_ERROR_ENUM error);

    private native int     nativeSetCommonAccessoryStateSupportedAccessoriesListChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_ACCESSORYSTATE_SUPPORTEDACCESSORIESLISTCHANGED_ACCESSORY_ENUM accessory);

    private native int     nativeSetCommonAccessoryStateAccessoryConfigChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_ACCESSORYSTATE_ACCESSORYCONFIGCHANGED_NEWACCESSORY_ENUM newAccessory, ARCOMMANDS_COMMON_ACCESSORYSTATE_ACCESSORYCONFIGCHANGED_ERROR_ENUM error);

    private native int     nativeSetCommonAccessoryStateAccessoryConfigModificationEnabled (long pdata, int dataTotalLength, byte enabled);

    private native int     nativeSetCommonChargerStateMaxChargeRateChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_CHARGERSTATE_MAXCHARGERATECHANGED_RATE_ENUM rate);

    private native int     nativeSetCommonChargerStateCurrentChargeStateChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_CHARGERSTATE_CURRENTCHARGESTATECHANGED_STATUS_ENUM status, ARCOMMANDS_COMMON_CHARGERSTATE_CURRENTCHARGESTATECHANGED_PHASE_ENUM phase);

    private native int     nativeSetCommonChargerStateLastChargeRateChanged (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_CHARGERSTATE_LASTCHARGERATECHANGED_RATE_ENUM rate);

    private native int     nativeSetCommonChargerStateChargingInfo (long pdata, int dataTotalLength, ARCOMMANDS_COMMON_CHARGERSTATE_CHARGINGINFO_PHASE_ENUM phase, ARCOMMANDS_COMMON_CHARGERSTATE_CHARGINGINFO_RATE_ENUM rate, byte intensity, byte fullChargingTime);

    private native int     nativeSetCommonRunStateRunIdChanged (long pdata, int dataTotalLength, String runId);


    private native int     nativeSetCommonDebugStatsSendPacket (long pdata, int dataTotalLength, String packet);

    private native int     nativeSetCommonDebugStatsStartSendingPacketFromDrone (long pdata, int dataTotalLength, byte frequency, byte packetSize, int date);

    private native int     nativeSetCommonDebugStatsStopSendingPacketFromDrone (long pdata, int dataTotalLength);

    private native int     nativeSetCommonDebugDebugSettingsGetAll (long pdata, int dataTotalLength);

    private native int     nativeSetCommonDebugDebugSettingsSet (long pdata, int dataTotalLength, short id, String value);

    private native int     nativeSetCommonDebugStatsEventSendPacket (long pdata, int dataTotalLength, String packet);

    private native int     nativeSetCommonDebugDebugSettingsStateInfo (long pdata, int dataTotalLength, byte listFlags, short id, String label, ARCOMMANDS_COMMONDEBUG_DEBUGSETTINGSSTATE_INFO_TYPE_ENUM type, ARCOMMANDS_COMMONDEBUG_DEBUGSETTINGSSTATE_INFO_MODE_ENUM mode, String range_min, String range_max, String range_step, String value);

    private native int     nativeSetCommonDebugDebugSettingsStateListChanged (long pdata, int dataTotalLength, short id, String value);


    private native int     nativeSetProProBoughtFeatures (long pdata, int dataTotalLength, long features);

    private native int     nativeSetProProResponse (long pdata, int dataTotalLength, byte listFlags, String signedChallenge);

    private native int     nativeSetProProActivateFeatures (long pdata, int dataTotalLength, long features);

    private native int     nativeSetProProStateSupportedFeatures (long pdata, int dataTotalLength, ARCOMMANDS_PRO_PROSTATE_SUPPORTEDFEATURES_STATUS_ENUM status, long features);

    private native int     nativeSetProProStateFeaturesActivated (long pdata, int dataTotalLength, long features);

    private native int     nativeSetProProEventChallengeEvent (long pdata, int dataTotalLength, String challenge);


    private native int     nativeSetWifiScan (long pdata, int dataTotalLength, byte band);

    private native int     nativeSetWifiUpdateAuthorizedChannels (long pdata, int dataTotalLength);

    private native int     nativeSetWifiSetApChannel (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_SELECTION_TYPE_ENUM type, ARCOMMANDS_WIFI_BAND_ENUM band, byte channel);

    private native int     nativeSetWifiSetSecurity (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM type, String key, ARCOMMANDS_WIFI_SECURITY_KEY_TYPE_ENUM key_type);

    private native int     nativeSetWifiSetCountry (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_COUNTRY_SELECTION_ENUM selection_mode, String code);

    private native int     nativeSetWifiSetEnvironement (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_ENVIRONEMENT_ENUM environement);

    private native int     nativeSetWifiScannedItem (long pdata, int dataTotalLength, String ssid, short rssi, ARCOMMANDS_WIFI_BAND_ENUM band, byte channel, byte list_flags);

    private native int     nativeSetWifiAuthorizedChannel (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_BAND_ENUM band, byte channel, byte environement, byte list_flags);

    private native int     nativeSetWifiApChannelChanged (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_SELECTION_TYPE_ENUM type, ARCOMMANDS_WIFI_BAND_ENUM band, byte channel);

    private native int     nativeSetWifiSecurityChanged (long pdata, int dataTotalLength, String key, ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM key_type);

    private native int     nativeSetWifiCountryChanged (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_COUNTRY_SELECTION_ENUM selection_mode, String code);

    private native int     nativeSetWifiEnvironementChanged (long pdata, int dataTotalLength, ARCOMMANDS_WIFI_ENVIRONEMENT_ENUM environement);

    private native int     nativeSetWifiRssiChanged (long pdata, int dataTotalLength, short rssi);


}
