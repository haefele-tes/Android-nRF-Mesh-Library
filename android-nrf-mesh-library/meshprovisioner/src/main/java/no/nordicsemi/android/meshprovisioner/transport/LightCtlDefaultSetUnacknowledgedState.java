package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling LightCtlDefaultSetUnacknowledged messages.
 */
@SuppressWarnings("WeakerAccess")
class LightCtlDefaultSetUnacknowledgedState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = LightCtlDefaultSetUnacknowledgedState.class.getSimpleName();

    /**
     * Constructs {@link LightCtlDefaultSetUnacknowledgedState}
     *
     * @param context                   Context of the application
     * @param src                       Source address
     * @param dst                       Destination address to which the message must be sent to
     * @param lightCtlDefaultSetUnacknowledged Wrapper class {@link LightCtlDefaultSetUnacknowledged} containing the opcode and parameters for {@link LightCtlDefaultSetUnacknowledged} message
     * @param callbacks                 {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of {@link #LightCtlDefaultSetUnacknowledgedState(Context, int, int, LightCtlDefaultSetUnacknowledged, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    LightCtlDefaultSetUnacknowledgedState(@NonNull final Context context,
                                   @NonNull final byte[] src,
                                   @NonNull final byte[] dst,
                                   @NonNull final LightCtlDefaultSetUnacknowledged lightCtlDefaultSetUnacknowledged,
                                   @NonNull final MeshTransport meshTransport,
                                   @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightCtlDefaultSetUnacknowledged, meshTransport, callbacks);
    }

    /**
     * Constructs {@link LightCtlDefaultSetUnacknowledgedState}
     *
     * @param context                   Context of the application
     * @param src                       Source address
     * @param dst                       Destination address to which the message must be sent to
     * @param lightCtlDefaultSetUnacknowledged Wrapper class {@link LightCtlDefaultSetUnacknowledged} containing the opcode and parameters for {@link LightCtlDefaultSetUnacknowledged} message
     * @param callbacks                 {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightCtlDefaultSetUnacknowledgedState(@NonNull final Context context,
                                   final int src,
                                   final int dst,
                                   @NonNull final LightCtlDefaultSetUnacknowledged lightCtlDefaultSetUnacknowledged,
                                   @NonNull final MeshTransport meshTransport,
                                   @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightCtlDefaultSetUnacknowledged, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_CTL_DEFAULT_SET_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightCtlDefaultSetUnacknowledged lightCtlDefaultSetUnacknowledged = (LightCtlDefaultSetUnacknowledged) mMeshMessage;
        final byte[] key = lightCtlDefaultSetUnacknowledged.getAppKey();
        final int akf = lightCtlDefaultSetUnacknowledged.getAkf();
        final int aid = lightCtlDefaultSetUnacknowledged.getAid();
        final int aszmic = lightCtlDefaultSetUnacknowledged.getAszmic();
        final int opCode = lightCtlDefaultSetUnacknowledged.getOpCode();
        final byte[] parameters = lightCtlDefaultSetUnacknowledged.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightCtlDefaultSetUnacknowledged.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending light ctl set acknowledged ");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null) {
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
            }
        }
    }
}
