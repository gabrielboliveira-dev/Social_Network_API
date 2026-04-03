# Social Network API

### Uma API RESTful moderna construída com Arquitetura Limpa e Java

### Visão Geral

Este projeto é uma API RESTful completa para uma rede social simples, desenvolvida para demonstrar o domínio de conceitos avançados e boas práticas de engenharia de software. A arquitetura foi construída seguindo os princípios da **Arquitetura Limpa (Clean Architecture)** e **SOLID**, garantindo um código altamente desacoplado, testável e de fácil manutenção.

O projeto utiliza um conjunto robusto de tecnologias de código aberto para criar uma solução escalável, com foco em segurança, eficiência e qualidade de código.

### Funcionalidades Implementadas

A API fornece as seguintes funcionalidades principais, todas protegidas por autenticação JWT:

  * **Autenticação e Autorização:**
      * Registro de novos usuários com validação de dados.
      * Login com geração de **JWT (JSON Web Token)** para autenticação segura.
      * Proteção de endpoints, permitindo acesso apenas a usuários autenticados e autorizados.

  * **Gestão de Usuários:**
      * Criação e visualização de perfis de usuário.
      * Funcionalidade de "seguir" e "deixar de seguir" outros usuários, estabelecendo relações sociais.
      * **Upload de Imagem de Perfil** com armazenamento local, permitindo personalização.

  * **Gestão de Posts:**
      * Criação, visualização, atualização e exclusão de posts.
      * **Upload de Imagem para Posts** com armazenamento local, enriquecendo o conteúdo.
      * **Adicionar e Remover "gostei" (likes)** em posts, para interação dos usuários.
      * **Adicionar e Excluir comentários** em posts, promovendo discussões.
      * Paginação na listagem de posts para melhor performance e experiência do usuário.

### Tecnologias Utilizadas

A aplicação utiliza as seguintes tecnologias, que foram configuradas para rodar em containers Docker:

  * **Linguagem de Programação:** Java 17
  * **Framework Backend:** Spring Boot 3.3.1 (Web, Data JPA, Security)
  * **Persistência:** PostgreSQL (banco de dados) e Spring Data JPA/Hibernate (acesso aos dados)
  * **Segurança:** Spring Security com JWT e BCrypt (criptografia de senhas)
  * **Build:** Maven
  * **Containerização:** Docker e Docker Compose (para ambiente de desenvolvimento e produção)
  * **Testes:** JUnit 5, Mockito, Spring Boot Test, Testcontainers (para testes de integração com PostgreSQL)
  * **Documentação da API:** Swagger/OpenAPI (para exploração interativa da API)
  * **Boas Práticas:** Arquitetura Limpa, SOLID, DTOs e Jakarta Bean Validation, Tratamento Global de Exceções.

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
│                       ├── domain/           <-- Entidades de Negócio e Interfaces de Repositório
│                       ├── application/      <-- Lógica de Negócio (Casos de Uso/Serviços)
│                       └── infrastructure/   <-- API (Controladores, DTOs), Configurações de Segurança e Persistência
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
    git clone https://github.com/gabrielboliveira-dev/Social_Network_API
    cd social-network-api
    ```

3.  **Configurações de Ambiente (Variáveis de Ambiente):**
    As credenciais e configurações sensíveis não ficam expostas no código. Utilizamos variáveis de ambiente, principalmente no `docker-compose.yml`. Certifique-se de preencher a variável `JWT_SECRET` de forma adequada para garantir que os tokens gerados sejam seguros. As configurações para o banco de dados (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) também são definidas via variáveis de ambiente no `docker-compose.yml`.

4.  **Construir e Iniciar os Containers:**
      * Execute o comando `docker-compose up --build`.
      * A primeira execução pode demorar, pois o Docker fará o download das imagens e a construção da sua aplicação.
      * Este comando irá criar dois containers: um para a sua API e outro para o PostgreSQL.
      * **Importante:** Se a porta 5432 estiver em uso, verifique se há outro serviço PostgreSQL rodando em sua máquina e pare-o, ou altere a porta no `docker-compose.yml`.

### Como Usar a API

A API estará disponível em `http://localhost:8080` e é versionada através do prefixo `/api/v1`. Você pode usar o **Postman** ou o **Swagger UI** para interagir com ela.

  * **Documentação da API (Swagger UI):**
      * Acesse `http://localhost:8080/swagger-ui.html` no seu navegador. Você poderá visualizar e testar todos os endpoints da sua API de forma interativa.

  * **Fluxos Básicos:**

    1.  **Registrar:** `POST` para `http://localhost:8080/api/v1/auth/register` com `username`, `email` e `password`.
    2.  **Login:** `POST` para `http://localhost:8080/api/v1/auth/login` com `username` e `password`. O corpo da resposta conterá o **JWT** necessário para autenticação em outras requisições.
    3.  **Criar um Post (Protegido):** `POST` para `http://localhost:8080/api/v1/posts` com o **JWT** no cabeçalho `Authorization: Bearer <seu_token>`.
    4. **Listar Posts (Paginado):** `GET` para `http://localhost:8080/api/v1/posts?page=0&size=10`.

### Como Executar os Testes

Para executar todos os testes do projeto (unitários e de integração), utilize o Maven:

```bash
mvn clean install
```

Este comando irá limpar o projeto, compilar o código, executar todos os testes e instalar o artefato no seu repositório Maven local.
