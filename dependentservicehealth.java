import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DependencyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        String dependencyUrl = "http://dependent-service/actuator/health"; // Replace with actual service URL
        try {
            RestTemplate restTemplate = new RestTemplate();
            String status = restTemplate.getForObject(dependencyUrl, String.class);
            if (status.contains("UP")) {
                return Health.up().build();
            } else {
                return Health.down().withDetail("Dependency Status", status).build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
