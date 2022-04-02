# Dockerfile from https://hub.docker.com/_/clojure
# TODO pin version
FROM clojure
# FROM clojure:1.10.3

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app

RUN lein with-profile cli bin
RUN mv "$(lein with-profile api ring uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" server.jar

CMD ["java", "-jar", "server.jar"]
