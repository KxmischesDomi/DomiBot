package me.kxmischesdomi.db.models;

import dev.morphia.annotations.*;
import lombok.*;
import me.kxmischesdomi.db.domain.Model;

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
@Entity(value = "guilds", useDiscriminator = false)
@Indexes({
        @Index(options = @IndexOptions(unique = true), fields = {
                @Field("guildId")
        })
})
public class GuildModel extends Model {

    private long guildId;

    private String cachedName;

    private String cachedIconURL;

    private LevelingModel guildLevelingProfile;

    // private ... guildMembers

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }

}
