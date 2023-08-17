package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling HealthAttentionGet messages.
 */
@SuppressWarnings("unused")
class HealthAttentionGetState extends GenericMessageState {

    private static final String TAG = HealthAttentionGetState.class.getSimpleName();

    /**
     * Constructs HealthAttentionGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param healthAttentionGet Wrapper class {@link HealthAttentionGet} containing the opcode and parameters for {@link HealthAttentionGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    HealthAttentionGetState(@NonNull final Context context,
                            @NonNull final byte[] src,
                            @NonNull final byte[] dst,
                            @NonNull final HealthAttentionGet healthAttentionGet,
                            @NonNull final MeshTransport meshTransport,
                            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), healthAttentionGet, meshTransport, callbacks);        createAccessMessage();
    }

    /**
     * Constructs HealthAttentionGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param healthAttentionGet Wrapper class {@link HealthAttentionGet} containing the opcode and parameters for {@link HealthAttentionGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    HealthAttentionGetState(@NonNull final Context context,
                            final int src,
                            final int dst,
                            @NonNull final HealthAttentionGet healthAttentionGet,
                            @NonNull final MeshTransport meshTransport,
                            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, healthAttentionGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.HEALTH_ATTENTION_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final HealthAttentionGet healthAttentionGet = (HealthAttentionGet) mMeshMessage;
        final byte[] key = healthAttentionGet.getAppKey();
        final int akf = healthAttentionGet.getAkf();
        final int aid = healthAttentionGet.getAid();
        final int aszmic = healthAttentionGet.getAszmic();
        final int opCode = healthAttentionGet.getOpCode();
        final byte[] parameters = healthAttentionGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        healthAttentionGet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Health Attention get");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
