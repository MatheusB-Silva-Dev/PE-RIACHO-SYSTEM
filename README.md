# Projeto de Extensão – Sistema de Orçamento - Riacho Alumínio

Olá! Meu nome é Antonio Matheus e este projeto faz parte do meu trabalho de extensão no curso de **Tecnologia em Análise e Desenvolvimento de Sistemas**. Nele, desenvolvi um **sistema de orçamento** que facilita o cadastro de clientes, a criação de orçamentos e a geração de PDFs para impressão — tudo para ajudar uma organização parceira a otimizar seus processos.

---

## 📌 Sobre o Projeto

Este projeto nasceu da vontade de aplicar na prática os conhecimentos adquiridos durante o curso, trazendo uma solução que une teoria e prática. O sistema foi construído com **Spring Boot**, **Hibernate** e **H2 (em modo persistente)**, com foco em oferecer uma experiência simples e funcional para usuários que precisam gerenciar orçamentos de forma prática.

### 🎯 Objetivos principais:

- Automatizar e simplificar o cadastro de clientes e orçamentos.
- Garantir a persistência dos dados, mesmo após reiniciar a aplicação.
- Oferecer um ambiente de visualização e administração do banco via console H2.
- Permitir a criação e o gerenciamento de orçamentos via navegadores web.
- Gerar arquivos PDF para impressão e compartilhamento.

---

## ✅ Funcionalidades

- **Cadastro de Clientes e Orçamentos**  
  Interface simples e objetiva para adicionar, editar e visualizar dados.

- **Geração de PDF**  
  Exportação de orçamentos em formato PDF com apenas um clique.

- **Persistência com H2**  
  Banco de dados configurado para persistência em arquivo local.

- **Console H2 Web**  
  Acesso via navegador ao console do H2 para consultas e testes.

---

## 🛠️ Tecnologias e Ferramentas

- **Linguagem:** Java 17
- **Framework:** Spring Boot
- **Banco de Dados:** H2 (persistente)
- **ORM:** Hibernate (JPA)
- **Build Tool:** Maven
- **Frontend:** HTML, CSS e JavaScript simples

---

## ⚙️ Configuração do Banco de Dados

No arquivo `application.properties`, a persistência do banco H2 já está configurada:

```properties
# Persistência em arquivo
spring.datasource.url=jdbc:h2:file:./data/testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Configuração do Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## 🚀 Deploy e Execução

### ✅ Pré-requisitos

- Java 17 instalado.

**(Para ambiente Windows)**  
Recomenda-se o uso do **NSSM** para configurar a aplicação como um serviço.

---

### 📦 Utilizando o JAR Disponível

1. **Download do JAR**  
   Baixe o arquivo em `JarParaExecução/system-0.0.1-SNAPSHOT.jar`.

2. **Executar via terminal:**
   ```bash
   java -jar artifacts/system-0.0.1-SNAPSHOT.jar
   ```

---

### 🧰 Deploy com NSSM (Windows)

1. Baixe e extraia o NSSM (ex.: `C:\nssm`).
2. Vá até a pasta `win64` ou `win32`, conforme sua arquitetura.
3. Execute:
   ```bash
   nssm.exe install MinhaAplicacao
   ```

4. Na janela aberta, configure:
    - **Application Path:** Caminho para o `java.exe` (ex.: `C:\Program Files\Java\jdk-17\bin\java.exe`)
    - **Arguments:**
      ```
      -jar "C:\caminho\para\o\jar\artifacts\system-0.0.1-SNAPSHOT.jar"
      ```
    - **Startup Directory:** Diretório onde o JAR está localizado (ex.: `artifacts/`)

5. Clique em **Install Service**.

6. Abra o Prompt de Comando como administrador e inicie o serviço:
   ```bash
   cd C:\nssm\win64
   nssm.exe start MinhaAplicacao
   ```

---

## 🌐 Acesso à Aplicação

- **Interface Principal:**  
  [http://localhost:8080](http://localhost:8080)

- **Console H2:**  
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 🎥 Demonstração

Um vídeo de aproximadamente **1 minuto** mostrando a aplicação em funcionamento está disponível:

- Diretamente na pasta: `VideoDemonstraçãoSystem/DemonstraçãoDoSistema.mp4`

---

## 📝 Termos de Autorização

Este projeto foi desenvolvido como parte de um trabalho de extensão no curso de **Tecnologia em Análise e Desenvolvimento de Sistemas**.  
Os termos de uso e autorização estão anexados neste repositório (ou disponíveis em documento separado, se necessário).

Os termos de autorização estão disponíveis na pasta `DocEvidencias`
Fotos da empresa disponiveis na pasta `DocEvidencias/FotosEmpresa`