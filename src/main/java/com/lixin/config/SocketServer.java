package com.lixin.config;

import com.lixin.socket.Client;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @description: websocket服务器
 * @author: zhenglubo
 * @create: 2019-10-23 16:50
 **/

@Component
@Slf4j
@ServerEndpoint("/socketserver/{username}")
public class SocketServer {

    private static CopyOnWriteArraySet<Client> socketServers = new CopyOnWriteArraySet<>();

    private Session session;

    @OnOpen
    public void webSocket(Session session, @RequestParam String username) {
        this.session = session;
        socketServers.add(new Client(session, username));
        log.info("【{}】：连接成功", username);
    }

    @OnMessage
    public void onMessage(String message, String[] users) {
        Client client = socketServers.stream().filter(cli -> cli.getSession() == this.session).collect(Collectors.toList()).get(0);
        for (String user : users) {
            socketServers.forEach(cli -> {
                if (cli.getUsername().equalsIgnoreCase(user)) {
                    try {
                        cli.getSession().getBasicRemote().sendText(message);
                        log.info("用户：{}，给：{}：发送消息：{},success", client.getUsername(), cli.getUsername(), message);
                    } catch (Exception e) {
                        log.error("用户：{}，给：{}：发送消息：{},fail", client.getUsername(), cli.getUsername(), message);
                    }
                }
            });
        }
    }

    @OnClose
    public void onClose() {
        socketServers.forEach(client -> {
            if (client.getSession().getId().equals(session.getId())) {

                log.info("客户端:【{}】断开连接", client.getUsername());
                socketServers.remove(client);
            }
        });
    }

    /**
     * 发生错误时触发
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        socketServers.forEach(client -> {
            if (client.getSession().getId().equals(session.getId())) {
                socketServers.remove(client);
                log.error("客户端:【{}】发生异常", client.getUsername());
                log.error(error.getMessage());
            }
        });
    }

    /**
     * 获取服务端当前客户端的连接数量，
     * 因为服务端本身也作为客户端接受信息，
     * 所以连接总数还要减去服务端
     * 本身的一个连接数
     * <p>
     * 这里运用三元运算符是因为客户端第一次在加载的时候
     * 客户端本身也没有进行连接，-1 就会出现总数为-1的情况，
     * 这里主要就是为了避免出现连接数为-1的情况
     *
     * @return
     */
    public synchronized int getOnlineNum() {
        return socketServers.stream().filter(client -> client.getSession() != session)
                .collect(Collectors.toList()).size();
    }

    /**
     * 获取在线用户名，前端界面需要用到
     *
     * @return
     */
    public synchronized List<String> getOnlineUsers() {

        return socketServers.stream()
                .filter(client -> client.getSession() != session)
                .map(Client::getUsername)
                .collect(Collectors.toList());
    }

    /**
     * 信息群发，我们要排除服务端自己不接收到推送信息
     * 所以我们在发送的时候将服务端排除掉
     *
     * @param message
     */
    public synchronized void sendAll(String message) {
        Client client1 = socketServers.stream().filter(client -> client.getSession() == this.session).collect(Collectors.toList()).get(0);
        //群发，不能发送给服务端自己
        socketServers.stream().filter(cli -> !cli.getUsername().equalsIgnoreCase(cli.getUsername()))
                .forEach(client -> {
                    try {
                        client.getSession().getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });

        log.info("服务端推送给所有客户端 :【{}】", message);
    }
}
