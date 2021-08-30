FROM maven:3-jdk-8-alpine

# Add your instructions to dockerize the application
# App must run on port 8000

COPY auth_api /auth_api

ADD build.sh /build.sh

RUN chmod +x /build.sh

RUN /build.sh

ENTRYPOINT ["java","-cp","/auth_api/target/auth-api-1.0-SNAPSHOT-jar-with-dependencies.jar", "com.wizeline.App"]

EXPOSE 8000
