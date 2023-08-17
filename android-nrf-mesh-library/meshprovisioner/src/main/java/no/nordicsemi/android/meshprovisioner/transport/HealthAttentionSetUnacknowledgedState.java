package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling HealthAttentionSetState messages.
 */
@SuppressWarnings("WeakerAccess")
class HealthAttentionSetUnacknowledgedState extends GenericMessageState {

    private static final String TAG = HealthAttentionSetUnacknowledgedState.class.getSimpleName();

    /**
     * Constructs {@link HealthAttentionSetState}
     *
     * @param context                       Context of the application
     * @param src                           Source address
     * @param dst                           Destination address to which the message must be sent to
     * @param healthAttentionSetUnacknowledged Wrapper class {@link HealthAttentionSetUnacknowledged} containing the opcode and parameters for {@link HealthAttentionSetUnacknowledged} message
     * @param callbacks                     {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    HealthAttentionSetUnacknowledgedState(@NonNull final Context context,
                                          @NonNull final byte[] src,
                                          @NonNull final byte[] dst,
                                          @NonNull final HealthAttentionSetUnacknowledged healthAttentionSetUnacknowledged,
                                          @NonNull final MeshTransport meshTransport,
                                          @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), healthAttentionSetUnacknowledged, meshTransport, callbacks);
    }

    /**
     * Constructs {@link HealthAttentionSetState}
     *
     * @param context                       Context of the application
     * @param src                           Source address
     * @param dst                           Destination address to which the message must be sent to
     * @param healthAttentionSetUnacknowledged Wrapper class {@link HealthAttentionSetUnacknowledged} containing the opcode and parameters for {@link HealthAttentionSetUnacknowledged} message
     * @param callbacks                     {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    HealthAttentionSetUnacknowledgedState(@NonNull final Context context,
                                          final int src,
                                          final int dst,
                                          @NonNull final HealthAttentionSetUnacknowledged healthAttentionSetUnacknowledged,
                                          @NonNull final MeshTransport meshTransport,
                                          @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, healthAttentionSetUnacknowledged, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.HEALTH_ATTENTION_SET_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final HealthAttentionSetUnacknowledged healthAttentionSetUnacknowledged = (HealthAttentionSetUnacknowledged) mMeshMessage;
        final byte[] key = healthAttentionSetUnacknowledged.getAppKey();
        final int akf = healthAttentionSetUnacknowledged.getAkf();
        final int aid = healthAttentionSetUnacknowledged.getAid();
        final int aszmic = healthAttentionSetUnacknowledged.getAszmic();
        final int opCode = healthAttentionSetUnacknowledged.getOpCode();
        final byte[] parameters = healthAttentionSetUnacknowledged.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        healthAttentionSetUnacknowledged.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Health Attention set unacknowledged: ");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null) {
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
            }
        }
    }
}
