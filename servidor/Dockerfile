FROM openjdk:24-oracle

WORKDIR /app

COPY src src

RUN javac -sourcepath src -d out src/main/Server.java

EXPOSE 3001

CMD ["java", "-cp", "out", "main.Server"]
