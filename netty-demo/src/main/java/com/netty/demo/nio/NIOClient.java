package com.netty.demo.nio;

import java.util.Scanner;

/**
 * nio client
 */
public class NIOClient {

    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 8081;
    private static EchoClientHandler clientHandler;

    public static void start(){
        start(DEFAULT_HOST,DEFAULT_PORT);
    }

    public static synchronized void start(String ip,int port){
        clientHandler = new EchoClientHandler(ip,port);
        new Thread(clientHandler,"Server").start();
    }

    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception{
        clientHandler.sendMsg(msg);
        return true;
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        // 运行客户端
        NIOClient.start();
        try {
            while (NIOClient.sendMsg(new Scanner(System.in).nextLine()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}