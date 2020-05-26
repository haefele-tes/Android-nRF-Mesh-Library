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
public class LightLightnessDefaultSet extends GenericMessage {

    private static final String TAG = LightLightnessDefaultSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.LIGHT_LIGHTNESS_DEFAULT_SET;
    private static final int LIGHT_LIGHTNESS_DEFAULT_SET_PARAMS_LENGTH = 2;

    private final int mLightness;

    /**
     * Constructs GenericLevelSet message.
     *
     * @param appKey               application key for this message
     * @param lightLightnessDefault       lightLightnessDefault
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public LightLightnessDefaultSet(@NonNull final byte[] appKey,
                             final int lightLightnessDefault) throws IllegalArgumentException {
        super(appKey);
        if (lightLightnessDefault < 0 || lightLightnessDefault > 0xFFFF)
            throw new IllegalArgumentException("Light lightness value must be between 0x0000 and 0xFFFF");
        this.mLightness = lightLightnessDefault;
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
        Log.v(TAG, "Lightness: " + mLightness);
        paramsBuffer = ByteBuffer.allocate(LIGHT_LIGHTNESS_DEFAULT_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort((short) mLightness);
        mParameters = paramsBuffer.array();
    }


}
