package no.nordicsemi.android.meshprovisioner.transport;


import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling LightCtlDefaultGetState messages.
 */
@SuppressWarnings("WeakerAccess")
class LightCtlDefaultGetState extends GenericMessageState {

    private static final String TAG = LightCtlDefaultGetState.class.getSimpleName();

    /**
     * Constructs LightCtlDefaultGetState
     *
     * @param context     Context of the application
     * @param src         Source address
     * @param dst         Destination address to which the message must be sent to
     * @param lightCtlDefaultGet Wrapper class {@link LightCtlDefaultGet} containing the opcode and parameters for {@link LightCtlDefaultGet} message
     * @param callbacks   {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of {@link #LightCtlDefaultGetState(Context, int, int, LightCtlDefaultGet, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    LightCtlDefaultGetState(@NonNull final Context context,
                     @NonNull final byte[] src,
                     @NonNull final byte[] dst,
                     @NonNull final LightCtlDefaultGet lightCtlDefaultGet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightCtlDefaultGet, meshTransport, callbacks);
    }

    /**
     * Constructs LightCtlDefaultGetState
     *
     * @param context     Context of the application
     * @param src         Source address
     * @param dst         Destination address to which the message must be sent to
     * @param lightCtlDefaultGet Wrapper class {@link LightCtlDefaultGet} containing the opcode and parameters for {@link LightCtlDefaultGet} message
     * @param callbacks   {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightCtlDefaultGetState(@NonNull final Context context,
                     final int src,
                     final int dst,
                     @NonNull final LightCtlDefaultGet lightCtlDefaultGet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightCtlDefaultGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_CTL_DEFAULT_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightCtlDefaultGet lightCtlDefaultGet = (LightCtlDefaultGet) mMeshMessage;
        final byte[] key = lightCtlDefaultGet.getAppKey();
        final int akf = lightCtlDefaultGet.getAkf();
        final int aid = lightCtlDefaultGet.getAid();
        final int aszmic = lightCtlDefaultGet.getAszmic();
        final int opCode = lightCtlDefaultGet.getOpCode();
        final byte[] parameters = lightCtlDefaultGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightCtlDefaultGet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Light Ctl default get");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
