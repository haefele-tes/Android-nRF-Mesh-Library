package no.nordicsemi.android.meshprovisioner.transport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericMoveSet message.
 */
@SuppressWarnings("unused")
public class GenericMoveSet extends GenericMessage {

    private static final String TAG = GenericMoveSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_MOVE_SET;
    private static final int GENERIC_MOVE_SET_TRANSITION_PARAMS_LENGTH = 4;
    private static final int GENERIC_MOVE_SET_PARAMS_LENGTH = 2;

    private final Integer mTransitionSteps;
    private final Integer mTransitionResolution;
    private final Integer mDelay;
    private final int mDeltaLevel;
    private final int tId;

    /**
     * Constructs GenericMoveSet message.
     *
     * @param appKey application key for this message
     * @param deltaLevel  boolean state of the GenericOnOffModel
     * @param tId    transaction id
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public GenericMoveSet(@NonNull final byte[] appKey,
                           final int deltaLevel,
                           final int tId) throws IllegalArgumentException {
        this(appKey, deltaLevel, tId, null, null, null);
    }

    /**
     * Constructs GenericMoveSet message.
     *
     * @param appKey               application key for this message
     * @param deltaLevel                boolean state of the GenericOnOffModel
     * @param tId                  transaction id
     * @param transitionSteps      transition steps for the level
     * @param transitionResolution transition resolution for the level
     * @param delay                delay for this message to be executed 0 - 1275 milliseconds
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public GenericMoveSet(@NonNull final byte[] appKey,
                           final int deltaLevel,
                           final int tId,
                           @Nullable final Integer transitionSteps,
                           @Nullable final Integer transitionResolution,
                           @Nullable final Integer delay) {
        super(appKey);
        this.mTransitionSteps = transitionSteps;
        this.mTransitionResolution = transitionResolution;
        this.mDelay = delay;
        this.mDeltaLevel = deltaLevel;
        this.tId = tId;
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
        Log.v(TAG, "Delta level: " + mDeltaLevel);
        if (mTransitionSteps == null || mTransitionResolution == null || mDelay == null) {
            paramsBuffer = ByteBuffer.allocate(GENERIC_MOVE_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            paramsBuffer.put((byte) mDeltaLevel);
            paramsBuffer.put((byte) tId);
        } else {
            Log.v(TAG, "Transition steps: " + mTransitionSteps);
            Log.v(TAG, "Transition step resolution: " + mTransitionResolution);
            paramsBuffer = ByteBuffer.allocate(GENERIC_MOVE_SET_TRANSITION_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            paramsBuffer.put((byte) mDeltaLevel);
            paramsBuffer.put((byte) tId);
            paramsBuffer.put((byte) (mTransitionResolution << 6 | mTransitionSteps));
            final int delay = mDelay;
            paramsBuffer.put((byte) delay);
        }
        mParameters = paramsBuffer.array();

    }
}
