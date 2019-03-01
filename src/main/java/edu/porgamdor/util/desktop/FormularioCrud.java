package edu.porgamdor.util.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import edu.porgamdor.util.desktop.ss.SSBotao;

//WindowBuilder
//http://download.eclipse.org/windowbuilder/WB/integration/4.7/

public abstract class FormularioCrud extends Formulario {

	protected SSBotao cmdSalvar = new SSBotao();
	protected SSBotao cmdSair = new SSBotao();

	public FormularioCrud() {
		init();
		super.getRodape().add(cmdSalvar);
		super.getRodape().add(cmdSair);
		cmdSair.setText("Fechar");
		cmdSalvar.setText("Salvar");
	}

	private void init() {
		adicionarListnerBotoes();

	}

	private Action buttonActionSair = new AbstractAction("Fechar") {

		@Override
		public void actionPerformed(ActionEvent evt) {
			sair();
		}
	};

	private Action buttonActionSalvar = new AbstractAction("Salvar") {

		@Override
		public void actionPerformed(ActionEvent evt) {
			salvar();
		}
	};

	protected void setAcaoBotao(String key, SSBotao botao, int keyEvent, Action buttonAction) {

		botao.setAction(buttonAction);

		buttonAction.putValue(Action.MNEMONIC_KEY, keyEvent);

		botao.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyEvent, 0), key);

		botao.getActionMap().put(key, buttonAction);
	}
	
	private void adicionarListnerBotoes() {
		setAcaoBotao("Salvar",cmdSalvar,KeyEvent.VK_F10,buttonActionSalvar);
		setAcaoBotao("Fechar",cmdSair,KeyEvent.VK_ESCAPE,buttonActionSair);
	}	
	
	protected abstract void salvar();
	
	private void sair() {
		super.fechar();
	}
}
