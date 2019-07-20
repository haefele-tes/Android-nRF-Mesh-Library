package no.nordicsemi.android.meshprovisioner.transport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericOnOffSet message.
 */
@SuppressWarnings("unused")
public class GenericUserPropertyGet extends GenericMessage {

    private static final String TAG = GenericUserPropertyGet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_USER_PROPERTY_GET;
    private static final int GENERIC_USER_PROPERTY_SET_PARAMS_LENGTH = 2;

    private final Integer mState;

    /**
     * Constructs GenericOnOffSet message.
     *
     * @param appKey               application key for this message
     * @param userPropertyKey                boolean userPropertyKey of the GenericOnOffModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public GenericUserPropertyGet(@NonNull final byte[] appKey,
                                  final int userPropertyKey
                                  ) {
        super(appKey);
        this.mState = userPropertyKey;
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
        Log.v(TAG, "State: " + (mState));
        paramsBuffer = ByteBuffer.allocate(GENERIC_USER_PROPERTY_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort(mState.shortValue());
        mParameters = paramsBuffer.array();

    }
}
