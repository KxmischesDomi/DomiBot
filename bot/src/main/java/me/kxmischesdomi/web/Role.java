package me.kxmischesdomi.web;

import io.javalin.security.RouteRole;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public enum Role implements RouteRole {

	LOGGED_IN,
	ANYONE

}
