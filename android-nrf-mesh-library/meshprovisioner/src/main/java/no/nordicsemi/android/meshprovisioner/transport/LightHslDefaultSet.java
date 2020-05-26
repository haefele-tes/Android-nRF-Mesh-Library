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
public class LightHslDefaultSet extends GenericMessage {

    private static final String TAG = LightCtlSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.LIGHT_HSL_DEFAULT_SET;
    private static final int LIGHT_LIGHTNESS_DEFAULT_SET_PARAMS_LENGTH = 6;

    private final int mLightness;
    private final int mHue;
    private final int mSaturation;

    /**
     * Constructs LightHslDefaultSet message.
     *
     * @param appKey               application key for this message
     * @param lightLightness       lightness of the LightHslDefaultModel
     * @param lightHue             hue of the LightHslDefaultModel
     * @param lightSaturation      saturation of the LightHslDefaultModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public LightHslDefaultSet(@NonNull final byte[] appKey,
                       final int lightLightness,
                       final int lightHue,
                       final int lightSaturation) throws IllegalArgumentException {
        super(appKey);
        if (lightLightness < 0 || lightLightness > 0xFFFF)
            throw new IllegalArgumentException("Light lightness value must be between 0 to 0xFFFF");
        if (lightHue < 0 || lightHue > 0xFFFF)
            throw new IllegalArgumentException("Light hue value must be between 0 to 0xFFFF");
        if (lightSaturation < 0 || lightSaturation > 0xFFFF)
            throw new IllegalArgumentException("Light hue value must be between 0 to 0xFFFF");
        this.mLightness = lightLightness;
        this.mHue = lightHue;
        this.mSaturation = lightSaturation;
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
        Log.v(TAG, "Hue: " + mHue);
        Log.v(TAG, "Saturation: " + mSaturation);

        paramsBuffer = ByteBuffer.allocate(LIGHT_LIGHTNESS_DEFAULT_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort((short) mLightness);
        paramsBuffer.putShort((short) mHue);
        paramsBuffer.putShort((short) mSaturation);

        mParameters = paramsBuffer.array();
    }


}
