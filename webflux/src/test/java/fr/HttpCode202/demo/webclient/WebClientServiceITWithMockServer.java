package fr.HttpCode202.demo.webclient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class WebClientServiceITWithMockServer {
    private static MockServerClient mockServer;
    private final CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    private WebClientService webClientService;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(8081);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    void callServer(CapturedOutput capturedOutput) {
        ConfigurationProperties.logLevel(Level.OFF.getName());
        mockServer.when(HttpRequest.request("/api/v1/response-server/number"), Times.once())
                .respond(HttpResponse.response().withStatusCode(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 500)
                        .withBody("999999"));

        int i = capturedOutput.getAll().length();

        webClientService.callServer();

        List<String> lines = capturedOutput.getAll().substring(i).lines().toList();

        assertEquals(3, lines.size());
        assertEquals("Start", lines.getFirst());
        assertEquals("999999", lines.get(1));
        assertEquals("End", lines.getLast());
    }

    @Test
    void callServerAsynchronousWithLatchAwait(CapturedOutput capturedOutput) {
        ConfigurationProperties.logLevel(Level.OFF.getName());
        mockServer.when(HttpRequest.request("/api/v1/response-server/number"), Times.once())
                .respond(HttpResponse.response().withStatusCode(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 500)
                        .withBody("999999"));

        int i = capturedOutput.getAll().length();

        webClientService.callServerAsynchronous();

        assertDoesNotThrow(() -> assertFalse(latch.await(1, TimeUnit.SECONDS)));

        List<String> lines = capturedOutput.getAll().substring(i).lines().toList();

        assertEquals(3, lines.size());
        assertEquals("Start", lines.getFirst());
        assertEquals("End", lines.get(1));
        assertEquals("999999", lines.getLast());
    }

    @Test
    void callServerAsynchronousWithoutLatchAwait(CapturedOutput capturedOutput) {
        ConfigurationProperties.logLevel(Level.OFF.getName());
        mockServer.when(HttpRequest.request("/api/v1/response-server/number"), Times.once())
                .respond(HttpResponse.response().withStatusCode(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 500)
                        .withBody("999999"));

        int i = capturedOutput.getAll().length();

        webClientService.callServerAsynchronous();

        List<String> lines = capturedOutput.getAll().substring(i).lines().toList();

        assertEquals(2, lines.size());
        assertEquals("Start", lines.getFirst());
        assertEquals("End", lines.getLast());
    }
}
