package demo.usul.feign.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InOutEnum {
    IN("in"),
    OUT("out"),
    TRANSFER("transfer");

    public final String inOut;
}
