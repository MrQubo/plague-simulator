FROM maven:3.5.4-jdk-10-slim as build
WORKDIR /app

ADD . /app

RUN mvn clean validate compile

CMD mvn exec:java


# build with:
#
#     docker build -t plague-simulator .
#
# run with:
#
#     docker run --name plague-simulator -it plague-simulator
#
# copy report with:
#
#     docker cp plague-simulator:/app/report_file.txt .
