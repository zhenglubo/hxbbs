package com.lixin.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

/**
 * @description: socket客户
 * @author: zhenglubo
 * @create: 2019-10-24 15:32
 **/

@Data
@AllArgsConstructor
public class Client {

    private static final long serialVersionUID = 8957107006902627635L;

    private Session session;
    private String username;
}
