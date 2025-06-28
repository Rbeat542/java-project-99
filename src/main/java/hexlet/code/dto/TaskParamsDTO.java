package hexlet.code.dto;

import org.springframework.web.bind.annotation.RequestParam;

public record TaskParamsDTO (
        @RequestParam(required = false) String titleCont,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long assigneeId,
        @RequestParam(required = false) Long labelId
) {}

