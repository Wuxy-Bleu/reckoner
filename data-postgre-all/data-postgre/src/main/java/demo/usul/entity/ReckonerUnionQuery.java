package demo.usul.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReckonerUnionQuery {

    private List<Union> page;
    private Integer totalSize;

    @Data
    @NoArgsConstructor
    public static class Union {

        private UUID id;
        private OffsetDateTime transDate;
        private String source;
    }
}
