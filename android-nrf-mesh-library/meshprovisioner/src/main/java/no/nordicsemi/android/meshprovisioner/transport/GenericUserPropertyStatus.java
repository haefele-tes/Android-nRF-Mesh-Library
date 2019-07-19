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
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.MeshAddress;
import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * To be used as a wrapper class for when creating the GenericOnOffStatus Message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class GenericUserPropertyStatus extends GenericStatusMessage implements Parcelable {

    private static final String TAG = GenericUserPropertyStatus.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_USER_PROPERTY_STATUS;
    private static final int GENERIC_USER_PROPERTY_STATUS_MANDATORY_LENGTH = 2;

    private int userPropertyKey;
    private Integer access;
    private byte[] data;

    private static final Creator<GenericUserPropertyStatus> CREATOR = new Creator<GenericUserPropertyStatus>() {
        @Override
        public GenericUserPropertyStatus createFromParcel(Parcel in) {
            final AccessMessage message = in.readParcelable(AccessMessage.class.getClassLoader());
            //noinspection ConstantConditions
            return new GenericUserPropertyStatus(message);
        }

        @Override
        public GenericUserPropertyStatus[] newArray(int size) {
            return new GenericUserPropertyStatus[size];
        }
    };

    /**
     * Constructs the GenericOnOffStatus mMessage.
     *
     * @param message Access Message
     */
    public GenericUserPropertyStatus(@NonNull final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
    void parseStatusParameters() {
        Log.v(TAG, "Received generic user property status from: " + MeshAddress.formatAddress(mMessage.getSrc(), true));
        final ByteBuffer buffer = ByteBuffer.wrap(mParameters).order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(0);
        userPropertyKey = buffer.getShort() & 0xFFFF;
        if (buffer.limit() > GENERIC_USER_PROPERTY_STATUS_MANDATORY_LENGTH) {
            access = buffer.get() & 0xFF;
            byte[] b = new byte[buffer.remaining()];
            buffer.get(b);
            data = b;
        }
        Log.v(TAG, "property key: " + userPropertyKey + "\naccess: " + access + "\ndata: " + MeshParserUtils.bytesToHex(data, false));
    }

    @Override
    int getOpCode() {
        return OP_CODE;
    }

    /**
     * Returns the present state of the GenericOnOffModel
     *
     * @return true if on and false other wise
     */
    public final byte[] getData() {
        return data;
    }

    public final Integer getAccess() {
        return access;
    }

    public final int getUserPropertyKey() {
        return userPropertyKey;
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
