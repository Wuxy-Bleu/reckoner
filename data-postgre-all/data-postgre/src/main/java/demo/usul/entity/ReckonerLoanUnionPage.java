package demo.usul.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReckonerLoanUnionPage {

    private List<ReckonerEntity> reckonerPage;
    private List<LoanEntity> loanPage;
    private Integer pageNum;
    private Integer pageSize;
    private Integer reckonerPageNum;
    private Integer loanPageNum;
    private Integer reckonerPageSize;
    private Integer loanPageSize;
    private Integer total;

    public ReckonerLoanUnionPage nextPage() {
        return this;
    }
}
