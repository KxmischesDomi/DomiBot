package me.kxmischesdomi.db.config;

import lombok.Builder;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record MorphiaConfig(String databaseHost, int databasePort, String databaseUser, String databasePassword, String databaseName) {
    @Builder public MorphiaConfig {}
}
