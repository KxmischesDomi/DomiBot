package me.kxmischesdomi.db.models;

import dev.morphia.annotations.Entity;
import lombok.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString(callSuper = true)
@Entity(useDiscriminator = false)
public class WebClientModel {

	private String accessToken;
	private String refreshToken;
	private long tokenResetMillis;
	private String[] scopes;

}
