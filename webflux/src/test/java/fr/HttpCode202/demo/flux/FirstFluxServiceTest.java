package fr.HttpCode202.demo.flux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class FirstFluxServiceTest {

    @Autowired
    private FirstFluxService firstFluxService;

    @Test
    void flux() {
        Flux<String> flux = firstFluxService.flux();

        StepVerifier.Step<String> step = StepVerifier.create(flux)
                .expectNext("One")
                .expectNext("Two");

        step.expectNextMatches("Three"::equals)
                .expectNextMatches("Four"::equals);

        step.expectNext("Five", "Six")
                .expectNext("Seven", "Eight");

        step.expectNextCount(1)
                .expectNext("Ten")
                .expectComplete()
                .verify();
    }

    @Test
    void fluxWithFilter() {
        Flux<String> flux = firstFluxService.fluxWithFilter();

        StepVerifier.create(flux)
                .expectNext("Three", "Four", "Five", "Seven", "Eight", "Nine")
                .expectComplete()
                .verify();
    }

    @Test
    void fluxWithFilterAndMap() {
        Flux<String> flux = firstFluxService.fluxWithFilterAndMap();

        StepVerifier.create(flux)
                .expectNext("THREE", "SEVEN", "EIGHT", "NINE")
                .expectComplete()
                .verify();
    }

    @Test
    void fluxWithError() {
        Flux<String> flux = firstFluxService.fluxWithError();

        StepVerifier.create(flux)
                .expectNext("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
                .expectErrorMatches(predicat -> predicat instanceof IllegalArgumentException
                        && "my error".equals(predicat.getMessage()))
                .verify();
    }

    @Test
    void fluxWithSleep() {
        Flux<String> flux = firstFluxService.fluxWithSleep();

        StepVerifier.create(flux)
                .expectNext("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
                .expectComplete()
                .verifyThenAssertThat()
                .tookMoreThan(Duration.ofMillis(50))
                .tookLessThan(Duration.ofMillis(150));
    }

    @Test
    void fluxWithZip() {
        Flux<String> flux = firstFluxService.fluxWithZip();

        StepVerifier.create(flux)
                .expectNext("OneFive", "TwoSix", "ThreeSeven", "FourEight")
                .expectComplete()
                .verifyThenAssertThat()
                .hasDiscardedElements()
                .hasDiscarded("Nine", "Ten");
    }

}
