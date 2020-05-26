package no.nordicsemi.android.meshprovisioner.transport;


import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling LightLightnessDefaultGetState messages.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
class LightLightnessDefaultGetState extends GenericMessageState {

    private static final String TAG = LightLightnessDefaultGetState.class.getSimpleName();

    /**
     * Constructs LightLightnessDefaultGetState
     *
     * @param context           Context of the application
     * @param src               Source address
     * @param dst               Destination address to which the message must be sent to
     * @param lightLightnessDefaultGet Wrapper class {@link LightLightnessDefaultGet} containing the opcode and parameters for {@link LightLightnessDefaultGet} message
     * @param callbacks         {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    LightLightnessDefaultGetState(@NonNull final Context context,
                           @NonNull final byte[] src,
                           @NonNull final byte[] dst,
                           @NonNull final LightLightnessDefaultGet lightLightnessDefaultGet,
                           @NonNull final MeshTransport meshTransport,
                           @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightLightnessDefaultGet, meshTransport, callbacks);
    }

    /**
     * Constructs LightLightnessDefaultGetState
     *
     * @param context           Context of the application
     * @param src               Source address
     * @param dst               Destination address to which the message must be sent to
     * @param lightLightnessDefaultGet Wrapper class {@link LightLightnessDefaultGet} containing the opcode and parameters for {@link LightLightnessDefaultGet} message
     * @param callbacks         {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightLightnessDefaultGetState(@NonNull final Context context,
                           final int src,
                           final int dst,
                           @NonNull final LightLightnessDefaultGet lightLightnessDefaultGet,
                           @NonNull final MeshTransport meshTransport,
                           @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightLightnessDefaultGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_LIGHTNESS_DEFAULT_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightLightnessDefaultGet lightLightnessDefaultGet = (LightLightnessDefaultGet) mMeshMessage;
        final byte[] key = lightLightnessDefaultGet.getAppKey();
        final int akf = lightLightnessDefaultGet.getAkf();
        final int aid = lightLightnessDefaultGet.getAid();
        final int aszmic = lightLightnessDefaultGet.getAszmic();
        final int opCode = lightLightnessDefaultGet.getOpCode();
        final byte[] parameters = lightLightnessDefaultGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightLightnessDefaultGet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Light Lightness default get");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
