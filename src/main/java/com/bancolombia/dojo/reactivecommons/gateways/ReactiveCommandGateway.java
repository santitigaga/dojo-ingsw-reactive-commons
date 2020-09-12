package com.bancolombia.dojo.reactivecommons.gateways;

import com.bancolombia.dojo.reactivecommons.config.Constants;
import com.bancolombia.dojo.reactivecommons.messages.QueryTasks;
import com.bancolombia.dojo.reactivecommons.messages.SaveTask;
import com.bancolombia.dojo.reactivecommons.messages.SaveWho;
import com.bancolombia.dojo.reactivecommons.messages.TaskList;
import com.bancolombia.dojo.reactivecommons.model.Task;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveCommandGateway {

    private final DirectAsyncGateway asyncGateway;
    private final Constants constants;

    public Mono<Void> emitSaveTask(Task task, String target) {
        Command<Task> command = new Command<>(SaveTask.NAME, UUID.randomUUID().toString(), task);
        return asyncGateway.sendCommand(command, target);
    }

    public Mono<Void> emitSaveWho(SaveWho saveWho, String target) {
        Command<SaveWho> command = new Command<>(SaveWho.NAME, UUID.randomUUID().toString(), saveWho);
        return asyncGateway.sendCommand(command, target);
    }


    public Mono<TaskList> getRemoteTasks(String target, String nodeName) {
        QueryTasks queryTasks = new QueryTasks(nodeName, constants.getNodeName());
        AsyncQuery<QueryTasks> asyncQuery = new AsyncQuery<>(QueryTasks.NAME, queryTasks);
        return asyncGateway.requestReply(asyncQuery, target, TaskList.class);
    }


}
