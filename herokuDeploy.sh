#!/bin/bash
mvn package -Pprod -DskipTests
heroku login
yo jhipster:heroku
heroku deploy:jar --jar target/*.war --app safh


