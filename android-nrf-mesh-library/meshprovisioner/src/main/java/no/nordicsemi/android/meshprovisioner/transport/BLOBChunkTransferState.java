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

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

@SuppressWarnings("WeakerAccess")
class BLOBChunkTransferState extends ConfigMessageState {

    private static final String TAG = BLOBChunkTransferState.class.getSimpleName();
    private final byte[] mDeviceKey;


    /**
     * Constructs the state for BLOBChunkTransfer message
     */
    BLOBChunkTransferState(@NonNull final Context context,
                                  final int src,
                                  final int dst,
                                  @NonNull final byte[] deviceKey,
                                  @NonNull final BLOBChunkTransfer blobChunkTransfer,
                                  @NonNull final MeshTransport meshTransport,
                                  @NonNull final InternalMeshMsgHandlerCallbacks callbacks) {
        super(context, blobChunkTransfer, meshTransport, callbacks);
        this.mSrc = src;
        this.mDst = dst;
        this.mDeviceKey = deviceKey;
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.BLOB_CHUNK_TRANSFER_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final BLOBChunkTransfer blobChunkTransfer = (BLOBChunkTransfer) mMeshMessage;
        final int akf = blobChunkTransfer.getAkf();
        final int aid = blobChunkTransfer.getAid();
        final int aszmic = blobChunkTransfer.getAszmic();
        final int opCode = blobChunkTransfer.getOpCode();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, mDeviceKey, akf, aid, aszmic, opCode, blobChunkTransfer.getParameters());
        blobChunkTransfer.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending composition data get");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0 && mMeshStatusCallbacks != null) {
            mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
