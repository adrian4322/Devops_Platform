package com.devops.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.devops.Entity.Pod;
import com.devops.Entity.Node;
import com.devops.Repository.PodRepository;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.api.model.PodList;

import com.devops.Dto.PodDto;

import java.util.List;
import java.util.ArrayList;

@Service
public class PodService {

    @Autowired
    private PodRepository podRepository;
    
    public List<Pod> getAllPods(){
        return podRepository.findAll();
    }

    public Pod getPodByName(String name){
        return podRepository.findPodByName(name);
    }

    public Pod getPodById(Long Id){
        return podRepository.findPodById(Id);
    }

    public PodDto convertToDto(Pod pod) {
        PodDto dto = new PodDto();
        dto.setId(pod.getId());
        dto.setName(pod.getName());
        dto.setNamespace(pod.getNamespace());
        dto.setPhase(pod.getPhase());
        dto.setPodIP(pod.getPodIP());
        dto.setHostIP(pod.getHostIP());
        dto.setRestartCount(pod.getRestartCount());
        dto.setNodeName(pod.getNodeName());
        return dto;
    }

    public List<Pod> syncPodsFromCluster(KubernetesClient client, List<Node> nodes){
        PodList podList = client.pods().list();

        List<Pod> pods = new ArrayList<>();
        for(io.fabric8.kubernetes.api.model.Pod fabricPod : podList.getItems()){
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

            nodes.stream()
                    .filter(n -> n.getName().equals(pod.getNodeName()))
                    .findFirst()
                    .ifPresent(n -> {
                        pod.setNode(n);
                        n.getPodsList().add(pod);
                    });

            pods.add(pod);
        }

        podRepository.saveAll(pods);

        return pods;
    }

}
