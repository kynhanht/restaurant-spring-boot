FROM java:8
ADD ./target/restaurant-1.0.jar restaurant-1.0.jar
ADD ./wait/wait-for-it.sh /wait-for-it.sh
RUN chmod 755 /wait-for-it.sh

EXPOSE 8080
ENTRYPOINT ["./wait-for-it.sh", "mysql-db:3306", "-t", "300", "--", "java", "-jar", "restaurant-1.0.jar"]
