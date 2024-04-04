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
public class BLOBBlockStart extends GenericMessage {

    private static final String TAG = BLOBBlockStart.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.BLOB_BLOCK_START;
    private static final int BLOB_BLOCK_START_PARAMS_LENGTH = 4;

    private final Integer mBlockNumber;
    private final Integer mChunkSize;

    /**
     * Constructs TimezoneSet message.
     */
    @SuppressWarnings("WeakerAccess")
    public BLOBBlockStart(@NonNull final byte[] appKey,
                          final Integer blockNumber,
						  final Integer chunkSize) {
        super(appKey);
        this.mBlockNumber = blockNumber;
        this.mChunkSize = chunkSize;

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
        Log.v(TAG, "BLOB Block Start: " + mBlockNumber + " Chunk Size: " + mChunkSize);
        paramsBuffer = ByteBuffer.allocate(BLOB_BLOCK_START_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort((short) mBlockNumber);
        paramsBuffer.putShort((short) mChunkSize);
        mParameters = paramsBuffer.array();
    }
}
