FROM openjdk:8-jre
ADD target/trimigi-agent-elasticsearch-${project.version}.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar