FROM eclipse-temurin:23 AS build
WORKDIR /opt
RUN apt-get update && apt-get install -y curl
RUN curl -fL https://github.com/Virtuslab/scala-cli/releases/latest/download/scala-cli-x86_64-pc-linux.gz | gzip -d > scala-cli && chmod +x scala-cli
COPY src src
RUN ./scala-cli --power package src -o server --assembly


FROM eclipse-temurin:23
WORKDIR /opt
COPY site site
COPY --from=build /opt/server server
EXPOSE 9000

CMD ["./server"]