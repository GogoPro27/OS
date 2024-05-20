FROM eclipse-temurin
WORKDIR /usr/src/myapp
RUN mkdir "data"
RUN mkdir "data/temperatures"
RUN mkdir "data/levels"
COPY TemperatureMonitor.java .
RUN javac TemperatureMonitor.java
CMD ["java","TemperatureMonitor"]
