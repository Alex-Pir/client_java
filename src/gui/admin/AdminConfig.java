package gui.admin;

import gui.BackListener;
import gui.GUI;
import interfaces.ClientFromServer;
import interfaces.ClientToServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class AdminConfig extends GUI {

	private static final String UPPER = "U";
	private static final String LOWWER = "L";
	private static final String FIGURES = "1";
	private static final int MAX_LENGTH = 10;
	private static final int MIN_LENGTH = 1;
	private static final int CURRENT_LENGTH = 1;
	private static final int MINOR_TICK_SPACING = 1;
	private static final int MAJOR_TICK_SPASING = 1;
	private static final int COUNT_CONFIG = 4;

	private static final String[] MAX_COMBO_BOX_ITEMS = { "2", "3", "4", "5",
			"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30" };

	private static final String[] MIN_COMBO_BOX_ITEMS = { "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
			"28", "29" };

	private JFrame admin_menu;

	private JComboBox<String> max_cBox;
	private JComboBox<String> min_cBox;
	private JSlider pass_length;
	private JCheckBox upper;
	private JCheckBox lowwer;
	private JCheckBox figures;

	public AdminConfig(final JFrame frame, final BufferedReader reader,
			final PrintWriter writer) {
		super(reader, writer, "Новая конфигурация");
		this.admin_menu = frame;
		initComponents();
	}

	@Override
	protected void initComponents() {

		ActionListener installListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				installButtonPerform();
			}
		};

		ActionListener gentConfigListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				getConfigButtonPerform();
			}
		};

		Border etched = BorderFactory.createEtchedBorder();
		Border timeTitle = BorderFactory.createTitledBorder(etched,
				"Срок действия пароля");
		Border lengthTitle = BorderFactory.createTitledBorder(etched,
				"Длина пароля");
		Border alphabetTitle = BorderFactory.createTitledBorder(etched,
				"Алфавит");

		this.pass_length = new JSlider(JSlider.HORIZONTAL, MIN_LENGTH,
				MAX_LENGTH, CURRENT_LENGTH);
		this.pass_length.setMinorTickSpacing(MINOR_TICK_SPACING);
		this.pass_length.setMajorTickSpacing(MAJOR_TICK_SPASING);
		this.pass_length.setPaintTicks(true);
		this.pass_length.setPaintLabels(true);

		JLabel max_pass_label = new JLabel("Максимальный");
		JLabel min_pass_label = new JLabel("Минимальный");
		min_pass_label.setPreferredSize(max_pass_label.getPreferredSize());

		this.max_cBox = new JComboBox<String>(MAX_COMBO_BOX_ITEMS);
		this.min_cBox = new JComboBox<String>(MIN_COMBO_BOX_ITEMS);

		this.upper = new JCheckBox("Большие латинские буквы");
		this.lowwer = new JCheckBox("Малые латинские буквы");
		this.figures = new JCheckBox("Цифры");

		JButton installConfig = new JButton("Установить конфигурацию");
		JButton getConfig = new JButton("Запросить текущую конфигурацию");
		JButton backButton = new JButton("Назад");

		JPanel minPanel = new JPanel();
		minPanel.add(min_pass_label);
		minPanel.add(this.min_cBox);

		JPanel maxPanel = new JPanel();
		maxPanel.add(max_pass_label);
		maxPanel.add(this.max_cBox);

		JPanel timePanel = new JPanel();
		timePanel.setBorder(timeTitle);
		timePanel.add(minPanel);
		timePanel.add(maxPanel);

		JPanel lengthPanel = new JPanel();
		lengthPanel.setBorder(lengthTitle);
		lengthPanel.add(this.pass_length);

		JPanel alphabetPanel = new JPanel();
		alphabetPanel.setBorder(alphabetTitle);
		alphabetPanel.add(this.upper);
		alphabetPanel.add(this.lowwer);
		alphabetPanel.add(this.figures);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(installConfig);
		buttonPanel.add(getConfig);
		buttonPanel.add(backButton);
		
		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		mainBox.add(timePanel);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(lengthPanel);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(alphabetPanel);
		mainBox.add(Box.createVerticalStrut(VERTICAL_SPACE));
		mainBox.add(buttonPanel);

		installConfig.addActionListener(installListener);
		getConfig.addActionListener(gentConfigListener);
		backButton.addActionListener(new BackListener(this.admin_menu, window));
		
		window.add(mainBox);
		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

	private boolean isFullParameners() {
		boolean result = false;
		int min = Integer.parseInt((String) (this.min_cBox.getSelectedItem()));
		int max = Integer.parseInt((String) (this.max_cBox.getSelectedItem()));
		if (min < max) {
			if (this.figures.isSelected() || this.lowwer.isSelected()
					|| this.upper.isSelected()) {
				result = true;
			}
		} else {
			JOptionPane.showMessageDialog(window, "Корректно заполните поля!",
					"Изменение конфигурации", JOptionPane.WARNING_MESSAGE);
		}
		return result;
	}

	private void getResponseFromServer() {
		try {
			String response = reader.readLine();
			if (response.equals(ClientFromServer.SUCCESSFUL)) {
				JOptionPane.showMessageDialog(window,
						"Конфигурация успешно изменена!",
						"Изменение конфигурации",
						JOptionPane.INFORMATION_MESSAGE);
				window.setVisible(false);
				this.admin_menu.setVisible(true);
			} else if (response.equals(ClientFromServer.ERROR)) {
				JOptionPane.showMessageDialog(window,
						"Не угалось изменить конфигурацию!",
						"Изменение конфигурации", JOptionPane.ERROR_MESSAGE);
				window.setVisible(false);
				this.admin_menu.setVisible(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void installButtonPerform() {
		if (isFullParameners()) {
			String alphabet = "";
			String min_pass = (String) min_cBox.getSelectedItem();
			String max_pass = (String) max_cBox.getSelectedItem();
			String length = Integer.toString(pass_length.getValue());
			if (upper.isSelected()) {
				alphabet += UPPER;
			}
			if (lowwer.isSelected()) {
				alphabet += LOWWER;
			}
			if (figures.isSelected()) {
				alphabet += FIGURES;
			}
			writer.println(ClientToServer.NEW_CONFIG);
			writer.println(min_pass);
			writer.println(max_pass);
			writer.println(length);
			writer.println(alphabet);
			getResponseFromServer();
		}
	}

	private void getConfigButtonPerform() {
		String[] currentConfig = getCurrentConfig();

		this.min_cBox.setSelectedItem(currentConfig[0]);
		this.max_cBox.setSelectedItem(currentConfig[1]);
		this.pass_length.setValue(Integer.parseInt(currentConfig[2]));

		String alphabet = currentConfig[3];
		int u = alphabet.indexOf(UPPER);
		int l = alphabet.indexOf(LOWWER);
		int f = alphabet.indexOf(FIGURES);

		if (u != -1) {
			this.upper.setSelected(true);
		} else {
			this.upper.setSelected(false);
		}
		if (l != -1) {
			this.lowwer.setSelected(true);
		} else {
			this.lowwer.setSelected(false);
		}
		if (f != -1) {
			this.figures.setSelected(true);
		} else {
			this.figures.setSelected(false);
		}

	}

	private String[] getCurrentConfig() {
		String[] config = new String[COUNT_CONFIG];
		try {
			writer.println(ClientToServer.MIN_TIME_PASS);
			String min_time_pass = reader.readLine();
			min_time_pass = getCommandWithoutR(min_time_pass);
			config[0] = min_time_pass;

			writer.println(ClientToServer.MAX_TIME_PASS);
			String max_time_pass = reader.readLine();
			max_time_pass = getCommandWithoutR(max_time_pass);
			config[1] = max_time_pass;

			writer.println(ClientToServer.PASS_LENGTH);
			String length_time_pass = reader.readLine();
			length_time_pass = getCommandWithoutR(length_time_pass);
			config[2] = length_time_pass;

			writer.println(ClientToServer.ALPHABET);
			String alphabet = reader.readLine();
			alphabet = getCommandWithoutR(alphabet);
			config[3] = alphabet;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return config;
	}

	public JFrame getWindow() {
		
		this.min_cBox.setSelectedItem(MIN_COMBO_BOX_ITEMS[0]);
		this.max_cBox.setSelectedItem(MAX_COMBO_BOX_ITEMS[0]);
		this.pass_length.setValue(MINOR_TICK_SPACING);
		this.upper.setSelected(false);
		this.lowwer.setSelected(false);
		this.figures.setSelected(false);
		
		return window;
	}
}
