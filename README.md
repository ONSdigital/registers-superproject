# registers superproject

The purpose of this superproject is to simplify the process of working with
the registers projects.  The scripts provided take care of cloning the various git repos,
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

## Play

To tryout the applications (and ensure they're running) you can hit the following URLs:

| App                 | URL(s)                                                                                                    | Notes                                 |
| ------------------- | ----------------------------------                                                                        | ------------------------------------- |
| sbr-ui              | http://localhost:3001                                                                                     | Login with admin/admin                |
| sbr-api             | http://localhost:9002/swagger.json                                                                        | Swagger doc for API endpoints         |
| sbr-control-api     | http://localhost:9002/swagger.json                                                                        | Swagger doc for API endpoints         |
| sbr-ch-data         | http://localhost:9003/swagger.json                                                                        | Swagger doc for API endpoints         |
| sbr-vat-data        | http://localhost:9004/swagger.json                                                                        | Swagger doc for API endpoints         |
| sbr-paye-data       | http://localhost:9005/swagger.json                                                                        | Swagger doc for API endpoints         |
| hbase               | http://localhost:16010/zk.jsp <br/> http://localhost:8085/rest.jsp <br/> http://localhost:9095/thrift.jsp | Zookeeper, Rest and Thrift endpoints  |

---
---


### Misc


#### Banner

The banner printed from the commands of this superproject was generated at 
http://patorjk.com/software/taag/#p=display&f=Big&t=sbr%20superproject

#### Docker cleanup commands

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
