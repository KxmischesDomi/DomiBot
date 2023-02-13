package me.kxmischesdomi.db.config;

import lombok.Builder;

public record MorphiaConfig(String databaseHost, int databasePort, String databaseUser, String databasePassword, String databaseName) {
    @Builder public MorphiaConfig {}
}
