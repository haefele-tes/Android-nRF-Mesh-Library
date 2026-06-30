package no.nordicsemi.android.meshprovisioner.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Supported algorithm type
 */
@SuppressWarnings("unused")
public enum AlgorithmType {

    /**
     * Static OOB Type
     */
    NONE((short) 0x0000),
    FIPS_P_256_ELLIPTIC_CURVE((short) 0x0001);

    private static final String TAG = AlgorithmType.class.getSimpleName();
    private short algorithmType;

    AlgorithmType(final short algorithmType) {
        this.algorithmType = algorithmType;
    }

    /**
     * Returns the algorithm oob type value
     */
    public short getAlgorithmType() {
        return algorithmType;
    }


    /**
     * Returns the oob method used for authentication
     *
     * @param method auth method used
     */
    public static AlgorithmType fromValue(final short method) {
        switch (method) {
            default:
            case 0x0000:
                return NONE;
            case 0x0001:
                return FIPS_P_256_ELLIPTIC_CURVE;
        }
    }

    /**
     * Parses the output oob action value
     *
     * @param algorithmTypeValue algorithm type
     * @return selected output action type
     */
    public static ArrayList<AlgorithmType> getAlgorithmTypeFromBitMask(final short algorithmTypeValue) {
        final AlgorithmType[] algorithmTypes = {FIPS_P_256_ELLIPTIC_CURVE};
        final ArrayList<AlgorithmType> supportedAlgorithms = new ArrayList<>();
        for (AlgorithmType algorithmType : algorithmTypes) {
            if ((algorithmTypeValue & algorithmType.ordinal()) == algorithmType.ordinal()) {
                supportedAlgorithms.add(algorithmType);
                Log.v(TAG, "Supported output oob action type: " + getAlgorithmTypeDescription(algorithmType));
            }
        }
        return supportedAlgorithms;
    }

    /**
     * Returns the algorithm description
     *
     * @param type {@link AlgorithmType} type
     * @return Input OOB type description
     */
    public static String getAlgorithmTypeDescription(final AlgorithmType type) {
        switch (type) {
            case FIPS_P_256_ELLIPTIC_CURVE:
                return "FIPS P-256 Elliptic Curve";
            default:
                return "Unknown";
        }
    }

    public static byte getAlgorithmValue(final short type) {
        // type is a bitmask from device capabilities:
        // Bit 0 (0x0001): FIPS P-256 with CMAC-AES128 (Mesh 1.0)
        // Bit 1 (0x0002): FIPS P-256 with HMAC-SHA256 (Mesh 1.1)

        // Prefer Mesh 1.0 algorithm if supported (better compatibility)
        if ((type & 0x0001) != 0) {
            return 0; // Mesh 1.0 algorithm
        }
        // Fall back to Mesh 1.1 only if device doesn't support Mesh 1.0
        if ((type & 0x0002) != 0) {
            return 1; // Mesh 1.1 algorithm
        }
        // Default to Mesh 1.0 if no algorithms specified (shouldn't happen per spec)
        return 0;
    }
}
