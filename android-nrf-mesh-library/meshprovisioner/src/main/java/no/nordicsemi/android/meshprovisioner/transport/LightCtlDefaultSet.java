package no.nordicsemi.android.meshprovisioner.transport;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a LightCtlDefaultSet message.
 */
@SuppressWarnings("unused")
public class LightCtlDefaultSet extends GenericMessage {

    private static final String TAG = LightCtlDefaultSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.LIGHT_CTL_DEFAULT_SET;
    private static final int LIGHT_CTL_SET_PARAMS_LENGTH = 6;

    private final int mLightness;
    private final int mTemperature;
    private final int mDeltaUv;



    /**
     * Constructs LightCtlDefaultSet message.
     *
     * @param appKey               application key for this message
     * @param lightLightness       lightLightness of the LightCtlModel
     * @param lightTemperature     temperature of the LightCtlModel
     * @param lightDeltaUv         delta uv of the LightCtlModel
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    @SuppressWarnings("WeakerAccess")
    public LightCtlDefaultSet(@NonNull final byte[] appKey,
                       final int lightLightness,
                       final int lightTemperature,
                       final int lightDeltaUv) throws IllegalArgumentException {
        super(appKey);
        if (lightLightness < 0 || lightLightness > 0xFFFF)
            throw new IllegalArgumentException("Light lightness value must be between 0 to 0xFFFF");
        if (lightTemperature < 0x0320 || lightTemperature > 0x4E20)
            throw new IllegalArgumentException("Light temperature value must be between 0x0320 to 0x4E20");
        if (lightDeltaUv != 0 && (lightDeltaUv < -32768 || lightDeltaUv > 32767))
            throw new IllegalArgumentException("Light delta uv value must be between 0x8000 to 0x7FFF (signed) or 0");
        this.mLightness = lightLightness;
        this.mTemperature = lightTemperature;
        this.mDeltaUv = lightDeltaUv;
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
        Log.v(TAG, "Temperature: " + mTemperature);
        Log.v(TAG, "Delta UV: " + mDeltaUv);

        paramsBuffer = ByteBuffer.allocate(LIGHT_CTL_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
        paramsBuffer.putShort((short) mLightness);
        paramsBuffer.putShort((short) mTemperature);
        paramsBuffer.putShort((short) mDeltaUv);

        mParameters = paramsBuffer.array();
    }
}
