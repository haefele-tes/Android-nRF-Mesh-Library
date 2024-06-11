package no.nordicsemi.android.meshprovisioner.transport;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling GenericAccessMessage messages.
 */
@SuppressWarnings("WeakerAccess")
class GenericAccessMessageState extends GenericMessageState {

    private static final String TAG = GenericAccessMessageState.class.getSimpleName();

    /**
     * Constructs GenericAccessMessageState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericAccessMessage Wrapper class {@link GenericAccessMessage} containing the opcode and parameters for {@link GenericAccessMessage} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    GenericAccessMessageState(@NonNull final Context context,
                              @NonNull final byte[] src,
                              @NonNull final byte[] dst,
                              @NonNull final GenericAccessMessage genericAccessMessage,
                              @NonNull final MeshTransport meshTransport,
                              @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), genericAccessMessage, meshTransport, callbacks);
    }

    /**
     * Constructs GenericLevelGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericAccessMessage Wrapper class {@link GenericAccessMessage} containing the opcode and parameters for {@link GenericAccessMessage} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericAccessMessageState(@NonNull final Context context,
                              final int src,
                              final int dst,
                              @NonNull final GenericAccessMessage genericAccessMessage,
                              @NonNull final MeshTransport meshTransport,
                              @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, genericAccessMessage, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_ACCESS_MESSAGE_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericAccessMessage genericAccessMessage = (GenericAccessMessage) mMeshMessage;
        final byte[] key = genericAccessMessage.getKey();
        final int akf = genericAccessMessage.getAkf();
        final int aid = genericAccessMessage.getAid();
        final int aszmic = genericAccessMessage.getAszmic();
        final int opCode = genericAccessMessage.getOpCode();
        final byte[] parameters = genericAccessMessage.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        genericAccessMessage.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Generic Access Message");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
