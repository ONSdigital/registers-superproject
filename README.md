# sbr superproject

The purpose of this superproject is to simplify the process of working with
the sbr projects.  The scripts provided take care of cloning the various git repos,
running the appropriate build command against each and running the apps using Docker 
compose.

## Prerequisites

You'll need the following tools installed:

 * git
 * scala
 * sbt
 * docker

## Build

Simply run

```bash
./build.scala
```

## Run

A simple wrapper for `docker-compose` has been provided:

```bash
./run.sh
```

---
---


### Misc

To remove all stopped containers, run:

```bash
docker container prune
```

To remove exited containers, run:

```bash
docker rm $(docker ps -a -f status=exited -q)
```

If you ever need to reset your whole Docker environment you can simply run:

```bash
docker system prune -a
```
