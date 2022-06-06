package gui.admin;

import gui.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Admin extends GUI {

	//private static final int VERT_SPACE = 6;
	private static final int BORDER = 12;

	private AdminRegistration admin_reg = null;
	private AdminConfig admin_config = null;
	@SuppressWarnings("unused")
	private AdminLog admin_log = null;
	
	public Admin(final BufferedReader reader, final PrintWriter writer) {
		super(reader, writer, "Меню");
		initComponents();
	}

	@Override
	protected void initComponents() {
		
		ActionListener configButtonListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				
				window.setVisible(false);
				if (admin_config == null) {
					admin_config = new AdminConfig(window, reader, writer);
				}
				else {
					admin_config.getWindow().setVisible(true);
				}
				
			}
		};
		
		ActionListener registrButtonListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent event) {
				window.setVisible(false);
				if (admin_reg == null) {
					admin_reg = new AdminRegistration(window, reader, writer);
				}
				else {
					admin_reg.getWindow().setVisible(true);
				}
			}
		};
		
		ActionListener logButtonListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent event) {
				window.setVisible(false);
				admin_log = new AdminLog(window, reader, writer);
				
			}
		};
		
		final JButton registrButton = new JButton(
				"Регистрация нового пользователя");
		final JButton configButton = new JButton("Изменение настроек");
		final JButton logButton = new JButton("Журнал");
		registrButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		configButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		logButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		mainPanel.setLayout(new GridLayout(3, 1));
		mainPanel.add(registrButton);
		mainPanel.add(configButton);
		mainPanel.add(logButton);
		
		registrButton.addActionListener(registrButtonListener);

		configButton.addActionListener(configButtonListener);

		logButton.addActionListener(logButtonListener);
		
		this.window.add(mainPanel, BorderLayout.CENTER);
		this.window.pack();
		this.window.setResizable(false);
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}

}
