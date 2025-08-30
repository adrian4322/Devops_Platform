package com.devops.Mapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.devops.Dto.ClusterCreateRequest;
import com.devops.Dto.ClusterResponseDto;
import com.devops.Entity.Cluster;

public class ClusterMapperTest {
    
    @Test
    void toEntity_mapsAllFields() {
        ClusterCreateRequest request = new ClusterCreateRequest();
        request.setName("prod");
        request.setEndpoint("https://k8s");
        request.setToken("SECRET");
        request.setStatus("ACTIVE");

        Cluster entity = ClusterMapper.toEntity(request);

       assertNotNull(entity);
       assertEquals("prod", entity.getName());
       assertEquals("https://k8s", entity.getEndpoint());
       assertEquals("ACTIVE", entity.getStatus());
    }

    @Test
    void convertToResponseDto_hidesToken_andMapsFields() {
        Cluster cluster = new Cluster();
        cluster.setId(1L);
        cluster.setName("prod");
        cluster.setEndpoint("https://k8s");
        cluster.setToken("SECRET");
        cluster.setStatus("ACTIVE");

        ClusterResponseDto dto = ClusterMapper.convertToResponseDto(cluster);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("prod", dto.getName());
        assertEquals("https://k8s", dto.getEndpoint());
        assertEquals("ACTIVE", dto.getStatus());
    }


}
