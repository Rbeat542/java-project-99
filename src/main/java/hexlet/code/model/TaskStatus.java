package hexlet.code.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "task_statuses")
public class TaskStatus implements BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    //@ToString.Include
    //@EqualsAndHashCode.Include
    private Long id;

    @NotNull()
    @Size(min = 1)
    //@ToString.Include
    private String name;

    @NotNull
    @Size(min = 1)
    //@OneToOne(mappedBy = "task")
    private String slug;

    @CreatedDate
    private LocalDateTime createdAt;


}