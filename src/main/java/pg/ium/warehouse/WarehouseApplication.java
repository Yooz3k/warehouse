package pg.ium.warehouse;

import lombok.extern.jbosslog.JBossLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pg.ium.warehouse.domain.Tyre;
import pg.ium.warehouse.repository.TyreRepository;

import java.math.BigDecimal;

@JBossLog
@SpringBootApplication
public class WarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(TyreRepository repository) {
        return (args) -> {
            repository.save(Tyre.builder()
                    .producer("Goodyear")
                    .name("Ultra Grp 9+")
                    .rimSize(16)
                    .price(new BigDecimal(320.00))
                    .quantity(8)
                    .build());
            repository.save(Tyre.builder()
					.producer("Bridgestone")
					.name("Blizzak")
					.rimSize(17)
					.price(new BigDecimal(514.30))
					.quantity(5)
                    .build());
        };
    }

}
