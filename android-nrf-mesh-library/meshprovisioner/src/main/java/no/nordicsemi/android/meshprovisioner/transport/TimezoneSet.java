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
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.MeshTAITime;
import no.nordicsemi.android.meshprovisioner.data.TimeZoneOffset;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitWriter;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TimezoneSet message.
 */
@SuppressWarnings("unused")
public class TimezoneSet extends GenericMessage {

    private static final String TAG = TimezoneSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TIME_ZONE_SET;
    private static final int TIME_ZONE_SET_LENGTH = 6;

    private TimeZoneOffset newTimeZoneOffset;
    private long timeOfChange;

    /**
     * Constructs TimezoneSet message.
     *
     * @param appKey               application key for this message
     * @param newTimeZoneOffset                boolean state of the GenericOnOffModel
     * @param timeOfChange                boolean state of the GenericOnOffModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public TimezoneSet(@NonNull final byte[] appKey,
                           final TimeZoneOffset newTimeZoneOffset,
                            final Long timeOfChange) {
        super(appKey);
        this.newTimeZoneOffset = newTimeZoneOffset;
        this.timeOfChange = timeOfChange;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        final ByteBuffer buffer = ByteBuffer.allocate(TIME_ZONE_SET_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        Log.d(TAG, "Creating message");
        Log.d(TAG, newTimeZoneOffset.toString());
        Log.d(TAG, "time of change:" + timeOfChange);
        buffer.put(newTimeZoneOffset.getEncodedValue());
        buffer.putInt((int) timeOfChange);
        buffer.put((byte) (timeOfChange >> 32));
        mParameters = buffer.array();

        // mParameters = ArrayUtils.reverseArray(bitWriter.toByteArray());
    }
}
