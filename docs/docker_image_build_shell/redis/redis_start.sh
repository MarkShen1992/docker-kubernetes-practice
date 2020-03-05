docker stop redis
docker rm redis
docker run -idt -p 6379:6379 -v `pwd`/data:/data --name redis -v `pwd`/redis.conf:/etc/redis/redis.conf redis
