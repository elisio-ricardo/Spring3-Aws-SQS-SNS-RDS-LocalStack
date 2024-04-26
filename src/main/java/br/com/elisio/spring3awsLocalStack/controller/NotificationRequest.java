package br.com.elisio.spring3awsLocalStack.controller;

import br.com.elisio.spring3awsLocalStack.domain.NotificationMessage;

record NotificationRequest(String from, String to, String content) {

    public NotificationMessage toDomain() {
        return new NotificationMessage(this.from, this.to, this.content);
    }
}