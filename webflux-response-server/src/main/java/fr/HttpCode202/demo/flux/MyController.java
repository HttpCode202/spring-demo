package fr.HttpCode202.demo.flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/response-server")
public class MyController {

    @GetMapping("/number")
    public int number() {
        return 123456789;
    }

    @GetMapping
    public int number2() {
        return 19;
    }

}
