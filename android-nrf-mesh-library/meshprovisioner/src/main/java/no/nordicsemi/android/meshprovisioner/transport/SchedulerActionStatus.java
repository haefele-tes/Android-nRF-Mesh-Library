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
import android.util.Log;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;

import no.nordicsemi.android.meshprovisioner.data.ScheduleEntry;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitReader;
import no.nordicsemi.android.meshprovisioner.utils.MeshAddress;
import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * To be used as a wrapper class for when creating the SchedulerActionStatus Message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class SchedulerActionStatus extends GenericStatusMessage implements Parcelable {

    private static final String TAG = SchedulerActionStatus.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.SCHEDULER_ACTION_STATUS;
    private static final int SCHEDULER_ACTION_STATUS_LENGTH = 10;



    private int index;
    private ScheduleEntry entry;

    private static final Creator<SchedulerActionStatus> CREATOR = new Creator<SchedulerActionStatus>() {
        @Override
        public SchedulerActionStatus createFromParcel(Parcel in) {
            final AccessMessage message = in.readParcelable(AccessMessage.class.getClassLoader());
            //noinspection ConstantConditions
            return new SchedulerActionStatus(message);
        }

        @Override
        public SchedulerActionStatus[] newArray(int size) {
            return new SchedulerActionStatus[size];
        }
    };

    /**
     * Constructs the SchedulerActionStatus mMessage.
     *
     * @param message Access Message
     */
    public SchedulerActionStatus(@NonNull final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
     void parseStatusParameters() {
        Log.d(TAG, "Received scheduler action status from: " + MeshAddress.formatAddress(mMessage.getSrc(), true));

        if (mParameters.length == SCHEDULER_ACTION_STATUS_LENGTH) {
            BitReader bitReader = new BitReader(ArrayUtils.reverseArray(mParameters));
            try {
                entry = new ScheduleEntry(bitReader);
            } catch (InvalidParameterException e) {
                Log.d(TAG, "Couldn't parse ScheduleEntry.");
            }
            index = (int) bitReader.getBits(4);
            Log.d(TAG, "Scheduler action status has index: "+index);
            Log.d(TAG, "Scheduler action status has entry: "+entry.toString());
        }
    }

    @Override
    int getOpCode() {
        return OP_CODE;
    }



    /**
     * Bit field defining an entry in the Schedule Register
     */
    public ScheduleEntry getEntry() {
        return entry;
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
