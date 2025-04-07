package by.bsuir.foodordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FoodOrderingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodOrderingServiceApplication.class, args);
    }
}

