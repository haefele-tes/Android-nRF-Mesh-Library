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
public class GenericAccessMessage extends MeshMessage {

    private static final String TAG = GenericAccessMessage.class.getSimpleName();
	private final int mOpcode;
	private final byte[] mKey;
	private final boolean mIsConfigMessage;

    /*
     * Constructs TimezoneSet message.
     */
    @SuppressWarnings("WeakerAccess")
    public GenericAccessMessage(final int opcode, @NonNull final byte[] payload, final boolean isConfigMessage, @NonNull final byte[] key) {
        super();
        mOpcode = opcode;
        mParameters = payload;
		mKey = key;
		mIsConfigMessage = isConfigMessage;
    }

	@Override
	int getAkf() {
		return mIsConfigMessage ? 0 : 1;
	}

	@Override
	int getAid() {
		if (mIsConfigMessage) {
			return 0;
		} else {
			return SecureUtils.calculateK4(this.mKey);
		}
	}

	@Override
    public int getOpCode() {
        return mOpcode;
    }

	@Override
	byte[] getParameters() {
		return mParameters;
	}

	byte[] getKey(){
		return mKey;
	}


}
