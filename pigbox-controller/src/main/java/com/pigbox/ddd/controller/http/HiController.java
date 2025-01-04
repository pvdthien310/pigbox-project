package com.pigbox.ddd.controller.http;

import com.pigbox.ddd.application.service.event.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@RestController
@RequestMapping("/hello")
public class HiController {

    @Autowired
    private EventAppService eventAppService;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/hi")
    @RateLimiter(name="backendA", fallbackMethod = "fallbackHello")
    public String a() {
        return eventAppService.sayHi("Tian");
    }

    public String fallbackHello(Throwable throwable) {
        return "Too many requests";
    }
    public String fallbackCircuitBreaker(Throwable throwable) {
        return "https://fakestoreapi.com/products/".concat(" is down!");
    }

    @GetMapping("/hi/v1")
    @RateLimiter(name="backendB", fallbackMethod = "fallbackHello")
    public String hiV1() {
        return eventAppService.sayHi("Tian");
    }

    @GetMapping("/circuit/breaker")
    @CircuitBreaker(name="backendCircuitBreaker", fallbackMethod = "fallbackCircuitBreaker")
//    @RateLimiter(name="backendB", fallbackMethod = "fallbackHello")
    public String circuitBreaker() {
        String url = "https://fakestoreapi.com/products/";
        SecureRandom random = new SecureRandom();
        return restTemplate.getForObject(url.concat(String.valueOf(random.nextInt(20) + 1)), String.class);
    }
}
