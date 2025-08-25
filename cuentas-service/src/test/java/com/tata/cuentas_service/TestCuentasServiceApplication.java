package com.tata.cuentas_service;

import org.springframework.boot.SpringApplication;

public class TestCuentasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CuentasServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
