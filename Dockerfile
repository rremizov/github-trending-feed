FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/github-trending-feed.jar /github-trending-feed/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/github-trending-feed/app.jar"]
