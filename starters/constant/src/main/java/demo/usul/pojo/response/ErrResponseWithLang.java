package demo.usul.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrResponseWithLang implements Serializable {

    @Serial
    private static final long serialVersionUID = -742521756846535932L;

    private Integer errCode;

    private String message;

    private String details;

}
