# CSD1
Agora é que vai. 

# csd-tp1-DSKVS
Repository for the CSD's first work.

# How to Launch
## Optional

On directory:

`$ mvn install`

## 1: Install Redis

`$ sudo apt-get update` 

`$ sudo apt-get install redis-server`

## 2: Make config files to Redis

On `~/.config/` make two files -- respectively to a client and to a server.

For example:

`bind 127.0.1.1`

`port 11000`

## 3: Launch two instances of Redis

Launch two instances like this:

`$ redis-server ~/.config/CONFIGFORREDIS.config`

## 4: On your IDE: 

Run 4 TreeMapServer's and 1 ConsoleClient -- Just launch. You should not have to worry about the configuration of hosts and ports.

## 5: All set:

Now you can do operations on Client.

The logs are wise on server-side.

## TODO:
`REST, SSL, addElement, writeElement, isElement, readElement`

`Dockerfile exists and it's not a mirage...`

`To build/run:`

`Build: $ docker build -t NAMEOFIMAGE . -- you should be on the main module of the project, where the Dockerfile should be`

`Run [BETA]: $ docker run --rm -v $PWD:/app -w /app/src/main/java/gettingstarted/server/ NAMEOFPROJECT javac TreeMapServer.java`

