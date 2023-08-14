package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling GenericLevelGet messages.
 */
@SuppressWarnings("unused")
class TimeGetState extends GenericMessageState {

    private static final String TAG = TimeGetState.class.getSimpleName();

    /**
     * Constructs GenericLevelGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TimeGet Wrapper class {@link TimeGet} containing the opcode and parameters for {@link TimeGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TimeGetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final TimeGet TimeGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), TimeGet, meshTransport, callbacks);        createAccessMessage();
    }

    /**
     * Constructs GenericLevelGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TimeGet Wrapper class {@link TimeGet} containing the opcode and parameters for {@link TimeGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TimeGetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final TimeGet TimeGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, TimeGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.TIME_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final TimeGet TimeGet = (TimeGet) mMeshMessage;
        final byte[] key = TimeGet.getAppKey();
        final int akf = TimeGet.getAkf();
        final int aid = TimeGet.getAid();
        final int aszmic = TimeGet.getAszmic();
        final int opCode = TimeGet.getOpCode();
        final byte[] parameters = TimeGet.getParameters();
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
