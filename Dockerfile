FROM openjdk:8-jre-alpine

WORKDIR /app

RUN apk --no-cache add --update \
      coreutils \
      tzdata

EXPOSE 9000

COPY ./target/scala-2.13/btlr.jar /app/

CMD ["java",  "-jar" , "/app/btlr.jar"]
