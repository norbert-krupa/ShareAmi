package com.shareami;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShareAmiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareAmiApplication.class, args);
	}

}
