package com.metrics.dockercompose.models;

public class Measurement {
    private String apiIdentifier;
    private int value;
    private String unit;
    private String timestamp;

    public Measurement(String apiIdentifier, int value, String unit, String timestamp) {
        this.apiIdentifier = apiIdentifier;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    public String getApiIdentifier() {
        return apiIdentifier;
    }

    public void setApiIdentifier(String apiIdentifier) {
        this.apiIdentifier = apiIdentifier;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
