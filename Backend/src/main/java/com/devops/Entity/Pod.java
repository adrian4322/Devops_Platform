package com.devops.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "pods")
public class Pod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private String namespace;
    private String phase;
    private String podIP;
    private String hostIP;

    @Column(name = "restart_count")
    private Integer RestartCount;
    
    @Column(name = "node_name")
    private String nodeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    public Pod() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }

    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; }

    public String getPodIP() { return podIP; }
    public void setPodIP(String podIP) { this.podIP = podIP; }

    public String getHostIP() { return hostIP; }
    public void setHostIP(String hostIP) { this.hostIP = hostIP; }

    public Integer getRestartCount() { return RestartCount; }
    public void setRestartCount(Integer restartCount) { RestartCount = restartCount; }

    public Node getNode() { return node; }
    public void setNode(Node node) { this.node = node; }

    public String getNodeName() {return nodeName;}
    public void setNodeName(String nodeName) {this.nodeName = nodeName;}

}
