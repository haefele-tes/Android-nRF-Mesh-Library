package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling BLOBBlockStartState messages.
 */
@SuppressWarnings("unused")
class BLOBBlockStartState extends GenericMessageState {

    private static final String TAG = BLOBBlockStartState.class.getSimpleName();


    /**
     * Constructs BLOBBlockStartStateState
     */
    BLOBBlockStartState(@NonNull final Context context,
                        final int src,
                        final int dst,
                        @NonNull final BLOBBlockStart blobBlockStart,
                        @NonNull final MeshTransport meshTransport,
                        @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, blobBlockStart, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.BLOB_BLOCK_START_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final BLOBBlockStart blobBlockStart = (BLOBBlockStart) mMeshMessage;
        final byte[] key = blobBlockStart.getAppKey();
        final int akf = blobBlockStart.getAkf();
        final int aid = blobBlockStart.getAid();
        final int aszmic = blobBlockStart.getAszmic();
        final int opCode = blobBlockStart.getOpCode();
        final byte[] parameters = blobBlockStart.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        blobBlockStart.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending BLOB block start");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
