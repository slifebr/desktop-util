package edu.porgamdor.util.desktop.ss;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import edu.porgamdor.util.desktop.ss.util.Texto;
import edu.porgamdor.util.desktop.ss.util.Validacao;


public class SSCampoMascara extends SSCampoTexto implements SSTextoComponente {
    protected JFormattedTextField txtMascara = new JFormattedTextField();    
    private MaskFormatter maskFormatter = new MaskFormatter();
    private boolean formatarAoAplicar = false;
    
    public void setText(String text) {
        if (formatarAoAplicar && Validacao.numero(text)) {            
            String formato = Texto.substitui(Texto.retiraSeparadores(getMascara()), "#", "0");
            NumberFormat nf = new DecimalFormat(formato);
            text = nf.format(Long.valueOf(text));            
        }
        super.setText(text);
    }
    
    public SSCampoMascara() {
    	setCaixaTexto(txtMascara);
        //setComponente(txtMascara);        
        //txtMascara.setColumns(15);
        //if (getDefaultFormatterFactory() == null)
        //    setDefaultFormatterFactory(new DefaultFormatterFactory());
        //getDefaultFormatterFactory().setEditFormatter(maskFormatter);
        //this.add(txtMascara, null);
    }
    
    public void setMascara(String mascara) throws ParseException {        
        maskFormatter.setMask(mascara); 
        atualizarMascara();
    }

    public String getMascara() {
        return maskFormatter.getMask();
    }

    public void setEspacoReservado(char espacoReservado) {        
        maskFormatter.setPlaceholderCharacter(espacoReservado);
        atualizarMascara();
    }

    public char getEspacoReservado() {
        return maskFormatter.getPlaceholderCharacter();
    }
    
    protected void atualizarMascara() {
        maskFormatter.install(txtMascara);
    }
    
    public Object getValue() {
        return txtMascara.getValue();        
    }

    public void setValue(Object value) {
        txtMascara.setValue(value);
        if (value == null)
            setText(null);
    }
    
    public String getClipText() {
        if (getText() == null)
            return null;
        else
            return Texto.retiraSeparadores(getText()).trim();
    }

    public void setFormatarAoAplicar(boolean formatarAoAplicar) {
        this.formatarAoAplicar = formatarAoAplicar;
    }

    public boolean isFormatarAoAplicar() {
        return formatarAoAplicar;
    }
    
    protected DefaultFormatterFactory getDefaultFormatterFactory() {
        return (DefaultFormatterFactory)txtMascara.getFormatterFactory();
    }
    
    protected void setDefaultFormatterFactory(DefaultFormatterFactory dff) {
        txtMascara.setFormatterFactory(dff);
    }
    
}
