package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        var spec =  Specification.allOf(
                        withTitleCont(params.titleCont()))
                .and(withAssigneeId(params.assigneeId()))
                .and(withStatus(params.status()))
                .and(withLabelId(params.labelId()));
        return spec;
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return ((root, query, cb) -> titleCont == null ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + titleCont + "%"));    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) ->
                status == null
                        ? cb.conjunction()
                        : cb.equal(root.get("taskStatus").get("slug"), status);
    }

    private Specification<Task> withAssigneeId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.join("assignee").get("id"), id);
        };
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) {
                return cb.conjunction();
            }
            Join<Task, Label> labelJoin = root.join("labels");
            return cb.equal(labelJoin.get("id"), labelId);
        };
    }
}
