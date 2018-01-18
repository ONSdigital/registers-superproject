# sbr superproject

The purpose of this superproject is to simplify the process of working with
the sbr projects.  Gitslave is used to aggregate git commands
across the repositories while Docker compose is employed to run the services.

## Populate

## Build

## Run

To remove all stopped containers, run:

```bash
docker container prune
```

To remove exited containers, run:

```bash
docker rm $(docker ps -a -f status=exited -q)
```

If you ever need to reset your Docker environment you can simply run:

```bash
docker system prune -a
```
