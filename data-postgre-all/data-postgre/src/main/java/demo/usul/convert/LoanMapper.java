package demo.usul.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.LoanDto;
import demo.usul.dto.LoanScheduleDto;
import demo.usul.dto.Transaction;
import demo.usul.dto.TransactionPage;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.LoanScheduleEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerLoanUnionPage;
import lombok.NoArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static demo.usul.dto.LoanDto.LoanType.INSTALLMENT;
import static lombok.AccessLevel.PRIVATE;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true))
public interface LoanMapper {

    @Mapping(target = "fromAcctEntity.id", source = "fromAcct")
    LoanEntity loanCreate2LoanEntity(LoanCreateDto loanCreateDto);

    @Mapping(target = "fromAcctDto", source = "fromAcctEntity")
    @Mapping(target = "loanScheduleDtoSet", source = "loanScheduleEntitySet")
    LoanDto loanEntity2Dto(LoanEntity loanEntity);

    List<LoanDto> loanEntities2Dtos(List<LoanEntity> list);

    LoanScheduleDto loanScheduleEntity2Dto(LoanScheduleEntity entity);

    Collection<LoanScheduleDto> loanScheduleEntities2Dtos(Collection<LoanScheduleEntity> entities);

    @Mappings(value = {
            @Mapping(source = "principal", target = "amount"),
            @Mapping(source = "fromAcctEntity.id", target = "fromAcctId"),
            @Mapping(source = "fromAcctEntity.name", target = "fromAcct"),
            @Mapping(source = "fromAcctEntity.cardTypeEntity.typeName", target = "fromAcctType"),
            @Mapping(target = "principals", expression = "java(Loan2TransactionHelper.mapPrincipals(loanEntity))"),
            @Mapping(target = "interests", expression = "java(Loan2TransactionHelper.mapInterests(loanEntity))"),
            @Mapping(target = "dueDates", expression = "java(Loan2TransactionHelper.mapDueDates(loanEntity))"),
            @Mapping(target = "installmentNum", expression = "java(Loan2TransactionHelper.mapInstallmentNum(loanEntity))"),

    })
    Transaction loanEntity2Transaction(LoanEntity loanEntity);

    List<Transaction> loanEntities2Transactions(List<LoanEntity> loanEntities);

    @AfterMapping
    default void setIsLoanFlag(LoanEntity loanEntity, @MappingTarget Transaction transaction) {
        transaction.setIsLoan(true);
    }

    @Mappings(value = {
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

    // reckoner + loan 转 transaction
    default TransactionPage mergeToTransactionPage(ReckonerLoanUnionPage unionPage) {
        List<Transaction> transactions = new ArrayList<>(loanEntities2Transactions(unionPage.getLoanPage()));
        transactions.addAll(reckonerEntities2Transactions(unionPage.getReckonerPage()));
        TransactionPage page = new TransactionPage();
        page.setPage(transactions.stream().sorted(Comparator.comparing(Transaction::getTransDate).reversed()).toList());
        page.setPageNum(unionPage.getPageNum());
        page.setPageSize(unionPage.getPageSize());
        page.setTotal(unionPage.getTotal());

        return page;
    }

    @Mappings(value = {
            @Mapping(source = "amount", target = "principal"),
            @Mapping(source = "fromAcctObj.id", target = "fromAcct")
    })
    LoanCreateDto reckonerEntity2LoanCreateDto(ReckonerEntity reckoner);

    @NoArgsConstructor(access = PRIVATE)
    class Loan2TransactionHelper {

        static List<BigDecimal> mapPrincipals(LoanEntity loan) {
            return !CharSequenceUtil.equals(loan.getLoanType(), INSTALLMENT.getType()) ?
                    null :
                    loan.getLoanScheduleEntitySet().stream()
                            .map(LoanScheduleEntity::getPrincipal)
                            .toList();
        }

        static List<BigDecimal> mapInterests(LoanEntity loan) {
            return !CharSequenceUtil.equals(loan.getLoanType(), INSTALLMENT.getType()) ?
                    null :
                    loan.getLoanScheduleEntitySet().stream()
                            .map(LoanScheduleEntity::getInterest)
                            .toList();
        }

        static List<LocalDate> mapDueDates(LoanEntity loan) {
            return CollUtil.isEmpty(loan.getLoanScheduleEntitySet()) ?
                    null :
                    loan.getLoanScheduleEntitySet().stream()
                            .map(LoanScheduleEntity::getDueDate)
                            .toList();
        }

        static Integer mapInstallmentNum(LoanEntity loan) {
            return CharSequenceUtil.equals(loan.getLoanType(), INSTALLMENT.getType()) ?
                    loan.getLoanScheduleEntitySet().size() :
                    null;
        }
    }


}
