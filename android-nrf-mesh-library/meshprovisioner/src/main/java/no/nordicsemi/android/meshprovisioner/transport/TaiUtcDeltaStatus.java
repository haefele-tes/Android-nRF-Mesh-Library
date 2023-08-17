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

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitReader;

/**
 * To be used as a wrapper class for when creating the TaiUtcDeltaStatus Message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class TaiUtcDeltaStatus extends GenericStatusMessage implements Parcelable {

    private static final String TAG = TaiUtcDeltaStatus.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TIME_STATUS;



    private Long taiDeltaChange;
    private Byte taiUtcDeltaCurrent;
    private Byte taiUtcDeltaNew;

    private static final Creator<TaiUtcDeltaStatus> CREATOR = new Creator<TaiUtcDeltaStatus>() {
        @Override
        public TaiUtcDeltaStatus createFromParcel(Parcel in) {
            final AccessMessage message = in.readParcelable(AccessMessage.class.getClassLoader());
            //noinspection ConstantConditions
            return new TaiUtcDeltaStatus(message);
        }

        @Override
        public TaiUtcDeltaStatus[] newArray(int size) {
            return new TaiUtcDeltaStatus[size];
        }
    };

    /**
     * Constructs the TaiUtcDeltaStatus mMessage.
     *
     * @param message Access Message
     */
    public TaiUtcDeltaStatus(@NonNull final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
     void parseStatusParameters() {
        BitReader bitReader = new BitReader(ArrayUtils.reverseArray(mParameters));
        if (bitReader.bitsLeft() == TIME_BIT_SIZE) {
            taiDeltaChange = (long) (bitReader.getBits(TAI_DELTA_CHANGE_BIT_SIZE));
            bitReader.getBits(1);
            taiUtcDeltaNew = (byte) bitReader.getBits(TAI_UTC_DELTA_NEW_BIT_SIZE);
            bitReader.getBits(1);
            taiUtcDeltaCurrent = (byte) bitReader.getBits(TAI_UTC_DELTA_CURRENT_BIT_SIZE);
        } else {
            taiDeltaChange = 0L;
            taiUtcDeltaNew = 0;
            taiUtcDeltaCurrent = 0;
        }
    }

    @Override
    int getOpCode() {
        return OP_CODE;
    }



    public long getTaiDeltaChange() {
        return taiDeltaChange;
    }

    public byte getTaiUtcDeltaCurrent() { return taiUtcDeltaCurrent; }

    public byte getTaiUtcDeltaNew() { return taiUtcDeltaNew; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        final AccessMessage message = (AccessMessage) mMessage;
        dest.writeParcelable(message, flags);
    }

    static final int TAI_DELTA_CHANGE_BIT_SIZE = 40;
    static final int TAI_UTC_DELTA_CURRENT_BIT_SIZE = 15;
    static final int TAI_UTC_DELTA_NEW_BIT_SIZE = 15;

    static final int TIME_BIT_SIZE = TAI_UTC_DELTA_CURRENT_BIT_SIZE + TAI_UTC_DELTA_NEW_BIT_SIZE + 1 + TAI_UTC_DELTA_CURRENT_BIT_SIZE + 1;

}
