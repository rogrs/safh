#!/bin/bash

shome=`dirname $0`
cd "$shome/target"
java -jar ./safh-0.0.2-SNAPSHOT.war &
