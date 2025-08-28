package com.devops.Config;

import org.springframework.stereotype.Component;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@Component
public class KubernetesClientFactory {
    
    
    public KubernetesClient buildClient(String endpoint, String token){
        Config config = new ConfigBuilder()
                        .withMasterUrl(endpoint)
                        .withOauthToken(token)
                        .withTrustCerts(true)
                        .build();

        return new KubernetesClientBuilder().withConfig(config).build();

    }
}   
