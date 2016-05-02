package kohdeluokat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Lainaus {
	private int numero;
	private Date lainausPvm;
	private Asiakas lainaaja;
	private ArrayList<NiteenLainaus> lista;
	
	public Lainaus()
	{
		super();
		numero = 0;
		lainausPvm = new Date();
		lainaaja = null;
		lista = null;
	}
	public Lainaus(int numero, Date LainausPvm, Asiakas lainaaja)
	{
		this.setNumero(numero);
		this.setLainaaja(lainaaja);
		this.setLainausPvm(LainausPvm);
	}
	public Lainaus(int numero, Date LainausPvm)
	{
		this.setNumero(numero);
		this.setLainausPvm(LainausPvm);
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = 0;
		if (numero > 0)
			this.numero = numero;
	}
	public Date getLainausPvm() {
		Date apu = null;
		
		if (lainausPvm != null)
			apu = (Date) lainausPvm.clone();
		return apu;
	}
	public String getLainausPcmDB()
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(lainausPvm);
	}
	public void setLainausPvm(Date lainausPvm) {

		if (lainausPvm != null)
			this.lainausPvm = (Date) lainausPvm.clone();
	
	}
	public Asiakas getLainaaja() {
		return lainaaja;
	}
	public void setLainaaja(Asiakas lainaaja) {
		this.lainaaja = lainaaja;
	}
	public void addNiteenLainaus(NiteenLainaus nl)
	{
		if (nl != null)
		{
			if (lista == null)
				lista = new ArrayList<NiteenLainaus>();
			
			lista.add(nl);
		}
	}
	public NiteenLainaus getNiteenLainaus(int i)
	{
		NiteenLainaus paluu = null;
		if (lista != null && i <lista.size())
			paluu = lista.get(i);
		
		return paluu;
	}
	public ArrayList<NiteenLainaus> getLista() {
		return lista;
	}
	public String toString()
	{
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
		String pvm= f.format(lainausPvm);
		
		String paluu=null;
		paluu = numero + " " + pvm + "\n" + lainaaja ;
		if (lista != null)
		{
			for (int i = 0;i <lista.size();i++)
				paluu = paluu + "\n" + lista.get(i);
		}
		return paluu;
	}

	
	
}
