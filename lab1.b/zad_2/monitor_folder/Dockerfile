FROM eclipse-temurin
WORKDIR /usr/src/myapp
RUN mkdir "data"
COPY TemperatureMonitor.java .
RUN javac TemperatureMonitor.java
CMD ["java","TemperatureMonitor"]
