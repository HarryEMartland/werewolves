FROM openjdk:8u131-jre-alpine

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/werewolves/werewolves.jar"]

ADD target/werewolves-*.*.*.jar /usr/share/werewolves/werewolves.jar