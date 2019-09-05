package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling HealthAttentionSetState messages.
 */
@SuppressWarnings("WeakerAccess")
class HealthAttentionSetState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = HealthAttentionSetState.class.getSimpleName();

    /**
     * Constructs {@link HealthAttentionSetState}
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param HealthAttentionSet Wrapper class {@link HealthAttentionSet} containing the opcode and parameters for {@link HealthAttentionSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of {@link #HealthAttentionSetState(Context, int, int, HealthAttentionSet, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    HealthAttentionSetState(@NonNull final Context context,
                            @NonNull final byte[] src,
                            @NonNull final byte[] dst,
                            @NonNull final HealthAttentionSet HealthAttentionSet,
                            @NonNull final MeshTransport meshTransport,
                            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), HealthAttentionSet, meshTransport, callbacks);
    }

    /**
     * Constructs {@link HealthAttentionSetState}
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param HealthAttentionSet Wrapper class {@link HealthAttentionSet} containing the opcode and parameters for {@link HealthAttentionSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    HealthAttentionSetState(@NonNull final Context context,
                            final int src,
                            final int dst,
                            @NonNull final HealthAttentionSet HealthAttentionSet,
                            @NonNull final MeshTransport meshTransport,
                            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, HealthAttentionSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.HEALTH_ATTENTION_SET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final HealthAttentionSet HealthAttentionSet = (HealthAttentionSet) mMeshMessage;
        final byte[] key = HealthAttentionSet.getAppKey();
        final int akf = HealthAttentionSet.getAkf();
        final int aid = HealthAttentionSet.getAid();
        final int aszmic = HealthAttentionSet.getAszmic();
        final int opCode = HealthAttentionSet.getOpCode();
        final byte[] parameters = HealthAttentionSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending Health Attention set acknowledged");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
