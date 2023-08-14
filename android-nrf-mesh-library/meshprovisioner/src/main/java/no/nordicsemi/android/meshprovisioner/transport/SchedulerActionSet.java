package no.nordicsemi.android.meshprovisioner.transport;

import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.data.ScheduleEntry;
import no.nordicsemi.android.meshprovisioner.data.TimeZoneOffset;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitWriter;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TimezoneSet message.
 */
@SuppressWarnings("unused")
public class SchedulerActionSet extends GenericMessage {

    private static final String TAG = SchedulerActionSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.SCHEDULER_ACTION_SET;
    private static final int SCHEDULER_ACTION_SET_INDEX_PARAMS_LENGTH = 4 + ScheduleEntry.SCHEDULER_ENTRY_PARAMS_BITS_LENGTH;

    private final int index;
    private final ScheduleEntry entry;
    /**
     * Constructs TimezoneSet message.
     *
     * @param appKey               application key for this message
     * @param index                boolean state of the GenericOnOffModel
     * @param entry                boolean state of the GenericOnOffModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public SchedulerActionSet(@NonNull final byte[] appKey, int index, ScheduleEntry entry) {
        super(appKey);
        this.index = index;
        this.entry = entry;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        BitWriter bitWriter = new BitWriter(SCHEDULER_ACTION_SET_INDEX_PARAMS_LENGTH);
        entry.assembleMessageParameters(bitWriter);
        bitWriter.write(index, 4);
        mParameters = ArrayUtils.reverseArray(bitWriter.toByteArray());
    }
}
