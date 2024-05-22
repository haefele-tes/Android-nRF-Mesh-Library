package no.nordicsemi.android.meshprovisioner.transport;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.MeshAddress;

/**
 * To be used as a wrapper class for when creating the Generic Access Message Status.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class GenericAccessMessageStatus extends GenericStatusMessage implements Parcelable {

    private static final String TAG = GenericAccessMessageStatus.class.getSimpleName();
    //private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_ON_POWER_UP_STATUS;
	private int mOpcode;

    private static final Creator<GenericAccessMessageStatus> CREATOR = new Creator<GenericAccessMessageStatus>() {
        @Override
        public GenericAccessMessageStatus createFromParcel(Parcel in) {
            final AccessMessage message = in.readParcelable(AccessMessage.class.getClassLoader());
            //noinspection ConstantConditions
            return new GenericAccessMessageStatus(message);
        }

        @Override
        public GenericAccessMessageStatus[] newArray(int size) {
            return new GenericAccessMessageStatus[size];
        }
    };

    /**
     * Constructs the GenericAccessMessageStatus mMessage.
     *
     * @param message Access Message
     */
    public GenericAccessMessageStatus(@NonNull final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
		this.mOpcode = message.getOpCode();
    }

	@Override
	void parseStatusParameters() {
	}

	public int getOpcode() {
		return mOpcode;
	}

	@Override
    int getOpCode() {
        return this.mOpcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        final AccessMessage message = (AccessMessage) mMessage;
        dest.writeParcelable(message, flags);
    }
}
