FROM openjdk:11-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./api/target/bikeshare-rental-api-1.0.0-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
