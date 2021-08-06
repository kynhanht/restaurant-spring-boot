FROM openjdk:8
ADD target/restaurant.jar restaurant.jar
EXPOSE 8080
ENTRYPOINT ["java", "jar", "restaurant.jar"]