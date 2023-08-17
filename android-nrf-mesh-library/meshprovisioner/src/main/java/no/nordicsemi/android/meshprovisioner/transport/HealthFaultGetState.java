package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling HealthFaultGetState messages.
 */
@SuppressWarnings("WeakerAccess")
class HealthFaultGetState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = HealthFaultGetState.class.getSimpleName();

    /**
     * Constructs {@link HealthFaultGetState}
     *
     * @param context        Context of the application
     * @param src            Source address
     * @param dst            Destination address to which the message must be sent
     *                       to
     * @param HealthFaultGet Wrapper class {@link HealthFaultGet} containing the
     *                       opcode and parameters for {@link HealthFaultGet}
     *                       message
     * @param callbacks      {@link InternalMeshMsgHandlerCallbacks} for internal
     *                       callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     * @deprecated in favour of
     *             {@link #HealthFaultGetState(Context, int, int, HealthFaultGet, MeshTransport, InternalMeshMsgHandlerCallbacks)}
     */
    @Deprecated
    HealthFaultGetState(@NonNull final Context context, @NonNull final byte[] src, @NonNull final byte[] dst,
            @NonNull final HealthFaultGet healthFaultGet, @NonNull final MeshTransport meshTransport,
            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), healthFaultGet, meshTransport,
                callbacks);
    }

    /**
     * Constructs {@link HealthFaultGetState}
     *
     * @param context        Context of the application
     * @param src            Source address
     * @param dst            Destination address to which the message must be sent
     *                       to
     * @param HealthFaultGet Wrapper class {@link HealthFaultGet} containing the
     *                       opcode and parameters for {@link HealthFaultGet}
     *                       message
     * @param callbacks      {@link InternalMeshMsgHandlerCallbacks} for internal
     *                       callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    HealthFaultGetState(@NonNull final Context context, final int src, final int dst,
            @NonNull final HealthFaultGet healthFaultGet, @NonNull final MeshTransport meshTransport,
            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, healthFaultGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.HEALTH_FAULT_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final HealthFaultGet healthFaultGet = (HealthFaultGet) mMeshMessage;
        final byte[] key = healthFaultGet.getAppKey();
        final int akf = healthFaultGet.getAkf();
        final int aid = healthFaultGet.getAid();
        final int aszmic = healthFaultGet.getAszmic();
        final int opCode = healthFaultGet.getOpCode();
        final byte[] parameters = healthFaultGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending Health Fault Get acknowledged");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
