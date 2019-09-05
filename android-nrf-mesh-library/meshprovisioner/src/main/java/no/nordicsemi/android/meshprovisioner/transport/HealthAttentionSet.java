package no.nordicsemi.android.meshprovisioner.transport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a HealthAttentionSet message.
 */
@SuppressWarnings("unused")
public class HealthAttentionSet extends GenericMessage {

    private static final String TAG = HealthAttentionSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.HEALTH_ATTENTION_SET;
    private static final int HEALTH_ATTENTION_SET_PARAMS_LENGTH = 1;

    private final int attention;

    /**
     * Constructs HealthAttentionSet message.
     *
     * @param appKey               Application key for this message
     * @param attention            Attention time
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public HealthAttentionSet(@NonNull final byte[] appKey, final int attention) {
        super(appKey);
        this.attention = attention;
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
        Log.v(TAG, "State: " + attention);
        paramsBuffer = ByteBuffer.allocate(HEALTH_ATTENTION_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.put((byte) this.attention);
        mParameters = paramsBuffer.array();

    }
}
