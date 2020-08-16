web: java $JAVA_OPTS -Ddw.server.applicationConnectors[0].port=$PORT -cp target/classes:target/dependency/* com.garrettestrin.PrivateGram.app.Main server app.yml
release: java -jar target/dependency/liquibase.jar --changeLogFile=src/main/resources/database/migrations.xml --url=$MIGRATION_DATABASE_URL --username=$MIGRATION_DATABASE_USER --password=$MIGRATION_DATABASE_PASSWORD update
