package pro.techdict.bib.bibserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class BibserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibserverApplication.class, args);
    }

}
