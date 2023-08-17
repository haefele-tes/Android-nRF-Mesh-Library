package no.nordicsemi.android.meshprovisioner.transport;

import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.SUB_SECOND_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TAI_SECONDS_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_AUTHORITY_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_ZONE_OFFSET_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.UNCERTAINTY_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.UTC_DELTA_BIT_SIZE;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.MeshTAITime;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitWriter;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TimeSet message.
 */
@SuppressWarnings("unused")
public class TimeSet extends GenericMessage {

    private static final String TAG = TimeSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TIME_SET;
    private static final int TIME_SET_PARAMS_LENGTH = 2;

    private final MeshTAITime taiTime;

    /**
     * Constructs TimeSet message.
     *
     * @param appKey               application key for this message
     * @param taiTime                boolean state of the GenericOnOffModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public TimeSet(@NonNull final byte[] appKey,
                           final MeshTAITime taiTime) {
        super(appKey);
        this.taiTime = taiTime;

        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        BitWriter bitWriter = new BitWriter(TIME_BIT_SIZE);

        // The state is a uint8 value representing the valid range of -64 through +191 (i.e., 0x40 represents a value of 0 and 0xFF represents a value of 191).
        bitWriter.write(taiTime.getTimeZoneOffset().getEncodedValue(), TIME_ZONE_OFFSET_BIT_SIZE);

        // The valid range is -255 through +32512 (i.e., 0x00FF represents a value of 0 and 0x7FFF represents a value of 32512).
        bitWriter.write(taiTime.getUtcDelta().getEncodedValue(), UTC_DELTA_BIT_SIZE);
        if (taiTime.isTimeAuthority()) {
            bitWriter.write(1, TIME_AUTHORITY_BIT_SIZE);
        } else {
            bitWriter.write(0, TIME_AUTHORITY_BIT_SIZE);
        }
        bitWriter.write(taiTime.getUncertainty(), UNCERTAINTY_BIT_SIZE);
        bitWriter.write(taiTime.getSubSecond(), SUB_SECOND_BIT_SIZE);
        bitWriter.write(taiTime.getTaiSeconds(), TAI_SECONDS_BIT_SIZE);

        mParameters = ArrayUtils.reverseArray(bitWriter.toByteArray());
    }
}
