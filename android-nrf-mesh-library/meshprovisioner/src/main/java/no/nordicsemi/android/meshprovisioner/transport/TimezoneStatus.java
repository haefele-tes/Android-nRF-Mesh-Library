/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.meshprovisioner.transport;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.data.TimeZoneOffset;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;

/**
 * To be used as a wrapper class for when creating the TimezoneStatus Message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class TimezoneStatus extends GenericStatusMessage implements Parcelable {

    private static final String TAG = TimezoneStatus.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TIME_ZONE_STATUS;

    private static final int TIME_ZONE_STATUS_LENGTH = 7;
    private TimeZoneOffset currentTimeZoneOffset = TimeZoneOffset.of((byte) 0x40);
    private TimeZoneOffset newTimeZoneOffset = TimeZoneOffset.of((byte) 0x40);
    private long timeOfChange = 0x0;

    private static final Creator<TimezoneStatus> CREATOR = new Creator<TimezoneStatus>() {
        @Override
        public TimezoneStatus createFromParcel(Parcel in) {
            final AccessMessage message = in.readParcelable(AccessMessage.class.getClassLoader());
            //noinspection ConstantConditions
            return new TimezoneStatus(message);
        }

        @Override
        public TimezoneStatus[] newArray(int size) {
            return new TimezoneStatus[size];
        }
    };

    /**
     * Constructs the TimezoneStatus mMessage.
     *
     * @param message Access Message
     */
    public TimezoneStatus(@NonNull final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
     void parseStatusParameters() {
        if (mParameters.length == TIME_ZONE_STATUS_LENGTH) {
            final ByteBuffer buffer = ByteBuffer.wrap(mParameters).order(ByteOrder.LITTLE_ENDIAN);
            currentTimeZoneOffset = TimeZoneOffset.of(buffer.get());
            newTimeZoneOffset = TimeZoneOffset.of(buffer.get());
            timeOfChange = ((long) buffer.getInt() & 0xFFFFFFFFl) | (((long) buffer.get() & 0xFF) << 32);
        }
    }

    @Override
    int getOpCode() {
        return OP_CODE;
    }

    /**
     * Returns the Current Time Zone Offset
     *
     * @return a TimeZoneOffset instance
     */
    @NonNull
    public TimeZoneOffset getCurrentTimeZoneOffset() {
        return currentTimeZoneOffset;
    }

    /**
     * Returns the New Time Zone Offset
     *
     * @return a TimeZoneOffset instance
     */
    @NonNull
    public TimeZoneOffset getNewTimeZoneOffset() {
        return newTimeZoneOffset;
    }

    /**
     * Returns the point in time (using the TAI Seconds format) when the New Time Zone Offset shall be applied.
     *
     * @return TAI in seconds
     */
    public long getTimeOfChange() {
        return timeOfChange;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        final AccessMessage message = (AccessMessage) mMessage;
        dest.writeParcelable(message, flags);
    }
}
