package demo.usul.mq;

import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MqConfig {

    public static final String ACCOUNT_UPDATE_DISTRIBUTED_TRANS_EXCHANGE = "account-update-distributed-trans";

    public static final String ACCOUNT_UPDATE_DISTRIBUTED_TRANS_PENDING_QUEUE = "account-update-distributed-trans-pending";
    public static final String ACCOUNT_UPDATE_DISTRIBUTED_TRANS_ROLLBACK_QUEUE = "account-update-distributed-trans-rollback";

    public static final String ACCOUNT_UPDATE_DISTRIBUTED_TRANS_PENDING_ROUTING_KEY = "account-update-distributed-trans-pending-routing-key";
    public static final String ACCOUNT_UPDATE_DISTRIBUTED_TRANS_ROLLBACK_ROUTING_KEY = "account-update-distributed-trans-rollback-routing-key";

//    @Bean
//    protected RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (ack) {
//                log.info("Message confirmed: " + correlationData);
//            } else {
//                log.info("Message not confirmed: " + cause);
//            }
//        });
//        return rabbitTemplate;
//    }

    @Bean
    protected Queue pendingQueue() {
        return new Queue(ACCOUNT_UPDATE_DISTRIBUTED_TRANS_PENDING_QUEUE, false);
    }

    @Bean
    protected Queue rollbackQueue() {
        return new Queue(ACCOUNT_UPDATE_DISTRIBUTED_TRANS_ROLLBACK_QUEUE);
    }

    @Bean
    protected DirectExchange exchange() {
        return new DirectExchange(ACCOUNT_UPDATE_DISTRIBUTED_TRANS_EXCHANGE);
    }

    @Bean
    protected Binding pendingBinding(@Qualifier("pendingQueue") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ACCOUNT_UPDATE_DISTRIBUTED_TRANS_PENDING_ROUTING_KEY);
    }

    @Bean
    protected Binding rollbackBinding(@Qualifier("rollbackQueue") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ACCOUNT_UPDATE_DISTRIBUTED_TRANS_ROLLBACK_ROUTING_KEY);
    }
}
