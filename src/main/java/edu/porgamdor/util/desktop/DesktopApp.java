package edu.porgamdor.util.desktop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.UIManager;

import edu.porgamdor.util.desktop.ambiente.Ambiente;
import edu.porgamdor.util.desktop.ambiente.FrmConfiguracao;
import edu.porgamdor.util.desktop.ambiente.Perfil;
import edu.porgamdor.util.desktop.ambiente.Splash;

public class DesktopApp {
	//private static String nomeAmbiente;
	private static Splash splash;
	public static boolean iniciarAplicacao() {
		boolean iniciar=false;
		String lf = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lf);
			splash = new Splash();
			if(!arquivoConfiguracao().exists()) {
				configurarAmbiente(Ambiente.LOCAL);
			}
			if(configurarAmbiente()) {
				exibirConfiguracao();
				fecharSplash();
			}else {
				iniciar=true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return iniciar;
	}
	public static void exibirConfiguracao() {
		FrmConfiguracao frm = new FrmConfiguracao();
		frm.setVisible(true);
	}
	public static void configurarSessao(MDI mdi, Perfil perfil) throws Exception  {
		mdi.setVisible(true);
		mdi.setSessao(perfil, getConfiguracao().getProperty(Ambiente.NOME));
	}
	public static void fecharSplash() {
		splash.dispose();
	}
	private static boolean configurarAmbiente() throws Exception {
		return getConfiguracao().getProperty(Ambiente.STATUS).equals("NOK");
	}
	//CONFIGURA UM AMBIENTE SELECIONADO
	public static void configurarAmbiente(Ambiente ambiente) throws Exception {
		String configuracao = configuracao(ambiente);
		if(!configuracao.trim().isEmpty()){
			FileWriter writer=null;
			BufferedWriter bf=null;
			File arquivo = arquivoConfiguracao();
			arquivo.createNewFile();
			writer = new FileWriter(arquivo);
			bf = new BufferedWriter(writer);
			bf.write(configuracao);
			bf.flush();
			bf.close();
			bf=null;
			writer=null;
		}
	}
	//OBTEM OU CRIA O ARQUIVO DE CONFIGURAÇÃO
	private static File arquivoConfiguracao() throws Exception{
		File diretorio = new File("/tandera/config/");
		File arquivo = new File(diretorio, "ambiente.properties");
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
		File backup = new File("/tandera/backup/");
		if (!backup.exists()) {
			backup.mkdirs();
		}
		
		return arquivo;
	}
	private static String configuracao(Ambiente ambiente) {
		StringBuilder sb =  new StringBuilder();
		sb.append(Ambiente.VERSAO +"=" + ambiente.getVersao()+"\n");
		sb.append(Ambiente.NOME +"=" + ambiente.getNome()+"\n");
		sb.append(Ambiente.DB_DRIVER +"=" + ambiente.getDbDriver()+"\n");
		sb.append(Ambiente.DB_URL +"="+ ambiente.getDbUrl()  +"\n");
		sb.append(Ambiente.DB_USER +"="+ ambiente.getDbUser()  +"\n");
		sb.append(Ambiente.DB_PASS +"="+ ambiente.getDbPass()  +"\n");
		sb.append(Ambiente.DB_DIALECT +"=" + ambiente.getDbDialect()+"\n");
		sb.append(Ambiente.DB_DDL +"=" + ambiente.getDbDdl()+"\n");
		sb.append(Ambiente.DB_SHOWSQL +"=" + ambiente.getDbShowSql()+"\n");
		sb.append(Ambiente.STATUS +"=" + ambiente.getStatus()+"\n");
		return sb.toString();
	}
	//LÊ AS PROPRIEDADES DO ARQUIVO ambiente.properties 
	private static Properties getConfiguracao() throws Exception {
		Properties config = new Properties();
		InputStream input = null;
		input = new FileInputStream(arquivoConfiguracao());
		config.load(input);
		return config;
	}

}
