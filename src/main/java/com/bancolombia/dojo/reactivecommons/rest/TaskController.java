package com.bancolombia.dojo.reactivecommons.rest;

import com.bancolombia.dojo.reactivecommons.config.Constants;
import com.bancolombia.dojo.reactivecommons.config.TasksRepository;
import com.bancolombia.dojo.reactivecommons.gateways.ReactiveCommandGateway;
import com.bancolombia.dojo.reactivecommons.gateways.ReactiveEventGateway;
import com.bancolombia.dojo.reactivecommons.messages.SaveWho;
import com.bancolombia.dojo.reactivecommons.messages.Whois;
import com.bancolombia.dojo.reactivecommons.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final ConcurrentHashMap<String, String> routingTable = new ConcurrentHashMap<>();
    private final ReactiveEventGateway eventGateway;
    private final ReactiveCommandGateway commandGateway;
    private final TasksRepository tasksRepository;
    private final Constants constants;

    @GetMapping(path = "/tasks/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Task> listTasks(@PathVariable("name") String name) {
        if (constants.getNodeName().equals(name)) {
            return tasksRepository.get();
        } else {
            if (!routingTable.contains(name)) {
                Whois whois = new Whois(name, constants.getAppName());
                return eventGateway.emitWhoIs(whois)
                        .then(eventGateway.register(name)
                                .flatMap(this::saveRoute)
                                .flatMap(saveWho -> commandGateway.getRemoteTasks(saveWho.getAppName(), constants.getNodeName())))
                        .flatMapMany(taskList -> Flux.fromIterable(taskList.getTasks()));
            } else {
                return commandGateway
                        .getRemoteTasks(routingTable.get(name), constants.getNodeName())
                        .flatMapMany(taskList -> Flux.fromIterable(taskList.getTasks()));
            }
        }
    }


    @PutMapping(path = "/tasks/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> saveTask(@PathVariable("name") String name, @RequestBody Task entTask) {
        if (constants.getNodeName().equals(name)) {
            return tasksRepository.save(entTask);
        } else {
            if (!routingTable.contains(name)) {
                Whois whois = new Whois(name, constants.getAppName());
                return eventGateway.emitWhoIs(whois)
                        .then(eventGateway.register(name)
                                .flatMap(this::saveRoute)
                                .flatMap(saveWho -> commandGateway.emitSaveTask(entTask, saveWho.getAppName()))
                        );
            } else {
                return commandGateway.emitSaveTask(entTask, routingTable.get(name));
            }
        }
    }


    private Mono<SaveWho> saveRoute(SaveWho saveWho) {
        return Mono.defer(() -> {
            routingTable.put(saveWho.getWho(), saveWho.getAppName());
            return Mono.just(saveWho);
        });
    }
}
