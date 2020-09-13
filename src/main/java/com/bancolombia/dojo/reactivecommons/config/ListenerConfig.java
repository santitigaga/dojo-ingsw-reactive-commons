package com.bancolombia.dojo.reactivecommons.config;

import com.bancolombia.dojo.reactivecommons.gateways.ReactiveCommandGateway;
import com.bancolombia.dojo.reactivecommons.gateways.ReactiveEventGateway;
import com.bancolombia.dojo.reactivecommons.messages.*;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.annotations.EnableMessageListeners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@EnableMessageListeners
@RequiredArgsConstructor
public class ListenerConfig {

    private final TasksRepository tasksRepository;
    private final ReactiveCommandGateway commandGateway;
    private final ReactiveEventGateway eventGateway;
    private final Constants constants;

    @Bean
    public HandlerRegistry listeners() {
        return HandlerRegistry.register()
                .listenEvent(Whois.NAME, this::processWhoIsEvents, Whois.class)
                .handleCommand(SaveWho.NAME, this::processSaveWhoCommands, SaveWho.class)
                .handleCommand(SaveTask.NAME, this::processSaveTaskCommands, SaveTask.class)
                .serveQuery(QueryTasks.NAME, this::processQueryTask, QueryTasks.class);
    }

    private Mono<Void> processWhoIsEvents(DomainEvent<Whois> message) {
        System.out.println("listen WhoIs events " + message.getData());
        if (message.getData().getWho().equals(constants.getNodeName())) {
            SaveWho saveWho = new SaveWho(constants.getNodeName(), constants.getAppName());
            return commandGateway.emitSaveWho(saveWho, message.getData().getReplyTo());
        }
        return Mono.empty();
    }


    private Mono<Void> processSaveWhoCommands(Command<SaveWho> message) {
        System.out.println("listen SaveWho Commands " + message.getData());
        eventGateway.routeReply(message.getData().getWho(), message.getData());
        return Mono.empty();
    }

    private Mono<Void> processSaveTaskCommands(Command<SaveTask> message) {
        System.out.println("listen SaveTask Commands " + message.getData());
        tasksRepository.save(message.getData());
        return Mono.empty();
    }

    private Mono<TaskList> processQueryTask(QueryTasks message) {
        System.out.println("listen QueryTasks " + message);
        return tasksRepository.get();
    }

}
