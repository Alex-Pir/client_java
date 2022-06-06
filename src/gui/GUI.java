package gui;

import interfaces.ClientToServer;

import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public abstract class GUI {

	protected static final int VERTICAL_SPACE = 6;
	protected static final int HORIZONTAL_SPACE = 6;
	protected static final int BORDER = 12;
	
	private static final String R_COMMAND = "/r/";
	private static final int SHIFT_IN_COMMAND = 3;
	private static final String YES_TEXT = "OptionPane.yesButtonText";
	private static final String NO_TEXT = "OptionPane.noButtonText";
	protected static final int ERROR = -1;
	protected static final String SUCCESSFUL = "successful";
	
	protected final BufferedReader reader;
	protected final PrintWriter writer;
	protected final JFrame window;
	
	protected GUI(final BufferedReader reader, final PrintWriter writer, String window_name)
	{
		this.reader = reader;
		this.writer = writer;
		this.window = new JFrame(window_name);
		UIManager.put(YES_TEXT, "Да");
		UIManager.put(NO_TEXT, "Нет");
		this.window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Image icon = new ImageIcon("icons/key.png").getImage();
		this.window.setIconImage(icon);
		window.addWindowListener(new WindowListener() {
			 
            public void windowActivated(WindowEvent event) {
 
            }
 
            public void windowClosed(WindowEvent event) {
 
            }
 
            public void windowClosing(WindowEvent event) {
                int reply = JOptionPane
                        .showConfirmDialog(event.getWindow(), "Закрыть программу?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    window.setVisible(false);
                    writer.println(ClientToServer.BUE);
                    System.exit(0);
                }
            }

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});	
	}

	
	
	protected String getCommandWithoutR(String command)
	{
		final int index_r = command.indexOf(R_COMMAND);
		String result = "";
		
		if (index_r != -1)
		{
			result = command.substring(SHIFT_IN_COMMAND);
		}
		
		return result;
	}

	protected abstract void initComponents();
	
}
