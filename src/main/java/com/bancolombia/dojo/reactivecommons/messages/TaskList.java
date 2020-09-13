package com.bancolombia.dojo.reactivecommons.messages;

import com.bancolombia.dojo.reactivecommons.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskList {
   private List<Task> tasks;
}
