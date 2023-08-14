package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TaiUtcDeltaGet message.
 */
@SuppressWarnings("unused")
public class TaiUtcDeltaGet extends GenericMessage {

    private static final String TAG = TaiUtcDeltaGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TAI_UTC_DELTA_GET;

    /**
     * Constructs TaiUtcDeltaGet message.
     *
     * @param appKey application key for this message
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public TaiUtcDeltaGet(@NonNull final byte[] appKey) throws IllegalArgumentException {
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
