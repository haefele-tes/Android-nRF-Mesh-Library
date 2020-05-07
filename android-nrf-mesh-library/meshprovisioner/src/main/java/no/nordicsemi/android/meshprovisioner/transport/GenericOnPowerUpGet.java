package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericOnPowerUpGet message.
 */
@SuppressWarnings("unused")
public class GenericOnPowerUpGet extends GenericMessage {

    private static final String TAG = GenericOnPowerUpGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_ON_POWER_UP_GET;

    /**
     * Constructs GenericOnPowerUpGet message.
     *
     * @param appKey application key for this message
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public GenericOnPowerUpGet(@NonNull final byte[] appKey) throws IllegalArgumentException {
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
