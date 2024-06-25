# Utilisez l'image OpenJDK basée sur Alpine
FROM alpine/java:22-jdk

# Copiez le JAR généré dans le conteneur
COPY target/bankaccount-0.0.1-SNAPSHOT.jar /bankaccount.jar

# Définissez le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/bankaccount.jar"]