package me.kxmischesdomi.db.domain;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Model {

    @Id
    private final ObjectId id;

    private Date updatedAt;
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = this.createdAt != null ? this.createdAt : new Date();
        this.updatedAt = new Date();
    }
}
