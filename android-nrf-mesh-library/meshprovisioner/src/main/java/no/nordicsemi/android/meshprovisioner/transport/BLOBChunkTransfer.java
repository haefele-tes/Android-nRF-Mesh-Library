package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a BLOBChunkTransfer message.
 */
@SuppressWarnings("unused")
public class BLOBChunkTransfer extends GenericMessage {

    private static final String TAG = BLOBChunkTransfer.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.BLOB_CHUNK_TRANSFER;

    private final int mChunkNumber;
    private final byte[] mChunkData;

    /**
     * Constructs BLOBChunkTransfer message.
     */
    public BLOBChunkTransfer(
		@NonNull final byte[] appKey,
        @NonNull final int chunkNumber,
        @NonNull final byte[] chunkData
        ) throws IllegalArgumentException {
        super(appKey);
        this.mChunkNumber = chunkNumber;
        this.mChunkData = chunkData;

        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
		mAid = SecureUtils.calculateK4(mAppKey);
		final ByteBuffer paramsBuffer = ByteBuffer.allocate(2 + mChunkData.length).order(ByteOrder.LITTLE_ENDIAN);
		paramsBuffer.putShort((short) this.mChunkNumber);
		for (byte mChunkDatum : this.mChunkData) {
			paramsBuffer.put((byte) mChunkDatum);
		}
		mParameters = paramsBuffer.array();
    }
}
