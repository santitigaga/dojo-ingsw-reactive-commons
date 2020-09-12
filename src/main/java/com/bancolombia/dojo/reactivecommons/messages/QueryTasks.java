package com.bancolombia.dojo.reactivecommons.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QueryTasks {
    public static final String NAME = "tasks.getAll";
    private String personName;
    private String requester;

    public void localData(){
        //si no request reply
    }

    public void listenRemoteQuery(){

    }
}
