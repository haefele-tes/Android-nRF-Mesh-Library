package no.nordicsemi.android.meshprovisioner.transport;


import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling LightHslDefaultGetState messages.
 */
class LightHslDefaultGetState extends GenericMessageState {

    private static final String TAG = LightHslDefaultGetState.class.getSimpleName();

    /**
     * Constructs LightHslDefaultGetState
     *
     * @param context     Context of the application
     * @param src         Source address
     * @param dst         Destination address to which the message must be sent to
     * @param lightHslDefaultGet Wrapper class {@link LightHslDefaultGet} containing the opcode and parameters for {@link LightHslDefaultGet} message
     * @param callbacks   {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of {@link LightHslDefaultGet}
     */
    @Deprecated
    LightHslDefaultGetState(@NonNull final Context context,
                     @NonNull final byte[] src,
                     @NonNull final byte[] dst,
                     @NonNull final LightHslDefaultGet lightHslDefaultGet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightHslDefaultGet, meshTransport, callbacks);
        createAccessMessage();
    }

    /**
     * Constructs LightHslDefaultGetState
     *
     * @param context     Context of the application
     * @param src         Source address
     * @param dst         Destination address to which the message must be sent to
     * @param lightHslDefaultGet Wrapper class {@link LightHslDefaultGet} containing the opcode and parameters for {@link LightHslDefaultGet} message
     * @param callbacks   {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightHslDefaultGetState(@NonNull final Context context,
                     final int src,
                     final int dst,
                     @NonNull final LightHslDefaultGet lightHslDefaultGet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightHslDefaultGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_HSL_DEFAULT_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightHslDefaultGet lightHslDefaultGet = (LightHslDefaultGet) mMeshMessage;
        final byte[] key = lightHslDefaultGet.getAppKey();
        final int akf = lightHslDefaultGet.getAkf();
        final int aid = lightHslDefaultGet.getAid();
        final int aszmic = lightHslDefaultGet.getAszmic();
        final int opCode = lightHslDefaultGet.getOpCode();
        final byte[] parameters = lightHslDefaultGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightHslDefaultGet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Light Hsl default get");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
