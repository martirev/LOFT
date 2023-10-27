FROM maven:3.8.1-openjdk-17-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
        openjfx \
    && rm -rf /var/lib/apt/lists/*
