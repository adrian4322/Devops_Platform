package com.devops.Service;

import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.api.model.NodeList;

import com.devops.Entity.Node;
import com.devops.Entity.Cluster;
import com.devops.Dto.NodeDto;
import com.devops.Repository.NodeRepository;
import com.devops.Mapper.NodeMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class NodeService {

        private final NodeRepository nodeRepository;

        public NodeService(NodeRepository nodeRepository){
            this.nodeRepository = nodeRepository;
        }

        public List<Node> getAllNodes(){
            return nodeRepository.findAll();
        }

        public NodeDto getNodeDtoByName(String name){
            return nodeRepository.findByName(name)
                    .map(NodeMapper::convertNodeToDto)
                    .orElseThrow(() -> new RuntimeException("Node-ul nu a fost gasit dupa nume:" + name));
        }

        public Optional<NodeDto> getNodeDtoById(Long id) {
            return nodeRepository.findById(id)
                    .map(NodeMapper::convertNodeToDto);
        }

        public List<NodeDto> getAllNodesDto() {
            return getAllNodes()
                    .stream()
                    .map(NodeMapper::convertNodeToDto)
                    .toList();
        }

        public List<Node> syncNodesFromCluster(KubernetesClient client, Cluster cluster){
            NodeList nodeList = client.nodes().list();

            List<Node> nodes = new ArrayList<>();
            for(io.fabric8.kubernetes.api.model.Node fabricNode : nodeList.getItems()){
                Node node = new Node();
                node.setName(fabricNode.getMetadata().getName());
                String nodeStatus = fabricNode.getStatus().getConditions().stream()
                                        .filter(c -> "Ready".equals(c.getType()))
                                        .map(c -> c.getStatus())
                                        .findFirst()
                                        .orElse("Unknown");
                                    node.setStatus(nodeStatus);
                node.setCluster(cluster);
                node.setPodsList(new ArrayList<>());
                nodes.add(node);
            }
            
            try {
                List<Node> savedNodes = nodeRepository.saveAll(nodes);
                return savedNodes;
            } catch (Exception e) {
                return nodes;
            }
        }

        public List<Node> getNodeByClusterId(Cluster cluster) {
            return nodeRepository.findNodesByClusterId(cluster.getId());
        }

        public List<Node> getNodeByClusterName(Cluster cluster) {
            return nodeRepository.findNodesByClusterName(cluster.getName());
        }
        
    
}
