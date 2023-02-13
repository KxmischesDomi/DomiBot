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
@Entity(useDiscriminator = false)
@Indexes({
        @Index(options = @IndexOptions(unique = true), fields = {
                @Field("userId")
        })
})
public class MemberModel extends Model {

    private long userId;

    private LevelingModel memberLevelingModel;

    @Reference
    private UserModel userModel;

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }

}
