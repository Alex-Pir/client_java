package gui.autent.controller;

import gui.SendNewPass;
import gui.admin.Admin;
import gui.autent.model.UserDataModel;
import interfaces.ClientToServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import client.Client;

public class EnterButtonListener implements ActionListener {

	private static final String STATUS_U = "U";
	private static final String STATUS_A = "A";
	private static final String R_COMMAND = "/r/";
	private static final int MANY_TIME = -1;
	private static final int SHIFT_IN_COMMAND = 3;
	private static final int MAX_CONNECTION_VALUE = 3;
	
	private final BufferedReader reader;
	private final PrintWriter writer;
	private final JButton enterButton;
	private String login;
	private String password;
	private int max_count;
	
	public EnterButtonListener(final JButton enterButton, final BufferedReader reader, final PrintWriter writer)
	{
		this.enterButton = enterButton;
		this.reader = reader;
		this.writer = writer;
		this.max_count = 0;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if (tryAut())
		{
			UserDataModel.getUpdate();
		
			this.login = UserDataModel.getLogin();
			this.password = UserDataModel.getPassword();
			this.writer.println(ClientToServer.AUTHORIZATION);
		
			sendUserData(this.login, this.password);
			try 
			{
				String response = reader.readLine();
				final String user_status = successAut(response);
				if (user_status.equals(STATUS_U) || user_status.equals(STATUS_A))
				{
					final long time = getPasswordTime();
					chooseMenu(time, user_status);
				}
				else
				{
					JOptionPane.showMessageDialog(UserDataModel.getWindow(), "Вы ввели неверные данные!\nОсталось попыток: " + (MAX_CONNECTION_VALUE - (++max_count)), "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
			} 
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
		else
		{
			this.enterButton.setEnabled(false);
		}
	
	}
	
	
	
	private void sendUserData(final String login, final String password)
	{
		this.writer.println(login);
		this.writer.println(password);
	}
	
	private String successAut(final String response)
	{
		final String status = getCommandWithoutR(response);
		return status;
	}
	
	private long getPasswordTime() throws IOException
	{
		this.writer.println(ClientToServer.TIME);
		final String response = this.reader.readLine();
		final String response_time = getCommandWithoutR(response);
		final long time = Long.parseLong(response_time);
		
		return time;
	}
	
	private void chooseMenu(final long time, final String user_status) throws IOException
	{
		
		if (time != MANY_TIME)
		{
			if (time == 0)
			{
				UserDataModel.getWindow().setVisible(false);
				new SendNewPass(this.reader, this.writer);
			}
			else
			{
				passwordChangeChoose(time, user_status);
			}
		}
		else if (time == MANY_TIME)
		{
			if (user_status.equals(STATUS_A))
			{
				UserDataModel.getWindow().setVisible(false);
				new Admin(this.reader, this.writer);
			}
			else
			{
				JOptionPane.showMessageDialog(UserDataModel.getWindow(), "Вы вошли как пользователь!");
				exit();
			}
		}
	}
	
	private void passwordChangeChoose(long time, String user_status) throws IOException
	{
		final int reply = JOptionPane.showConfirmDialog(UserDataModel.getWindow(), String.format(
				"До окончания действия Вашего пароля осталось %d дней.\nЖелаете сменить пароль сейчас?", time),
				"Срок действия пароля истекает",
				JOptionPane.YES_NO_OPTION);
		
		if (reply == JOptionPane.YES_OPTION)
		{
			UserDataModel.getWindow().setVisible(false);
			new SendNewPass(this.reader, this.writer);
		}
		else if(reply == JOptionPane.NO_OPTION)
		{
			if (user_status.equals(STATUS_A))
			{
				UserDataModel.getWindow().setVisible(false);
				new Admin(this.reader, this.writer);
			}
			else
			{
				JOptionPane.showMessageDialog(UserDataModel.getWindow(), "Вы вошли как пользователь!");
				exit();
			}
		}
	}
	
	private String getCommandWithoutR(String command)
	{
		final int index_r = command.indexOf(R_COMMAND);
		String result = "";
		
		if (index_r != -1)
		{
			result = command.substring(SHIFT_IN_COMMAND);
		}
		
		return result;
	}
	
	private boolean tryAut()
	{
		if (max_count < MAX_CONNECTION_VALUE)
		{
			return true;
		}
		
		return false;
	}
	
	private void exit() throws IOException
	{
		this.writer.println(ClientToServer.BUE);
		this.writer.close();
		this.reader.close();
		Client.getSocket().close();
		System.exit(0);
	}
	
}
