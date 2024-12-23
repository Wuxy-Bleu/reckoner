package demo.usul.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class TransactionPage {

    private Collection<Transaction> content;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean isLast;
}
