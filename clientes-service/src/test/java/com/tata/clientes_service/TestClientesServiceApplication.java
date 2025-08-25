package com.tata.clientes_service;

import org.springframework.boot.SpringApplication;

public class TestClientesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ClientesServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
