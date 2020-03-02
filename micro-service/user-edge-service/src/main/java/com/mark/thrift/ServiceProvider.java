package com.mark.thrift;

import com.mark.enums.ServiceType;
import com.mark.thrift.message.MessageService;
import com.mark.thrift.user.UserService;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceProvider {

    @Value("${thrift.user.ip}")
    private String serverIP;
    @Value("${thrift.user.port}")
    private int serverPort;

    @Value("${thrift.message.ip}")
    private String messageServerIP;
    @Value("${thrift.message.port}")
    private int messageServerPort;

    public UserService.Client getUserService() {
        return (UserService.Client) getService(serverIP, serverPort, ServiceType.USER);
    }

    public MessageService.Client getMessageService() {
        return (MessageService.Client) getService(messageServerIP, messageServerPort, ServiceType.MESSAGE);
    }

    public <T> T getService(String ip, int port, ServiceType serviceType) {
        TSocket socket = new TSocket(ip, port, 3000);
        TTransport transport = new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            return null;
        }

        TProtocol protocol = new TBinaryProtocol(transport);

        TServiceClient client = null;
        switch (serviceType) {
            case USER:
                client = new UserService.Client(protocol);
                break;
            case MESSAGE:
                client = new MessageService.Client(protocol);
                break;
        }

        return (T) client;
    }
}
