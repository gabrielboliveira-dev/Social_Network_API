# API REST para uma Rede Social Simples

Este projeto consiste numa API RESTful desenvolvida em Java com Spring Boot para simular as funcionalidades básicas de uma rede social. O objetivo principal é demonstrar familiaridade com diversas tecnologias modernas de desenvolvimento backend e servir como um projeto de aprendizado.

## Tecnologias Utilizadas

Este projeto utiliza as seguintes tecnologias:

* **Linguagem de Programação:** Java
* **Framework Backend:** Spring Boot
* **Framework REST:** Spring MVC
* **Persistência de Dados:** Spring Data JPA, Hibernate
* **Banco de Dados:** PostgreSQL
* **Segurança:** Spring Security (implementação futura)
* **Testes:** JUnit, Mockito
* **Build e Gerenciamento de Dependências:** Maven
* **Containerização:** Docker, Docker Compose
* **Versionamento de Código:** Git
* **Ambiente de Nuvem (Implantação Futura):** AWS (EC2, RDS, S3, CloudWatch)
* **Mensageria (Implantação Futura):** JMS, RabbitMQ (ou similar)
* **Integração Contínua/Entrega Contínua (Implantação Futura):** Jenkins
* **Orquestração de Contêineres (Implantação Futura):** Kubernetes
* **Princípios de Desenvolvimento:** SOLID, DDD (em estudo), Clean Code, Design Patterns

## Funcionalidades Atuais (MVP - Minimum Viable Product)

* **Gestão de Utilizadores:**
    * Registo de Utilizadores (criar conta com nome de utilizador, email e senha).
    * ‘Login’ de Utilizadores (autenticação para aceder a funcionalidades protegidas - implementação futura).
    * Visualização de Perfil de Utilizador (nome de utilizador).
* **Gestão de Posts:**
    * Criação de Posts (apenas texto por enquanto).
    * Listagem de Posts (todos os utilizadores).
    * Visualização de um Post específico.
    * Exclusão de um Post (apenas pelo criador).
* **Seguir/Deixar de Seguir Utilizadores:**
    * Seguir outros utilizadores.
    * Listagem de Utilizadores que um utilizador segue (seguindo).
    * Listagem de Utilizadores que seguem um utilizador (seguidores).

## Pré-requisitos

Antes de executar o projeto, você precisará ter as seguintes ferramentas instaladas:

* **Java JDK:** (Versão recomendada: 17 ou superior)
* **Maven:** (Versão recomendada: 3.6.x ou superior)
* **Docker:** (Para containerização)
* **Docker Compose:** (Para orquestração de contêineres local)
* **PostgreSQL:** (Para o banco de dados)
* **Git:** (Para versionamento de código)

## Configuração do Projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Gabriel-Oliveiraa/socialnetwork
    cd socialnetwork
    ```

2.  **Configure o banco de dados PostgreSQL:**
    * Crie um banco de dados chamado `social_network` (ou outro nome de sua preferência).
    * Configure as credenciais de acesso ao banco de dados no arquivo `src/main/resources/application.properties` ou `application.yml`. Exemplo em `application.properties`:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/social_network
        spring.datasource.username=seu_usuario
        spring.datasource.password=sua_senha
        spring.jpa.hibernate.ddl-auto=update
        ```

3.  **Execute o backend com Maven (para desenvolvimento):**
    ```bash
    mvn spring-boot:run
    ```
    O backend estará disponível em `http://localhost:8080`.

4.  **Execute o backend com Docker Compose (para ambiente de desenvolvimento):**
    * Certifique-se de ter o Docker e Docker Compose instalados.
    * No diretório raiz do projeto, execute:
        ```bash
        docker-compose up -d
        ```
    * Isso irá construir e iniciar o container da aplicação Spring Boot e do PostgreSQL. A aplicação estará acessível em `http://localhost:8080`.

## Endpoints da API (Exemplos)

Aqui estão alguns exemplos dos endpoints da API REST (sujeitos a alterações):

* **POST /api/users/register:** Registra um novo utilizador.
* **POST /api/users/login:** Autentica um utilizador (implementação futura).
* **GET /api/users/{userId}:** Obtém informações de um utilizador específico.
* **POST /api/posts:** Cria um novo post.
* **GET /api/posts:** Lista todos os posts.
* **GET /api/posts/{postId}:** Obtém um post específico.
* **DELETE /api/posts/{postId}:** Exclui um post (requer autenticação - implementação futura).
* **POST /api/follow/{userIdToFollow}:** Segue um utilizador.
* **DELETE /api/follow/{userIdToUnfollow}:** Deixa de seguir um utilizador.
* **GET /api/users/{userId}/following:** Lista os utilizadores que um utilizador segue.
* **GET /api/users/{userId}/followers:** Lista os seguidores de um utilizador.

## Próximos Passos e Melhorias Futuras

Este projeto está em desenvolvimento e as seguintes melhorias estão planejadas:

* Implementação completa de **autenticação e autorização** com Spring Security (JWT).
* Adicionar funcionalidade para **editar posts**.
* Implementar **upload de imagens** para posts e perfis (integrando com AWS S3).
* Desenvolver a funcionalidade de **"gostar" (like)** em posts.
* Implementar **comentários** em posts.
* Melhorar o **feed de posts** para exibir apenas os posts dos utilizadores seguidos.
* Adicionar **testes de integração** para os endpoints da API.
* Configurar um pipeline de **Integração Contínua/Entrega Contínua (CI/CD)** com Jenkins.
* Realizar o **deployment na AWS** utilizando EC2 e RDS.
* Explorar o uso de **mensageria** (RabbitMQ) para funcionalidades assíncronas.
* Considerar a **documentação da API** com Swagger/OpenAPI.
* Explorar conceitos de **DDD (Domain-Driven Design)** para melhor modelagem do domínio.

## Contribuição

Contribuições são bem-vindas! Se você tiver alguma sugestão de melhoria ou encontrar algum problema, sinta-se à vontade para abrir uma issue ou enviar um pull request.
