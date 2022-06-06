package gui.admin;

import gui.BackListener;
import gui.GUI;
import interfaces.Advisor;
import interfaces.ClientFromServer;
import interfaces.ClientToServer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import advisor.AdvisorForPass;

public class AdminRegistration extends GUI {

	private static final int TEXT_FIELD_LENGTH = 20;

	private static final String EMPTY = "";

	private final JFrame admin_menu;

	private JTextField user_name;
	private JTextField user_pass;
	private JTextField re_user_pass;
	private JRadioButton status_a;
	private JRadioButton status_u;

	private String status;
	private Advisor advisorForPass;
	private JLabel alphabet;
	
	public AdminRegistration(JFrame frame, final BufferedReader reader,
			final PrintWriter writer) {
		super(reader, writer, "Регистрация нового пользователя");

		this.admin_menu = frame;
		this.user_name = new JTextField(TEXT_FIELD_LENGTH);
		this.user_pass = new JTextField(TEXT_FIELD_LENGTH);
		this.re_user_pass = new JTextField(TEXT_FIELD_LENGTH);
		this.status_a = new JRadioButton("Admin");
		this.status_u = new JRadioButton("User");
		this.advisorForPass = new AdvisorForPass();
		this.status = EMPTY;

		initComponents();
	}

	protected void initComponents() {
		ActionListener actionRadio = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				status = ((JRadioButton) event.getSource()).getText();
				status = status.substring(0, 1);
			}
		};

		ActionListener actionButton = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (updateUserDataAndSend()) {
					buttonPerform();
				}
			}
		};

		ActionListener genPassButton = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				writer.println(ClientToServer.ALPHABET);
				String alphabet = "";
				String pass_length = "";
				String newPass = "";
				try {
					alphabet = reader.readLine();
					alphabet = getCommandWithoutR(alphabet);
					alphabet = advisorForPass.genAlphabet(alphabet);
					pass_length = getPassLength();
					newPass = advisorForPass.genPassword(alphabet, pass_length);
				} catch (IOException | NoSuchAlgorithmException | InterruptedException e) {

					e.printStackTrace();
				}

				user_pass.setText(newPass);
			}
		};

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.status_a);
		buttonGroup.add(this.status_u);

		JButton regButton = new JButton("Зарегистрировать");
		JButton genPass = new JButton("Сгенерировать пароль");
		JButton backButton = new JButton("Назад");

		this.alphabet = getAlphabet();
		JLabel name_label = new JLabel("Логин:");
		JLabel pass_label = new JLabel("Пароль:");
		JLabel re_pass_label = new JLabel("Повторите пароль:");
		Dimension label_size = re_pass_label.getPreferredSize();
		name_label.setPreferredSize(label_size);
		pass_label.setPreferredSize(label_size);

		Box nameBox = Box.createHorizontalBox();
		nameBox.add(name_label);
		nameBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		nameBox.add(this.user_name);

		Box passBox = Box.createHorizontalBox();
		passBox.add(pass_label);
		passBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		passBox.add(this.user_pass);

		Box rePassBox = Box.createHorizontalBox();
		rePassBox.add(re_pass_label);
		rePassBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		rePassBox.add(this.re_user_pass);

		Box radioBox = Box.createHorizontalBox();
		radioBox.add(this.status_a);
		radioBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		radioBox.add(this.status_u);

		Border etched = BorderFactory.createEtchedBorder();
		Border alphabetTitle = BorderFactory.createTitledBorder(etched,
				"Алфавит");
		JPanel alphabetPanel = new JPanel();
		alphabetPanel.setBorder(alphabetTitle);
		alphabetPanel.add(alphabet);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(regButton);
		buttonPanel.add(genPass);
		buttonPanel.add(backButton);

		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		mainBox.add(nameBox);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(passBox);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(rePassBox);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(radioBox);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(alphabetPanel);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(buttonPanel);

		this.status_a.addActionListener(actionRadio);
		this.status_u.addActionListener(actionRadio);

		regButton.addActionListener(actionButton);
		genPass.addActionListener(genPassButton);
		backButton.addActionListener(new BackListener(this.admin_menu, window));

		window.add(mainBox);
		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	private boolean updateUserDataAndSend() {
		boolean result = false;
		String login = this.user_name.getText();
		String pass = this.user_pass.getText();
		String re_pass = this.re_user_pass.getText();
		String status = this.status;
		int pass_length = -1;
		try {
			pass_length = Integer.parseInt(getPassLength());
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		if (pass.length() == pass_length) {
			if (pass.equals(re_pass) && !pass.equals(EMPTY)
					&& !login.equals(EMPTY) && !status.equals(EMPTY)) {
				writer.println(ClientToServer.REGISTRATION);
				writer.println(login);
				writer.println(pass);
				writer.println(status);
				result = true;
			} else {
				JOptionPane.showMessageDialog(window, "Заполните поля!",
						"Регистрация", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane
					.showMessageDialog(
							window,
							String.format(
									"Длина введённого пароля не соответствует установленной в конфигурации!\nНеобходимая длина пароля: %s",
									pass_length), "Регистрация",
							JOptionPane.WARNING_MESSAGE);
		}
		return result;
	}

	private void buttonPerform() {

		try {
			String response = reader.readLine();
			if (response.equals(ClientFromServer.LOGIN_IS_EXSIST)) {
				thisIsExsist(this.user_name.getText());
			} else if (response.equals(ClientFromServer.PASS_IS_EXSIST)) {
				thisIsExsist(this.user_pass.getText());
			} else if (response.equals(ClientFromServer.MISTAKE_ALPHABET)) {
				mistakeInAlphabet();
			} else {
				JOptionPane.showMessageDialog(window,
						"Регистрация успешно завершена!", "Регистрация",
						JOptionPane.INFORMATION_MESSAGE);
				window.setVisible(false);
				admin_menu.setVisible(true);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void thisIsExsist(String value) {
		final int reply = JOptionPane
				.showConfirmDialog(
						window,
						String.format(
								"Пользователь с атрибутом \"%s\" не может быть зарегистрирован!\n Желаете повторить операцию регистрации?",
								value), "Регистрация",
						JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.NO_OPTION) {
			window.setVisible(false);
			admin_menu.setVisible(true);
		}
	}

	private void mistakeInAlphabet() {
		final int reply = JOptionPane
				.showConfirmDialog(
						window,
						"В пароле используются недопустимые символы!\n Желаете повторить операцию регистрации?",
						"Регистрация", JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.NO_OPTION) {
			window.setVisible(false);
			admin_menu.setVisible(true);
		}
	}

	private JLabel getAlphabet() {
		writer.println(ClientToServer.ALPHABET);
		JLabel label = null;
		try {
			String alph = reader.readLine();
			alph = this.advisorForPass.genAlphabet(alph);
			alph = this.advisorForPass.getReallyAlphabet(alph);
			label = new JLabel(alph);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return label;
	}
	
	private String getPassLength() throws IOException {
		writer.println(ClientToServer.PASS_LENGTH);
		String pass_length = reader.readLine();
		pass_length = getCommandWithoutR(pass_length);
		return pass_length;
	}

	public JFrame getWindow() {
		this.alphabet.setText(getAlphabet().getText());
		this.user_name.setText(EMPTY);
		this.user_pass.setText(EMPTY);
		this.re_user_pass.setText(EMPTY);
		return window;
	}
}
