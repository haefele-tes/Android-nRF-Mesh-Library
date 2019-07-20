package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling GenericLevelGet messages.
 */
@SuppressWarnings("unused")
class GenericUserPropertyGetState extends GenericMessageState {

    private static final String TAG = GenericUserPropertyGetState.class.getSimpleName();

    /**
     * Constructs GenericLevelGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericUserPropertyGet Wrapper class {@link GenericUserPropertyGet} containing the opcode and parameters for {@link GenericUserPropertyGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    GenericUserPropertyGetState(@NonNull final Context context,
                                @NonNull final byte[] src,
                                @NonNull final byte[] dst,
                                @NonNull final GenericUserPropertyGet genericUserPropertyGet,
                                @NonNull final MeshTransport meshTransport,
                                @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), genericUserPropertyGet, meshTransport, callbacks);
        createAccessMessage();
    }

    /**
     * Constructs GenericLevelGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericUserPropertyGet Wrapper class {@link GenericUserPropertyGet} containing the opcode and parameters for {@link GenericUserPropertyGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericUserPropertyGetState(@NonNull final Context context,
                                final int src,
                                final int dst,
                                @NonNull final GenericUserPropertyGet genericUserPropertyGet,
                                @NonNull final MeshTransport meshTransport,
                                @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, genericUserPropertyGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_USER_PROPERTY_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericUserPropertyGet genericUserPropertyGet = (GenericUserPropertyGet) mMeshMessage;
        final byte[] key = genericUserPropertyGet.getAppKey();
        final int akf = genericUserPropertyGet.getAkf();
        final int aid = genericUserPropertyGet.getAid();
        final int aszmic = genericUserPropertyGet.getAszmic();
        final int opCode = genericUserPropertyGet.getOpCode();
        final byte[] parameters = genericUserPropertyGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Generic UserProperty get");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
