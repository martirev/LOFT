# THIS DOCKERFILE IS USED TO CREATE THE IMAGE FOR GITLAB CI/CD
# READ ABOUT IT IN docs/release3/README.md

FROM maven:3.8.1-openjdk-17-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
        openjfx \
    && rm -rf /var/lib/apt/lists/*