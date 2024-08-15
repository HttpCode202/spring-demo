package fr.HttpCode202.demo.flux;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FirstFluxService {

    private Flux<String> numberfluxOneToFive() {
        return Flux.just("One", "Two", "Three", "Four");
    }

    private Flux<String> numberfluxSixToTen() {
        return Flux.just("Five", "Six", "Seven", "Eight", "Nine", "Ten");
    }

    public Flux<String> numberflux() {
        return numberfluxOneToFive().concatWith(numberfluxSixToTen());
    }

    public Flux<String> numberfluxWithError() {
        return numberflux().concatWith(Mono.error(new IllegalArgumentException("my error")));
    }

    public Flux<String> numberfluxWithSleep() {
        return numberfluxOneToFive().concatWith(
                Mono.create(callback -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }
                    callback.success();
                })).concatWith(numberfluxSixToTen());
    }

    public Flux<String> numberfluxWithZip() {
        return numberfluxOneToFive().zipWith(
                numberfluxSixToTen(),
                (String flux1, String flux2) -> String.format("%s%s", flux1, flux2)
        );
    }

}
