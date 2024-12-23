package demo.usul.service;

import demo.usul.entity.LoanEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerLoanUnionPage;
import demo.usul.entity.ReckonerUnionSubQuery;
import demo.usul.repository.LoanRepository;
import demo.usul.repository.ReckonerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReckonerServiceV3 {

    private final ReckonerService reckonerService;
    private final ReckonerRepository reckonerRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public ReckonerServiceV3(ReckonerService reckonerService, ReckonerRepository reckonerRepository, LoanRepository loanRepository) {
        this.reckonerService = reckonerService;
        this.reckonerRepository = reckonerRepository;
        this.loanRepository = loanRepository;
    }

    public ReckonerLoanUnionPage getAllTransactions(int pageNum, int pageSize) {
        List<ReckonerUnionSubQuery> unionPage = loanRepository.findAllTransactionsPageableUnion2Table(PageRequest.of(pageNum, pageSize));
        long rSize = unionPage.stream().filter(e -> "a".equals(e.getSource())).count();
        long lSize = unionPage.size() - rSize;
        List<ReckonerEntity> reckonerPage = reckonerRepository.findOrderByTransDateAndIdLimitSize((int) rSize);
        List<LoanEntity> loanPage = loanRepository.findOrderByTransDateAndIdLimitSize((int) lSize);

        ReckonerLoanUnionPage page = new ReckonerLoanUnionPage();
        page.setReckonerPage(reckonerPage);
        page.setLoanPage(loanPage);
        page.setPageNum(pageNum);
        page.setPageSize((int) (rSize + lSize));
        page.setReckonerPageNum(pageNum);
        page.setReckonerPageSize((int) rSize);
        page.setLoanPageNum(pageNum);
        page.setLoanPageSize((int) lSize);
        return page;
    }

    public void deleteReckoner(UUID id) {
        int i = reckonerRepository.updateIsAliveByIsAliveTrueAndId(id);
        log.info("{} reckoner deleted from table", i);
    }
}
