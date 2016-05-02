package kohdeluokat;



public class PostinumeroAlue {
	private String postinro;
	private String postitmp;
	
	public PostinumeroAlue()
	{
		postinro=postitmp= null;
	}
	public PostinumeroAlue(String postinro, String postitmp)
	{
		this.setPostinro(postinro);
		this.setPostitmp(postitmp);
	}
	public String getPostinro() {
		return postinro;
	}
	public void setPostinro(String postinro) {
		this.postinro = null;
		if(postinro!= null  && postinro.trim().matches("\\d{5}") )
			this.postinro = postinro.trim();
	}
	public String getPostitmp() {
		return postitmp;
	}
	public void setPostitmp(String postitmp) {
		this.postitmp = null;
		if (postitmp != null && postitmp.trim().length() > 0)
			this.postitmp = postitmp.trim().toUpperCase();
	}
	
	public String toString()
	{
		return postinro + " " + postitmp;
	}
}
