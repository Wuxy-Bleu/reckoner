package demo.usul.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardErrResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 5151527661955943638L;

    private String code;

    private String message;

    private String example;

    private Map<String, String> details;

}
