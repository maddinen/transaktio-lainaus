package kohdeluokat;

public class Nide {
	private Kirja kirja;
	private int nidenro;
	
	public Nide()
	{
		super();
	}
	public Nide (Kirja kirja, int nidenro)
	{
		this.kirja = kirja;
		this.setNidenro(nidenro);
	}
	public Kirja getKirja() {
		return kirja;
	}
	public void setKirja(Kirja kirja) {
		this.kirja = kirja;
	}
	public int getNidenro() {
		return nidenro;
	}
	public void setNidenro(int nidenro) {
		this.nidenro = 0;
		if (nidenro > 0)
			this.nidenro = nidenro;
	}
	public String toString ()
	{
		return kirja + " " + nidenro;
	}
	
}
