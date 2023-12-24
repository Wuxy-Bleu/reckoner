package demo.usul.controller.advice;

import demo.usul.pojo.response.ErrResponseWithLang;
import demo.usul.properties.I18nMsgProperties;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static demo.usul.ErrCode.ACCT_ERR_00A;
import static demo.usul.ErrCode.VALID_ERR;

@RestControllerAdvice
@RequiredArgsConstructor
public class AccountAdvice {

    private final I18nMsgProperties i18nMsgProperties;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrResponseWithLang> handleConstraintViolationException(ConstraintViolationException ex) {
        String errMsg = i18nMsgProperties.getErrMsg(ACCT_ERR_00A);
        return ResponseEntity.badRequest().body(ErrResponseWithLang.builder().errCode(VALID_ERR).message(errMsg).details(ex.getMessage()).build());
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
