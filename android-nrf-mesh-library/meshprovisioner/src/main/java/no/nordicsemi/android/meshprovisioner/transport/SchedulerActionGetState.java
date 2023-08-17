package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling SchedulerActionGet messages.
 */
@SuppressWarnings("unused")
class SchedulerActionGetState extends GenericMessageState {

    private static final String TAG = SchedulerActionGetState.class.getSimpleName();

    /**
     * Constructs SchedulerActionGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param schedulerActionGet Wrapper class {@link SchedulerActionGet} containing the opcode and parameters for {@link SchedulerActionGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    SchedulerActionGetState(@NonNull final Context context,
                         @NonNull final byte[] src,
                         @NonNull final byte[] dst,
                         @NonNull final SchedulerActionGet schedulerActionGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), schedulerActionGet, meshTransport, callbacks);        createAccessMessage();
    }

    /**
     * Constructs SchedulerActionGetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param schedulerActionGet Wrapper class {@link SchedulerActionGet} containing the opcode and parameters for {@link SchedulerActionGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    SchedulerActionGetState(@NonNull final Context context,
                         final int src,
                         final int dst,
                         @NonNull final SchedulerActionGet schedulerActionGet,
                         @NonNull final MeshTransport meshTransport,
                         @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, schedulerActionGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.SCHEDULER_ACTION_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final SchedulerActionGet schedulerActionGet = (SchedulerActionGet) mMeshMessage;
        final byte[] key = schedulerActionGet.getAppKey();
        final int akf = schedulerActionGet.getAkf();
        final int aid = schedulerActionGet.getAid();
        final int aszmic = schedulerActionGet.getAszmic();
        final int opCode = schedulerActionGet.getOpCode();
        final byte[] parameters = schedulerActionGet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        schedulerActionGet.setMessage(message);
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
