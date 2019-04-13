FROM openjdk:11

WORKDIR /app

ADD target/serviciopps-2.1-BETA.jar /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar"]
CMD ["serviciopps-2.1-BETA.jar"]