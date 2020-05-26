package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling GenericLevelSet messages.
 */
@SuppressWarnings("WeakerAccess")
class LightLightnessDefaultSetState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = LightLightnessDefaultSetState.class.getSimpleName();

    /**
     * Constructs {@link LightLightnessDefaultSetState}
     *
     * @param context           Context of the application
     * @param src               Source address
     * @param dst               Destination address to which the message must be sent to
     * @param lightLightnessDefaultSet Wrapper class {@link LightLightnessDefaultSet} containing the opcode and parameters for {@link GenericLevelSet} message
     * @param callbacks         {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of {@link #LightLightnessDefaultSetState(Context, int, int, LightLightnessDefaultSet, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    LightLightnessDefaultSetState(@NonNull final Context context,
                           @NonNull final byte[] src,
                           @NonNull final byte[] dst,
                           @NonNull final LightLightnessDefaultSet lightLightnessDefaultSet,
                           @NonNull final MeshTransport meshTransport,
                           @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightLightnessDefaultSet, meshTransport, callbacks);
    }

    /**
     * Constructs {@link LightLightnessDefaultSetState}
     *
     * @param context           Context of the application
     * @param src               Source address
     * @param dst               Destination address to which the message must be sent to
     * @param lightLightnessDefaultSet Wrapper class {@link LightLightnessDefaultSet} containing the opcode and parameters for {@link GenericLevelSet} message
     * @param callbacks         {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightLightnessDefaultSetState(@NonNull final Context context,
                           final int src,
                           final int dst,
                           @NonNull final LightLightnessDefaultSet lightLightnessDefaultSet,
                           @NonNull final MeshTransport meshTransport,
                           @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightLightnessDefaultSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_LIGHTNESS_DEFAULT_SET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightLightnessDefaultSet lightLightnessDefaultSet = (LightLightnessDefaultSet) mMeshMessage;
        final byte[] key = lightLightnessDefaultSet.getAppKey();
        final int akf = lightLightnessDefaultSet.getAkf();
        final int aid = lightLightnessDefaultSet.getAid();
        final int aszmic = lightLightnessDefaultSet.getAszmic();
        final int opCode = lightLightnessDefaultSet.getOpCode();
        final byte[] parameters = lightLightnessDefaultSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightLightnessDefaultSet.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending light lightness set acknowledged");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
