package me.kxmischesdomi.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Getter
@Setter
public final class Config {

	private String version;
	private String support;
	private String website;
	private String invite;
	private String token;
	private MorphiaConfig morphiaConfig;

	@Getter
	@Setter
	public static final class MorphiaConfig {

		private String host;
		private int port;
		private String username;
		private String password;
		private String database;

	}

}
