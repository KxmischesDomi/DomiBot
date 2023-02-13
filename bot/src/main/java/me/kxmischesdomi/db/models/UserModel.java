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
@Entity(value = "users", useDiscriminator = false)
@Indexes({
        @Index(options = @IndexOptions(unique = true), fields = {
                @Field("userId")
        })
})
public class UserModel extends Model {

    private long userId;

    private String cachedName;

    private String cachedDiscriminator;

    private String cachedAvatarURL;

    private LevelingModel userLevelingProfile;

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }

}
