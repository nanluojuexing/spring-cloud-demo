package com.netty.demo.nio;

import java.io.IOException;

/**
 * nio 模型 server端
 */
public class NIOServer {

    /**
     * 设置监听的端口号
     */
    private static int DEFAULT_PORT = 8081;

    private static EchoServerHandler echoServerHandler;

    public static void start(){
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port) {
        if (echoServerHandler != null) {
            echoServerHandler.stop();
        }
        echoServerHandler = new EchoServerHandler(port);
        new Thread(echoServerHandler, "Server").start();
    }

    public static void main(String[] args) throws IOException {
        // 运行服务器
        NIOServer.start();
    }
}
