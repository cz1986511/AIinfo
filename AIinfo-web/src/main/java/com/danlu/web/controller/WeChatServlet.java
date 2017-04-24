package com.danlu.web.controller;

import java.io.IOException;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/websocket")
public class WeChatServlet {

    private static Logger logger = LoggerFactory.getLogger(WeChatServlet.class);
    private static Vector<Session> room = new Vector<Session>();

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        if (!StringUtils.isBlank(message)) {
            logger.info("Received: 大虾" + session.getId() + "号:" + message);
            String msg = "";
            for (Session se : room) {
                msg = "大虾" + se.getId() + "号:" + message;
                if (se.equals(session)) {
                    msg = "Me:" + message;
                }
                se.getAsyncRemote().sendText(msg);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        room.add(session);
        logger.info("Client connected sessionId:" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        room.remove(session);
        logger.info("Connection closed sessionId:" + session.getId());
    }

}
