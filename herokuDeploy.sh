#!/bin/bash
mvn clean install
heroku login
yo jhipster:heroku
heroku deploy:jar --jar target/*.war --app safh


