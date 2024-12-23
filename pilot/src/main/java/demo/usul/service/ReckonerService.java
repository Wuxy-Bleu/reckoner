package demo.usul.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import demo.usul.beans.LoanCreateReq;
import demo.usul.converter.LoanMapper;
import demo.usul.dto.ReckonerDto;
import demo.usul.feign.LoanFeign;
import demo.usul.feign.ReckonerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReckonerService {

    private final ReckonerFeign reckonerFeign;
    private final LoanFeign loanFeign;
    private final CsvMapper csvMapper;
    private final LoanMapper loanMapper;

    @Autowired
    public ReckonerService(ReckonerFeign reckonerFeign, LoanFeign loanFeign, CsvMapper csvMapper, LoanMapper loanMapper) {
        this.reckonerFeign = reckonerFeign;
        this.loanFeign = loanFeign;
        this.csvMapper = csvMapper;
        this.loanMapper = loanMapper;
    }

    public Long countDistinctByFromAcctAllIgnoreCase(UUID fromAcct) {
        return reckonerFeign.countByFromAcct(fromAcct);
    }

    public List<ReckonerDto> retrieveByFromAcctName(String fromAcctName) {
        return reckonerFeign.retrieveByFromAcctName(fromAcctName);
    }

    public List<ReckonerDto> retrieveByToAcctName(String name) {
        return reckonerFeign.retrieveByToAcctName(name);
    }

    public Page<ReckonerDto> retrieveAll() {
        return reckonerFeign.retrieveAll(1000, 0);
    }

    public String retrieveAllAsCsv() throws JsonProcessingException {
        CsvSchema header = csvMapper.schemaFor(ReckonerDto.class).withHeader();
        return csvMapper.writer(header).writeValueAsString(retrieveAll());
    }

    /**
     * default way to create a reckoner record, transactional, will modify account balance
     *
     * @param reckoner new record
     * @return created record, throw exception if anything wrong
     */
    public ReckonerDto createOne(ReckonerDto reckoner) {
        return reckonerFeign.createOne(reckoner);
    }

    /**
     * create record without modify account balance, transactional
     */
    public ReckonerDto createOneNoTrigger(ReckonerDto reckonerDto) {
        return reckonerFeign.createOneNoTrigger(reckonerDto, false);
    }

    public Object newCreditLoan(LoanCreateReq loanCreateReq) {
        return loanFeign.createLoan(loanMapper.loanCreateReq2Dto(loanCreateReq));
    }
}
