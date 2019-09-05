package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a HealthAttentionGet message.
 */
@SuppressWarnings("unused")
public class HealthAttentionGet extends GenericMessage {

    private static final String TAG = HealthAttentionGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.HEALTH_ATTENTION_GET;

    /**
     * Constructs HealthAttentionGet message.
     *
     * @param appKey application key for this message
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public HealthAttentionGet(@NonNull final byte[] appKey) throws IllegalArgumentException {
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
