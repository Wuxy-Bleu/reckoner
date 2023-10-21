FROM openjdk:17.0
EXPOSE 9099
ADD target/hello-1.0-SNAPSHOT.jar springboot-k8s-demo.jar
ENTRYPOINT ["java", "-jar", "/springboot-k8s-demo.jar"]