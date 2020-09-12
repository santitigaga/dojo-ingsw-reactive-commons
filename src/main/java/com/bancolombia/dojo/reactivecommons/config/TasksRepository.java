package com.bancolombia.dojo.reactivecommons.config;

import com.bancolombia.dojo.reactivecommons.model.Task;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@NoArgsConstructor
public class TasksRepository  {
        private static final List<Task> tasks = new CopyOnWriteArrayList<>();
        public Mono<Void> save(Task task) {
            tasks.add(task);
            return Mono.empty();
        }

        public Flux<Task> get() {
            return Flux.fromIterable(tasks);
        }
}
