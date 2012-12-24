package com.fapiko.shellcraft;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ljandrew
 * Date: 12/24/12
 * Time: 4:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommandSenderWrapper implements CommandSender {

	private static Logger logger = Logger.getLogger(CommandSenderWrapper.class.getName());

	private Server bukkitServer;
	private SocketChannel client;
	private CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

	public CommandSenderWrapper(SocketChannel client, Server bukkitServer) {
		this.client = client;
		this.bukkitServer = bukkitServer;
	}

	@Override
	public void sendMessage(String s) {
		try {
			client.write(encoder.encode(CharBuffer.wrap(s + "\n")));
		} catch (CharacterCodingException cce) {
			logger.log(Level.SEVERE, "", cce);
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, "", ioe);
		}
	}

	@Override
	public void sendMessage(String[] strings) {
		for (String message : strings) {
			sendMessage(message);
		}
	}

	@Override
	public Server getServer() {
		return bukkitServer;
	}

	@Override
	public String getName() {
		return "console";
	}

	@Override
	public boolean isPermissionSet(String s) {
		return true;
	}

	@Override
	public boolean isPermissionSet(Permission permission) {
		return true;
	}

	@Override
	public boolean hasPermission(String s) {
		return true;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return true;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int i) {
		return null;
	}

	@Override
	public void removeAttachment(PermissionAttachment permissionAttachment) {

	}

	@Override
	public void recalculatePermissions() {

	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return null;
	}

	@Override
	public boolean isOp() {
		return true;
	}

	@Override
	public void setOp(boolean b) {

	}
}
