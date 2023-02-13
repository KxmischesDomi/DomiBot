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

	private int messages = 0;

	private int cachedLevel = 1;

	private int cachedXpToThisLevel = 0;

	private int cachedXpToNextLevel = 100;

	// Not used for servers and users.
	private long lastTimeLeveledMillis = 0;

	public void recalculateCachedValues(float multiplier) {

		int level = 1;
		int xpToNextLevel = (int) (100 * multiplier);
		int xpToThisLevel = 0;

		while (xp >= xpToNextLevel) {
			level++;
			xpToThisLevel = xpToNextLevel;
			xpToNextLevel = (int) (5 * (level ^ 2) * multiplier + 50 * level * multiplier + 100 * multiplier + xpToThisLevel);
		}

		this.cachedLevel = level;
		this.cachedXpToThisLevel = xpToThisLevel;
		this.cachedXpToNextLevel = xpToNextLevel;

	}

}
