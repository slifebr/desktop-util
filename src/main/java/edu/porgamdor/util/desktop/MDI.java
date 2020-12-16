package edu.porgamdor.util.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import edu.porgamdor.util.desktop.ambiente.Perfil;
import edu.porgamdor.util.desktop.ambiente.Sessao;
import edu.porgamdor.util.desktop.ss.util.Imagem;

//Multiple Document Interface
public class MDI extends JFrame {
	private JDesktopPane areaTrabalho = new JDesktopPane(); 
	private static Sessao sessao;
	private JMenuBar barraMenu = new JMenuBar();
	private JLabel lblNome = new JLabel("NOME");
	private JLabel lblUsuario = new JLabel("USUARIO");
	private JLabel lblAmbiente = new JLabel("AMBIENTE");
	
	private JLabel imagemFundo = new JLabel();
	private ImageIcon imgFundo;  
	public MDI() {
		areaTrabalho.setBackground(Color.LIGHT_GRAY);
		areaTrabalho.setVisible(true);
		getContentPane().setLayout(new BorderLayout());
		
		
		JLabel lblName = new JLabel("NOME:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblUser = new JLabel("USUÁRIO:");
		lblUser.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblEnv = new JLabel("AMBIENTE:");
		lblEnv.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblVersao = new JLabel("Versão: 1.0");
		
		lblVersao.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUsuario.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAmbiente.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lblUsuario.setForeground(Color.BLUE);
		lblNome.setForeground(Color.BLUE);
		lblAmbiente.setForeground(Color.BLUE);
		
		JPanel barraSessao = new JPanel();
		barraSessao.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(barraSessao,BorderLayout.NORTH);
		
		barraSessao.add(lblUser);
		barraSessao.add(lblUsuario);
		
		barraSessao.add(lblName);
		barraSessao.add(lblNome);
		
		barraSessao.add(lblEnv);
		barraSessao.add(lblAmbiente);
		
		barraSessao.add(lblVersao);
		
		getContentPane().add(areaTrabalho, BorderLayout.CENTER);
		setJMenuBar(barraMenu);
		setTitle("MDI -Multiple Document Interface");
		
		this.setIconImage(Imagem.png("cfip", "janela").getImage());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(new Rectangle(870, 630));
		setLocationRelativeTo(null);
		
		//imgFundo = Imagem.jpg("cfip", "EuCresci");
		//imagemFundo.setIcon(imgFundo);
		imgFundo = Imagem.jpg("cfip", "EuCresci");
		imagemFundo.setIcon(imgFundo);
		areaTrabalho.add(imagemFundo);
		areaTrabalho.setBackground(Color.LIGHT_GRAY);
		areaTrabalho.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				desktopPane_componentResized(e);
			}
		});
	}
	private void desktopPane_componentResized(ComponentEvent e) {
		if (imgFundo == null)
			return;

		int top, left;

		top = (areaTrabalho.getWidth() / 2) - (imgFundo.getIconWidth() / 2);
		left = (areaTrabalho.getHeight() / 2) - (imgFundo.getIconHeight() / 2);

		imagemFundo.setBounds(top, left - 20, imgFundo.getIconWidth(), imgFundo.getIconHeight());
	}
	public JDesktopPane getAreaTrabalho() {
		return areaTrabalho;
	}
	public Sessao getSessao() {
		return sessao;
	}
	public void setSessao(Perfil perfil,String ambiente) {
		sessao = new Sessao();
		sessao.setPerfil(perfil);
		sessao.setAmbiente(ambiente);
		if(sessao!=null) {
			lblNome.setText(sessao.getPerfil().getNome());
			lblUsuario.setText(sessao.getPerfil().getLogin());
			lblAmbiente.setText(sessao.getAmbiente());
		}
	}
	public JMenuBar getBarraMenu() {
		return barraMenu;
	}
	public static Perfil getPerfil() {
		return sessao.getPerfil();
	}
	public static String getAmbiente() {
		return sessao.getAmbiente();
	}
	public static Integer getPerfilId() {
		return getPerfil().getId();
	}
}
