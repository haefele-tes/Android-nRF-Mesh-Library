package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling GenericLevelSet messages.
 */
@SuppressWarnings("WeakerAccess")
class LightLightnessDefaultSetUnacknowledgedState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = LightLightnessDefaultSetUnacknowledgedState.class.getSimpleName();

    /**
     * Constructs {@link LightLightnessDefaultSetUnacknowledgedState}
     *
     * @param context                         Context of the application
     * @param src                             Source address
     * @param dst                             Destination address to which the message must be sent to
     * @param lightLightnessDefaultSetUnacknowledged Wrapper class {@link LightLightnessDefaultSetUnacknowledged} containing the opcode and parameters for {@link GenericLevelSet} message
     * @param callbacks                       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    LightLightnessDefaultSetUnacknowledgedState(@NonNull final Context context,
                                         @NonNull final byte[] src,
                                         @NonNull final byte[] dst,
                                         @NonNull final LightLightnessDefaultSetUnacknowledged lightLightnessDefaultSetUnacknowledged,
                                         @NonNull final MeshTransport meshTransport,
                                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightLightnessDefaultSetUnacknowledged, meshTransport, callbacks);
    }

    /**
     * Constructs {@link LightLightnessDefaultSetUnacknowledgedState}
     *
     * @param context                         Context of the application
     * @param src                             Source address
     * @param dst                             Destination address to which the message must be sent to
     * @param lightLightnessDefaultSetUnacknowledged Wrapper class {@link LightLightnessDefaultSetUnacknowledged} containing the opcode and parameters for {@link GenericLevelSet} message
     * @param callbacks                       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightLightnessDefaultSetUnacknowledgedState(@NonNull final Context context,
                                         final int src,
                                         final int dst,
                                         @NonNull final LightLightnessDefaultSetUnacknowledged lightLightnessDefaultSetUnacknowledged,
                                         @NonNull final MeshTransport meshTransport,
                                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightLightnessDefaultSetUnacknowledged, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_LIGHTNESS_DEFAULT_SET_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightLightnessDefaultSetUnacknowledged lightLightnessDefaultSetUnacknowledged = (LightLightnessDefaultSetUnacknowledged) mMeshMessage;
        final byte[] key = lightLightnessDefaultSetUnacknowledged.getAppKey();
        final int akf = lightLightnessDefaultSetUnacknowledged.getAkf();
        final int aid = lightLightnessDefaultSetUnacknowledged.getAid();
        final int aszmic = lightLightnessDefaultSetUnacknowledged.getAszmic();
        final int opCode = lightLightnessDefaultSetUnacknowledged.getOpCode();
        final byte[] parameters = lightLightnessDefaultSetUnacknowledged.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightLightnessDefaultSetUnacknowledged.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending Generic Level set acknowledged ");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null) {
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
            }
        }
    }
}
