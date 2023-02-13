package me.kxmischesdomi.db.models;

import dev.morphia.annotations.*;
import lombok.*;
import me.kxmischesdomi.db.domain.Model;

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
        }),
        @Index(options = @IndexOptions(), fields = {
                @Field("cachedName")
        }),
        @Index(options = @IndexOptions(), fields = {
                @Field("cachedIcon")
        })
})
public class UserModel extends Model {

    private Long userId;

    private String cachedName;

    private String cachedIcon;

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }

}
