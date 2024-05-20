FROM eclipse-temurin
WORKDIR /usr/src/myapp
RUN mkdir "data"
COPY TemperatureSensor.java .
RUN javac TemperatureSensor.java
CMD ["java","TemperatureSensor"]
