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

	private SocketChannel client;
	private CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

	public CommandSenderWrapper(SocketChannel client) {
		this.client = client;
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
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getName() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isPermissionSet(String s) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isPermissionSet(Permission permission) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean hasPermission(String s) {
		return true;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int i) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void removeAttachment(PermissionAttachment permissionAttachment) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void recalculatePermissions() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isOp() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setOp(boolean b) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
