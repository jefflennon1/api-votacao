# api-votacao

API REST para gerenciamento de sessões de votação cooperativa, permitindo cadastro de pautas, controle de sessões e contabilização de votos.

---

## Tecnologias utilizadas

- Java 25
- Spring Boot 4.0.3
- Spring Data JPA
- MySQL
- Flyway (migrations)
- Lombok
- SpringDoc OpenAPI (Swagger)
- JUnit 5 + Mockito (testes)
- Maven

---

## Pré-requisitos

Antes de rodar o projeto, você vai precisar ter instalado na sua máquina:

- [Java 25](https://www.oracle.com/java/technologies/downloads/)
- [MySQL](https://dev.mysql.com/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [Spring Tools Suite 4 (STS)](https://spring.io/tools) ou outra IDE de sua preferência

---

## Configuração do banco de dados

1. Acesse o MySQL e crie usuario E SENHA com o nome:

```
root
```

2. O arquivo `src/main/resources/application.properties` já possui instruções automáticas de criação do banco de dados, desde que o usuário e senha sejam root e o banco de dados esteja configurado na porta 3306:

```
spring.datasource.url=jdbc:mysql://localhost:3306/api-votacao?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true

```

> As tabelas serão criadas automaticamente pelo **Flyway** na primeira execução. Dados de exemplo também serão inseridos automaticamente para facilitar os testes.

---

## Configuração do Lombok no STS

O instalador do Lombok não reconhece o executável do STS 5.x automaticamente. Para resolver isso, siga os passos abaixo:

1. Abra o arquivo `SpringToolsForEclipse.ini` localizado na pasta de instalação do STS. Exemplo:

```
C:\Users\seu-usuario\Documents\sts\sts-5.1.1.RELEASE\SpringToolsForEclipse.ini
```

2. Adicione a seguinte linha no **final do arquivo**:

```
-javaagent:C:\Users\seu-usuario\Documents\sts\sts-5.1.1.RELEASE\lombok.jar
```

> Ajuste o caminho conforme a localização da sua instalação do STS.

3. Salve o arquivo, feche e reabra o STS.

4. Clique com botão direito no projeto e execute:
   - `Maven > Update Project (Alt + F5)`
   - `Project > Clean`

---

## Como rodar o projeto

### Pelo STS

1. Clone o repositório:

```bash
git clone https://github.com/jefflennon1/api-votacao.git
```

2. Importe o projeto no STS:
   - `File > Import > Maven > Existing Maven Projects`
   - Selecione a pasta do projeto clonado

3. Aguarde o Maven baixar as dependências

4. Clique com botão direito no projeto:
   - `Run As > Spring Boot App`

### Pelo terminal

```bash
git clone https://github.com/seu-usuario/api-votacao.git
cd api-votacao
mvn spring-boot:run
```

---

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8081/api-votacao/swagger-ui/index.html
```

---

## Endpoints disponíveis

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/v1/pautas` | Cadastrar nova pauta |
| `GET` | `/api/v1/pautas` | Listar todas as pautas |
| `GET` | `/api/v1/pautas/{id}` | Buscar pauta por ID |
| `POST` | `/api/v1/pautas/{id}/sessao` | Abrir sessão de votação |
| `POST` | `/api/v1/pautas/{id}/votos` | Registrar voto |
| `GET` | `/api/v1/pautas/{id}/resultado` | Obter resultado da votação |

---

## Exemplos de uso

### Cadastrar uma pauta

```http
POST /api/v1/pautas
Content-Type: application/json

{
    "titulo": "Aprovação do orçamento anual",
    "descricao": "Votação para aprovação do orçamento para o ano de 2026"
}
```

### Abrir uma sessão de votação

```http
POST /api/v1/pautas/1/sessao
Content-Type: application/json

{
    "duracaoEmMinutos": 5
}
```

> Se `duracaoEmMinutos` não for informado, a sessão ficará aberta por **1 minuto** por padrão.

### Registrar um voto

```http
POST /api/v1/pautas/1/votos
Content-Type: application/json

{
    "associadoId": "associado-001",
    "voto": true
}
```

> `true` = Sim, `false` = Não. Cada associado pode votar apenas uma vez por pauta.

### Obter resultado da votação

```http
GET /api/v1/pautas/1/resultado
```

Resposta:

```json
{
    "pautaId": 1,
    "tituloPauta": "Aprovação do orçamento anual",
    "totalVotos": 3,
    "votosSim": 2,
    "votosNao": 1,
    "resultado": "APROVADA"
}
```


### Validar CPF do associado

```http
GET /api/v1/cpf/391.142.817-08
```

Resposta quando o CPF é válido (resultado aleatório):

```json
{
    "status": "ABLE_TO_VOTE"
}
```

ou

```json
{
    "status": "UNABLE_TO_VOTE"
}
```

Resposta quando o CPF é inválido:

```json
{
    "status": 404,
    "mensagem": "CPF inválido: 123",
    "timestamp": "2026-03-19T10:00:00"
}
```

> **Importante:** o CPF pode ser informado com ou sem máscara. Ambos os formatos são aceitos:
> - Com máscara: `391.142.817-08` ✅
> - Sem máscara: `39114281708` ✅

Para gerar CPFs válidos para teste, acesse [gerarcpf.com.br](https://www.geradorcpf.com).




---

## Rodando os testes
 - Você pode executar os testes unitários pela linha de comanddo utilizando:

```bash
mvn test
```

- Caso você esteja utilizando a IDE Spring Tools (assim como eu) você pode executar os testes utilizando o seguinte fluxo:

```
Clique com o botão direito em cima do projeto > Run As > JUnit Test
```

---

## Decisões técnicas

- **Spring Boot 4.x + Java 25**: versões mais recentes e estáveis disponíveis, garantindo melhor performance e suporte de longo prazo (LTS)
- **MySQL**: banco de dados relacional robusto e amplamente utilizado no mercado
- **Flyway**: controle de versão do banco de dados, garantindo que as tabelas e dados sejam criados automaticamente e de forma consistente em qualquer ambiente
- **JPA + Spring Data**: abstração do banco de dados com queries geradas automaticamente pelo nome dos métodos, reduzindo código boilerplate
- **Lombok**: redução de código repetitivo (getters, setters, builders, logs)
- **DTOs separados por Request/Response**: evita expor as entidades diretamente nos endpoints, garantindo maior controle sobre os dados de entrada e saída
- **Versionamento da API via URL (`/api/v1/`)**: estratégia simples e amplamente adotada que permite evoluir a API sem quebrar clientes existentes
- **GlobalExceptionHandler**: centraliza o tratamento de erros, retornando respostas padronizadas em JSON para qualquer exceção da aplicação

---
## Estrutura do projeto
```
src/
├── main/
│   ├── java/br/com/jefferson/
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Interfaces de acesso ao banco
│   │   ├── service/        # Regras de negócio
│   │   ├── resource/       # Endpoints REST
│   │   ├── client/         # Facade de validação de CPF
│   │   ├── dto/            # Objetos de transferência de dados
│   │   └── exception/      # Exceções customizadas e handler global
│   └── resources/
│       ├── db/migration/   # Scripts Flyway
│       └── application.properties
└── test/
    └── java/br/com/jefferson/
        └── service/        # Testes unitários
        
```


## Teste de performance

Abaixo deixo algumas imagens dos endpoints realizados pelo Jmeter.

<img width="1365" height="767" alt="image" src="https://github.com/user-attachments/assets/a458bcb6-b934-4bab-830e-467882f04ff4" />


<img width="1365" height="763" alt="image" src="https://github.com/user-attachments/assets/11e75a66-d976-4cbe-9ae1-7664ded59790" />

