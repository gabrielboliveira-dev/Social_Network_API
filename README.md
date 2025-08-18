# Social Network API

### Uma API RESTful moderna construída com Arquitetura Limpa e Java

### Visão Geral

Este projeto é uma API RESTful completa para uma rede social simples, desenvolvida para demonstrar o domínio de conceitos avançados e boas práticas de engenharia de software. A arquitetura foi construída seguindo os princípios da **Arquitetura Limpa (Clean Architecture)** e **SOLID**, garantindo um código altamente desacoplado, testável e de fácil manutenção.

O projeto utiliza um conjunto robusto de tecnologias de código aberto para criar uma solução escalável, com foco em segurança, eficiência e qualidade de código.

### Funcionalidades Implementadas

A API fornece as seguintes funcionalidades principais, todas protegidas por autenticação JWT:

  * **Autenticação e Autorização:**

      * Registro de novos usuários com validação de dados.
      * Login com geração de **JWT (JSON Web Token)** para autenticação.
      * Proteção de endpoints, permitindo acesso apenas a usuários autenticados.

  * **Gestão de Usuários:**

      * Visualização de perfis de usuário.
      * Funcionalidade de "seguir" e "deixar de seguir" outros usuários.
      * **Upload de Imagem de Perfil** com armazenamento local.

  * **Gestão de Posts:**

      * Criação, visualização e exclusão de posts.
      * **Upload de Imagem para Posts** com armazenamento local.
      * **Adicionar e Remover "gostei" (likes)** em posts.
      * **Adicionar e Excluir comentários** em posts.

### Tecnologias Utilizadas

A aplicação utiliza as seguintes tecnologias, que foram configuradas para rodar em containers Docker:

  * **Linguagem de Programação:** Java 17
  * **Framework Backend:** Spring Boot 3.3.1 (Web, Data JPA, Security)
  * **Persistência:** PostgreSQL (banco de dados) e Spring Data JPA/Hibernate (acesso aos dados)
  * **Segurança:** Spring Security com JWT e BCrypt (criptografia de senhas)
  * **Build:** Maven
  * **Containerização:** Docker e Docker Compose
  * **Documentação da API:** Swagger/OpenAPI
  * **Boas Práticas:** Arquitetura Limpa, SOLID, DTOs e Jakarta Bean Validation

### Estrutura do Projeto

A arquitetura do projeto é dividida em camadas para isolar as responsabilidades e as regras de negócio das tecnologias externas, conforme o padrão de Arquitetura Limpa.

```
social-network-api/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── socialnetwork/
│                       ├── domain/           <-- Entidades e Interfaces de Repositório
│                       ├── application/      <-- Lógica de Negócio (Serviços)
│                       └── infrastructure/   <-- API, Configurações de Segurança e DTOs
├── Dockerfile                        <-- Instruções para construir a imagem da aplicação
├── docker-compose.yml                <-- Orquestração da aplicação e do banco de dados
└── README.md
```

### Como Executar o Projeto

Para executar a aplicação e o banco de dados localmente em containers, siga estas instruções:

1.  **Pré-requisitos:**

      * Tenha o **Java JDK 17+** e o **Maven** instalados.
      * Tenha o **Docker Desktop** instalado e em execução no seu computador.

2.  **Clone o Repositório e Navegue até a Pasta:**

    ```bash
    git clone https://www.dio.me/articles/enviando-seu-projeto-para-o-github
    cd social-network-api
    ```

3.  **Configurar o `application.properties`:**

      * Abra o arquivo `src/main/resources/application.properties`.
      * Defina as suas credenciais de banco de dados e a chave secreta JWT.
      * **Observação:** O `docker-compose.yml` já configura o banco de dados, então o `application.properties` será usado para a aplicação principal.

4.  **Construir e Iniciar os Containers:**

      * Execute o comando `docker-compose up --build`.
      * A primeira execução pode demorar, pois o Docker fará o download das imagens e a construção da sua aplicação.
      * Este comando irá criar dois containers: um para a sua API e outro para o PostgreSQL.
      * **Importante:** Se a porta 5432 estiver em uso, pare o serviço do PostgreSQL que está rodando diretamente na sua máquina.

### Como Usar a API

A API estará disponível em `http://localhost:8080`. Você pode usar o **Postman** ou o **Swagger UI** para interagir com ela.

  * **Documentação da API (Swagger UI):**

      * Acesse `http://localhost:8080/swagger-ui.html` no seu navegador. Você poderá visualizar e testar todos os endpoints da sua API de forma interativa.

  * **Fluxo Básico via Postman:**

    1.  **Registrar:** `POST` para `http://localhost:8080/api/auth/register` com `username`, `email` e `password`.
    2.  **Login:** `POST` para `http://localhost:8080/api/auth/login` com `username` e `password`. O corpo da resposta conterá o **JWT** necessário.
    3.  **Criar um Post (Protegido):** `POST` para `http://localhost:8080/api/posts` com o **JWT** no cabeçalho `Authorization: Bearer <seu_token>`.
