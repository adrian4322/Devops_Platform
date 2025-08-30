package com.devops.Service;

import com.devops.Config.KubernetesClientFactory;
import com.devops.Dto.ClusterCreateRequest;
import com.devops.Dto.ClusterResponseDto;
import com.devops.Entity.Cluster;
import com.devops.Entity.Node;
import com.devops.Repository.ClusterRepository;
import com.devops.Service.ClusterService;
import com.devops.Service.NodeService;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClusterServiceTest {
    
    @Mock ClusterRepository clusterRepository;
    @Mock NodeService nodeService;
    @Mock KubernetesClientFactory clientFactory;
    @Mock KubernetesClient kubernetesClient;

    @InjectMocks ClusterService clusterService;

    Cluster saved;

    @BeforeEach
    void setup() {
        saved = new Cluster();
        saved.setId(10L);
        saved.setName("prod");
        saved.setEndpoint("https://k8s");
        saved.setToken("SECRET");
        saved.setStatus("ACTIVE");
    }

    @Test
    void getAllClustersResponseDto_mapsEntites() {
        when(clusterRepository.findAll()).thenReturn(List.of(saved));

        var list = clusterService.getAllClustersResponseDto();

        assertEquals(1, list.size());
        assertEquals(10L, list.get(0).getId());
        assertEquals("prod", list.get(0).getName());
    }

    @Test
    void getClusterResponseDtoById_returnOptional() {
        when(clusterRepository.findById(10L))
                    .thenReturn(Optional.of(saved));

        Optional<ClusterResponseDto> dto = clusterService.getClusterDtoById(10L);

        assertEquals("prod", dto.get().getName());
        assertTrue(dto.isPresent());
    }

    @Test
    void createCluster_persists_andUpdateMetrics() {
        ClusterCreateRequest request = new ClusterCreateRequest();
        request.setName("prod");
        request.setEndpoint("https://k8s");
        request.setToken("SECRET");
        request.setStatus("ACTIVE");

        when(clusterRepository.save(any(Cluster.class))).thenReturn(saved);
        when(clientFactory.buildClient("https://k8s", "SECRET")).thenReturn(kubernetesClient);
        when(nodeService.syncNodesFromCluster(kubernetesClient, saved)).thenReturn(List.of(new Node()));

        ClusterResponseDto dto = clusterService.createCluster(request);

        assertEquals(10L, dto.getId());
        verify(clusterRepository, times(2)).save(any(Cluster.class));
        verify(nodeService).syncNodesFromCluster(kubernetesClient, saved);
    }

}
