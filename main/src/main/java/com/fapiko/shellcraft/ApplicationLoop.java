package com.fapiko.shellcraft;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

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

	private static ApplicationLoop instance;
	private static Logger logger = Logger.getLogger(ApplicationLoop.class.getName());

	private ServerSocketChannel serverSocketChannel;
	private ShellCraft parent;
	private ArrayList<SocketChannel> socketChannels = new ArrayList<SocketChannel>();

	private boolean running = false;
	private boolean stopApplicationLoop = false;

	public ApplicationLoop() {

	}

	public static ApplicationLoop getInstance(ShellCraft parent) {

		if (instance == null) {
			instance = new ApplicationLoop();
			instance.parent = parent;
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

						buffer.flip();
						Charset charset = Charset.forName("UTF-8");
						CharsetDecoder decoder = charset.newDecoder();
						CharBuffer charBuffer = decoder.decode(buffer);

						String command = charBuffer.toString().trim();
						logger.info(command);
						// Detect client disconnection
						if (charBuffer.length() == 0) {
							logger.info("Client disconnected");
							key.channel().close();
						} else {
							parent.getServer().dispatchCommand(new CommandSenderWrapper(client), command);
						}

					}

				}
			}

			sleep(10);

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
