package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link demo.usul.entity.ReckonerTypeEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReckonerTypeDto implements Serializable {

    @NotNull
    @NotBlank
    private String typeName;

    private OffsetDateTime createdAt;

    private OffsetDateTime lastUpdateAt;
}