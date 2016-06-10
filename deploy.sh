#!/bin/bash
mvn clean install
mvn package -Pprod -DskipTests
scp -i /home/rogerio/ssh/keyRoger.pem ./target/safh-0.0.2-SNAPSHOT.war ubuntu@52.67.61.182:/home/ubuntu
