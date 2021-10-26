package no.nordicsemi.android.meshprovisioner.transport;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling unacknowledged GenericMoveSet messages.
 */
@SuppressWarnings("WeakerAccess")
class GenericMoveSetUnacknowledgedState extends GenericMessageState {

    private static final String TAG = GenericMoveSetUnacknowledgedState.class.getSimpleName();

    /**
     * Constructs {@link GenericMoveSetUnacknowledgedState}
     *
     * @param context                Context of the application
     * @param src                    Source address
     * @param dst                    Destination address to which the message must be sent to
     * @param genericMoveSetUnacked Wrapper class {@link GenericMoveSetUnacknowledged} containing the opcode and parameters for {@link GenericMoveSetUnacknowledged} message
     * @param callbacks              {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    @Deprecated
    GenericMoveSetUnacknowledgedState(@NonNull final Context context,
                                      @NonNull final byte[] src,
                                      @NonNull final byte[] dst,
                                      @NonNull final GenericMoveSetUnacknowledged genericMoveSetUnacked,
                                      @NonNull final MeshTransport meshTransport,
                                      @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        this(context, MeshParserUtils.bytesToInt(src), MeshParserUtils.bytesToInt(dst), genericMoveSetUnacked, meshTransport, callbacks);
    }

    /**
     * Constructs {@link GenericMoveSetUnacknowledgedState}
     *
     * @param context                Context of the application
     * @param src                    Source address
     * @param dst                    Destination address to which the message must be sent to
     * @param genericMoveSetUnacked Wrapper class {@link GenericMoveSetUnacknowledged} containing the opcode and parameters for {@link GenericMoveSetUnacknowledged} message
     * @param callbacks              {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericMoveSetUnacknowledgedState(@NonNull final Context context,
                                      final int src,
                                      final int dst,
                                      @NonNull final GenericMoveSetUnacknowledged genericMoveSetUnacked,
                                      @NonNull final MeshTransport meshTransport,
                                      @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, src, dst, genericMoveSetUnacked, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_MOVE_SET_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericMoveSetUnacknowledged genericMoveSet = (GenericMoveSetUnacknowledged) mMeshMessage;
        final byte[] key = genericMoveSet.getAppKey();
        final int akf = genericMoveSet.getAkf();
        final int aid = genericMoveSet.getAid();
        final int aszmic = genericMoveSet.getAszmic();
        final int opCode = genericMoveSet.getOpCode();
        final byte[] parameters = genericMoveSet.getParameters();
        message = mMeshTransport.createMeshMessage(mSrc, mDst, key, akf, aid, aszmic, opCode, parameters);
        genericMoveSet.setMessage(message);
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending Generic Move set acknowledged ");
        super.executeSend();
        if (message.getNetworkPdu().size() > 0) {
            if (mMeshStatusCallbacks != null) {
                mMeshStatusCallbacks.onMeshMessageSent(mDst, mMeshMessage);
            }
        }
    }
}
