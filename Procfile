web: java $JAVA_OPTS -Ddw.server.applicationConnectors[0].port=$PORT -cp target/classes:target/dependency/* com.garrettestrin.PrivateGram.app.Main server app.yml
release: java -jar target/dependency/liquibase.jar --changeLogFile=src/main/resources/database/migrations.xml --url=$DW_DATABASE_URL --username=$DW_DATABASE_USER --password=$DW_DATABASE_PASSWORD update
