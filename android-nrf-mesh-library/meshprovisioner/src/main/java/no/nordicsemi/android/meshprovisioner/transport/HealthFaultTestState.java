package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling HealthFaultTestState messages.
 */
@SuppressWarnings("WeakerAccess")
class HealthFaultTestState extends GenericMessageState implements LowerTransportLayerCallbacks {

    private static final String TAG = HealthFaultTestState.class.getSimpleName();

    /**
     * Constructs {@link HealthFaultTestState}
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent
     *                        to
     * @param HealthFaultTest Wrapper class {@link HealthFaultTest} containing the
     *                        opcode and parameters for {@link HealthFaultTest}
     *                        message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal
     *                        callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    HealthFaultTestState(@NonNull final Context context, @NonNull final byte[] src, @NonNull final byte[] dst,
            @NonNull final HealthFaultTest healthFaultTest, @NonNull final MeshTransport meshTransport,
            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), healthFaultTest, meshTransport,
                callbacks);
    }

    /**
     * Constructs {@link HealthFaultTestState}
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent
     *                        to
     * @param HealthFaultTest Wrapper class {@link HealthFaultTest} containing the
     *                        opcode and parameters for {@link HealthFaultTest}
     *                        message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal
     *                        callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    HealthFaultTestState(@NonNull final Context context, final int src, final int dst,
            @NonNull final HealthFaultTest healthFaultTest, @NonNull final MeshTransport meshTransport,
            @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, healthFaultTest, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.HEALTH_FAULT_TEST_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final HealthFaultTest healthFaultTest = (HealthFaultTest) mMeshMessage;
        final byte[] key = healthFaultTest.getAppKey();
        final int akf = healthFaultTest.getAkf();
        final int aid = healthFaultTest.getAid();
        final int aszmic = healthFaultTest.getAszmic();
        final int opCode = healthFaultTest.getOpCode();
        final byte[] parameters = healthFaultTest.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending Health Fault Test acknowledged");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
