#!/bin/bash

shome=`dirname $0`
java -jar "$shome/target/safh-0.0.1-SNAPSHOT.war --spring.profiles.active=prod" &
