package edu.porgamdor.util.desktop.ambiente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import edu.porgamdor.util.desktop.ss.util.Imagem;

public class Splash extends JFrame {
	private JLabel logotipo;
	public Splash() {
		
		logotipo = new JLabel(Imagem.jpg("cfip", "splash"));
		getContentPane().add(logotipo, BorderLayout.CENTER);
		logotipo.setBorder(BorderFactory.createEtchedBorder());
		JLabel lblVersao = new JLabel("TANDERA - Versão: 1.0 - 201804.08 - Desenvolvido por: Slifebr/ALopes");
		lblVersao.setBorder(BorderFactory.createEtchedBorder());
		lblVersao.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVersao.setForeground(Color.BLUE);
		lblVersao.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblVersao, BorderLayout.SOUTH);
		setSize(400,300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}