package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling LightCtlSetState messages.
 */
@SuppressWarnings("WeakerAccess")
class LightHslDefaultSetUnacknowledgedState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = LightHslDefaultSetUnacknowledgedState.class.getSimpleName();

    /**
     * Constructs {@link LightHslDefaultSetUnacknowledgedState}
     *
     * @param context                   Context of the application
     * @param src                       Source address
     * @param dst                       Destination address to which the message must be sent to
     * @param lightHslDefaultSetUnacknowledged Wrapper class {@link LightLightnessSet} containing the opcode and parameters for {@link GenericLevelSet} message
     * @param callbacks                 {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated  in favour of {@link #LightHslDefaultSetUnacknowledgedState(Context, int, int, LightHslDefaultSetUnacknowledged, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    LightHslDefaultSetUnacknowledgedState(@NonNull final Context context,
                                   @NonNull final byte[] src,
                                   @NonNull final byte[] dst,
                                   @NonNull final LightHslDefaultSetUnacknowledged lightHslDefaultSetUnacknowledged,
                                   @NonNull final MeshTransport meshTransport,
                                   @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightHslDefaultSetUnacknowledged, meshTransport, callbacks);
    }

    /**
     * Constructs {@link LightHslDefaultSetUnacknowledgedState}
     *
     * @param context                   Context of the application
     * @param src                       Source address
     * @param dst                       Destination address to which the message must be sent to
     * @param lightHslDefaultSetUnacknowledged Wrapper class {@link LightLightnessSet} containing the opcode and parameters for {@link GenericLevelSet} message
     * @param callbacks                 {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightHslDefaultSetUnacknowledgedState(@NonNull final Context context,
                                   final int src,
                                   final int dst,
                                   @NonNull final LightHslDefaultSetUnacknowledged lightHslDefaultSetUnacknowledged,
                                   @NonNull final MeshTransport meshTransport,
                                   @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightHslDefaultSetUnacknowledged, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_HSL_DEFAULT_SET_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightHslDefaultSetUnacknowledged lightHslDefaultSetUnacknowledged = (LightHslDefaultSetUnacknowledged) mMeshMessage;
        final byte[] key = lightHslDefaultSetUnacknowledged.getAppKey();
        final int akf = lightHslDefaultSetUnacknowledged.getAkf();
        final int aid = lightHslDefaultSetUnacknowledged.getAid();
        final int aszmic = lightHslDefaultSetUnacknowledged.getAszmic();
        final int opCode = lightHslDefaultSetUnacknowledged.getOpCode();
        final byte[] parameters = lightHslDefaultSetUnacknowledged.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightHslDefaultSetUnacknowledged.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending light hsl set acknowledged ");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null) {
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
            }
        }
    }
}
