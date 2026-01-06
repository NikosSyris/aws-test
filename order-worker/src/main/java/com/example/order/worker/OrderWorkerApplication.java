package com.example.order.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.order")
public class OrderWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderWorkerApplication.class, args);
    }
}

