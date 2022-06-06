package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class BackListener implements ActionListener {

	private JFrame parentFrame;
	private JFrame thisFrame;
	
	public BackListener(JFrame parentFrame, JFrame thisFrame)
	{
		this.parentFrame = parentFrame;
		this.thisFrame = thisFrame;
	}
	@Override
	public void actionPerformed(ActionEvent event)
	{
		this.thisFrame.setVisible(false);
		this.parentFrame.setVisible(true);
	}
}
