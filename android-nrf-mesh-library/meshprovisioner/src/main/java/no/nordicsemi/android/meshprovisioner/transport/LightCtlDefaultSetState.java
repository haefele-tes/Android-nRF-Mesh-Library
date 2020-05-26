package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling LightCtlDefaultSet messages.
 */
@SuppressWarnings("WeakerAccess")
class LightCtlDefaultSetState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = LightCtlDefaultSetState.class.getSimpleName();

    /**
     * Constructs {@link LightCtlDefaultSetState}
     *
     * @param context     Context of the application
     * @param dst         Destination address to which the message must be sent to
     * @param lightCtlDefaultSet Wrapper class {@link LightCtlDefaultSetState} containing the opcode and parameters for {@link LightCtlDefaultSetState} message
     * @param callbacks   {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    LightCtlDefaultSetState(@NonNull final Context context,
                     @NonNull final byte[] src,
                     @NonNull final byte[] dst,
                     @NonNull final LightCtlDefaultSet lightCtlDefaultSet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), lightCtlDefaultSet, meshTransport, callbacks);
    }

    /**
     * Constructs {@link LightCtlDefaultSetState}
     *
     * @param context     Context of the application
     * @param dst         Destination address to which the message must be sent to
     * @param lightCtlDefaultSet Wrapper class {@link LightCtlDefaultSetState} containing the opcode and parameters for {@link LightCtlDefaultSetState} message
     * @param callbacks   {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    LightCtlDefaultSetState(@NonNull final Context context,
                     final int src,
                     final int dst,
                     @NonNull final LightCtlDefaultSet lightCtlDefaultSet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, lightCtlDefaultSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.LIGHT_CTL_DEFAULT_SET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final LightCtlDefaultSet lightCtlDefaultSet = (LightCtlDefaultSet) mMeshMessage;
        final byte[] key = lightCtlDefaultSet.getAppKey();
        final int akf = lightCtlDefaultSet.getAkf();
        final int aid = lightCtlDefaultSet.getAid();
        final int aszmic = lightCtlDefaultSet.getAszmic();
        final int opCode = lightCtlDefaultSet.getOpCode();
        final byte[] parameters = lightCtlDefaultSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        lightCtlDefaultSet.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending light ctl set acknowledged ");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
