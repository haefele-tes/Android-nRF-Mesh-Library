package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling TaiUtcDeltaSetState messages.
 */
@SuppressWarnings("unused")
class TaiUtcDeltaSetState extends GenericMessageState {

    private static final String TAG = TaiUtcDeltaSetState.class.getSimpleName();

    /**
     * Constructs TaiUtcDeltaSetStateState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param taiUtcDeltaSet Wrapper class {@link TaiUtcDeltaSetState} containing the opcode and parameters for {@link TaiUtcDeltaSetState} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TaiUtcDeltaSetState(@NonNull final Context context,
                        @NonNull final byte[] src,
                        @NonNull final byte[] dst,
                        @NonNull final TaiUtcDeltaSet taiUtcDeltaSet,
                        @NonNull final MeshTransport meshTransport,
                        @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), taiUtcDeltaSet, meshTransport, callbacks);
        createAccessMessage();
    }

    /**
     * Constructs TaiUtcDeltaSetStateState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param taiUtcDeltaSet Wrapper class {@link TaiUtcDeltaSetState} containing the opcode and parameters for {@link TaiUtcDeltaSetState} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TaiUtcDeltaSetState(@NonNull final Context context,
                        final int src,
                        final int dst,
                        @NonNull final TaiUtcDeltaSet taiUtcDeltaSet,
                        @NonNull final MeshTransport meshTransport,
                        @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, taiUtcDeltaSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.TAI_UTC_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final TaiUtcDeltaSet taiUtcDeltaSet = (TaiUtcDeltaSet) mMeshMessage;
        final byte[] key = taiUtcDeltaSet.getAppKey();
        final int akf = taiUtcDeltaSet.getAkf();
        final int aid = taiUtcDeltaSet.getAid();
        final int aszmic = taiUtcDeltaSet.getAszmic();
        final int opCode = taiUtcDeltaSet.getOpCode();
        final byte[] parameters = taiUtcDeltaSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        taiUtcDeltaSet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending time get");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
