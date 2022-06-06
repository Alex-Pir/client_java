package gui.autent.model;

import interfaces.DataUpdate;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class UserDataModel {

	private static String login;
	private static String password;
	private static JFrame window;
	private static List<DataUpdate> datas = new ArrayList<>();
	
	public static void add(DataUpdate data_update)
	{
		datas.add(data_update);
	}
	
	public static String getLogin()
	{
		return login;
	}
	
	public static String getPassword()
	{
		return password;
	}
	
	public static JFrame getWindow()
	{
		return window;
	}
	
	public static void setLogin(final String login_value)
	{
		login = login_value;
	}
	
	public static void setPassword(final String pass_value)
	{
		password = pass_value;
	}
	
	public static void setWindow(final JFrame window_frame)
	{
		window = window_frame;
	}
	
	public static void getUpdate()
	{
		for(DataUpdate data_update : datas)
		{
			data_update.update();
		}
	}
}
