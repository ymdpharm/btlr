FROM openjdk:8-jre-slim
# alpine doesnot work https://github.com/googleapis/google-cloud-java/issues/2740

WORKDIR /app

RUN apt-get update && apt-get install -y \
      coreutils \
      tzdata

EXPOSE 9000

COPY ./target/scala-2.13/btlr.jar /app/

CMD ["java",  "-jar" , "/app/btlr.jar"]
