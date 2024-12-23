package demo.usul.convert;

import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.LoanDto;
import demo.usul.dto.LoanScheduleDto;
import demo.usul.dto.Transaction;
import demo.usul.dto.TransactionPage;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.LoanScheduleEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerLoanUnionPage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true))
public interface LoanMapper {

    @Mapping(target = "fromAcctEntity.id", source = "fromAcct")
    LoanEntity loanCreate2LoanEntity(LoanCreateDto loanCreateDto);

    List<LoanDto> loanEntities2Dtos(List<LoanEntity> list);

    @Mapping(target = "fromAcctDto", source = "fromAcctEntity")
    @Mapping(target = "loanScheduleDtoSet", source = "loanScheduleEntitySet")
    LoanDto loanEntity2Dto(LoanEntity loanEntity);

    LoanScheduleDto loanScheduleEntity2Dto(LoanScheduleEntity entity);

    Collection<LoanScheduleDto> loanScheduleEntities2Dtos(Collection<LoanScheduleEntity> entities);

    @Mappings(value = {
            @Mapping(source = "principal", target = "amount"),
            @Mapping(source = "fromAcctEntity.id", target = "fromAcctId"),
            @Mapping(source = "fromAcctEntity.name", target = "fromAcct"),
            @Mapping(source = "fromAcctEntity.cardTypeEntity.typeName", target = "fromAcctType")
    })
    Transaction loanEntity2Transaction(LoanEntity loanEntity);

    List<Transaction> loanEntities2Transactions(List<LoanEntity> loanEntities);

    @AfterMapping
    default void setIsLoanFlag(LoanEntity loanEntity, @MappingTarget Transaction transaction) {
        transaction.setIsLoan(true);
    }

    @Mappings(value = {
            @Mapping(source = "fromAcct", target = "fromAcctId"),
            @Mapping(source = "toAcct", target = "toAcctId"),
            @Mapping(source = "fromAcctObj.name", target = "fromAcct"),
            @Mapping(source = "toAcctObj.name", target = "toAcct"),
            @Mapping(source = "fromAcctObj.cardTypeEntity.typeName", target = "fromAcctType"),
            @Mapping(source = "toAcctObj.cardTypeEntity.typeName", target = "toAcctType")
    })
    Transaction reckonerEntity2Transaction(ReckonerEntity reckoner);

    List<Transaction> reckonerEntities2Transactions(List<ReckonerEntity> reckonerEntities);

    @AfterMapping
    default void setIsLoanFlag2(ReckonerEntity reckoner, @MappingTarget Transaction transaction) {
        transaction.setIsLoan(false);
    }

    default TransactionPage mergeToTransactionPage(ReckonerLoanUnionPage union) {
        List<Transaction> transactions = new ArrayList<>(loanEntities2Transactions(union.getLoanPage()));
        transactions.addAll(reckonerEntities2Transactions(union.getReckonerPage()));
        TransactionPage page = new TransactionPage();
        page.setContent(transactions);
        page.setPageNum(union.getPageNum());
        page.setPageSize(union.getPageSize());
        return page;
    }


}
