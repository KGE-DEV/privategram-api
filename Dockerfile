FROM java:8  
COPY . /target/sample-0.1-SNAPSHOT.jar
WORKDIR ./
#RUN javac sample-0.1-SNAPSHOT.jar
CMD ["java -cp target/PrivateGram-0.1-SNAPSHOT.jar com.garrettestrin.PrivateGram.app.Main"]