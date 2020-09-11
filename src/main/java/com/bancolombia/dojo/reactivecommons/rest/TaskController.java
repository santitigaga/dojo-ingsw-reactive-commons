package com.bancolombia.dojo.reactivecommons.rest;

import com.bancolombia.dojo.reactivecommons.model.Task;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
public class TaskController {

    @GetMapping(path = "/tasks/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Task>> listTasks(@PathVariable("name") String name) {
        return Mono.just(Collections.emptyList());
    }


    @PutMapping(path = "/tasks/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> saveTask(@PathVariable("name") String name) {
        return Mono.empty();
    }

}
