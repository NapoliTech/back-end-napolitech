## Back-End Napolitech 🎯

Bem-vindo ao motor por trás da aplicação **Napolitech**, desenvolvido com **Spring Boot** e alimentado pelo banco de dados **MySQL**! 🚀  
Este projeto é totalmente containerizado com **Docker**, garantindo uma execução simples e eficiente. 🐳

---

### 🔧 Pré-requisitos

Antes de começar, certifique-se de ter os seguintes itens instalados no seu sistema:

- **Docker** 🐳
- **Docker Compose** ⚙️

---

### 🚀 Como executar o projeto

#### 1️⃣ Clone o repositório 📂
```bash
git clone https://github.com/seu-usuario/back-end-napolitech.git
cd back-end-napolitech
```

#### 2️⃣ Construa e inicie os containers com Docker Compose 🏗️
```bash
docker-compose up --build
```

#### 3️⃣ Acesse a aplicação 🌐
A API estará disponível na porta **8080**! 🎯  
[http://localhost:8080](http://localhost:8080)

---

### 🛠️ Tecnologias utilizadas

- **Spring Boot** 3.4.2 🚀
- **MySQL** 8.0 🐳
- **Docker** ⚙️
- **Docker Compose** 🔁
- **Maven** 📦

---

### 📂 Estrutura do projeto

```plaintext
back-end-napolitech/
├── src/
│   ├── main/
│   ├── test/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
```

---

### 🐳 Configuração do Docker

#### ⚙️ Dockerfile
O **Dockerfile** cria uma imagem baseada no **OpenJDK 21**, instala o **Maven** e empacota o código fonte.

#### ⚙️ docker-compose.yml
Define dois serviços principais:
- **app** 🖥️ → A aplicação Java que se comunica com o banco de dados.
- **db** 🗄️ → Um container **MySQL** que hospeda o banco `pizzaria_db`.

---

### 🔐 Variáveis de ambiente

As credenciais do banco de dados já estão configuradas no `docker-compose.yml`:

- `SPRING_DATASOURCE_URL`: `jdbc:mysql://db:3306/pizzaria_db?createDatabaseIfNotExist=true`
- `SPRING_DATASOURCE_USERNAME`: `napolitech`
- `SPRING_DATASOURCE_PASSWORD`: `napolitech_dev`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: `update`

---

### 🛡️ Licença

Este projeto é distribuído sob a licença **MIT**. Consulte o arquivo [LICENSE](./LICENSE) para mais detalhes.
