package no.nordicsemi.android.meshprovisioner.transport;

import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.PADDING_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.TAI_SECONDS_BIT_SIZE;
import static no.nordicsemi.android.meshprovisioner.transport.TimeStatus.UTC_DELTA_BIT_SIZE;

import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.data.TaiUtcDelta;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.ArrayUtils;
import no.nordicsemi.android.meshprovisioner.utils.BitWriter;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TimezoneSet message.
 */
@SuppressWarnings("unused")
public class TaiUtcDeltaSet extends GenericMessage {

    private static final String TAG = TaiUtcDeltaSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.TAI_UTC_DELTA_SET;
    private static final int TAI_UTC_DELTA_SET_PARAMS_LENGTH = 2;

    private final TaiUtcDelta mNewDelta;
	private final Long mTimeOfChange;

    /**
     * Constructs TimezoneSet message.
     *
     * @param appKey               application key for this message
     * @param newDelta                boolean state of the GenericOnOffModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public TaiUtcDeltaSet(@NonNull final byte[] appKey,
                          final TaiUtcDelta newDelta,
						  final Long timeOfChange) {
        super(appKey);
        this.mNewDelta = newDelta;
		this.mTimeOfChange = timeOfChange;

        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        BitWriter bitWriter = new BitWriter(TAI_UTC_DELTA_SET_BIT_SIZE);

		bitWriter.write(mTimeOfChange, TAI_SECONDS_BIT_SIZE);
		bitWriter.write(0, PADDING_BIT_SIZE);
		bitWriter.write(mNewDelta.getEncodedValue(), UTC_DELTA_BIT_SIZE);

        mParameters = ArrayUtils.reverseArray(bitWriter.toByteArray());
    }

	static final int TAI_UTC_DELTA_SET_BIT_SIZE = TAI_SECONDS_BIT_SIZE + PADDING_BIT_SIZE + UTC_DELTA_BIT_SIZE;
}
