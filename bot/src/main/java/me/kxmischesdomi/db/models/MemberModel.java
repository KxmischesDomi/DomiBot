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
@Entity(value = "members", useDiscriminator = false)
public class MemberModel extends Model {

    private long guildId;

    private long userId;

    private LevelingModel memberLevelingModel;

    @Reference
    private UserModel userModel;

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }

}
