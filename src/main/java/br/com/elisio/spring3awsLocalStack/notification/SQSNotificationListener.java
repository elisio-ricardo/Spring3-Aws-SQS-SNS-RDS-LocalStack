package br.com.elisio.spring3awsLocalStack.notification;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SQSNotificationListener {

    //Não precisa de controller ele vai ficar escutando a fila
    @SqsListener(value = "${spring.cloud.aws.events.queue}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void queueListener(Message<String> message) {
        try {
            log.info("message consumed {}", message.getPayload());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }




}
