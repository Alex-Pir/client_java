package gui.autent;

import gui.GUI;
import gui.autent.controller.EnterButtonListener;
import gui.autent.model.UserDataModel;
import interfaces.DataUpdate;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Autentification extends GUI implements Runnable, DataUpdate {

	private static final int HOR_PLACE = 6;
	private static final int VERT_PLACE = 6;
	private static final int BORDER = 12;
	private static final int LIMIT = 12;

	private JTextField loginField;
	private JPasswordField passField;
	
	public Autentification(final BufferedReader reader, final PrintWriter writer)
	{	
		super(reader, writer, "Вход");
		this.loginField = new JTextField(15);
		this.passField = new JPasswordField(15);
		
		UserDataModel.add(this);
	}
	
	@Override
	public void run()
	{
		initComponents();
	}


	@Override
	public void update() 
	{
		final String login = this.loginField.getText();
		final String password = new String(this.passField.getPassword());
		UserDataModel.setLogin(login);
		UserDataModel.setPassword(password);
		UserDataModel.setWindow(this.window);
	}

	@Override
	protected void initComponents() {
		JLabel loginLabel = new JLabel("Логин: ");
		JLabel passLabel = new JLabel("Пароль: ");
		
		loginLabel.setPreferredSize(passLabel.getPreferredSize());
		
		JButton entrButton = new JButton("Вход");
		
		Box loginBox = Box.createHorizontalBox();
		loginBox.add(loginLabel);
		loginBox.add(Box.createHorizontalStrut(HOR_PLACE));
		loginBox.add(loginField);
		
		Box passBox = Box.createHorizontalBox();
		passBox.add(passLabel);
		passBox.add(Box.createHorizontalStrut(HOR_PLACE));
		passBox.add(passField);
		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(entrButton);
		
		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		mainBox.add(loginBox);
		mainBox.add(Box.createVerticalStrut(VERT_PLACE));
		mainBox.add(passBox);
		mainBox.add(Box.createVerticalStrut(VERT_PLACE));
		mainBox.add(buttonBox);
		
		
		
		this.passField.setDocument(new PlainDocument()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
				
				if (str == null)
					return;
				if ((getLength() + str.length()) <= LIMIT) {
					super.insertString(offset, str, attr);
				}
				
			}
		});

		entrButton.addActionListener(new EnterButtonListener(entrButton, reader, writer));
		
		this.window.add(mainBox);
		this.window.pack();
		this.window.setResizable(false);
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
		
	}
	
}
