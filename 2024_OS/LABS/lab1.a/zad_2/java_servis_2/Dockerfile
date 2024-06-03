FROM eclipse-temurin

WORKDIR /usr/src/myapp

RUN mkdir data

COPY SoundLevelMonitor.java .

RUN javac SoundLevelMonitor.java

ENTRYPOINT ["java","SoundLevelMonitor"] 
