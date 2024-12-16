//package demo.usul.service.trams;
//
//import demo.usul.dto.AccountUpdateDto;
//import demo.usul.mq.MqConfig;
//import demo.usul.service.AccountService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.support.PeriodicTrigger;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AccountDistriTramService {
//
//    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
//    private final RabbitTemplate rabbitTemplate;
//    private final DirectExchange directExchange;
//
//    public void scheduledRollback() {
//        threadPoolTaskScheduler.schedule(
//                () -> {
//                    log.info(Thread.currentThread().getName() + "xxxxxxx");
//                    updateDistributedTransRollback();
//                },
//                new PeriodicTrigger(Duration.ofSeconds(5)));
//    }
//
//    public void updateDistributedTrans(List<AccountUpdateDto> accountUpdateDtos) {
//        rabbitTemplate.convertAndSend(
//                directExchange.getName(),
//                MqConfig.ACCOUNT_UPDATE_DISTRIBUTED_TRANS_PENDING_ROUTING_KEY,
//                new Object(),
//                m -> {
//                    log.info(m.toString());
//                    return m;
//                },
//                new CorrelationData());
//    }
//
//    public void updateDistributedTransRollback() {
//        rabbitTemplate.convertAndSend(
//                directExchange.getName(),
//                MqConfig.ACCOUNT_UPDATE_DISTRIBUTED_TRANS_PENDING_ROUTING_KEY,
//                "rollback");
//    }
//}
