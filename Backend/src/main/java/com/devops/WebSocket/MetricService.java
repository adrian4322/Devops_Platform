package com.devops.WebSocket;

import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.api.model.NodeList;
import com.devops.Config.KubernetesClientFactory;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class MetricService {


    private final SimpMessagingTemplate messagingTemplate;
    private final KubernetesClientFactory clientFactory;

    public MetricService(SimpMessagingTemplate messagingTemplate, KubernetesClientFactory clientFactory) {
        this.messagingTemplate = messagingTemplate;
        this.clientFactory = clientFactory;
    }

    @Scheduled(fixedRate = 2000)
    public void sendMetrics(String endpoint, String token) {

        KubernetesClient client = clientFactory.buildClient(endpoint, token);

        NodeList nodeList = client.nodes().list();


        for(io.fabric8.kubernetes.api.model.Node fabricNode : nodeList.getItems()){
            MetricMessage message = MetricMessage.builder()
                    .node(fabricNode.getMetadata().getName())
                    .cpuUsage(Double.parseDouble(fabricNode.getStatus().getCapacity().get("cpu").getAmount()))
                    .memoryUsage(Double.parseDouble(fabricNode.getStatus().getCapacity().get("memory").getAmount()))
                    .timestamp(System.currentTimeMillis())
                    .build();

            messagingTemplate.convertAndSend("/topic/metrics", message);           

        }
    }

}
