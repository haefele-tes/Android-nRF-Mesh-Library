package no.nordicsemi.android.meshprovisioner.transport;

import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a HealthFaultGet message.
 */
@SuppressWarnings("unused")
public class HealthFaultGet extends GenericMessage {

    private static final String TAG = HealthFaultGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.HEALTH_FAULT_GET;
    private static final int HEALTH_FAULT_GET_PARAMS_LENGTH = 2;

    private final int companyId;

    /**
     * Constructs HealthFaultGet message.
     *
     * @param appKey    Application key for this message
     * @param companyId Company id
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public HealthFaultGet(@NonNull final byte[] appKey, final int companyId) {
        super(appKey);
        this.companyId = companyId;
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
        Log.v(TAG, "State: " + companyId);
        paramsBuffer = ByteBuffer.allocate(HEALTH_FAULT_GET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort((short) this.companyId);
        mParameters = paramsBuffer.array();

    }
}
