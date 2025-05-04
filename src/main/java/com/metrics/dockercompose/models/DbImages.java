package com.metrics.dockercompose.models;

import java.util.Arrays;
import java.util.List;

public class DbImages {
    public static final List<String> DATABASE_IMAGES = Arrays.asList(
        "postgres",
        "mysql",
        "mongo",
        "redis",
        "mariadb",
        "cassandra",
        "couch"
    );
}
