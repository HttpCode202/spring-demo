package fr.HttpCode202.demo.flux;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FluxService {

    private Flux<String> fluxOneToFive() {
        return Flux.just("One", "Two", "Three", "Four");
    }

    private Flux<String> fluxSixToTen() {
        return Flux.just("Five", "Six", "Seven", "Eight", "Nine", "Ten");
    }

    public Flux<String> flux() {
        return fluxOneToFive().concatWith(fluxSixToTen());
    }

    public Flux<String> fluxWithFilter() {
        return flux().filter(predicat -> predicat.length() > 3);
    }

    public Flux<String> fluxWithFilterAndMap() {
        return flux().filter(predicat -> predicat.length() > 3 && !predicat.startsWith("F"))
                .map(String::toUpperCase);
    }


    public Flux<String> fluxWithError() {
        return flux().concatWith(Mono.error(new IllegalArgumentException("my error")));
    }

    public Flux<String> fluxWithSleep() {
        return fluxOneToFive().concatWith(
                Mono.create(callback -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }
                    callback.success();
                })).concatWith(fluxSixToTen());
    }

    public Flux<String> fluxWithZip() {
        return fluxOneToFive().zipWith(
                fluxSixToTen(),
                (String flux1, String flux2) -> String.format("%s%s", flux1, flux2)
        );
    }

}
