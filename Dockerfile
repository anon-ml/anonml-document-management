FROM ubuntu:17.10

RUN apt-get update
RUN apt-get install -y openjdk-8-jre
RUN apt-get install -y dirmngr
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6
RUN echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.4.list
RUN apt-get update
RUN mkdir -p /data/db
RUN apt-get install -y mongodb-org
RUN sudo apt-get install -y poppler-utils

COPY start.sh /
ADD ./target/docmgmt-0.0.2-SNAPSHOT.jar /

EXPOSE 27017
EXPOSE 9001

ENTRYPOINT /start.sh