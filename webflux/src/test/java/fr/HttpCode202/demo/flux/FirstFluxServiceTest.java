package fr.HttpCode202.demo.flux;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class FirstFluxServiceTest {

    static class Utils {
        @Autowired
        protected FirstFluxService firstFluxService;
    }

    @Nested
    class numberfluxTest extends Utils {

        @Test
        void defaultCase() {
            Flux<String> flux = firstFluxService.numberflux();

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
        void withFilter() {
            Flux<String> flux = firstFluxService.numberflux()
                    .filter(predicat -> predicat.length() > 3);

            StepVerifier.create(flux)
                    .expectNext("Three", "Four", "Five", "Seven", "Eight", "Nine")
                    .expectComplete()
                    .verify();
        }

        @Test
        void withFilterAndMap() {
            Flux<String> flux = firstFluxService.numberflux()
                    .filter(predicat -> predicat.length() > 3 && !predicat.startsWith("F"))
                    .map(String::toUpperCase);

            StepVerifier.create(flux)
                    .expectNext("THREE", "SEVEN", "EIGHT", "NINE")
                    .expectComplete()
                    .verify();
        }

    }

    @Nested
    class numberfluxWithErrorTest extends Utils {

        @Test
        void defaultCase() {
            Flux<String> flux = firstFluxService.numberfluxWithError();

            StepVerifier.create(flux)
                    .expectNext("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
                    .expectErrorMatches(predicat -> predicat instanceof IllegalArgumentException
                            && "my error".equals(predicat.getMessage()))
                    .verify();
        }

    }

    @Nested
    class numberfluxWithSleepTest extends Utils {

        @Test
        void defaultCase() {
            Flux<String> flux = firstFluxService.numberfluxWithSleep();

            StepVerifier.create(flux)
                    .expectNext("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
                    .expectComplete()
                    .verifyThenAssertThat()
                    .tookMoreThan(Duration.ofMillis(50))
                    .tookLessThan(Duration.ofMillis(150));
        }

    }

    @Nested
    class numberfluxWithZipTest extends Utils {

        @Test
        void defaultCase() {
            Flux<String> flux = firstFluxService.numberfluxWithZip();

            StepVerifier.create(flux)
                    .expectNext("OneFive", "TwoSix", "ThreeSeven", "FourEight")
                    .expectComplete()
                    .verifyThenAssertThat()
                    .hasDiscardedElements()
                    .hasDiscarded("Nine", "Ten");
        }

    }

}
