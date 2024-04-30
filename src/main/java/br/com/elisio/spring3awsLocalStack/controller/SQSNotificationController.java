package br.com.elisio.spring3awsLocalStack.controller;

import br.com.elisio.spring3awsLocalStack.config.EventsConfig;
import br.com.elisio.spring3awsLocalStack.notification.SQSMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class SQSNotificationController {

    //Classe para testar e enviar mensagem diretamente para a fila sem ativar o topico SNS

    private EventsConfig eventsConfig;

    private final SQSMessageService sqsMessageServic;

    @PostMapping("/queue")
    public void queue(@RequestBody NotificationRequest request){
        log.info("Sending message toward SQS");
        sqsMessageServic.sendMessageQueue(request.toDomain());
        log.info("message sent");
    }
}