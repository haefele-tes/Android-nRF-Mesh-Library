package no.nordicsemi.android.meshprovisioner.transport;

import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * Allows sending arbitrary access messages
 */
@SuppressWarnings("unused")
public class GenericAccessMessage extends GenericMessage {

    private static final String TAG = GenericAccessMessage.class.getSimpleName();
	private final int opcode;

    /*
     * Constructs TimezoneSet message.
     */
    @SuppressWarnings("WeakerAccess")
    public GenericAccessMessage(@NonNull final byte[] appKey, final int opcode, @NonNull final byte[] payload) {
        super(appKey);
        this.opcode = opcode;
        mParameters = payload;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return this.opcode;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        // Nothing to do here
    }
}
