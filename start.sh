#!/bin/bash

shome=`dirname $0`
cd "$shome/target"
java -jar ./safh-0.0.1-SNAPSHOT.war --spring.profiles.active=prod,no-liquibase &
