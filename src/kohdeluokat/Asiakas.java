package kohdeluokat;

public class Asiakas {
	private int numero;
	private String etunimi;
	private String sukunimi;
	private String osoite;
	private PostinumeroAlue posti;
	
	public Asiakas() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Asiakas(int id, String etunimi, String sukunimi, String osoite, PostinumeroAlue posti) {
		super();
		this.setNumero(id);
		this.setOsoite(osoite);
		this.setEtunimi(etunimi);
		this.setSukunimi(sukunimi);
		this.setPosti(posti);
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int id) {
		numero = 0;
		if (id > 0)
			numero = id;
	}
	public String getEtunimi() {
		return etunimi;
	}
	public void setEtunimi(String etunimi) {
		this.etunimi = null;
		
		this.etunimi = muokkaaHenkiloNimi(etunimi);
	}
	private String muokkaaHenkiloNimi(String nimi)
	{
		String paluu = null, apu;
		int i;
		String sana;
		
		if (nimi != null && nimi.trim().length() > 0)			
		{
			// turhat välit pois
			nimi = nimi.trim();
			nimi = nimi.replaceAll("\\s+", " ");
			nimi = nimi.replaceAll(" - ", "-");
			nimi = nimi.replaceAll("- ", "-");
			nimi = nimi.replaceAll(" -", "-");
			
			//System.out.println(nimi);
			if (nimi.matches ("([a-zåäöA-ZÅÄÖ ]-?){2,30}"))
			{
				nimi = nimi.toLowerCase();
				apu = "";
				// jokainen sana alkaa isolla
				i = nimi.indexOf(' ');
				while (i != -1)
				{
					sana = nimi.substring(0,i);
					sana = sana.substring(0,1).toUpperCase()+sana.substring(1);
					
					apu = apu + sana + " " ;
					
					nimi = nimi.substring(i+1);
					i = nimi.indexOf(' ');
				}
				nimi = nimi.substring(0,1).toUpperCase()+nimi.substring(1);
				apu = apu + nimi;
				// - jälkeinen sana isolla alukirjaimella
				nimi = apu;
				apu="";
				i = nimi.indexOf('-');
				while (i != -1)
				{
					apu = apu + nimi.substring(0,i)+"-";
					nimi = nimi.substring(i+1);
					nimi = nimi.substring(0,1).toUpperCase()+nimi.substring(1);
					
					i = nimi.indexOf('-');
				}
				nimi = nimi.substring(0,1).toUpperCase()+nimi.substring(1);
				apu = apu + nimi;
				
				paluu = apu;
			}
		}
		
		return paluu;
	}
	// katu talonnumero rappu asunto esim. Bertilintie 34 B 12
	private String muokkaaOsoite (String osoite)
		{
			String paluu= null;
			
			if (osoite != null && osoite.trim().length() > 0)
			{
				osoite = osoite.trim();
				osoite = osoite.replaceAll("\\s+"," ");
//				System.out.println(osoite);
				if (osoite.matches("([a-z åäöA-ZÅÄÖ]-?){3,}[0-9]*[a-zåäö A-ZÅÄÖ]*[0-9]*") 
						&& osoite.length() <=30)
					paluu = osoite.substring(0,1).toUpperCase() + osoite.substring(1);
			}
			
			return paluu;
		}
	public String getSukunimi() {
		return sukunimi;
	}
	public void setSukunimi(String sukunimi) {
		this.sukunimi = null;
		
		this.sukunimi = muokkaaHenkiloNimi(sukunimi);
	}
	public String getOsoite() {
		return osoite;
	}
	public void setOsoite(String osoite) {
		this.osoite = muokkaaOsoite(osoite);
	}
	public PostinumeroAlue getPosti() {
		return posti;
	}
	
	public void setPosti(PostinumeroAlue posti) {
		this.posti = posti;
	}
	@Override
	public String toString() {
		return "Asiakas: numero=" + numero + ", etunimi=" + etunimi + ", sukunimi="
				+ sukunimi + ", osoite=" + osoite + " " + posti;
	}
		
		
}