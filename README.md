# CSD1
D-KSVS

# csd-tp1-DSKVS
Repository for the CSD's first work.

# How to Launch

## 1: Install Redis

`$ sudo docker pull redis` 

## 2: Run redis Servers

 $ docker run --name redisX -d redis
  onde "redisX" é o nome do servidor, à sua escolha.

Ver o IP que o servidor redis está ligado
 $ docker  inspect redisX

Stop containers do docker
 $ docker stop $(docker ps -a -q)


Remover os containers do docker
 $ docker rm $(docker ps -a -q)


## 3: Run BFT-SMaRt replicas

On your IDE import the GitHub project, then run TreeMapServer class with following argumentes:
"id of the bft-smart replica" , "IP from Redis Server"
For example: 3 172.17.0.2

## 4: Run API Server
 Just run the class named "Server"
 It will connect to an instance of TreeMapServer.
 
