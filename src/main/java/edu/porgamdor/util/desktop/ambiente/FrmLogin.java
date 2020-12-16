package edu.porgamdor.util.desktop.ambiente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import edu.porgamdor.util.desktop.DesktopApp;
import edu.porgamdor.util.desktop.MDI;
import edu.porgamdor.util.desktop.ss.SSBotao;
import edu.porgamdor.util.desktop.ss.SSCabecalho;
import edu.porgamdor.util.desktop.ss.SSCampoSenha;
import edu.porgamdor.util.desktop.ss.SSCampoTexto;
import edu.porgamdor.util.desktop.ss.SSMensagem;
import edu.porgamdor.util.desktop.ss.util.Texto;

public abstract class FrmLogin extends JFrame {
	private JPanel pnlLogin = new JPanel();
	private SSBotao btOk = new SSBotao();
	private SSBotao btSair = new SSBotao();
	private SSCampoTexto txtLogin = new SSCampoTexto();
	private SSCampoSenha txtSenha = new SSCampoSenha();

	public FrmLogin() {
		// this.setIconImage(Imagem.png("cfip", "janela").getImage());
		setTitle("Tandera");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(259, 261));
		setLocationRelativeTo(null);

		pnlLogin.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlLogin.setLayout(new BorderLayout(0, 0));
		setContentPane(pnlLogin);

		SSCabecalho cabecalho = new SSCabecalho();
		cabecalho.setTitulo("GESTOR - LOGIN");
		cabecalho.setDescricao("Acesse o sistema");

		pnlLogin.add(cabecalho, BorderLayout.NORTH);

		JPanel conteudo = new JPanel();
		conteudo.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlLogin.add(conteudo, BorderLayout.CENTER);
		GridBagLayout gbl_conteudo = new GridBagLayout();
		conteudo.setLayout(gbl_conteudo);
		txtLogin.setColunas(10);

		txtLogin.setRotulo("Usuário");
		GridBagConstraints gbc_txtUsuario = new GridBagConstraints();
		gbc_txtUsuario.weightx = 1.0;
		gbc_txtUsuario.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtUsuario.insets = new Insets(5, 5, 0, 5);
		gbc_txtUsuario.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsuario.gridx = 0;
		gbc_txtUsuario.gridy = 0;
		conteudo.add(txtLogin, gbc_txtUsuario);

		txtSenha.setRotulo("Senha");
		GridBagConstraints gbc_txtSenha = new GridBagConstraints();
		gbc_txtSenha.weighty = 1.0;
		gbc_txtSenha.anchor = GridBagConstraints.NORTHEAST;
		gbc_txtSenha.insets = new Insets(5, 5, 0, 5);
		gbc_txtSenha.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSenha.gridx = 0;
		gbc_txtSenha.gridy = 1;
		conteudo.add(txtSenha, gbc_txtSenha);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		pnlLogin.add(panel_1, BorderLayout.SOUTH);

		btOk.setText("LOGIN");
		btOk.setIcone("login");

		panel_1.add(btOk);

		btSair.setText("SAIR");
		btSair.setIcone("fechar");
		panel_1.add(btSair);
		txtLogin.setTudoMaiusculo(false);
		txtSenha.setTudoMaiusculo(false);
		txtLogin.setText("tandera");
		txtSenha.setText("1234");

		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
	}

	public void logar(ActionListener listener) {
		btOk.addActionListener(listener);
	}

	public void exibir() {
		this.setVisible(true);
		DesktopApp.fecharSplash();
	}

	public boolean iniciarAplicacao(MDI mdi, Perfil perfil) {
		try {
			if (perfil != null) {
				this.dispose();
				DesktopApp.configurarSessao(mdi, perfil);
				return true;
			} else {
				return !SSMensagem.pergunta("Login ou senha inválida\nDeseja cadastrar ou resgatar sua senha");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getLogin() {
		return txtLogin.getText();
	}

	public String getSenha() {
		return txtSenha.getText();
	}

	public String getSenhaMD5() throws Exception {
		return Texto.md5(getSenha());
	}

	public void abrirCadastroPerfil(FrmPerfil frm, Perfil perfil) {
		perfil.setLogin(getLogin());
		frm.setPerfil(perfil);
		frm.setVisible(true);
	}

	private void fechar() {
		System.exit(0);
	}
}
