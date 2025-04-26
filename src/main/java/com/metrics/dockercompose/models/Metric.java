package com.metrics.dockercompose.models;

public class Metric {
    private String name;
    private String collectorStrategy;

    public Metric(String name, String collectorStrategy) {
        this.name = name;
        this.collectorStrategy = collectorStrategy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollectorStrategy() {
        return collectorStrategy;
    }

    public void setCollectorStrategy(String collectorStrategy) {
        this.collectorStrategy = collectorStrategy;
    }
}
