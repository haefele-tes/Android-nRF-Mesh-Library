package no.nordicsemi.android.meshprovisioner.transport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericOnPowerUpSet message.
 */
@SuppressWarnings("unused")
public class GenericOnPowerUpSet extends GenericMessage {

    private static final String TAG = GenericOnPowerUpSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_ON_POWER_UP_SET;
    private static final int GENERIC_ON_POWER_UP_SET_PARAMS_LENGTH = 1;

    private final Integer mState;

    /**
     * Constructs GenericOnPowerUpSet message.
     *
     * @param appKey application key for this message
     * @param state  boolean state of the GenericOnPowerUpModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public GenericOnPowerUpSet(@NonNull final byte[] appKey, final int state) throws IllegalArgumentException {
        super(appKey);
        this.mState = state;
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
        Log.v(TAG, "State: " + mState);
        paramsBuffer = ByteBuffer.allocate(GENERIC_ON_POWER_UP_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.put((byte) (mState & 0xff));
        mParameters = paramsBuffer.array();
    }
}
