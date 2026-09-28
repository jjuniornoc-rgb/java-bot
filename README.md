# ☕ Java Discord Bot (`JDA 5.x`)

Um bot para Discord moderno, resiliente e altamente escalável escrito em **Java 17/21** utilizando a biblioteca **JDA 5 (Java Discord API)** com suporte nativo a **Slash Commands** (`/ping`).

Totalmente preparado para hospedagem local e nuvem como **Discloud**.

---

## 🚀 Funcionalidades

- ⚡ **Slash Commands Nativos**: Registra comandos diretamente pela API do Discord (guilda ou global).
- 🏓 **Comando `/ping`**:
  - Latência de ida e volta (Roundtrip HTTP da interação).
  - Ping do WebSocket gateway (`jda.getGatewayPing()`).
  - Tempo de atividade do bot (*Uptime* formatado em horas, minutos e segundos).
  - Métricas de uso de memória da máquina virtual Java (JVM).
  - Embed estilizado em Laranja Java com versão da JDK instalada.
- ☁️ **Compatível com Discloud**: Arquivo `discloud.config` configurado com `maven-shade-plugin` para gerar JAR executável único.
- 🔒 **Variáveis de Ambiente**: Gestão segura com `dotenv-java`.

---

## 📁 Estrutura do Projeto

```text
java-bot/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── bot/
│                       └── Main.java       # Código principal do bot e handlers
├── pom.xml                                 # Definição do Maven e dependências (JDA, Dotenv, Logback)
├── discloud.config                         # Arquivo de configuração da Discloud
├── .env.example                            # Modelo de variáveis de ambiente
├── .gitignore                              # Arquivos ignorados pelo Git
└── README.md                               # Documentação do projeto
```

---

## 🛠️ Pré-requisitos

- **Java JDK 17 ou 21** instalado.
- **Apache Maven 3.8+** instalado.
- Conta no [Discord Developer Portal](https://discord.com/developers/applications).

---

## ⚙️ Configuração e Instalação Local

### 1. Clonar o Repositório
```bash
git clone https://github.com/jjuniornoc-rgb/java-bot.git
cd java-bot
```

### 2. Configurar as Variáveis de Ambiente
Copie o arquivo `.env.example` para `.env`:
```bash
cp .env.example .env
```
Edite o arquivo `.env` e insira o seu token:
```env
DISCORD_TOKEN=seu_token_aqui_sem_aspas

# Opcional: ID do seu servidor de testes (para sincronização instantânea)
GUILD_ID=
```

### 3. Compilar o Projeto (Gerar Fat JAR)
```bash
mvn clean package -DskipTests
```
Isso gerará o executável em `target/bot.jar`.

---

## ▶️ Executando o Bot

```bash
java -jar target/bot.jar
```

Você verá no console:
```text
==================================================
🤖 Bot Conectado: SeuBot#0000 (ID: 1234567890)
⚡ Framework:     JDA 5.x (Java)
📡 Servidores:    1
==================================================
✅ Slash Commands sincronizados globalmente!
```

---

## ☁️ Hospedagem na Discloud

1. Comprima os seguintes arquivos num arquivo `.zip`:
   - `src/`
   - `pom.xml`
   - `discloud.config`
   - `.env`
2. No painel da [Discloud](https://discloudbot.com) ou via CLI da Discloud:
   ```bash
   discloud upload
   ```
3. A Discloud executará o comando de build `mvn clean package -DskipTests` e inicializará o bot através de `target/bot.jar`.

---

## 📜 Licença

Distribuído sob a licença MIT. Consulte `LICENSE` para mais informações.
