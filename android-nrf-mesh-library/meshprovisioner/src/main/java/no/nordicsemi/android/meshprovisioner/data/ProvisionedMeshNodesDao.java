package no.nordicsemi.android.meshprovisioner.data;


import androidx.annotation.RestrictTo;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import no.nordicsemi.android.meshprovisioner.transport.ProvisionedMeshNode;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@SuppressWarnings("unused")
@Dao
public interface ProvisionedMeshNodesDao {

    @Query("SELECT * from nodes WHERE mesh_uuid == :meshUuid")
    List<ProvisionedMeshNode> loadMeshNodes(final String meshUuid);
}
