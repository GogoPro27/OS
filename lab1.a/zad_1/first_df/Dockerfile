FROM debian:11-slim

RUN apt-get update

RUN apt-get install -y nginx

ENTRYPOINT ["nginx", "-g", "daemon off;"]
