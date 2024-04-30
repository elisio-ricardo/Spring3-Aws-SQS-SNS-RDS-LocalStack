package br.com.elisio.spring3awsLocalStack.notification;

import br.com.elisio.spring3awsLocalStack.config.EventsConfig;
import br.com.elisio.spring3awsLocalStack.domain.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor //Se qser testar a fila enviando uma menssagem diretamente para ela
public class SQSMessageService {

    private final EventsConfig config;

    private final NotificationMessagingTemplate notificationTemplate;
    private final QueueMessagingTemplate messagingTemplate;

    //Esse envia a mensagem para o topico SNS
    public void notifyTopic(NotificationMessage message) {
        log.info("Notifying topic {}", config.getTopicName());
        notificationTemplate.sendNotification(config.getTopicName(), message, "notification");
    }

    //Este envia a mensagem para a Queue
    public void sendMessageQueue(NotificationMessage message) {
        log.info("Notifying queue {}", config.getQueueName());
        messagingTemplate.convertAndSend(config.getQueueName(), message);
    }
}
