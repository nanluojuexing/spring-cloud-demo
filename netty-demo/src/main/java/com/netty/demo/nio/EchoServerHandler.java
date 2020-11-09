package com.netty.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class EchoServerHandler implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverChannel;

    private boolean stop;

    public EchoServerHandler(int port) {
        try {
            // 获得选择器，多路复用器
            this.selector = Selector.open();
            //获得一个serverChannel
            serverChannel = ServerSocketChannel.open();
            //设置为非阻塞模式 如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
            serverChannel.configureBlocking(false);
            // 绑定端口号和制定队列
            serverChannel.socket().bind(new InetSocketAddress(port),1024);
            // 把 选择器注册到channel上，关注链接事件
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 优雅停机
     */
    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {

        try {
            while (!stop){
                // 选择器每隔1s唤醒一次
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()){
                    selectionKey = iterator.next();
                    iterator.remove();
                    try {
                        // 处理事件
                        handleInput(selectionKey);
                    } catch (IOException e) {
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 连接事件
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 通过ServerSocketChannel的accept创建SocketChannel实例
                // 完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.write(ByteBuffer.wrap("服务端连接成功，向客户端发送消息".getBytes()));
                //连接建立后关注读事件
                sc.register(selector, SelectionKey.OP_READ);
            }
            // 读事件
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuff = ByteBuffer.allocate(1024);
                //非阻塞的
                // 读取请求码流，返回读取到的字节数
                int read = sc.read(readBuff);
                if (read > 0) {
                    // 将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    readBuff.flip();
                    // 将缓冲区可读字节数组复制到新建的数组中
                    byte[] bytes = new byte[readBuff.remaining()];
                    readBuff.get(bytes);
                    String body = new String(bytes, "utf-8");
                    System.out.println("服务收到消息：" + body);
                    String currentTime = String.valueOf(new Date(System.currentTimeMillis()).getTime());
                    doWrite(sc, currentTime);
                } else if (read < 0) {
                    key.cancel();
                    sc.close();
                } else {
                }
            }
        }
    }

    /**
     * 异步发送应答消息
     * @param sc
     * @param content
     * @throws IOException
     */
    private void doWrite(SocketChannel sc, String content) throws IOException {
        if (content != null && content.trim().length() > 0) {
            byte[] bytes = content.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            sc.write(byteBuffer);
            System.out.println("res end");
        }
    }
}
