package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a SchedulerGet message.
 */
@SuppressWarnings("unused")
public class SchedulerGet extends GenericMessage {

    private static final String TAG = SchedulerGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.SCHEDULER_GET;

    /**
     * Constructs SchedulerGet message.
     *
     * @param appKey application key for this message
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public SchedulerGet(@NonNull final byte[] appKey) throws IllegalArgumentException {
        super(appKey);
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
		mAid = SecureUtils.calculateK4(mAppKey);
    }
}
