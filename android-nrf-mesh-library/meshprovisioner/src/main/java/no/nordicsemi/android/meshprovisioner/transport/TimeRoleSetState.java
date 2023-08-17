package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling TimeRoleGet messages.
 */
@SuppressWarnings("unused")
class TimeRoleSetState extends GenericMessageState {

    private static final String TAG = TimeRoleSetState.class.getSimpleName();

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
    TimeRoleSetState(@NonNull final Context context,
                     @NonNull final byte[] src,
                     @NonNull final byte[] dst,
                     @NonNull final TimeRoleSet timeRoleGet,
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
    TimeRoleSetState(@NonNull final Context context,
                     final int src,
                     final int dst,
                     @NonNull final TimeRoleSet timeRoleGet,
                     @NonNull final MeshTransport meshTransport,
                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, timeRoleGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.TAI_UTC_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
         final TimeRoleSet timeRoleSet = (TimeRoleSet) mMeshMessage;
        final byte[] key = timeRoleSet.getAppKey();
        final int akf = timeRoleSet.getAkf();
        final int aid = timeRoleSet.getAid();
        final int aszmic = timeRoleSet.getAszmic();
        final int opCode = timeRoleSet.getOpCode();
        final byte[] parameters = timeRoleSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        timeRoleSet.setMessage(message);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending time get");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
