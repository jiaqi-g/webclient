## Why we need a new "webclient"?

*Webclient* is actually a simple web server used for connecting the backend programs.
Initially it is served for big-data based systems running in research labs, but it is
modified so that it can be built as a web service module for any programs.

## Why not Apache Tomcat?

Tomcat is such a heavy-weighted server, which we do is to build a really simple web server
with majority of the functionalities of Apache Tomcat. Users won't need to be suffered from
huge amounts of configuration files and they can easily modify it to satisfy their own needs. 

## Main Components

It wraps the *NanoHttpd* which is a very simple java based server, and provides basic functionalities such as Parameter parsing, Json Data manipulation,
JDBC database connection and logs.

## Applications

It can be used for building personal website, course-based website and project website.

## Run steps

1. modify "config" file to reflect actual settings of needed paths
2. run "run.sh" to compile/package/start server

Thanks for all of your feedbacks!

author: victor 
