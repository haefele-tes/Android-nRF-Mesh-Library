package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a BLOBTransferStart message.
 */
@SuppressWarnings("unused")
public class BLOBTransferStart extends GenericMessage {

    private static final String TAG = BLOBTransferStart.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.BLOB_TRANSFER_START;

    private final int mTransferMode;
    private final byte[] mBlobId; // TODO: Use BlobId class
    private final Integer mBlobSize;
    private final int mBlockSizeLog;
    private final int mClientMTUSize;

    /**
     * Constructs BLOBTransferStart message.
     */
    public BLOBTransferStart(
		@NonNull final byte[] appKey,
        @NonNull final int transferMode,
        @NonNull final byte[] blobId,
        @NonNull final int blobSize,
        @NonNull final int blockSizeLog,
        @NonNull final int clientMTUSize
        ) throws IllegalArgumentException {
        super(appKey);
        this.mTransferMode = transferMode;
        this.mBlobId = blobId;
        this.mBlobSize = blobSize;
        this.mBlockSizeLog = blockSizeLog;
        this.mClientMTUSize = clientMTUSize;

        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
		final ByteBuffer paramsBuffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
		paramsBuffer.put((byte) this.mTransferMode);
		paramsBuffer.put((byte) this.mBlobId[0]);
		paramsBuffer.put((byte) this.mBlobId[1]);
		paramsBuffer.put((byte) this.mBlobId[2]);
		paramsBuffer.put((byte) this.mBlobId[3]);
		paramsBuffer.put((byte) this.mBlobId[4]);
		paramsBuffer.put((byte) this.mBlobId[5]);
		paramsBuffer.put((byte) this.mBlobId[6]);
		paramsBuffer.put((byte) this.mBlobId[7]);
		final byte[] blobSize = new byte[]{(byte) ((this.mBlobSize >> 24) & 0xFF), (byte) ((this.mBlobSize >> 16) & 0xFF), (byte) ((this.mBlobSize >> 8) & 0xFF), (byte) (this.mBlobSize & 0xFF)};
		paramsBuffer.put(blobSize[3]);
		paramsBuffer.put(blobSize[2]);
		paramsBuffer.put(blobSize[1]);
		paramsBuffer.put(blobSize[0]);
		paramsBuffer.put((byte) this.mBlockSizeLog);
		paramsBuffer.putShort((short) this.mClientMTUSize);
		mParameters = paramsBuffer.array();
	}
}
