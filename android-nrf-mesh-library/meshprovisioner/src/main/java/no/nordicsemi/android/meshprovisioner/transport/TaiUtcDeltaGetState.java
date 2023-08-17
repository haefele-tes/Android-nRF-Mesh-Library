package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling TaiUtcDeltaGet messages.
 */
@SuppressWarnings("unused")
class TaiUtcDeltaGetState extends GenericMessageState {

    private static final String TAG = TaiUtcDeltaGetState.class.getSimpleName();

    /**
     * Constructs TaiUtcDeltaGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param taiUtcDeltaGet Wrapper class {@link TaiUtcDeltaGet} containing the opcode and parameters for {@link TaiUtcDeltaGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TaiUtcDeltaGetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final TaiUtcDeltaGet taiUtcDeltaGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), taiUtcDeltaGet, meshTransport, callbacks);        createAccessMessage();
    }

    /**
     * Constructs TaiUtcDeltaGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param taiUtcDeltaGet Wrapper class {@link TaiUtcDeltaGet} containing the opcode and parameters for {@link TaiUtcDeltaGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TaiUtcDeltaGetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final TaiUtcDeltaGet taiUtcDeltaGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, taiUtcDeltaGet, meshTransport, callbacks);
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
        final TaiUtcDeltaGet taiUtcDeltaGet = (TaiUtcDeltaGet) mMeshMessage;
        final byte[] key = taiUtcDeltaGet.getAppKey();
        final int akf = taiUtcDeltaGet.getAkf();
        final int aid = taiUtcDeltaGet.getAid();
        final int aszmic = taiUtcDeltaGet.getAszmic();
        final int opCode = taiUtcDeltaGet.getOpCode();
        final byte[] parameters = taiUtcDeltaGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        taiUtcDeltaGet.setMessage(message);
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
