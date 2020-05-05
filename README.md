# Demo of MyBatis and Cockroach DB

This demo is fashioned off of the Cockroach DB demos here: https://www.cockroachlabs.com/docs/v19.2/build-a-java-app-with-cockroachdb.html

## How To Run the Demo

1. Install cockroach

   `brew install cockroachdb/tap/cockroach`

1. Start a Cockroach cluster

   `cockroach start --insecure`

   You could also run constantly in the background with `brew services start cockroachdb/tap/cockroach`

1. Connect to cockroach shell

   `cockroach sql --insecure`

1. Run SQL commands to create the user and database...

   ```sql
   CREATE USER IF NOT EXISTS maxroach;
   CREATE DATABASE bank;
   GRANT ALL ON DATABASE bank TO maxroach;
   \q
   ```
