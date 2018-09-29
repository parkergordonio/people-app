FROM openjdk:8-jre
COPY svc /svc
EXPOSE 9000
CMD /svc/bin/start -Dplay.crypto.secret=secret -Dpidfile.path=/var/run/play.pid