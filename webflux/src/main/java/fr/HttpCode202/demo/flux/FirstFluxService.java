package fr.HttpCode202.demo.flux;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FirstFluxService {

    public Flux<String> numberflux() {
        return Flux.just("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten");
    }

    public Flux<String> numberfluxWithError() {
        return numberflux().concatWith(Mono.error(new IllegalArgumentException("my error")));
    }

}
