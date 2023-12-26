package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class CommonColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = 830583455728838257L;

    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Shanghai")
    private OffsetDateTime lastUpdatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Shanghai")
    private OffsetDateTime createdAt;
}
