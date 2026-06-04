package br.com.fiap.javaadv.VeloSpace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VeloSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeloSpaceApplication.class, args);
	}

}
