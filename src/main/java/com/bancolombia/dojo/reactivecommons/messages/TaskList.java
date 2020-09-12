package com.bancolombia.dojo.reactivecommons.messages;

import com.bancolombia.dojo.reactivecommons.model.Task;
import lombok.Data;

import java.util.List;

@Data
public class TaskList {
   private List<Task> tasks;
}
