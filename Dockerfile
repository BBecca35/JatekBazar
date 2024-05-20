FROM openjdk:17-jdk-alpine3.14
COPY "./target/JatekBazar.jar" "JatekBazar.jar"
EXPOSE 8080
CMD [ "java", "-jar", "JatekBazar.jar" ]