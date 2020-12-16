package edu.porgamdor.util.desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import edu.porgamdor.util.desktop.ss.SSBotao;
import edu.porgamdor.util.desktop.ss.SSGrade;

public abstract class FormularioConsulta extends Formulario implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel filtro = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	private SSGrade tabela = new SSGrade();

	// private SSBotao cmdIncluir = new SSBotao();
	// private SSBotao cmdAlterar = new SSBotao();
	protected SSBotao cmdFechar = new SSBotao();

	public FormularioConsulta() {
		// definir propriedades
		setConteudoLayout(new BorderLayout());
		filtro.setLayout(new GridBagLayout());
		scroll.setViewportView(tabela);
		getConteudo().add(filtro, BorderLayout.NORTH);

		getConteudo().add(scroll, BorderLayout.CENTER);
		setAlinhamentoRodape(FlowLayout.LEFT);
		
		super.getRodape().add(cmdFechar);
		cmdFechar.setText("Fechar");

		init();

	}

	private void init() {
		adicionarListner();
	}

	private void adicionarListner() {
		//this.addMouseListener(this);
		
		tabela.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				SSGrade table =(SSGrade) e.getSource();
		        Point point = e.getPoint();
		        int row = table.rowAtPoint(point);
		        if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
		        	System.out.println(" consume double click");
					alterar();
		        }				
			/*
				//SSMensagem.avisa("1   Selecione um item da lista");
				System.out.println(" consume double click11111");
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					System.out.println(" consume double click");
					alterar();
				}
				*/
			}
			
		});
       
		setAcaoBotao("Fechar", cmdFechar, KeyEvent.VK_ESCAPE, buttonActionSair);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println("Click at (" + e.getX() + ":" + e.getY() + ")");
		System.out.println(e.toString());
		if (e.getClickCount() == 2)
			System.out.println("  and it's a double click!");
		else
			System.out.println("  and it's a simple click!");
	}

	protected void setAcaoBotao(String key, SSBotao botao, int keyEvent, Action buttonAction) {
		botao.setAction(buttonAction);
		buttonAction.putValue(Action.MNEMONIC_KEY, keyEvent);
		botao.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyEvent, 0), key);
		botao.getActionMap().put(key, buttonAction);
	}

	public JPanel getFiltro() {
		return filtro;
	}

	public SSGrade getTabela() {
		return tabela;
	}
	/*
	 * // metodos comuns private void exibirCadastro(Estado entidade) { Formulario
	 * frm = SpringDesktopApp.getBean(formInclusao); frm.setEntidade(entidade);
	 * this.exibir(frm); }
	 */

	private Action buttonActionSair = new AbstractAction("Fechar") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent evt) {
			sair();
		}
	};

	protected abstract void alterar();

	protected abstract void sair();

}
