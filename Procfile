web: java $JAVA_OPTS -Ddw.server.applicationConnectors[0].port=$PORT -cp target/classes:target/dependency/* com.garrettestrin.PrivateGram.app.Main server app.yml
release: java -jar target/dependency/liquibase.jar --changeLogFile=src/main/resources/database/migrations.xml --url=${dw.database.url} --username=${dw.database.user} --password=${dw.database.password} update
