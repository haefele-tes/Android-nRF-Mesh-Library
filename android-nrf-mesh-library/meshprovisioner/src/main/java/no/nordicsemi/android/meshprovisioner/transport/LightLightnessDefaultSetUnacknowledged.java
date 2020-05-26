package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericLevelSet message.
 */
@SuppressWarnings("unused")
public class LightLightnessDefaultSetUnacknowledged extends GenericMessage {

    private static final String TAG = LightLightnessDefaultSetUnacknowledged.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.LIGHT_LIGHTNESS_DEFAULT_SET_UNACKNOWLEDGED;
    private static final int GENERIC_LEVEL_DEFAULT_SET_PARAMS_LENGTH = 2;

    private final int mLevel;



    /**
     * Constructs GenericLevelSet message.
     *
     * @param appKey               application key for this message
     * @param level                level of the GenericLevelModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public LightLightnessDefaultSetUnacknowledged(@NonNull final byte[] appKey,
                                           final int level) throws IllegalArgumentException {
        super(appKey);
        if (level < 0 || level > 0xFFFF)
            throw new IllegalArgumentException("Light lightness value must be between 0x0000 and 0xFFFF");
        this.mLevel = level;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey);
        Log.v(TAG, "Set light lightness default unack: " + mLevel);

        final ByteBuffer paramsBuffer;
        paramsBuffer = ByteBuffer.allocate(GENERIC_LEVEL_DEFAULT_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort((short) mLevel);
        mParameters = paramsBuffer.array();
    }


}
