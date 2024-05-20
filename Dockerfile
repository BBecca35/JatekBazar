FROM openjdk:17.0-jdk-slim-bullseye
COPY "./target/JatekBazar.jar" "/application/JatekBazar.jar"
EXPOSE 8082
CMD [ "java", "-jar", "/application/JatekBazar.jar" ]