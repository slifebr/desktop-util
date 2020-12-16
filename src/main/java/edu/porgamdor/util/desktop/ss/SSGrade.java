package edu.porgamdor.util.desktop.ss;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.porgamdor.util.desktop.ss.tabela.SSModeloColuna;
import edu.porgamdor.util.desktop.ss.tabela.SSModeloTabela;
import edu.porgamdor.util.desktop.ss.tabela.TipoSelecao;
import edu.porgamdor.util.desktop.ss.util.Formatador;
import edu.porgamdor.util.desktop.ss.util.Reflexao;
import edu.porgamdor.util.desktop.ss.util.Texto;


/**
 * A <code>SSGrade</code> é usada para mostrar dados de objetos em uma tabela
 * of cells.
 *
 * @beaninfo
 *   attribute: isContainer false
 * description: Mostra dados de objetos em uma grade.
 *
 * @version 1.0 08/18/08
 * @author Frank Marlon M. dos Santos
 */
public class SSGrade extends JTable {
    private Object value;
    private TipoSelecao tipoSelecao;
    private int quantidadeLinhasVisiveis = 15;
    
    public SSGrade() {                        
        super(new SSModeloTabela(), new SSModeloColuna());        
        setAutoCreateColumnsFromModel(true);
        setTipoSelecao(TipoSelecao.SELECAO_UNICA);
        setAutoResizeMode(SSGrade.AUTO_RESIZE_OFF);
        //setBackground(UIManager.getColor("control"));        
       
    }
    
    public SSModeloTabela getModeloTabela() {
        return (SSModeloTabela)getModel();
    }
    
    public SSModeloColuna getModeloColuna() {
        return (SSModeloColuna)getColumnModel();
    }
    
    public void setModeloTabela(SSModeloTabela modelo) {
        setModel(modelo);
    }
    
    public void setModeloColuna(SSModeloColuna modelo) {
        setColumnModel(modelo);
    }
    
    public void setValue(Object value) {
    	clearSelection();
        this.value = value;
        if (value instanceof ResultSet) {
            try {
                carregarGrade((ResultSet)value);
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }
        } else
            carregraGrade();        
        this.updateUI();
    }

    public Object getValue() {
        return value;
    }
    
    public Object getRowData(int linha) {
        if (linha < 0)
            return null;
        
        try {
            if (value instanceof List)
                return ((List)value).get(linha);
            else
                return linha + 1;
        }catch(ArrayIndexOutOfBoundsException aio) {
            return null;
        }
    }
    
    public Object getSelectedRowData() {
        return getRowData(getSelectedRow());
    }
    
    public Object getLinhaSelecionada() {
        return getSelectedRowData();
    }
    
    public Object[] getSelectedRowsData() {
        return getLinhasSelecionadas();        
    }
    
    public Object[] getLinhasSelecionadas() {
        int[] linhas = getSelectedRows();
        List selecionados = new ArrayList();        
        Object selecao;
        for (int i = 0; i < linhas.length; i++) {
            selecao = getRowData(linhas[i]);
            if (selecao != null)
                selecionados.add(selecao);
        }
        
        return selecionados.toArray();
    }
    
    public void atualizarGrade() {
        carregraGrade();
        updateUI();
    }
    
    private void carregraGrade() {
        SSModeloColuna modeloColuna = (SSModeloColuna)getColumnModel();
        DefaultTableModel modeloTabela = (DefaultTableModel)getModel();
        Vector linha;
        String nomeCampo, formato, mascara;
        Object valor = null;
        TableColumn coluna;
        int indexLinha = 0;
        
        modeloTabela.getDataVector().clear();
        
        if (getValue() == null)
            return;
        
        for (Object registro : (List)value) {
            linha = new Vector();
            modeloTabela.addRow(linha);
            for (int i = 0; i < modeloColuna.getColumnCount(); i++) {
                coluna = modeloColuna.getColumn(i);
                valor = null;
                nomeCampo = modeloColuna.getCampo(coluna);
                try {
                    valor = Reflexao.buscarValorCampoRecursivo(registro, nomeCampo);
                
                    formato = modeloColuna.getFormato(coluna);
                    
                    if (formato == null && valor != null) {
                        if (valor instanceof Boolean)
                            valor = (Boolean.TRUE.equals(valor)? "Sim": "Não");
                        else if (valor instanceof Double)
                            valor = NumberFormat.getNumberInstance().format(valor);
                        else if (valor instanceof Date)
                            valor = DateFormat.getInstance().format(valor);
                    }
                    
                    if (formato != null && valor != null && formato.length() > 0)
                        valor = formatarCampo(valor, formato);                    
                    
                    //Aplica máscara
                    mascara = modeloColuna.getMascara(coluna);
                    if (mascara != null && valor != null && mascara.length() > 0) {
                        valor = Texto.aplicaMascara(valor.toString(), mascara);
                    }
                                    
                    linha.add(valor);
                    modeloTabela.setValueAt(valor, indexLinha, coluna.getModelIndex());
                }catch(Exception ee) {
                    System.err.println(ee);
                }
            }            
            //modeloTabela.addRow(linha);
            indexLinha++;
        }        
    }
    
    private Object buscarCampo(String nomeCampo, Object objeto) {        
        StringTokenizer token = new StringTokenizer(nomeCampo, ".", false);
        Object valor = null;
        String nomeMetodo = null;
        boolean simNao = false;
        
        while(token.hasMoreElements()) {
            nomeCampo = (String)token.nextElement();
            if (nomeCampo != null) {                                
                try {
                    Field campo = objeto.getClass().getDeclaredField(nomeCampo);
                    if (campo.getType().equals(Boolean.class) || campo.getType().equals(boolean.class)) {
                        nomeMetodo = "is";
                        simNao = true;
                    } else {
                        nomeMetodo = "get";
                        simNao = false;
                    }
                            
                    nomeMetodo += nomeCampo.substring(0, 1).toUpperCase() + nomeCampo.substring(1);
                    
                    valor = objeto.getClass().getMethod(nomeMetodo, null).invoke(objeto, null);                    
                    if (simNao) {
                        valor = valor.equals(true)? "Sim" : "Não";
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println(nomeMetodo + ": " + e.toString());
                    return null;
                } catch (IllegalAccessException e) {
                    System.out.println(nomeMetodo + ": " + e.toString());
                    return null;
                } catch (InvocationTargetException e) {
                    System.out.println(nomeMetodo + ": " + e.toString());
                   return null;
                } catch (NoSuchFieldException e) {
                    System.out.println(nomeCampo + " não encontrado.");
                    return null;
                }
            }
        }
        return valor;
    }

    private Object formatarCampo(Object valor, String formato) {
        try {
            if (formato.equals("periodo")) {
                Method metodo;
                String nomeMetodo = "formatar" + Texto.primeiraMaiuscula(formato);
                metodo = Formatador.class.getMethod(nomeMetodo, valor.getClass());
                
                valor = metodo.invoke(null, valor);
            } else if (formato.equals("cpfCnpj")) {
                valor = Formatador.formatarCpfCnpj((Long)valor);
            } else if (valor instanceof Date) {
                valor = Formatador.formatar((Date)valor, formato);
            } else if (valor instanceof Number) {
                valor = Formatador.formatar((Number)valor, formato);
            } else if (valor instanceof Boolean) {
                valor = (Boolean.TRUE.equals(valor)? "Sim": "Não");
            } else {
                valor = Formatador.formatar(valor.toString(), formato);
            }
        }catch(Exception e) {
            System.out.println(e.toString() + ": " + valor);
            return valor;
        }
        return valor;
    }
       

    public void setTipoSelecao(TipoSelecao tipoSelecao) {
        this.tipoSelecao = tipoSelecao;
        getSelectionModel().setSelectionMode(tipoSelecao.getCodigo());
    }

    public TipoSelecao getTipoSelecao() {
        return tipoSelecao;
               
    }

    public void setQuantidadeLinhasVisiveis(int quantidadeLinhasVisiveis) {
        this.quantidadeLinhasVisiveis = quantidadeLinhasVisiveis;
    }

    public int getQuantidadeLinhasVisiveis() {
        return quantidadeLinhasVisiveis;
    }
        
    public Dimension getPreferredScrollableViewportSize() {
        int largura, altura;
        largura = (int)getPreferredSize().getWidth();
        altura = quantidadeLinhasVisiveis * getRowHeight();
        
        if (altura == 0)
            altura = (int)getPreferredSize().getHeight();;
        
        Dimension d = getPreferredSize();
        d.setSize(largura, altura);
        
        return d;
    }

    private void carregarGrade(ResultSet rs) throws SQLException {
        SSModeloColuna modeloColuna = (SSModeloColuna)getColumnModel();
        DefaultTableModel modeloTabela = (DefaultTableModel)getModel();
        Vector linha;
        String nomeCampo, formato, mascara;
        Object valor = null;
        TableColumn coluna;
        int indexLinha = 0;
        
        modeloTabela.getDataVector().clear();
        
        if (rs == null)
            return;
        
        while (rs.next()) {
            linha = new Vector();
            modeloTabela.addRow(linha);
            for (int i = 0; i < modeloColuna.getColumnCount(); i++) {
                coluna = modeloColuna.getColumn(i);
                valor = null;
                nomeCampo = modeloColuna.getCampo(coluna);
                try {
                    valor = rs.getObject(nomeCampo);
                
                    formato = modeloColuna.getFormato(coluna);
                    
                    if (formato == null && valor != null) {
                        if (valor instanceof Boolean)
                            valor = (Boolean.TRUE.equals(valor)? "Sim": "Não");
                        else if (valor instanceof Double)
                            valor = NumberFormat.getNumberInstance().format(valor);
                        else if (valor instanceof Date)
                            valor = DateFormat.getInstance().format(valor);
                    }
                    
                    if (formato != null && valor != null && formato.length() > 0)
                        valor = formatarCampo(valor, formato);                    
                    
                    //Aplica máscara
                    mascara = modeloColuna.getMascara(coluna);
                    if (mascara != null && valor != null && mascara.length() > 0) {
                        valor = Texto.aplicaMascara(valor.toString(), mascara);
                    }
                                    
                    linha.add(valor);
                    modeloTabela.setValueAt(valor, indexLinha, coluna.getModelIndex());
                }catch(Exception ee) {
                    System.err.println(ee);
                }
            }            
            //modeloTabela.addRow(linha);
            indexLinha++;
        }             
    }
    

}
