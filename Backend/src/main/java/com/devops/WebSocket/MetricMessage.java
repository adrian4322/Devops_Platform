package com.devops.WebSocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetricMessage {
    private String node;
    private double cpuUsage;
    private double memoryUsage;
    private long timestamp;
}
