package com.netty.demo.nbio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;

public class TimeServerHandleTask implements Runnable {

    SocketChannel socketChannel;
    public TimeServerHandleTask(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    @Override
    public void run() {
        try {
            ByteBuffer requestBuffer = ByteBuffer.allocate("GET CURRENT TIME".length());
            //尝试读取数据，因为是非阻塞，所以如果没有数据会立即返回。
            int bytesRead = socketChannel.read(requestBuffer);
            //如果没有读取到数据，说明当前SocketChannel并没有发送请求，不需要处理
            if (bytesRead <= 0) {
                return;
            }
            //如果读取到了数据，则需要考虑粘包、解包问题，这个while代码是为了读取一个完整的请求信息"GET CURRENT TIME"，
            while (requestBuffer.hasRemaining()) {
                socketChannel.read(requestBuffer);
            }
            String requestStr = new String(requestBuffer.array());
            if (!"GET CURRENT TIME".equals(requestStr)) {
                String bad_request = "BAD_REQUEST";
                ByteBuffer responseBuffer = ByteBuffer.allocate(bad_request.length());
                responseBuffer.put(bad_request.getBytes());
                responseBuffer.flip();
                socketChannel.write(responseBuffer);
            } else {
                String timeStr = Calendar.getInstance().getTime().toLocaleString();
                ByteBuffer responseBuffer = ByteBuffer.allocate(timeStr.length());
                responseBuffer.put(timeStr.getBytes());
                responseBuffer.flip();
                socketChannel.write(responseBuffer);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
