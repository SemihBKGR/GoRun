package com.semihbkgr.gorun.server.socket;

import com.semihbkgr.gorun.server.message.MessageMarshaller;
import com.semihbkgr.gorun.server.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class RunWebSocketHandler implements WebSocketHandler {

    private final MessageMarshaller messageMarshaller;
    private final MessageProcessingService messageExecutor;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final RunWebSocketSession runWebSocketSession = new RunWebSocketSession();
        final RunWebSocketContext runWebSocketContext = new RunWebSocketContext(runWebSocketSession, messageExecutor);
        return session
                .receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(messageMarshaller::unmarshall)
                .flatMap(runWebSocketContext::processMessage)
                .map(messageMarshaller::marshall)
                .map(session::textMessage)
                .flatMap(i -> session.send(Mono.just(i)))
                .then();
    }

}
