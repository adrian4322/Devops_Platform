package com.devops.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.devops.Entity.Node;
import com.devops.Entity.Cluster;
import com.devops.Repository.NodeRepository;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.api.model.NodeList;


import com.devops.Dto.NodeDto;

import java.util.List;
import java.util.ArrayList;

@Service
public class NodeService {

        @Autowired
        private NodeRepository nodeRepository;

        public List<Node> getAllNodes(){
            return nodeRepository.findAll();
        }

        public Node getNodeByName(String name){
            return nodeRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Node-ul nu a fost gasit dupa nume:" + name));
        }

        public Node getNodeById(Long id) {
            return nodeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Node-ul nu a fost gasit dupa id:" + id));
        }

        public NodeDto convertToDto(Node node){
                NodeDto dto = new NodeDto();
                dto.setId(node.getId());
                dto.setName(node.getName());
                dto.setStatus(node.getStatus());
                dto.setPodsList(node.getPodsList());
                return dto;
        }

        public List<NodeDto> getAllNodesDto() {
            List<NodeDto> nodesDto = new ArrayList<>();
            for(Node node : getAllNodes()){
                nodesDto.add(convertToDto(node));
            }
            return nodesDto;
        }

        public List<Node> syncNodesFromCluster(KubernetesClient client){
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
                node.setPodsList(new ArrayList<>());
                nodes.add(node);
            }
            
            nodeRepository.saveAll(nodes);
            return nodes;
        }

        public List<Node> getNodeByClusterId(Cluster cluster) {
            return nodeRepository.findNodesByClusterId(cluster.getId());
        }

        public List<Node> getNodeByClusterName(Cluster cluster) {
            return nodeRepository.findNodesByClusterName(cluster.getName());
        }
        
    
}
