package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a BLOBTransferStart message.
 */
@SuppressWarnings("unused")
public class BLOBTransferStart extends GenericMessage {

    private static final String TAG = BLOBTransferStart.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.BLOB_TRANSFER_START;

    private final Integer mTransferMode;
    private final byte[] mBlobId; // TODO: Use BlobId class
    private final Integer mBlobSize;
    private final Integer mBlockSizeLog;
    private final Integer mClientMTUSize;

    /**
     * Constructs BLOBTransferStart message.
     */
    public BLOBTransferStart(
        @NonNull final int transferMode,
        @NonNull final byte[] blobId,
        @NonNull final int blobSize,
        @NonNull final int blockSizeLog,
        @NonNull final int clientMTUSize
        ) throws IllegalArgumentException {
        super();
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
    }
}
