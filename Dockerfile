# ============================================================
#  MediCare - Medical Store Management System
#  Recipe that tells the cloud (Render) how to build and run
#  this project. You never run this file yourself.
#
#  It is done in two stages so the final image stays small:
#    stage 1 (build) - has Maven + full JDK, builds the jar
#    stage 2 (run)   - only Java, just runs the finished jar
# ============================================================


# ---------- STAGE 1 : build the jar ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# The backend needs the website folder too, because the build
# copies MediCareWebsite into the jar (see pom.xml).
COPY MedicareBackend/pom.xml MedicareBackend/pom.xml
COPY MedicareBackend/src     MedicareBackend/src
COPY MediCareWebsite         MediCareWebsite

WORKDIR /app/MedicareBackend

# -DskipTests: the tests need a database, which is not available
# while building. The app itself is still compiled normally.
RUN mvn -B -DskipTests clean package


# ---------- STAGE 2 : run the jar ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Take only the finished jar from stage 1
COPY --from=build /app/MedicareBackend/target/*.jar app.jar

# Render tells the app which port to use through the PORT
# variable; application.properties reads it.
EXPOSE 8080

# -XX:MaxRAMPercentage keeps Java inside the small amount of
# memory a free cloud machine gives us.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
