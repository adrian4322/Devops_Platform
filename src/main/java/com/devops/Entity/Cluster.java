package com.devops.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name="clusters")
public class Cluster {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele nu poate sa fie gol")
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String endpoint;
    private String token;
    private String status;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL)
    private List<Node> nodesList;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL)
    private List<Pod> podsList;

    public Cluster() {}

    public Long getId() {return id;}
    public void setId(Long id) { this.id = id;}

    public String getName() { return name; }
    public void setName(String name) {this.name = name;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Node> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<Node> nodesList) {
        this.nodesList = nodesList;
    }

    public List<Pod> getPodsList() {
        return podsList;
    }

    public void setPodsList(List<Pod> podsList) {
        this.podsList = podsList;
    }

}