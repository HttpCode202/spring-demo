package fr.HttpCode202.demo.webclient;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Disabled(value = "Before running these tests, start the webflux-response-error project")
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class WebClientServiceIT {
    @Autowired
    private WebClientService webClientService;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Test
    void callServer(CapturedOutput capturedOutput) {
        int i = capturedOutput.getAll().length();

        webClientService.callServer();

        List<String> lines = capturedOutput.getAll().substring(i).lines().toList();

        assertEquals(3, lines.size());
        assertEquals("Start", lines.getFirst());
        assertEquals("123456789", lines.get(1));
        assertEquals("End", lines.getLast());
    }

    @Test
    void callServerAsynchronousWithLatchAwait(CapturedOutput capturedOutput) {
        int i = capturedOutput.getAll().length();

        webClientService.callServerAsynchronous();

        assertDoesNotThrow(() -> assertFalse(latch.await(1, TimeUnit.SECONDS)));

        List<String> lines = capturedOutput.getAll().substring(i).lines().toList();

        assertEquals(3, lines.size());
        assertEquals("Start", lines.getFirst());
        assertEquals("End", lines.get(1));
        assertEquals("123456789", lines.getLast());
    }

    @Test
    void callServerAsynchronousWithoutLatchAwait(CapturedOutput capturedOutput) {
        int i = capturedOutput.getAll().length();

        webClientService.callServerAsynchronous();

        List<String> lines = capturedOutput.getAll().substring(i).lines().toList();

        assertEquals(2, lines.size());
        assertEquals("Start", lines.getFirst());
        assertEquals("End", lines.getLast());
    }
}
