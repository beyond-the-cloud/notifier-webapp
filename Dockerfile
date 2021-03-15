FROM maven:3.6.3-jdk-8 as builder
WORKDIR /usr/home/app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package

FROM gcr.io/distroless/java:8
COPY --from=builder /usr/home/app/target/notifier.jar notifier.jar
EXPOSE 8080
CMD ["notifier.jar"]