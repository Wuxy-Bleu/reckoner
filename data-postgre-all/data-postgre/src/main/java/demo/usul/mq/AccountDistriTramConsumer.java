package demo.usul.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountDistriTramConsumer {

    private final RabbitTemplate rabbitTemplate;

    //    @RabbitListener(queues = MqConstant.ACCOUNT_UPDATE_DISTRIBUTED_TRANS_DONE_QUEUE)
    public void receiveDoneMsg(Object object) {
        rabbitTemplate.receiveAndConvert();
    }


}
