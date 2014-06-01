#!/bin/bash
mvn install:install-file -Dfile=lib/nanohttpd-2.1.0.jar -DgroupId=com.victor -DartifactId=nanohttpd -Dversion=2.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib/nanohttpd-webserver-2.1.0-jar-with-dependencies.jar -DgroupId=com.victor -DartifactId=nanohttpd-webserver -Dversion=2.1.0 -Dpackaging=jar
mvn install:install-file -DgroupId=javax.jdo -DartifactId=jdo2-api -Dversion=2.3-ec -Dpackaging=jar -Dfile=lib/jdo2-api-2.3-ec.jar
