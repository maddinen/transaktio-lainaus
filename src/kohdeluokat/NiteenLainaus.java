package kohdeluokat;

import java.text.SimpleDateFormat;
import java.util.Date;



public class NiteenLainaus {
	private Nide nide;
	private Date palautusPvm;
	
	public NiteenLainaus()
	{
		super();
		palautusPvm = null;
		nide = null;
	}
	public NiteenLainaus(Nide nide, Date palautuspvm)
	{
		this.nide = nide;
		this.setPalautusPvm (palautuspvm);
	}
	public Nide getNide() {
		return nide;
	}
	public void setNide(Nide nide) {
		this.nide = nide;
	}
	public Date getPalautusPvm() {
		Date apu = null;
		
		if (palautusPvm != null)
			apu = (Date) palautusPvm.clone();
		return apu;
	}
	public void setPalautusPvm(Date palautusPvm) {
		this.palautusPvm = null;
		if (palautusPvm != null)
			this.palautusPvm = (Date) palautusPvm.clone();
	}
	public String toString()
	{
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
		String pvm="";
		if ( palautusPvm != null)
			pvm = f.format(palautusPvm);
		return nide + " " + pvm;
	}
	
}
