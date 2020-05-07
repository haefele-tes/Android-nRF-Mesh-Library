package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling GenericOnPowerUpGet messages.
 */
@SuppressWarnings("unused")
class GenericOnPowerUpGetState extends GenericMessageState {

    private static final String TAG = GenericOnPowerUpGetState.class.getSimpleName();

    /**
     * Constructs GenericOnPowerUpGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericOnPowerUpGet Wrapper class {@link GenericOnPowerUpGet} containing the opcode and parameters for {@link GenericOnPowerUpGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    GenericOnPowerUpGetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final GenericOnPowerUpGet genericOnPowerUpGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), genericOnPowerUpGet, meshTransport, callbacks);
        createAccessMessage();
    }

    /**
     * Constructs GenericOnPowerUpGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param genericOnPowerUpGet Wrapper class {@link GenericOnPowerUpGet} containing the opcode and parameters for {@link GenericOnPowerUpGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericOnPowerUpGetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final GenericOnPowerUpGet genericOnPowerUpGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, genericOnPowerUpGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_ON_POWER_UP_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericOnPowerUpGet genericOnPowerUpGet = (GenericOnPowerUpGet) mMeshMessage;
        final byte[] key = genericOnPowerUpGet.getAppKey();
        final int akf = genericOnPowerUpGet.getAkf();
        final int aid = genericOnPowerUpGet.getAid();
        final int aszmic = genericOnPowerUpGet.getAszmic();
        final int opCode = genericOnPowerUpGet.getOpCode();
        final byte[] parameters = genericOnPowerUpGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Generic OnPowerUp get");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
