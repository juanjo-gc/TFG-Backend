FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN apk update && apk add dos2unix && dos2unix mvnw
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM eclipse-temurin:17-jdk-alpine as finalApp
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# COPY --from=build /workspace/app/target/*.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java","-cp","app:app/lib/*","es.uca.tfg.backend.BackendApplication"]