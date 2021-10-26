package no.nordicsemi.android.meshprovisioner.transport;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericMoveSetUnacknowledged message.
 */
@SuppressWarnings("unused")
public class GenericMoveSetUnacknowledged extends GenericMessage {

    private static final String TAG = GenericMoveSetUnacknowledged.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_MOVE_SET_UNACKNOWLEDGED;
    private static final int GENERIC_MOVE_SET_TRANSITION_PARAMS_LENGTH = 5;
    private static final int GENERIC_MOVE_SET_PARAMS_LENGTH = 3;

    private final Integer mTransitionSteps;
    private final Integer mTransitionResolution;
    private final Integer mDelay;
    private final int deltaLevel;
    private final int tId;

    /**
     * Constructs GenericMoveSetUnacknowledged message.
     *
     * @param appKey application key for this message
     * @param deltaLevel  deltaLevel of the GenericMoveModel
     * @param tId    transaction id
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public GenericMoveSetUnacknowledged(@NonNull final byte[] appKey,
                                        final int deltaLevel,
                                        final int tId) throws IllegalArgumentException {
        this(appKey, deltaLevel, tId, null, null, null);
    }

    /**
     * Constructs GenericMoveSetUnacknowledged message.
     *
     * @param appKey               application key for this message
     * @param transitionSteps      transition steps for the Move
     * @param transitionResolution transition resolution for the Move
     * @param delay                delay for this message to be executed 0 - 1275 milliseconds
     * @param deltaLevel                Move of the GenericMoveModel
     * @param tId                  transaction id
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public GenericMoveSetUnacknowledged(@NonNull final byte[] appKey,
                                        @NonNull final int deltaLevel,
                                        @NonNull final int tId,
                                        @Nullable final Integer transitionSteps,
                                        @Nullable final Integer transitionResolution,
                                        @Nullable final Integer delay
                                        ) throws IllegalArgumentException {
        super(appKey);
        this.mTransitionSteps = transitionSteps;
        this.mTransitionResolution = transitionResolution;
        this.mDelay = delay;
        this.tId = tId;
//        if (deltaLevel < Short.MIN_VALUE || deltaLevel > Short.MAX_VALUE)
//            throw new IllegalArgumentException("Generic Move value must be between -32768 to 32767");
        this.deltaLevel = deltaLevel;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        final ByteBuffer paramsBuffer;
        Log.v(TAG, "Move: " + deltaLevel + " with tid: " + tId);
        if (mTransitionSteps == null || mTransitionResolution == null || mDelay == null) {
            paramsBuffer = ByteBuffer.allocate(GENERIC_MOVE_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            paramsBuffer.putShort((short) deltaLevel);
            paramsBuffer.put((byte) tId);
        } else {
            Log.v(TAG, "Transition steps: " + mTransitionSteps);
            Log.v(TAG, "Transition step resolution: " + mTransitionResolution);
            paramsBuffer = ByteBuffer.allocate(GENERIC_MOVE_SET_TRANSITION_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            paramsBuffer.putShort((short) (deltaLevel));
            paramsBuffer.put((byte) tId);
            paramsBuffer.put((byte) (mTransitionResolution << 6 | mTransitionSteps));
            final int delay = mDelay;
            paramsBuffer.put((byte) delay);
        }
        mParameters = paramsBuffer.array();
    }
}
