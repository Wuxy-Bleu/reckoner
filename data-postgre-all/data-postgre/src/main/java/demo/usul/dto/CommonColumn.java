package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class CommonColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = 830583455728838257L;

    private OffsetDateTime lastUpdatedAt;

    private OffsetDateTime createdAt;
}
