package no.nordicsemi.android.meshprovisioner.data;

import androidx.annotation.NonNull;

public final class TaiUtcDelta {
    private final short encodedValue;

    private TaiUtcDelta(short encodedValue) {
        this.encodedValue = encodedValue;
    }


    @NonNull
    public static TaiUtcDelta of(short encodedValue) {
        return new TaiUtcDelta(encodedValue);
    }


    @NonNull
    public static TaiUtcDelta encode(short offset) {
        return new TaiUtcDelta( (short) (offset - 0xff));
    }

    public short getEncodedValue() {
        return encodedValue;
    }

    public short getSeconds() {
        return (short) (encodedValue + 0xFF);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiUtcDelta that = (TaiUtcDelta) o;
        return encodedValue == that.encodedValue;
    }

    @Override
    public int hashCode() {
        return Short.valueOf(encodedValue).hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "TaiUtcDelta{" +
                "encodedValue=" + encodedValue +
                " seconds=" + getSeconds() +
                '}';
    }
}
