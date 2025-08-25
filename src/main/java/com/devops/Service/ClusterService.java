package com.devops.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.devops.Entity.Cluster;
import com.devops.Entity.Node;
import com.devops.Entity.Pod;
import com.devops.Service.PodService;
import com.devops.Repository.ClusterRepository;
import com.devops.Dto.ClusterDto;
import com.k8s.KubernetesClientFactory;

import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class ClusterService {
    
    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private PodService podService;

    @Autowired
    private KubernetesClientFactory clientFactory;

    public List<Cluster> getAllClusters(){
        return clusterRepository.findAll();
    }

    public Cluster getClusterByName(String name) {
        return clusterRepository.findByName(name);
    }

    public Cluster getClusterById(Long id) {
        return clusterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clusterul nu a fost gasit pe baza id-ului." + id));
    }

    public Cluster createCluster(Cluster cluster) {
        Cluster saved = clusterRepository.save(cluster);
        updateClusterMetrics(saved);
        return saved;
    }

    public ClusterDto convertToDto(Cluster cluster){
        ClusterDto dto = new ClusterDto();
        dto.setId(cluster.getId());
        dto.setName(cluster.getName());
        dto.setEndpoint(cluster.getEndpoint());
        dto.setToken(cluster.getToken());
        return dto;
    }

    public List<ClusterDto> getAllClustersDto() {
        List<ClusterDto> clustersDto = new ArrayList<>();
        for(Cluster cluster : getAllClusters()){
            clustersDto.add(convertToDto(cluster));
        }
        return clustersDto;
    }

    public Optional<ClusterDto> getClusterDtoById(Long id){
        return clusterRepository.findById(id)
                .map(this::convertToDto);
    }


    public ClusterDto getClusterDtoByName(String name) {
        return convertToDto(getClusterByName(name));
    }

    private void updateClusterMetrics(Cluster cluster) {
        try {
            KubernetesClient client = clientFactory.buildClient(cluster.getEndpoint(), cluster.getToken());

             List<Node> nodeList = nodeService.syncNodesFromCluster(client);
             List<Pod> podList = podService.syncPodsFromCluster(client, nodeList);

             cluster.setNodesList(nodeList);
             cluster.setPodsList(podList);

            clusterRepository.save(cluster);
        } catch (Exception e) {
            new RuntimeException("Eroare la update cluster");
        }
    }


}
