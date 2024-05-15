package no.nordicsemi.android.meshprovisioner.transport;

import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a TimezoneSet message.
 */
@SuppressWarnings("unused")
public class BLOBBlockGet extends GenericMessage {

    private static final String TAG = BLOBBlockGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.BLOB_BLOCK_GET;

    /*
     * Constructs TimezoneSet message.
     */
    @SuppressWarnings("WeakerAccess")
    public BLOBBlockGet(@NonNull final byte[] appKey) {
        super(appKey);
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
        Log.v(TAG, "BLOB Block Get");
        paramsBuffer = ByteBuffer.allocate(0);
        mParameters = paramsBuffer.array();
    }
}
