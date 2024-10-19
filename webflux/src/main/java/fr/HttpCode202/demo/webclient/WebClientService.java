package fr.HttpCode202.demo.webclient;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {
    WebClient webClient = WebClient.create("http://localhost:8081");

    public void callServer() {
        Mono<Integer> mono = webClient.get()
                .uri("/api/v1/response-server/number")
                .retrieve()
                .bodyToMono(Integer.class);
        System.out.println("Start");
        System.out.println(mono.block());
        System.out.println("End");
    }

    public void callServerAsynchronous() {
        Mono<Integer> mono = webClient.get()
                .uri("/api/v1/response-server/number")
                .retrieve()
                .bodyToMono(Integer.class);
        System.out.println("Start");
        mono.subscribe(System.out::println);
        System.out.println("End");
    }
}
