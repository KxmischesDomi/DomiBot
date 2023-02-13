package me.kxmischesdomi.db.models;

import dev.morphia.annotations.*;
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
public class LevelingModel {

	private int xp = 0;

	private int cachedLevel = 1;

}
