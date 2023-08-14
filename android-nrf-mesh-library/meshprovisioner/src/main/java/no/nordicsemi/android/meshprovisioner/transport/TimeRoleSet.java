package no.nordicsemi.android.meshprovisioner.transport;

import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.SUB_SECOND_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TAI_SECONDS_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_AUTHORITY_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_ZONE_OFFSET_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TIME_ZONE_START_RANGE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.UNCERTAINTY_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.UTC_DELTA_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.UTC_DELTA_START_RANGE;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.MeshTAITime;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitWriter;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TimezoneSet message.
 */
@SuppressWarnings("unused")
public class TimeRoleSet extends GenericMessage {

    private static final String TAG = TimeRoleSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TIME_ROLE_SET;
    private static final int TIME_ROLE_SET_LENGTH = 1;

    private final int role;

    /**
     * Constructs TimezoneSet message.
     *
     * @param appKey               application key for this message
     * @param role                boolean state of the GenericOnOffModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public TimeRoleSet(@NonNull final byte[] appKey, final int role) {
        super(appKey);
        this.role = role;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        final ByteBuffer buffer = ByteBuffer.allocate(TIME_ROLE_SET_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (role));
        mParameters = buffer.array();

    }
}
