package hexlet.code.repository;

import java.util.Optional;

import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long>, JpaSpecificationExecutor<TaskStatus> {
    Optional<TaskStatus> findBySlug(String slug);
}
