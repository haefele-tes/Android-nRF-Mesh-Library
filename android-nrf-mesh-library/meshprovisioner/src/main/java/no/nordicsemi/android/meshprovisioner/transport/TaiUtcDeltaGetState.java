package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

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
     * @param TaiUtcDeltaGet Wrapper class {@link TaiUtcDeltaGet} containing the opcode and parameters for {@link TaiUtcDeltaGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TaiUtcDeltaGetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final TaiUtcDeltaGet TaiUtcDeltaGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), TaiUtcDeltaGet, meshTransport, callbacks);        createAccessMessage();
    }

    /**
     * Constructs TaiUtcDeltaGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TaiUtcDeltaGet Wrapper class {@link TaiUtcDeltaGet} containing the opcode and parameters for {@link TaiUtcDeltaGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TaiUtcDeltaGetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final TaiUtcDeltaGet TaiUtcDeltaGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, TaiUtcDeltaGet, meshTransport, callbacks);
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
        final TaiUtcDeltaGet TaiUtcDeltaGet = (TaiUtcDeltaGet) mMeshMessage;
        final byte[] key = TaiUtcDeltaGet.getAppKey();
        final int akf = TaiUtcDeltaGet.getAkf();
        final int aid = TaiUtcDeltaGet.getAid();
        final int aszmic = TaiUtcDeltaGet.getAszmic();
        final int opCode = TaiUtcDeltaGet.getOpCode();
        final byte[] parameters = TaiUtcDeltaGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
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
