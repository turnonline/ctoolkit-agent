FROM openjdk:8-jre
ADD target/ctoolkit-agent-mongo-${project.version}.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar