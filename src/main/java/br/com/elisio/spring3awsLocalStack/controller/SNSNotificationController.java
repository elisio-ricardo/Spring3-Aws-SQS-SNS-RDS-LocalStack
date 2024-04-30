package br.com.elisio.spring3awsLocalStack.controller;


import br.com.elisio.spring3awsLocalStack.config.EventsConfig;
import br.com.elisio.spring3awsLocalStack.notification.SNSNotificationService;
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
public class SNSNotificationController {

    //Classe para enviar mensagem para o topico SNS

    private EventsConfig eventsConfig;
    private final SNSNotificationService service;

    @PostMapping("/topic")
    public void topic(@RequestBody NotificationRequest request){
        System.out.println("Peguei a chave " + eventsConfig.getQueueName());
        log.info("Sending message toward SNS");
        service.notifyTopic(request.toDomain());
    }

}
