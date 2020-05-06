# Demo of MyBatis and Cockroach DB

This demo is fashioned off of the Cockroach DB demos here: https://www.cockroachlabs.com/docs/v19.2/build-a-java-app-with-cockroachdb.html

The demo shows how to use [MyBatis](https://mybatis.org/mybatis-3/) to interact with [CockroachDB](https://www.cockroachlabs.com/). The demo is a Spring Boot application
and uses the [MyBatis Spring Integration](https://mybatis.org/spring/) to make things easy to configure.

## How To Run the Demo

1. Install cockroach: `brew install cockroachdb/tap/cockroach`

1. Start a Cockroach cluster: `cockroach start --insecure`

   You could also run CockroachDB constantly in the background with `brew services start cockroachdb/tap/cockroach`

1. Connect to cockroach shell: `cockroach sql --insecure`

   If you are running CockroachDB from the command line, you will need to do this in a separate terminal session.

1. Run SQL commands to create the user and database...

   ```sql
   CREATE USER IF NOT EXISTS maxroach;
   CREATE DATABASE bank;
   GRANT ALL ON DATABASE bank TO maxroach;
   \q
   ```

1. The application is configured to connect to CockroachDB running locally at the default port (26257). If you have CockroachDB running elsewhere, modify
   the values in `src/main/resources/application.yml` appropriately.

1. Run the application: `./gradlew bootRun` - you should see the Spring app start, then several messages about database updates. Note that this requires
   CockroachDB to be running and available as configured in `src/main/resources/application.yml`

1. You can also run the integration tests with `./gradlew test`. This will start CockroachDB in a Docker container and run the tests against that instance.
   This requires you to have Docker installed, but you do not need to install CockroachDB manually.
