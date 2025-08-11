# Social Network API

### Uma API RESTful construída com Arquitetura Limpa, Código Limpo e Boas Práticas de Engenharia de Software.

### Visão Geral

Este projeto é uma API RESTful para uma rede social simples, desenvolvida com o objetivo principal de demonstrar o domínio de conceitos fundamentais e boas práticas de desenvolvimento de software. A arquitetura foi projetada seguindo os princípios da **Arquitetura Limpa (Clean Architecture)** e **SOLID**, garantindo um código desacoplado, testável e de fácil manutenção.

O projeto utiliza um conjunto de tecnologias modernas e de código aberto para criar uma base robusta e escalável, focando em segurança, eficiência e qualidade de código.

### Funcionalidades (MVP - Minimum Viable Product)

A API fornece as seguintes funcionalidades principais:

  * **Autenticação e Autorização:**
      * Registro de novos usuários.
      * Login com geração de **JWT (JSON Web Token)** para autenticação.
      * Proteção de endpoints, permitindo acesso apenas a usuários autenticados.
  * **Gestão de Usuários:**
      * Visualização de perfil de usuário.
      * Funcionalidade de "seguir" e "deixar de seguir" outros usuários.
  * **Gestão de Posts:**
      * Criação, visualização e exclusão de posts.
  * **Feed de Posts:**
      * Visualização de um feed de posts.

### Tecnologias Utilizadas

  * **Linguagem de Programação:** Java 17
  * **Framework Backend:** Spring Boot 3.3.1
  * **Persistência de Dados:** Spring Data JPA e Hibernate
  * **Banco de Dados:** PostgreSQL
  * **Segurança:** Spring Security (com JWT)
  * **Mensageria:** RabbitMQ (futuro)
  * **Build e Dependências:** Maven
  * **Containerização:** Docker e Docker Compose
  * **Versionamento:** Git
  * **Ferramenta de Gerenciamento de BD:** DBeaver
  * **Utilitários:** Lombok (redução de código boilerplate)
  * **Testes:** JUnit e Mockito (futuro)
  * **Boas Práticas:** Arquitetura Limpa, SOLID, DTOs e Clean Code

### Arquitetura do Projeto

O projeto é estruturado em camadas distintas para isolar as regras de negócio das preocupações de infraestrutura, seguindo o padrão da Arquitetura Limpa.

```
social-network-api/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── socialnetwork/
│                       ├── domain/           <-- Camada de Domínio (Regras de Negócio Puras)
│                       │   ├── entity/           <-- Entidades (User, Post)
│                       │   └── repository/       <-- Interfaces de Repositório (UserRepository)
│                       ├── application/      <-- Camada de Aplicação (Lógica de Uso do Domínio)
│                       │   └── service/          <-- Implementações de Serviço (UserService)
│                       ├── infrastructure/   <-- Camada de Infraestrutura (Tecnologia Externa)
│                       │   ├── config/           <-- Configurações de Segurança e Outros
│                       │   ├── controller/       <-- Endpoints da API REST
│                       │   ├── dto/              <-- DTOs para comunicação externa
│                       │   └── security/         <-- Utilitários de JWT e Filtros
│                       └── SocialNetworkApplication.java
```

  * **Domain (Domínio):** O núcleo da aplicação. Contém as entidades e interfaces que definem o "o quê" do nosso sistema (o que é um usuário, um post, etc.). Não depende de nenhuma tecnologia externa.
  * **Application (Aplicação):** Contém a lógica de negócio específica do aplicativo, orquestrando as entidades do domínio para realizar ações (o "como").
  * **Infrastructure (Infraestrutura):** Lida com todos os detalhes tecnológicos, como a API REST, o banco de dados, a segurança, etc. Esta camada depende das camadas internas, mas as camadas internas não dependem dela.

### Como Executar o Projeto

1.  **Pré-requisitos:** Certifique-se de ter o **Java JDK 17+**, **Maven** e **Docker** instalados.
2.  **Clone o repositório:**
    ```bash
    git clone https://docs.github.com/articles/referencing-and-citing-content
    cd social-network-api
    ```
3.  **Configure o ambiente com Docker Compose:**
      * Este comando irá levantar o banco de dados PostgreSQL em um container.
    <!-- end list -->
    ```bash
    docker-compose up -d
    ```
4.  **Configure as propriedades da aplicação:**
      * Abra o arquivo `src/main/resources/application.properties`.
      * Preencha as credenciais do seu banco de dados PostgreSQL e a chave secreta do JWT.
5.  **Execute a aplicação:**
      * A partir da linha de comando, no diretório raiz do projeto:
    <!-- end list -->
    ```bash
    mvn spring-boot:run
    ```
      * O backend estará disponível em `http://localhost:8080`.

### Próximos Passos e Oportunidades de Melhoria

  * **DTOs:** Refatorar a API para usar **DTOs (Data Transfer Objects)** para todas as operações de CRUD, protegendo as entidades e validando a entrada com **Jakarta Bean Validation**.
  * **Recursos Avançados:** Adicionar funcionalidades de "gostar" e "comentar" em posts.
  * **Armazenamento de Imagens:** Implementar a funcionalidade de upload de imagens (para perfis e posts) com armazenamento local.
  * **Testes Automatizados:** Adicionar cobertura de testes unitários (JUnit e Mockito) e de integração (com `TestContainers` para o banco de dados).
  * **Documentação:** Integrar o **Swagger/OpenAPI** para documentar e explorar os endpoints da API.
  * **CI/CD:** Configurar um pipeline de Integração Contínua e Entrega Contínua com **Jenkins** e **Docker**.
  * **Mensageria:** Implementar funcionalidades assíncronas (como envio de notificações) usando **RabbitMQ**.
