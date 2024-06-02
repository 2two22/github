FROM openjdk:18
ARG JAR_FILE=*.jar
ARG DB_URL
ENV DB_URL=jdbc:mysql://twotwo-db.c7iaym6c0pb0.ap-northeast-2.rds.amazonaws.com:3306/github
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","app.jar"]

