package no.nordicsemi.android.meshprovisioner.transport;

import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a HealthFaultTest message.
 */
@SuppressWarnings("unused")
public class HealthFaultTest extends GenericMessage {
    private static final String TAG = HealthFaultTest.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.HEALTH_FAULT_TEST;
    private static final int HEALTH_FAULT_TEST_PARAMS_LENGTH = 3;

    private final int testId;
    private final int companyId;

    /**
     * Constructs HealthFaultTest message.
     *
     * @param appKey    Application key for this message
     * @param testId    Test id
     * @param companyId Company id
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public HealthFaultTest(@NonNull final byte[] appKey, final int testId, final int companyId) {
        super(appKey);
        this.testId = testId;
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
        Log.v(TAG, "Testid: " + testId + " with companyid " + companyId);
        paramsBuffer = ByteBuffer.allocate(HEALTH_FAULT_TEST_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.put((byte) this.testId);
        paramsBuffer.putShort((short) this.companyId);
        mParameters = paramsBuffer.array();

    }
}
