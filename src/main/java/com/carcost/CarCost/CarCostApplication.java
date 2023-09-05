package com.carcost.CarCost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CarCostApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarCostApplication.class, args);
	}

}
