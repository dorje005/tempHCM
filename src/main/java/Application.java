import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by odorjee on 2/18/2016.
 */

@SpringBootApplication
@ComponentScan("controller") //  MUST USE SINCE CONTROLLER IS IN DIFFERENT PACKAGE.
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
