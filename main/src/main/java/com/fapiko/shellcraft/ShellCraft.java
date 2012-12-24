package com.fapiko.shellcraft;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class ShellCraft extends JavaPlugin {

	private static Logger logger = Logger.getLogger(ShellCraft.class.getName());
	private ApplicationLoop applicationLoop = ApplicationLoop.getInstance();

	private boolean running = false;

	@Override
	public void onEnable() {

		logger = getLogger();

		applicationLoop.setStopApplicationLoop(false);

		logger.info("ShellCraft: onEnable has been invoked");
	}

	@Override
	public void onDisable() {

		applicationLoop.setStopApplicationLoop(true);
		try {
			applicationLoop.join();
		} catch (InterruptedException ie) {
			logger.log(Level.SEVERE, "", ie);
		}

		logger.info("ShellCraft: onDisable has been invoked");

	}

}
