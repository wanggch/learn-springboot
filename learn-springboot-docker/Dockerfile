FROM openjdk:8
VOLUME /tmp
ADD target/learn-springboot-docker.jar /learn-springboot-docker.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/learn-springboot-docker.jar"]