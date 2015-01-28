#!/bin/bash
#mvn assembly:assembly -DdescriptorId=jar-with-dependencies -Dmaven.test.skip=true
mvn package -Dmaven.test.skip=true
