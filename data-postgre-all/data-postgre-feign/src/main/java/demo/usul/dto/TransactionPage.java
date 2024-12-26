package demo.usul.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class TransactionPage {

    private Collection<Transaction> page;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean isLast;

    private Integer total;
}
