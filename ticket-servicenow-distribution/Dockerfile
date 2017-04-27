#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#
#  USAGE: (from .. dir)
#    docker build -f ticket-servicenow-distribution/Dockerfile --tag=ticket-servicenow ticket-servicenow
#

# Start from base image with Java 8
FROM java:8-jre-alpine

RUN mkdir -p /opt/dell/cpsd/ticket-servicenow/bin
RUN mkdir -p /opt/dell/cpsd/ticket-servicenow/logs
RUN mkdir -p /opt/dell/cpsd/ticket-servicenow/lib
RUN mkdir -p /opt/dell/cpsd/ticket-servicenow/install

COPY target/dependency/lib/ticket-servicenow-*.jar /opt/dell/cpsd/ticket-servicenow/lib/ticket-servicenow.jar
COPY docker/image/bin/run.sh /opt/dell/cpsd/ticket-servicenow/bin/

RUN chmod 755 /opt/dell/cpsd/ticket-servicenow/bin/run.sh

EXPOSE 8080

CMD ["/opt/dell/cpsd/ticket-servicenow/bin/run.sh"]
