package no.nordicsemi.android.meshprovisioner.transport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;


/**
 * State class for handling TimezoneSet messages.
 */
@SuppressWarnings("unused")
class TimezoneSetState extends GenericMessageState {

    private static final String TAG = TimezoneSetState.class.getSimpleName();

    /**
     * Constructs TimezoneSetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TimezoneSet Wrapper class {@link TimezoneSet} containing the opcode and parameters for {@link TimezoneSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    TimezoneSetState(@NonNull final Context context,
                 @NonNull final byte[] src,
                 @NonNull final byte[] dst,
                 @NonNull final TimezoneSet TimezoneSet,
                 @NonNull final MeshTransport meshTransport,
                 @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), TimezoneSet, meshTransport, callbacks);
        createAccessMessage();
    }

    /**
     * Constructs TimezoneSetState
     *
     * @param context         Context of the application
     * @param src             Source address
     * @param dst             Destination address to which the message must be sent to
     * @param TimezoneSet Wrapper class {@link TimezoneSet} containing the opcode and parameters for {@link TimezoneSet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    TimezoneSetState(@NonNull final Context context,
                 final int src,
                 final int dst,
                 @NonNull final TimezoneSet TimezoneSet,
                 @NonNull final MeshTransport meshTransport,
                 @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, TimezoneSet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.TIME_ZONE_SET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final TimezoneSet TimezoneSet = (TimezoneSet) mMeshMessage;
        final byte[] key = TimezoneSet.getAppKey();
        final int akf = TimezoneSet.getAkf();
        final int aid = TimezoneSet.getAid();
        final int aszmic = TimezoneSet.getAszmic();
        final int opCode = TimezoneSet.getOpCode();
        final byte[] parameters = TimezoneSet.getParameters();
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
