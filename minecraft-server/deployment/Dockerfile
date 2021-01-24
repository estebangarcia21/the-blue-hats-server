FROM gradle:6.8.0-jdk8 AS builder

COPY --chown=gradle:gradle .. /server/src

WORKDIR /server/src
RUN gradle shadowJar --no-daemon

FROM openjdk:8-jre-slim

WORKDIR /server/runtime
EXPOSE 25565

RUN ./build.sh
RUN mkdir

COPY --from=builder /server/src/build/libs/*.jar /server/runtime/the-blue-hats-server-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "-Xms2g", "-Xmx2g", "-XX:+UseG1GC", "spigot-1.8.8.jar", "nogui"]
