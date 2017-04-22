package io.delimeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
@PropertySource({"classpath:delimeat.properties"})
@EntityScan(basePackages = {"io.delimeat"},
        basePackageClasses = {Jsr310JpaConverters.class}
)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
