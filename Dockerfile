FROM java:8-alpine

ADD target/uberjar/github-trending-feed.jar /github-trending-feed/app.jar
RUN addgroup -S app && adduser -S -D -H -G app app

EXPOSE 3000

USER app:app
CMD ["java", "-Xmx128m", "-Xss512k", "-XX:+CMSClassUnloadingEnabled", "-XX:+UseConcMarkSweepGC", "-jar", "/github-trending-feed/app.jar"]
