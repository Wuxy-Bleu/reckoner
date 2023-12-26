package demo.usul.decoder;

import demo.usul.exception.PostgreDeleteException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RetreiveMessageErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (methodKey.equals("AccountFeign#delete(List)")
                && (HttpStatus.GONE.equals(HttpStatus.valueOf(response.status())))) {
            return new PostgreDeleteException("xxx");
        }
        return errorDecoder.decode(methodKey, response);
    }
}

