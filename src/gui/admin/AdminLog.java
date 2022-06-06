package gui.admin;

import gui.BackListener;
import gui.GUI;
import interfaces.ClientFromServer;
import interfaces.ClientToServer;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class AdminLog extends GUI {

	private static final String EMPTY = "";
	private static final String SEPARATOR = "/";

	private static final int WIDTH = 450;
	private static final int HEIGHT = 350;
	
	private static final String[] TABLE_HEADERS = { "Время", "Код", "Логин" };

	private final JFrame admin_menu;
	private JTable logTable;
	private String[][] data;

	public AdminLog(final JFrame frame, final BufferedReader reader,
			final PrintWriter writer) {
		super(reader, writer, "Журнал");

		this.admin_menu = frame;
		ArrayList<String[]> logsFromServer = getDataFromServer();
		setLogsInArray(logsFromServer);
		initComponents();
	}

	@Override
	protected void initComponents() {
		
		this.logTable = new JTable(this.data, TABLE_HEADERS);
		JScrollPane tablePane = new JScrollPane(this.logTable);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JButton backButton = new JButton("Назад");
		backButton.addActionListener(new BackListener(this.admin_menu, window));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backButton);
		
		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		mainBox.add(tablePane);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(buttonPanel);
		
		window.add(mainBox);
		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	private ArrayList<String[]> getDataFromServer() {
		String response = EMPTY;
		ArrayList<String[]> logsFromServer = new ArrayList<>();
		writer.println(ClientToServer.GET_LOG);
		try {
			response = reader.readLine();
			while (!response.equals(ClientFromServer.SUCCESSFUL) && !response.equals(ClientFromServer.ERROR))
			{
				String[] responseToTableData = response.split(SEPARATOR);
				logsFromServer.add(responseToTableData);
				response = reader.readLine();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return logsFromServer;
	}

	
	private void setLogsInArray(ArrayList<String[]> list) {
		int size = list.size();
		this.data = new String[size][TABLE_HEADERS.length];

		for (int i = 0; i < size; i++) {
			this.data[i] = list.get(i);
		}
	}

}
