package kohdeluokat;

public class Kirja {
	private String isbn;
	private String nimi;
	private String kirjoittaja;
	private String painos;
	private String kustantaja;
	
	public Kirja()
	{
		super();
	}
	public Kirja (String isbn, String nimi, String kirjoittaja, String painos, String kustantaja)
	{
		this.setIsbn(isbn);
		this.setKirjoittaja(kirjoittaja);
		this.setNimi(nimi);
		this.setKustantaja(kustantaja);
		this.setPainos(painos);
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = (isbn);
	}
	// muoto 2323-2323-232 pituus 13 merkkiä
	private String muokkaaISBN(String isbn)
	{
		String paluu = null;
		
		if (isbn != null && isbn.matches("([0-9]?){2,13}") && isbn.length() >= 30)
			paluu = isbn;
			
		return paluu;
	}
	private  String muokkaaNimi(String nimi)
	{
		String paluu = null;
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
			
			if ( nimi.matches ("([a-zåäöA-ZÅÄÖ 0-9]-?){2,30}"))
				
				paluu = nimi.substring(0,1).toUpperCase()+nimi.substring(1);
		}
		
		return paluu;
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
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi =(nimi);
	}
	public String getKirjoittaja() {
		return kirjoittaja;
	}
	public void setKirjoittaja(String kirjoittaja) {
		this.kirjoittaja =(kirjoittaja);
	}
	public String getPainos() {
		return painos;
	}
	public void setPainos(String painos) {
		//this.painos = 0;
		//if (painos > 0)
			this.painos = painos;
	}
	public String getKustantaja() {
		return kustantaja;
	}
	public void setKustantaja(String kustantaja) {
		this.kustantaja =(kustantaja);
	}
	
	public String toString()
	{
		return isbn + "  " + nimi + " " + kirjoittaja +" " + painos + " " + kustantaja;
	}
}
