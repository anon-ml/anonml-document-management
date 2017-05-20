package ml.anon.docmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class AppDocumentManagement {

    public static void main(String[] args) {
        SpringApplication.run(AppDocumentManagement.class, args);

    }
}
