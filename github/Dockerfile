FROM openjdk:18
ARG JAR_FILE=github/build/libs/*.jar
ARG DB_URL
ENV DB_URL=${DB_URL}
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]

