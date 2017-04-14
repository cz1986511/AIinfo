package com.danlu.web.controller;

import java.io.IOException;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;

@ServerEndpoint("/websocket")
public class WeChatServlet {

    private static Vector<Session> room = new Vector<Session>();

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        if (!StringUtils.isBlank(message)) {
            System.out.println("Received: 大虾" + session.getId() + "号:" + message);
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
        System.out.println("Client connected");
    }

    @OnClose
    public void onClose(Session session) {
        room.remove(session);
        System.out.println("Connection closed");
    }

}
