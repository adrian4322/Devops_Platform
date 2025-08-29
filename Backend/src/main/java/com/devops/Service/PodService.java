package com.devops.Service;

import org.springframework.stereotype.Service;

import com.devops.Entity.Pod;
import com.devops.Entity.Node;
import com.devops.Dto.PodDto;
import com.devops.Mapper.PodMapper;
import com.devops.Repository.PodRepository;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.api.model.PodList;

import java.util.List;
import java.util.ArrayList;

@Service
public class PodService {

    private final PodRepository podRepository;

    public PodService(PodRepository podRepository){
        this.podRepository=podRepository;
    }
    
    public List<PodDto> getAllPodsDto(){
        return podRepository.findAll()
                .stream()
                .map(PodMapper::convertToDto)
                .toList();
    }

    public List<PodDto> getAllPodsInNamespace(String namespace) {
    List<Pod> podsInNamespace = podRepository.findByNamespace(namespace);

    return podsInNamespace.stream()
            .map(PodMapper::convertToDto)
            .toList();
}


    public Pod getPodByName(String name){
        return podRepository.findPodByName(name);
    }

    public Pod getPodById(Long Id){
        return podRepository.findPodById(Id);
    }

    public List<Pod> syncPodsFromCluster(KubernetesClient client, List<Node> nodes){
        PodList podList = client.pods().inAnyNamespace().list();

        List<Pod> pods = new ArrayList<>();
        for(io.fabric8.kubernetes.api.model.Pod fabricPod : podList.getItems()){
            if(fabricPod.getMetadata() == null || fabricPod.getStatus() == null || fabricPod.getSpec() == null) {
                continue;
            }
            
            if(fabricPod.getSpec().getNodeName() == null) {
                continue;
            }
            
            Pod pod = new Pod();
            pod.setName(fabricPod.getMetadata().getName());
            pod.setNamespace(fabricPod.getMetadata().getNamespace());
            pod.setPhase(fabricPod.getStatus().getPhase());
            pod.setPodIP(fabricPod.getStatus().getPodIP());
            pod.setHostIP(fabricPod.getStatus().getHostIP());
            pod.setNodeName(fabricPod.getSpec().getNodeName());

            if (fabricPod.getStatus().getContainerStatuses() != null && !fabricPod.getStatus().getContainerStatuses().isEmpty()) {
                pod.setRestartCount(fabricPod.getStatus().getContainerStatuses().get(0).getRestartCount());
            }

            for(Node node : nodes) {
                if(node.getName().equals(pod.getNodeName())) {
                    pod.setNode(node);
                    node.getPodsList().add(pod);
                    break;
                }
            }

            pods.add(pod);
        }

        try {
            List<Pod> savedPods = podRepository.saveAll(pods);
            return savedPods;
        } catch (Exception e) {
            return pods;
        }
    }

}
