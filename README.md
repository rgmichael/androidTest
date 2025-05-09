# GitReposApp

GitReposApp é um aplicativo Android desenvolvido com Kotlin que permite visualizar repositórios públicos do GitHub e favoritar os seus preferidos. O app utiliza uma arquitetura moderna com Jetpack Compose, MVVM, Room, Retrofit e Hilt.

## 📱 Funcionalidades

- Listagem paginada de repositórios públicos do GitHub.
- Marcar e desmarcar repositórios como favoritos.
- Visualização separada de repositórios favoritos.
- Interface moderna utilizando Jetpack Compose.

---

## ⚙️ Requisitos

- Android Studio Hedgehog ou mais recente
- SDK Android 33 ou superior
- JDK 17
- Conexão com a internet (para chamadas à API do GitHub)

---

🛠️ Tecnologias e bibliotecas
- Kotlin
- Jetpack Compose (UI declarativa)
- Room (persistência local)
- Retrofit (API REST)
- Hilt (injeção de dependência)
- Coroutines & Flow (concorrência)
- MVVM (Model-View-ViewModel)

> Futuras implementações

- GPS com localização precisa
- Checagem de conexão com a internet
- Mudar ícone
- Criar splash
- Criar Login e autenticação


🗂 Estrutura do Projeto

app/
├── data/           # Fonte de dados (API e banco local)
├── domain/         # Modelos de domínio e use cases
├── ui/             # Telas, navegação e componentes de UI
├── di/             # Módulos de injeção com Hilt
├── MainActivity.kt # Ponto de entrada da aplicação


## ▶️ Como executar o projeto

1. Clone este repositório:

   ```bash
   git clone https://github.com/seu-usuario/GitReposApp.git

Navegue até a pasta do projeto:

cd GitReposApp

Sincronize o projeto com o Gradle:

No Android Studio, clique em "File > Sync Project with Gradle Files".

Compile e execute o app:

Selecione um emulador ou dispositivo físico e clique em Run (ou pressione Shift + F10).