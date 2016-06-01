#!/bin/bash

ssh -i /home/rogerio/ssh/keyRoger.pem ubuntu@52.67.61.182
java -jar ./safh-0.0.1-SNAPSHOT.war --spring.profiles.active=prod &
