package dev.tt.poc.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//https://learn.temporal.io/tutorials/java/build-an-email-drip-campaign/
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})

@SpringBootApplication
public class SubcriptionWorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubcriptionWorkflowApplication.class, args);
	}

}
