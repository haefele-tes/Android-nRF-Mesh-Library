package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling TimeRoleGet messages.
 */
@SuppressWarnings("unused")
class TimeRoleGetState extends GenericMessageState {

    private static final String TAG = TimeRoleGetState.class.getSimpleName();

    /**
     * Constructs TimeRoleGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param timeRoleGet Wrapper class {@link TimeRoleGet} containing the opcode and parameters for {@link TimeRoleGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TimeRoleGetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final TimeRoleGet timeRoleGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), timeRoleGet, meshTransport, callbacks);        createAccessMessage();
    }

    /**
     * Constructs TimeRoleGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param timeRoleGet Wrapper class {@link TimeRoleGet} containing the opcode and parameters for {@link TimeRoleGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TimeRoleGetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final TimeRoleGet timeRoleGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, timeRoleGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.TIME_ROLE_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
         final TimeRoleGet timeRoleGet = (TimeRoleGet) mMeshMessage;
        final byte[] key = timeRoleGet.getAppKey();
        final int akf = timeRoleGet.getAkf();
        final int aid = timeRoleGet.getAid();
        final int aszmic = timeRoleGet.getAszmic();
        final int opCode = timeRoleGet.getOpCode();
        final byte[] parameters = timeRoleGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        timeRoleGet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending time role get");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
