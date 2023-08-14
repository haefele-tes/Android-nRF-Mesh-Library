package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a SchedulerActionGet message.
 */
@SuppressWarnings("unused")
public class SchedulerActionGet extends GenericMessage {

    private static final String TAG = SchedulerActionGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.SCHEDULER_ACTION_GET;
    private static final int SCHEDULER_ACTION_GET_PARAMS_LENGTH = 1;

    private final int mIndex;

    /**
     * Constructs SchedulerActionGet message.
     *
     * @param appKey application key for this message
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public SchedulerActionGet(@NonNull final byte[] appKey, final int index) throws IllegalArgumentException {
        super(appKey);
        this.mIndex = index;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        final ByteBuffer paramsBuffer = ByteBuffer.allocate(SCHEDULER_ACTION_GET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.put((byte) mIndex);
        mParameters = paramsBuffer.array();
    }
}
