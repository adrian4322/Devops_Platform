package com.devops.Service;

import org.springframework.stereotype.Service;

import com.devops.Entity.Cluster;
import com.devops.Entity.Node;
import com.devops.Repository.ClusterRepository;
import com.devops.Dto.ClusterCreateRequest;
import com.devops.Dto.ClusterResponseDto;
import com.devops.Mapper.ClusterMapper;
import com.devops.Config.KubernetesClientFactory;

import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClusterService {
    
    private final ClusterRepository clusterRepository;
    private final NodeService nodeService;
    private final KubernetesClientFactory clientFactory;

    public ClusterService(ClusterRepository clusterRepository,
                          NodeService nodeService,
                          KubernetesClientFactory clientFactory){
            
        this.clusterRepository=clusterRepository;
        this.nodeService=nodeService;
        this.clientFactory=clientFactory;

    }

    public List<ClusterResponseDto> getAllClustersResponseDto() {
        return clusterRepository.findAll().stream()
                .map(ClusterMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ClusterResponseDto> getClusterDtoById(Long id){
        return clusterRepository.findById(id)
                .map(ClusterMapper::convertToResponseDto);
    }

    public Optional<ClusterResponseDto> getClusterDtoByName(String name) {
        return clusterRepository.findByName(name)
                .map(ClusterMapper::convertToResponseDto);
    }

    
    public ClusterResponseDto createCluster(ClusterCreateRequest request) {
        Cluster cluster = ClusterMapper.toEntity(request);
        Cluster saved = clusterRepository.save(cluster);
        updateClusterMetrics(saved);
        return ClusterMapper.convertToResponseDto(saved);
    }

    private void updateClusterMetrics(Cluster cluster) {
        try {
            KubernetesClient client = clientFactory.buildClient(cluster.getEndpoint(), cluster.getToken());

             List<Node> nodeList = nodeService.syncNodesFromCluster(client, cluster);
             cluster.setNodesList(nodeList);

            clusterRepository.save(cluster);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la update cluster", e);
        }
    }


}
