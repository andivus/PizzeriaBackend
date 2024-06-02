FROM gradle:jdk-21-and-22-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon -x test

FROM amazoncorretto:22-alpine3.19

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs /app/
COPY --from=build /home/gradle/src/build/resources /app/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/Pizzeria-all.jar","-config=/app/application.conf"]