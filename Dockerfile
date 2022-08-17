FROM bellsoft/liberica-openjdk-centos

COPY target/volna-client-bot.jar volna-client-bot.jar
COPY app.properties app.properties

ENTRYPOINT ["java", "-jar", "volna-client-bot.jar"]
