package com.bancolombia.dojo.reactivecommons.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Whois {
    public static final String NAME = "system.whois";
    private String who;
    private String replyTo;

}
