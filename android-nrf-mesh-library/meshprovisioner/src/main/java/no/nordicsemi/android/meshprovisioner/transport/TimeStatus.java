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
import androidx.annotation.Nullable;

import no.nordicsemi.android.meshprovisioner.data.TimeZoneOffset;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitReader;

/**
 * To be used as a wrapper class for when creating the TimeStatus Message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class TimeStatus extends GenericStatusMessage implements Parcelable {

    private static final String TAG = TimeStatus.class.getSimpleName();
    private static final int TIME_STATUS_MANDATORY_LENGTH = 5;

    private static final int OP_CODE = ApplicationMessageOpCodes.TIME_STATUS;
    private Long taiSeconds;
    private Short subSecond;
    private Short uncertainty;
    private Boolean timeAuthority;
    private Short utcDelta;
    private TimeZoneOffset timeZoneOffset;

    private static final Creator<TimeStatus> CREATOR = new Creator<TimeStatus>() {
        @Override
        public TimeStatus createFromParcel(Parcel in) {
            final AccessMessage message = in.readParcelable(AccessMessage.class.getClassLoader());
            //noinspection ConstantConditions
            return new TimeStatus(message);
        }

        @Override
        public TimeStatus[] newArray(int size) {
            return new TimeStatus[size];
        }
    };

    /**
     * Constructs the TimeStatus mMessage.
     *
     * @param message Access Message
     */
    public TimeStatus(@NonNull final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
    void parseStatusParameters() {
        BitReader bitReader = new BitReader(ArrayUtils.reverseArray(mParameters));
        if (bitReader.bitsLeft() == TIME_BIT_SIZE) {
            timeZoneOffset = TimeZoneOffset.of((byte) bitReader.getBits(TIME_ZONE_OFFSET_BIT_SIZE));
            utcDelta = (short) (bitReader.getBits(UTC_DELTA_BIT_SIZE) & 0xffff);
            timeAuthority = bitReader.getBits(TIME_AUTHORITY_BIT_SIZE) == 1;
            uncertainty = (short) (bitReader.getBits(UNCERTAINTY_BIT_SIZE) & 0xff);
            subSecond = (short) (bitReader.getBits(SUB_SECOND_BIT_SIZE) & 0xff);
            taiSeconds = bitReader.getBits(TAI_SECONDS_BIT_SIZE);
        } else {
            taiSeconds = 0L;
            subSecond = 0;
            uncertainty = 0;
            timeAuthority = false;
            utcDelta = 0;
            timeZoneOffset = TimeZoneOffset.of((byte) 0x00);
        }
    }

    @Override
    int getOpCode() {
        return OP_CODE;
    }

    @Nullable
    public Long getTaiSeconds() {
        return taiSeconds;
    }

    @Nullable
    public Short getSubSecond() {
        return subSecond;
    }

    @Nullable
    public Short getUncertainty() {
        return uncertainty;
    }

    @Nullable
    public Boolean isTimeAuthority() {
        return timeAuthority;
    }

    @Nullable
    public Short getUtcDelta() {
        return utcDelta;
    }

    @Nullable
    public TimeZoneOffset getTimeZoneOffset() {
        return timeZoneOffset;
    }

    static final int TAI_SECONDS_BIT_SIZE = 40;
    static final int SUB_SECOND_BIT_SIZE = 8;
    static final int UNCERTAINTY_BIT_SIZE = 8;
    static final int TIME_AUTHORITY_BIT_SIZE = 1;
	static final int PADDING_BIT_SIZE = 1;
    static final int UTC_DELTA_BIT_SIZE = 15;
    static final int TIME_ZONE_OFFSET_BIT_SIZE = 8;
    static final int TIME_BIT_SIZE = TAI_SECONDS_BIT_SIZE + SUB_SECOND_BIT_SIZE + UNCERTAINTY_BIT_SIZE + TIME_AUTHORITY_BIT_SIZE
            + UTC_DELTA_BIT_SIZE + TIME_ZONE_OFFSET_BIT_SIZE;

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
