package emil.dobrev.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserInteractionApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserInteractionApplication.class, args);
    }
}