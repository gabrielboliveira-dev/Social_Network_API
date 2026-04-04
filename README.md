# 🚀 Social Network API: Uma API RESTful Moderna com Arquitetura Limpa

## ✨ Visão Geral

Este projeto é uma API RESTful completa para uma rede social simplificada, desenvolvida com foco em demonstrar o domínio de conceitos avançados e boas práticas de engenharia de software. A arquitetura foi cuidadosamente construída seguindo os princípios da **Arquitetura Limpa (Clean Architecture)** e **SOLID**, garantindo um código altamente desacoplado, testável e de fácil manutenção.

A API oferece um conjunto robusto de funcionalidades para gestão de usuários, posts e interações sociais, tudo isso suportado por tecnologias de código aberto e configurado para rodar em containers Docker, promovendo escalabilidade, segurança e eficiência.

## 🛠️ Tecnologias e Ferramentas

A aplicação utiliza as seguintes tecnologias, configuradas para rodar em containers Docker para um ambiente de desenvolvimento e produção consistente:

*   **Linguagem de Programação:** Java 17
*   **Framework Backend:** Spring Boot 3.3.1 (Web, Data JPA, Security, AMQP, Validation)
    *   *Justificativa:* Escolha robusta para construir aplicações Java de forma rápida e eficiente, com ecossistema vasto para diversas necessidades.
*   **Persistência:** PostgreSQL (banco de dados) e Spring Data JPA/Hibernate (acesso aos dados)
    *   *Justificativa:* PostgreSQL é um banco de dados relacional poderoso e confiável. Spring Data JPA simplifica a interação com o banco de dados, e Hibernate é o ORM padrão.
*   **Migrações de Banco de Dados:** Flyway
    *   *Justificativa:* Garante o controle de versão do esquema do banco de dados, facilitando a evolução e a consistência entre ambientes.
*   **Segurança:** Spring Security com JWT (JSON Web Token) e BCrypt (criptografia de senhas)
    *   *Justificativa:* Padrão da indústria para autenticação e autorização em aplicações Spring, oferecendo segurança robusta e flexibilidade.
*   **Build:** Maven
*   **Containerização:** Docker e Docker Compose
    *   *Justificativa:* Permite empacotar a aplicação e suas dependências em ambientes isolados, garantindo portabilidade e facilidade de implantação.
*   **Testes:** JUnit 5, Mockito, Spring Boot Test, Testcontainers
    *   *Justificativa:* Suíte completa para testes unitários, de integração e de componentes, com Testcontainers permitindo testes de integração com serviços reais (como PostgreSQL) em containers.
*   **Documentação da API:** Swagger/OpenAPI (springdoc-openapi-starter-webmvc-ui)
    *   *Justificativa:* Geração automática de documentação interativa da API, facilitando o desenvolvimento e o consumo por parte de outros sistemas ou desenvolvedores.
*   **Produtividade:** Lombok
    *   *Justificativa:* Reduz o código boilerplate (getters, setters, construtores, etc.), tornando o código mais limpo e conciso.

## 🏛️ Arquitetura

O projeto adota uma **Arquitetura Limpa (Clean Architecture)**, dividindo o código em camadas distintas para isolar as regras de negócio das tecnologias externas e garantir alta coesão e baixo acoplamento.

*   **`domain`**: Contém as entidades de negócio (User, Post, Comment, Like) e as interfaces de repositório. Representa as regras de negócio mais internas e agnósticas a frameworks.
*   **`application`**: Implementa a lógica de negócio específica da aplicação através de Casos de Uso (Use Cases) e Serviços (Services). Orquestra as operações do domínio.
*   **`infrastructure`**: Camada mais externa, responsável por detalhes de implementação como controladores REST, DTOs (Data Transfer Objects), configurações de segurança, e implementações de persistência (repositórios JPA).

## ✅ Funcionalidades Implementadas

A API fornece as seguintes funcionalidades principais, todas protegidas por autenticação JWT:

### 🔐 Autenticação e Autorização
*   **Registro de Usuários:** Criação de novas contas com validação de dados.
*   **Login Seguro:** Autenticação de usuários com geração de JWT (JSON Web Token) para acesso seguro.
*   **Proteção de Endpoints:** Controle de acesso a recursos da API, permitindo acesso apenas a usuários autenticados e autorizados.

### 👤 Gestão de Usuários
*   **Criação e Visualização de Perfis:** Registro e consulta de perfis de usuário.
*   **Seguir/Deixar de Seguir:** Estabelecimento e remoção de relações sociais entre usuários.
*   **Upload de Imagem de Perfil:** Personalização do perfil com armazenamento local de imagens.

### 📝 Gestão de Posts
*   **CRUD de Posts:** Criação, visualização, atualização e exclusão de posts.
*   **Upload de Imagem para Posts:** Enriquecimento de posts com imagens, armazenadas localmente.
*   **Likes em Posts:** Adicionar e remover "gostei" em posts para interação dos usuários.
*   **Comentários em Posts:** Adicionar e excluir comentários em posts, promovendo discussões.
*   **Listagem Paginada:** Visualização de posts com suporte a paginação para melhor performance e experiência do usuário.

## ⚙️ Como Rodar o Projeto

Para executar a aplicação e o banco de dados localmente em containers, siga estas instruções:

1.  **Pré-requisitos:**
    *   Tenha o **Java JDK 17+** e o **Maven** instalados.
    *   Tenha o **Docker Desktop** instalado e em execução no seu computador.

2.  **Clone o Repositório e Navegue até a Pasta:**

    ```bash
    git clone https://github.com/gabrielboliveira-dev/Social_Network_API
    cd Social_Network_API
    ```

3.  **Configurações de Ambiente (`.env`):**
    *   Crie um arquivo chamado `.env` na raiz do projeto (ao lado do `docker-compose.yml`).
    *   Preencha-o com as seguintes variáveis, **substituindo `SUA_CHAVE_SECRETA_MUITO_LONGA_E_COMPLEXA_AQUI` por uma string aleatória e forte de pelo menos 32 caracteres (256 bits) para o `JWT_SECRET`**.

        ```
        # Banco de Dados
        DB_USERNAME=postgres
        DB_PASSWORD=postgres
        DB_URL=jdbc:postgresql://db:5432/postgres

        # JWT
        JWT_SECRET=SUA_CHAVE_SECRETA_MUITO_LONGA_E_COMPLEXA_AQUI
        JWT_EXPIRATION=86400000
        ```
    *   *Importante:* A segurança do seu JWT depende diretamente da força desta chave. Não use o placeholder em produção.

4.  **Construir e Iniciar os Containers:**

    ```bash
    docker-compose up --build
    ```
    *   A primeira execução pode demorar, pois o Docker fará o download das imagens e a construção da sua aplicação.
    *   Este comando irá criar dois containers: um para a sua API (`app`) e outro para o PostgreSQL (`db`).
    *   Verifique os logs para garantir que ambos os serviços iniciaram sem erros. A aplicação Spring Boot deve exibir `Started SocialnetworkApplication in X.XXX seconds`.

5.  **Acessar a API:**
    *   A API estará disponível em `http://localhost:8080`.
    *   **Documentação da API (Swagger UI):** Acesse `http://localhost:8080/swagger-ui.html` no seu navegador para explorar e testar todos os endpoints de forma interativa.

## 🗺️ Endpoints Principais

| Método | Rota                                     | Descrição                                         |
| :----- | :--------------------------------------- | :------------------------------------------------ |
| `POST` | `/api/v1/auth/register`                  | Registra um novo usuário.                         |
| `POST` | `/api/v1/auth/login`                     | Autentica um usuário e retorna um JWT.            |
| `GET`  | `/api/v1/users/me`                       | Retorna o perfil do usuário autenticado.          |
| `POST` | `/api/v1/users/{userId}/follow`          | Segue um usuário específico.                      |
| `POST` | `/api/v1/posts`                          | Cria um novo post.                                |
| `GET`  | `/api/v1/posts`                          | Lista todos os posts (com paginação).             |
| `POST` | `/api/v1/posts/{postId}/comments`        | Adiciona um comentário a um post.                 |
| `POST` | `/api/v1/posts/{postId}/like`            | Adiciona um "gostei" a um post.                   |
| `POST` | `/api/v1/images/profile`                 | Faz upload da imagem de perfil do usuário.        |

## 🌟 Demonstração de Boas Práticas

Este projeto é um exemplo prático da aplicação de diversas boas práticas de desenvolvimento de software:

*   **Arquitetura Limpa:** Separação clara de responsabilidades entre as camadas de domínio, aplicação e infraestrutura.
*   **Princípios SOLID:** Código projetado para ser flexível, extensível e de fácil manutenção.
*   **DTOs (Data Transfer Objects):** Utilização de DTOs para desacoplar a camada de apresentação da camada de domínio.
*   **Jakarta Bean Validation:** Validação de dados de entrada para garantir a integridade das informações.
*   **Tratamento Global de Exceções:** Gerenciamento centralizado de erros para fornecer respostas consistentes e informativas da API.
*   **Testes Abrangentes:** Inclusão de testes unitários e de integração (com Testcontainers) para garantir a qualidade e a robustez do código.
*   **Segurança:** Implementação de autenticação JWT e criptografia de senhas com BCrypt.