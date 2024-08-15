package fr.HttpCode202.demo;

/*import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.configuration.Configuration;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Delay;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.regex.Pattern;

import static org.mockserver.model.HttpRequest.request;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class WebClientTest {
    private final Logger logger = LoggerFactory.getLogger(WebClientTest.class);

    //@Autowired
    private WebClient.Builder webClientBuilder;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:1234")
            //.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .filter((request, next) -> {
                logger.info("URL: {}", request.url());
                return next.exchange(request);
            }).build();

    private final Pattern pattern = Pattern.compile("\\r?\\n");

    private static ClientAndServer clientAndServer;

    @BeforeAll
    public static void beforeAll() {
        Configuration configuration = new Configuration();
        configuration.logLevel(Level.ERROR);
        clientAndServer = ClientAndServer.startClientAndServer(configuration, 1234);
    }

    @AfterAll
    public static void afterAll() {
        clientAndServer.stop();
    }

    @BeforeEach
    public void beforeEach() {
        clientAndServer.reset();
    }

    @Test
    void webClient_get_synchronous(CapturedOutput output) {
        String body = "body get-web-client";

        clientAndServer.when(request())
                .respond(HttpResponse.response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withDelay(Delay.milliseconds(1000))
                        .withBody(body));

        logger.info("Before Get");

        String response = webClient.get()
                .uri("get-web-client")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        logger.info("After Get");

        String[] logs = pattern.split(output.getAll());

        Assertions.assertEquals(body, response);
        Assertions.assertEquals(3, logs.length);
        Assertions.assertTrue(logs[0].contains("Before Get"));
        Assertions.assertTrue(logs[1].contains("URL: http://localhost:1234/get-web-client"));
        Assertions.assertTrue(logs[2].contains("After Get"));
    }

    @Test
    void webClient_get_asynchronous(CapturedOutput output) {
        String body = "body get-web-client";

        clientAndServer.when(request().withPath("/get-web-client"))
                .respond(HttpResponse.response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(body));

        logger.info("Before Get");

        webClient.get()
                .uri("http://localhost:1234/get-web-client")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(15000))
                //.onErrorResume(throwable -> Mono.just("Timeout"))
                .subscribe(consumer -> {
                    String[] logs = pattern.split(output.getAll());

                    Assertions.assertEquals(body, consumer);
                    Assertions.assertEquals(3, logs.length);
                    Assertions.assertTrue(logs[0].contains("Before Get"));
                    Assertions.assertTrue(logs[1].contains("After Get"));
                    Assertions.assertTrue(logs[2].contains("URL: http://localhost:1234/get-web-client"));
                });

        logger.info("After Get");
    }

    @Test
    void webClient_get_asynchronous2(CapturedOutput output) {
        String body = "body get-web-client";

    }
}
*/