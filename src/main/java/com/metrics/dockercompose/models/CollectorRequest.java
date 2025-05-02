package com.metrics.dockercompose.models;

public class CollectorRequest {
    private String dockerComposePath;

    public String getDockerComposePath() {
        return dockerComposePath;
    }

    public void setDockerComposePath(String dockerComposePath) {
        this.dockerComposePath = dockerComposePath;
    }
}
