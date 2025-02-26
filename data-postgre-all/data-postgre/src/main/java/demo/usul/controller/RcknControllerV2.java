package demo.usul.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.ReckonerDto;
import demo.usul.dto.ReckonerTypeDto;
import demo.usul.repository.fragments.ReckonerFragRepositoryImpl;
import demo.usul.service.RcknSvcV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/rckn/v2")
public class RcknControllerV2 {

    private final ReckonerController reckonerController;
    private final RcknSvcV2 rcknSvcV2;
    private final ReckonerMapper reckonerMapper;

    @Autowired
    public RcknControllerV2(ReckonerController reckonerController, RcknSvcV2 rcknSvcV2, ReckonerMapper reckonerMapper) {
        this.reckonerController = reckonerController;
        this.rcknSvcV2 = rcknSvcV2;
        this.reckonerMapper = reckonerMapper;
    }

    @PostMapping({"", "/{trigger}"})
    public ReckonerDto createV2(@RequestBody ReckonerDto dto, @PathVariable(required = false) Optional<Boolean> trigger, @RequestParam(required = false) List<String> tags) {
//        dto.setTags("[ " + String.join(", ", tags) + " ]");
        return reckonerController.createOne(dto, trigger);
    }

    @PostMapping("/yuebao-profit/{blc}")
    public ReckonerDto createYuEBaoProfit(@PathVariable String blc, @RequestParam String transDate, @RequestParam String transTime, @RequestParam(required = false) Optional<Boolean> trigger) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.IN)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .toAcctObj(AccountDto.builder().name("余额宝").build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName("Profit").build())
                .build();
        return reckonerController.createOne(build, trigger);
    }

    @PostMapping("/hua-bei/food/{blc}")
    public ReckonerDto createHuaBei(@PathVariable String blc,
                                    @RequestParam String transDate, @RequestParam String transTime,
                                    @RequestParam(required = false) List<String> tags,
                                    @RequestParam(required = false) String descr) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.OUT)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .fromAcctObj(AccountDto.builder().name("花呗").build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName("Food").build())
                .tags(CollUtil.isEmpty(tags) ? null : tags)
                .descr(ObjectUtil.isEmpty(descr) ? null : descr)
                .build();
        return reckonerController.createOne(build, Optional.of(true));
    }

    @PostMapping("/out/{type}/{acct}")
    public ReckonerDto createOutflow4Food(@PathVariable String type,
                                          @PathVariable String acct,
                                          @RequestParam String blc,
                                          @RequestParam String transDate, @RequestParam String transTime,
                                          @RequestParam(required = false) List<String> tags,
                                          @RequestParam(required = false) String descr,
                                          @RequestParam(required = false) Optional<Boolean> trigger) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.OUT)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .fromAcctObj(AccountDto.builder().name(acct).build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName(type).build())
                .tags(CollUtil.isEmpty(tags) ? null : tags)
                .descr(ObjectUtil.isEmpty(descr) ? null : descr)
                .build();
        return reckonerController.createOne(build, trigger);
    }

    @PostMapping("/transfer/Due_Payment/{fAcct}/{tAcct}")
    public ReckonerDto createTransferDuePayment(@PathVariable String fAcct,
                                                @PathVariable String tAcct,
                                                @RequestParam String blc,
                                                @RequestParam String transDate, @RequestParam String transTime,
                                                @RequestParam(required = false) List<String> tags,
                                                @RequestParam(required = false) String descr,
                                                @RequestParam(required = false) Optional<Boolean> trigger) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.TRANSFER)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .fromAcctObj(AccountDto.builder().name(fAcct).build())
                .toAcctObj(AccountDto.builder().name(tAcct).build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName("Due Payment").build())
                .tags(CollUtil.isEmpty(tags) ? null : tags)
                .descr(ObjectUtil.isEmpty(descr) ? null : descr)
                .build();
        return reckonerController.createOne(build, trigger);
    }


    @PostMapping("/refund/yu_e_bao/{blc}")
    public ReckonerDto createRefund(@PathVariable String blc,
                                    @RequestParam String transDate, @RequestParam String transTime,
                                    @RequestParam(required = false) List<String> tags,
                                    @RequestParam(required = false) String descr) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.IN)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .toAcctObj(AccountDto.builder().name("余额宝").build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName("Refund").build())
                .tags(CollUtil.isEmpty(tags) ? null : tags)
                .descr(ObjectUtil.isEmpty(descr) ? null : descr)
                .build();
        return reckonerController.createOne(build, Optional.of(true));
    }

    @GetMapping("/info/{reckonerId}")
    public Map<String, Object> retrieveOneInfo(@PathVariable UUID reckonerId) {
        return rcknSvcV2.retrieveOneInfo(reckonerId);
    }

    @PutMapping("/one/f_acct/{id}")
    public ReckonerDto updateWithMongoDoc(@PathVariable UUID id, @RequestParam String name) {
        return rcknSvcV2.updateFAcct(id, name);
    }

    @GetMapping("/stats/to/{name}")
    public Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsToAcc(@PathVariable String name) {
        return rcknSvcV2.statsToAcc(name);
    }

    @GetMapping("/stats/from/{fAcc}")
    public Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsFromAcc(@PathVariable String fAcc) {
        return rcknSvcV2.statsFromAcc(fAcc);
    }

    @PostMapping("/transfer/{fAcct}/{tAcct}")
    public ReckonerDto createTransfer(@PathVariable String fAcct,
                                      @PathVariable String tAcct,
                                      @RequestParam String blc,
                                      @RequestParam String transDate, @RequestParam String transTime,
                                      @RequestParam(required = false) List<String> tags,
                                      @RequestParam(required = false) String descr,
                                      @RequestParam(required = false) Optional<Boolean> trigger) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.TRANSFER)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .fromAcctObj(AccountDto.builder().name(fAcct).build())
                .toAcctObj(AccountDto.builder().name(tAcct).build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName("Transfer").build())
                .tags(CollUtil.isEmpty(tags) ? null : tags)
                .descr(ObjectUtil.isEmpty(descr) ? null : descr)
                .build();
        return reckonerController.createOne(build, trigger);
    }

    @GetMapping("/cond")
    public Page<ReckonerDto> retrieveCond(@RequestParam(required = false) Optional<String> acc,
                                          @RequestParam(required = false) Optional<String> rcknType,
                                          @RequestParam(required = false) Optional<Integer> inOut,
                                          @RequestParam(required = false) Optional<Boolean> isOrderByTransDate,
                                          @RequestParam(required = false) Optional<Boolean> isOrderByAmount,
                                          @RequestParam(defaultValue = "0") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return rcknSvcV2.retrieveCondPageable(acc, rcknType, inOut, isOrderByTransDate, isOrderByAmount, PageRequest.of(pageNum, pageSize));
    }

    @GetMapping("/stat/acc")
    public Collection<ReckonerFragRepositoryImpl.AccStat> statsGroupByAcc(@RequestParam(required = false) Optional<Boolean> isMonthly,
                                                                          @RequestParam(required = false) Optional<Boolean> isWeekly,
                                                                          @RequestParam(required = false) Optional<Boolean> isGroupByType,
                                                                          @RequestParam(required = false) Optional<String> acc,
                                                                          @RequestParam(required = false) Optional<OffsetDateTime> timeBegin,
                                                                          @RequestParam(required = false) Optional<OffsetDateTime> timeEnd) {
        return rcknSvcV2.statsGroupByAcc(isMonthly, isWeekly, isGroupByType, acc, timeBegin, timeEnd);
    }

    @PostMapping("/in/{tAcct}")
    public ReckonerDto createInflow(@PathVariable String tAcct,
                                    @RequestParam String typeName,
                                    @RequestParam String blc,
                                    @RequestParam String transDate,
                                    @RequestParam String transTime,
                                    @RequestParam(required = false) List<String> tags,
                                    @RequestParam(required = false) String descr,
                                    @RequestParam(required = false) Optional<Boolean> trigger) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDate.parse(transDate), LocalTime.parse(transTime), ZoneOffset.of("+8"));
        ReckonerDto build = ReckonerDto.builder()
                .inOut(ReckonerDto.InOutEnum.IN)
                .amount(new BigDecimal(blc))
                .transDate(offsetDateTime)
                .toAcctObj(AccountDto.builder().name(tAcct).build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName(typeName).build())
                .tags(CollUtil.isEmpty(tags) ? null : tags)
                .descr(ObjectUtil.isEmpty(descr) ? null : descr)
                .build();
        return reckonerController.createOne(build, trigger);
    }
}
