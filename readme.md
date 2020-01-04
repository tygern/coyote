# Coyote

![coyote](readme-images/coyote.jpg)

A simple application using [Ktor](https://ktor.io), [Exposed](https://github.com/JetBrains/Exposed),
and [Flyway](https://flywaydb.org/).

## Build

1.  Create the databases.
    ```bash
    mysql -uroot < databases/create_databases.sql
    ```

1.  Run migrations.
    ```bash
    ./gradlew testMigrate
    ```

1.  Run the build.
    ```bash
    ./gradlew clean build
    ```

## Run

1.  Run migrations.
    ```bash
    ./gradlew devMigrate
    ```

1.  Run the application.
    ```bash
    ./gradlew run
    ```

1.  Execute the HTTP requests listed in the [requests file](requests.http).
    IntelliJ can [easily execute](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html)
    these requests.
