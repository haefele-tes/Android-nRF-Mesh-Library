package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling GenericOnPowerUpSetState messages.
 */
@SuppressWarnings("WeakerAccess")
class GenericOnPowerUpSetState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = GenericOnPowerUpSetState.class.getSimpleName();

    /**
     * Constructs {@link GenericOnPowerUpSetState}
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericOnPowerUpSet Wrapper class {@link GenericOnPowerUpSet} containing the opcode and parameters for {@link GenericOnPowerUpSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of {@link #GenericOnPowerUpSetState(Context, int, int, GenericOnPowerUpSet, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    GenericOnPowerUpSetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final GenericOnPowerUpSet genericOnPowerUpSet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), genericOnPowerUpSet, meshTransport, callbacks);
    }

    /**
     * Constructs {@link GenericOnPowerUpSetState}
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericOnPowerUpSet Wrapper class {@link GenericOnPowerUpSet} containing the opcode and parameters for {@link GenericOnPowerUpSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericOnPowerUpSetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final GenericOnPowerUpSet genericOnPowerUpSet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, genericOnPowerUpSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_ON_POWER_UP_SET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericOnPowerUpSet genericOnPowerUpSet = (GenericOnPowerUpSet) mMeshMessage;
        final byte[] key = genericOnPowerUpSet.getAppKey();
        final int akf = genericOnPowerUpSet.getAkf();
        final int aid = genericOnPowerUpSet.getAid();
        final int aszmic = genericOnPowerUpSet.getAszmic();
        final int opCode = genericOnPowerUpSet.getOpCode();
        final byte[] parameters = genericOnPowerUpSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending Generic OnOff set acknowledged");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
