FROM openjdk:8-jre
ADD target/trimigi-agent-sql-${project.version}.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar