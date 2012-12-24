package com.fapiko.shellcraft;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationLoop extends Thread {

	public static ApplicationLoop instance;
	private static Logger logger = Logger.getLogger(ApplicationLoop.class.getName());

	private ServerSocketChannel serverSocketChannel;
	private ArrayList<SocketChannel> socketChannels = new ArrayList<SocketChannel>();

	private boolean running = false;
	private boolean stopApplicationLoop = false;

	public ApplicationLoop() {

	}

	public static ApplicationLoop getInstance() {

		if (instance == null) {
			instance = new ApplicationLoop();
		}

		return instance;

	}

	@Override
	public void run() {

		try {
			applicationLoop();
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, "", ioe);
		} catch (InterruptedException ie) {
			logger.log(Level.SEVERE, "", ie);
		}

	}

	public void applicationLoop() throws IOException, InterruptedException {

		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(5060));
		serverSocketChannel.configureBlocking(false);

		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		do {

			int readyChannels = selector.selectNow();

			if (readyChannels > 0) {

				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				while(keyIterator.hasNext()) {

					SelectionKey key = keyIterator.next();
					keyIterator.remove();

					if (stopApplicationLoop) {
						key.channel().close();
						continue;
					}

					if (key.isAcceptable()) {

						SocketChannel client = serverSocketChannel.accept();
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ);
						continue;

					}

					if (key.isReadable()) {

						SocketChannel client = (SocketChannel) key.channel();
						client.configureBlocking(false);
						int BUFFER_SIZE = 32;
						ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);


						client.read(buffer);

						Charset charset = Charset.forName("UTF-8");
						CharsetDecoder decoder = charset.newDecoder();
						CharBuffer charBuffer = decoder.decode(buffer);

						logger.info("Message Received: ");
						logger.info(charBuffer.toString());

					}

				}
			}

			sleep(100);

		} while (!stopApplicationLoop);

		logger.info("out");

		selector.close();
		serverSocketChannel.close();
		while (serverSocketChannel.isOpen()) {
			logger.info("closing serverSocket");
			sleep(10);
		}

		logger.info("closed");

		running = false;

	}

	public void setStopApplicationLoop(boolean stopApplicationLoop) {

		this.stopApplicationLoop = stopApplicationLoop;
		if (!stopApplicationLoop && !running) {
			running = true;
			start();
		}

	}

}
