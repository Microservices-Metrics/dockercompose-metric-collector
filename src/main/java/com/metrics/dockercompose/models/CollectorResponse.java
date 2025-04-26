package com.metrics.dockercompose.models;

public class CollectorResponse {
    private Metric metric;
    private Measurement measurement;

    public CollectorResponse(Metric metric, Measurement measurement) {
        this.metric = metric;
        this.measurement = measurement;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }
}
