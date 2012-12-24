package com.fapiko.shellcraft;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationLoop extends Thread {

	public static ApplicationLoop instance;
	private static Logger logger = Logger.getLogger(ApplicationLoop.class.getName());

	private ServerSocketChannel serverSocketChannel;

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
//		telnetSocket = new ServerSocket(2000);
//		telnetSocket.setSoTimeout(10);

		while (!stopApplicationLoop) {

			SocketChannel socketChannel = serverSocketChannel.accept();

			sleep(10);

//			logger.info("Pew pew");

		}
	}

	public void setStopApplicationLoop(boolean stopApplicationLoop) {

		this.stopApplicationLoop = stopApplicationLoop;
		if (!stopApplicationLoop && !running) {
			running = true;
			start();
		} else if (stopApplicationLoop && running) {
			running = false;
			try {
				serverSocketChannel.close();
			} catch (IOException ioe) {
				logger.log(Level.SEVERE, "", ioe);
			}

			while (serverSocketChannel.isOpen()) {
				try {
					sleep(10);
				} catch (InterruptedException ie) {
					logger.log(Level.SEVERE, "", ie);
				}
			}
		}

	}

}
