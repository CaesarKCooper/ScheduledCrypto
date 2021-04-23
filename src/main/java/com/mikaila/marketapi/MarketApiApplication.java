package com.mikaila.marketapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class MarketApiApplication {

	private static final Logger log = LoggerFactory.getLogger(MarketApiApplication.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public static void main(String[] args) {

		SpringApplication.run(MarketApiApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		return builder.build();
	}

	@Scheduled(fixedRate = 5000)
	public void everyFive(){

		RestTemplate restTemplate = new RestTemplate();
		Crypto[] crypto = restTemplate.getForObject("https://api.n.exchange/en/api/v1/price/BTCLTC/latest/", Crypto[].class);
		log.info(crypto[0].toString());
	}

	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			everyFive();
			reportCurrentTime();
		};

	}

}
