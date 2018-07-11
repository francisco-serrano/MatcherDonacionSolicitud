FROM openjdk:8
RUN mkdir -p /opt /servicio-pps
ADD target/correctorortografico-1.0-SNAPSHOT.jar /opt/servicio-pps
EXPOSE 8080
EXPOSE 8081
CMD ["java", "-jar", "/opt/packt/servicio-pps/correctorortografico-1.0-SNAPSHOT.jar"]