package com.devops.Dto;

import jakarta.validation.constraints.NotBlank;

public class ClusterCreateRequest{

    @NotBlank(message="Numele nu poate sa fie gol")
    private String name;

    @NotBlank(message="Endpoint este obligatoriu")
    private String endpoint;

    @NotBlank(message="Token-ul este obligatoriu")
    private String token;
    private String status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}