package no.nordicsemi.android.meshprovisioner.models;

import android.os.Parcel;

@SuppressWarnings("WeakerAccess")
public class LightLcClient extends SigModel {

    public static final Creator<LightLcClient> CREATOR = new Creator<LightLcClient>() {
        @Override
        public LightLcClient createFromParcel(final Parcel source) {
            return new LightLcClient(source);
        }

        @Override
        public LightLcClient[] newArray(final int size) {
            return new LightLcClient[size];
        }
    };

    public LightLcClient(final int modelId) {
        super(modelId);
    }

    private LightLcClient(final Parcel source) {
        super(source);
    }

    @Override
    public String getModelName() {
        return "Light LC Client";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.parcelMeshModel(dest, flags);
    }
}
