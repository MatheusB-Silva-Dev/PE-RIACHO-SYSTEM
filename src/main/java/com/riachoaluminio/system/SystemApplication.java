package com.riachoaluminio.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Classe principal da aplicação
// @SpringBootApplication combina:
// - @SpringBootConfiguration: permite registrar beans adicionais
// - @EnableAutoConfiguration: ativa a configuração automática do Spring Boot
// - @ComponentScan: escaneia automaticamente todos os pacotes e classes anotadas
// Esta classe inicia a aplicação e monta o fluxo completo: Controller -> Service -> Repository

/*
Observação importante:
@SpringBootApplication permite também:
Excluir autoconfigurações específicas com "exclude"
Ex: @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
Importar classes de configuração externas com @Import
 */


@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}
