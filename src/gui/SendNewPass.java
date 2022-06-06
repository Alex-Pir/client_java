package gui;

import interfaces.Advisor;
import interfaces.ClientFromServer;
import interfaces.ClientToServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import advisor.AdvisorForPass;
import client.Client;

public class SendNewPass extends GUI {

	private static final int LIMIT = 12;

	private final Advisor advisor;
	private JTextField newPass;
	private JTextField newPassRepeat;

	public SendNewPass(final BufferedReader reader, final PrintWriter writer) {
		super(reader, writer, "Изменение пароля");
		this.advisor = new AdvisorForPass();
		this.newPass = new JTextField(15);
		this.newPassRepeat = new JTextField(15);

		initComponents();
	}

	@Override
	protected void initComponents() {
		JLabel newPassLabel = new JLabel("Новый пароль: ");
		JLabel newPassRepeatLabel = new JLabel("Повторите ввод: ");
		JLabel alphabet = getAlphabet();
		
		newPassLabel.setPreferredSize(newPassRepeatLabel.getPreferredSize());

		JButton sendButton = new JButton("Ок");
		JButton genPassButton = new JButton("Сгенерировать пароль");

		Box newPassBox = Box.createHorizontalBox();
		newPassBox.add(newPassLabel);
		newPassBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		newPassBox.add(this.newPass);

		Box newPassRepeatBox = Box.createHorizontalBox();
		newPassRepeatBox.add(newPassRepeatLabel);
		newPassRepeatBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		newPassRepeatBox.add(this.newPassRepeat);

		Border etched = BorderFactory.createEtchedBorder();
		Border alphabetTitle = BorderFactory.createTitledBorder(etched,
				"Алфавит");
		JPanel alphabetPanel = new JPanel();
		alphabetPanel.setBorder(alphabetTitle);
		alphabetPanel.add(alphabet);
		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(sendButton);
		buttonBox.add(Box.createHorizontalStrut(HORIZONTAL_SPACE));
		buttonBox.add(genPassButton);

		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		mainBox.add(newPassBox);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(newPassRepeatBox);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(alphabetPanel);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(buttonBox);

		this.newPass.setDocument(new PlainDocument() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {

				if (str == null)
					return;
				if ((getLength() + str.length()) <= LIMIT) {
					super.insertString(offset, str, attr);
				}

			}
		});

		this.newPassRepeat.setDocument(new PlainDocument() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {

				if (str == null)
					return;
				if ((getLength() + str.length()) <= LIMIT) {
					super.insertString(offset, str, attr);
				}

			}
		});

		genPassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String alphabet = "";
				String pass_length = "";
				try {
					writer.println(ClientToServer.ALPHABET);
					alphabet = getCommandWithoutR(reader.readLine());
					alphabet = advisor.genAlphabet(alphabet);
					pass_length = getPassLength();
					newPass.setText(advisor.genPassword(alphabet, pass_length));
				} catch (IOException | NoSuchAlgorithmException
						| InterruptedException e) {
					e.printStackTrace();
				}
			}

		});

		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				sendButtonPerform();
			}
		});

		this.window.add(mainBox);
		this.window.pack();
		this.window.setResizable(false);
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}

	private String getPassLength() throws IOException {
		writer.println(ClientToServer.PASS_LENGTH);
		String pass_length = reader.readLine();
		pass_length = getCommandWithoutR(pass_length);
		return pass_length;
	}

	private void sendButtonPerform() {
		int pass_length = -1;
		try {
			pass_length = Integer.parseInt(getPassLength());

			if (newPass.getText().length() == pass_length) {
				if (newPass.getText().equals(newPassRepeat.getText())) {
					writer.println(ClientToServer.NEW_PASS);
					writer.println(newPass.getText());

					String result = reader.readLine();
					if (result.equals(ClientFromServer.SUCCESSFUL)) {
						JOptionPane.showMessageDialog(window,
								"Пароль изменён!\nПовторите вход.",
								"Изменение пароля",
								JOptionPane.INFORMATION_MESSAGE);
						reader.close();
						writer.close();
						Client.getSocket().close();
						System.exit(0);
					} else {
						JOptionPane
								.showMessageDialog(
										window,
										"Пароль не изменён!\nВозможно данный пароль уже был использован\nили в пароле используются недопустимые символы.",
										"Изменение пароля",
										JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(window,
							"Вы ввели некорректные значения!",
							"Изменение пароля", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane
						.showMessageDialog(
								window,
								String.format(
										"Длина введённого пароля не соответствует установленной в конфигурации!\nНеобходимая длина пароля: %s",
										pass_length), "Изменение пароля",
								JOptionPane.WARNING_MESSAGE);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private JLabel getAlphabet() {
		writer.println(ClientToServer.ALPHABET);
		JLabel label = null;
		try {
			String alph = reader.readLine();
			alph = this.advisor.genAlphabet(alph);
			alph = this.advisor.getReallyAlphabet(alph);
			label = new JLabel(alph);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return label;
	}

}
