package springboot.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the startpoint for the springboot REST server.
 */
@SpringBootApplication
public class LoftApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoftApplication.class, args);
    }
}
