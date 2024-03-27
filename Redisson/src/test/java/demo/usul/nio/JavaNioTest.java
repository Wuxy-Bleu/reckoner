package demo.usul.nio;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

class JavaNioTest {

    @Test
    void testJavaNio() throws IOException {
        SelectorProvider provider = SelectorProvider.provider();
        AbstractSelector abstractSelector = provider.openSelector();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(19999));
        serverSocketChannel.configureBlocking(false);
    }
}
