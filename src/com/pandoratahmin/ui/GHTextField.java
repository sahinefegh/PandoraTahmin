package com.pandoratahmin.ui;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.*;

public class GHTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	public static Font textFieldFont = com.pandoratahmin.ui.FontManager.getFont(Font.PLAIN, 16);
	public static Font numaraFont = com.pandoratahmin.ui.FontManager.getFont(Font.BOLD, 18);
	JLabel lblNumber;
	int digits;

	GHTextField(String text) {
		lblNumber = new JLabel(text);
		lblNumber.setFont(numaraFont);
		lblNumber.setHorizontalAlignment(SwingConstants.LEFT);
		digits = text.length();
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					e.consume();
				}
				if ((getText() + e.getKeyChar()).length() > 3) {
					e.consume();
				}
			}
		});
		this.setFont(textFieldFont);
	}

	public String getText() {
		return super.getText().toUpperCase(Locale.ENGLISH);
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		lblNumber.setVisible(b);
	}

	public void setGHLocation(int x, int y) {
		super.setBounds(x, y, 55, 25);
		lblNumber.setBounds(x - ((digits + 1) * 10), y, 50, 25);
	}

	public JLabel getLabel() {
		return lblNumber;
	}
}
