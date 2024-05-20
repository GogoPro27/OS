FROM eclipse-temurin

WORKDIR /usr/src/myapp

RUN mkdir data

COPY SoundLevelSensor.java .

RUN javac SoundLevelSensor.java

ENTRYPOINT ["java","SoundLevelSensor"]
