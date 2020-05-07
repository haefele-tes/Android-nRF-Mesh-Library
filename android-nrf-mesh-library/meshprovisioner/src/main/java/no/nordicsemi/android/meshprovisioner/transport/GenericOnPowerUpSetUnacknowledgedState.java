package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling GenericOnPowerUpSetState messages.
 */
@SuppressWarnings("WeakerAccess")
class GenericOnPowerUpSetUnacknowledgedState extends GenericMessageState {

    private static final String TAG = GenericOnPowerUpSetUnacknowledgedState.class.getSimpleName();

    /**
     * Constructs {@link GenericOnPowerUpSetState}
     *
     * @param context                       Context of the application
     * @param src                           Source address
     * @param dst                           Destination address to which the message must be sent to
     * @param genericOnPowerUpSetUnacknowledged Wrapper class {@link GenericOnPowerUpSetUnacknowledged} containing the opcode and parameters for {@link GenericOnPowerUpSetUnacknowledged} message
     * @param callbacks                     {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    GenericOnPowerUpSetUnacknowledgedState(@NonNull final Context context,
                                       @NonNull final byte[] src,
                                       @NonNull final byte[] dst,
                                       @NonNull final GenericOnPowerUpSetUnacknowledged genericOnPowerUpSetUnacknowledged,
                                       @NonNull final MeshTransport meshTransport,
                                       @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), genericOnPowerUpSetUnacknowledged, meshTransport, callbacks);
    }

    /**
     * Constructs {@link GenericOnPowerUpSetState}
     *
     * @param context                       Context of the application
     * @param src                           Source address
     * @param dst                           Destination address to which the message must be sent to
     * @param genericOnPowerUpSetUnacknowledged Wrapper class {@link GenericOnPowerUpSetUnacknowledged} containing the opcode and parameters for {@link GenericOnPowerUpSetUnacknowledged} message
     * @param callbacks                     {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericOnPowerUpSetUnacknowledgedState(@NonNull final Context context,
                                       final int src,
                                       final int dst,
                                       @NonNull final GenericOnPowerUpSetUnacknowledged genericOnPowerUpSetUnacknowledged,
                                       @NonNull final MeshTransport meshTransport,
                                       @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, genericOnPowerUpSetUnacknowledged, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_ON_POWER_UP_SET_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericOnPowerUpSetUnacknowledged genericOnPowerUpSetUnacknowledged = (GenericOnPowerUpSetUnacknowledged) mMeshMessage;
        final byte[] key = genericOnPowerUpSetUnacknowledged.getAppKey();
        final int akf = genericOnPowerUpSetUnacknowledged.getAkf();
        final int aid = genericOnPowerUpSetUnacknowledged.getAid();
        final int aszmic = genericOnPowerUpSetUnacknowledged.getAszmic();
        final int opCode = genericOnPowerUpSetUnacknowledged.getOpCode();
        final byte[] parameters = genericOnPowerUpSetUnacknowledged.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Generic OnOff set unacknowledged: ");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null) {
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
            }
        }
    }
}
