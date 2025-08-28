package com.devops.Dto;

public class PodDto {

    private Long id;
    private String name;
    private String namespace;
    private String nodeName;
    private String phase;
    private String podIP;
    private String hostIP;
    private Integer RestartCount;



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

    public String getNodeName() {return nodeName;}
    public void setNodeName(String nodeName) {this.nodeName = nodeName;}

}
