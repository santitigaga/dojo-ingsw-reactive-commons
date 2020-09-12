package com.bancolombia.dojo.reactivecommons.gateways;

import com.bancolombia.dojo.reactivecommons.messages.SaveWho;
import com.bancolombia.dojo.reactivecommons.messages.Whois;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.concurrent.Queues;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
@EnableDomainEventBus
public class ReactiveEventGateway {
    private final ConcurrentHashMap<String, UnicastProcessor<SaveWho>> processors = new ConcurrentHashMap<>();
    private final DomainEventBus eventBus;

    public Mono<Void> emitWhoIs(Whois whoIs) {
        return Mono.from(eventBus.emit(new DomainEvent<>(Whois.NAME, UUID.randomUUID().toString(), whoIs)));
    }

    public Mono<SaveWho> register(String correlationID) {
        final UnicastProcessor<SaveWho> processor = UnicastProcessor.create(Queues.<SaveWho>one().get());
        processors.put(correlationID, processor);
        return processor.singleOrEmpty();
    }

    public void routeReply(String correlationID, SaveWho data) {
        final UnicastProcessor<SaveWho> processor = processors.remove(correlationID);
        if (processor != null) {
            processor.onNext(data);
            processor.onComplete();
        }
    }
}
