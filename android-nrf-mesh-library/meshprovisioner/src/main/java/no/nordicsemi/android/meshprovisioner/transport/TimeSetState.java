package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling TimeSet messages.
 */
@SuppressWarnings("unused")
class TimeSetState extends GenericMessageState {

    private static final String TAG = TimeSetState.class.getSimpleName();

    /**
     * Constructs TimeSetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TimeSet Wrapper class {@link TimeSet} containing the opcode and parameters for {@link TimeSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TimeSetState(@NonNull final Context context,
                 @NonNull final byte[] src,
                 @NonNull final byte[] dst,
                 @NonNull final TimeSet TimeSet,
                 @NonNull final MeshTransport meshTransport,
                 @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), TimeSet, meshTransport, callbacks);
        createAccessMessage();
    }

    /**
     * Constructs TimeSetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TimeSet Wrapper class {@link TimeSet} containing the opcode and parameters for {@link TimeSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TimeSetState(@NonNull final Context context,
                 final int src,
                 final int dst,
                 @NonNull final TimeSet TimeSet,
                 @NonNull final MeshTransport meshTransport,
                 @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, TimeSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.TIME_SET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final TimeSet TimeSet = (TimeSet) mMeshMessage;
        final byte[] key = TimeSet.getAppKey();
        final int akf = TimeSet.getAkf();
        final int aid = TimeSet.getAid();
        final int aszmic = TimeSet.getAszmic();
        final int opCode = TimeSet.getOpCode();
        final byte[] parameters = TimeSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending time set");
        super.executeSend();

        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
        }
    }
}
