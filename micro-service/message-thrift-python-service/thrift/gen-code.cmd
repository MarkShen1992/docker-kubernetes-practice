REM generate the python code
thrift --gen py -out ../ message.thrift

thrift-0.13.0.exe --gen java -out ../../message-thrift-service-api/src/main/java message.thrift