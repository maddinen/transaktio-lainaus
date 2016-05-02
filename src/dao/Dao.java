package dao;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kohdeluokat.*;



public class Dao {
	private Connection yhdista() throws SQLException { // yhdistetään tietokantaan
		Connection tietokantayhteys = null;
		// Alkumääritykset
		String JDBCAjuri = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://localhost:15001/a1500616";
		String kayttajanimi = "a1500882";
		String salasana = "suXAsP63h";

		try {
			Class.forName(JDBCAjuri).newInstance(); // ajurin lataus

			// otetaan yhteys tietokantaan
			tietokantayhteys = DriverManager.getConnection(url, kayttajanimi,
					salasana);

			// yhteyden otto ei onnistu
		} catch (SQLException sqlE) {
			System.err.println("Tietokantayhteyden avaaminen ei onnistunut. "
					+ url + "\n" + sqlE.getMessage() + " " + sqlE.toString()
					+ "\n");
			throw (sqlE);
		} catch (Exception e) {
			System.err.println("TIETOKANTALIITTYN VIRHETILANNE: "
					+ "JDBC:n omaa tietokanta-ajuria ei loydy.\n\n"
					+ e.getMessage() + " " + e.toString() + "\n");
			e.printStackTrace();
			System.out.print("\n");
			throw (new SQLException("Tietokanta-ajuria ei loydy!"));
		}
		return tietokantayhteys;
	}
	
	// haetaan tietokannasta kaikki asiat mitä kokonaisuudessaan halutaan (????)
	public List<Lainaus> haeLainaukset() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet tulosjoukko = null;
		List<Lainaus> lainauslista = null;
		//Lainaus lainaus = null;
		Asiakas asiakas = null;
		//boolean jatkuu = false;
		String sql = "select la.la_numero, la.lainauspvm, a.as_numero, a.as_postinro,  a.as_etunimi,  a.as_sukunimi, a.as_osoite, p.postitmp, k.isbn, k.nimi, k.kirjoittaja, k.painos, k.kustantaja, n.nidenro, nl.palautuspvm from Lainaus la JOIN Asiakas a ON a.as_numero = la.as_numero JOIN PostinumeroAlue p ON p.postinro = a.as_postinro JOIN Niteenlainaus nl ON nl.la_numero = la.la_numero JOIN Nide n ON n.isbn = nl.isbn and nl.nidenro=n.nidenro JOIN Kirja k ON k.isbn = n.isbn;";

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				/*
				 * level - one of the following Connection constants:
				 * Connection.TRANSACTION_READ_UNCOMMITTED ,
				 * Connection.TRANSACTION_READ_COMMITTED ,
				 * Connection.TRANSACTION_REPEATABLE_READ or
				 * Connection.TRANSACTION_SERIALIZABLE.
				 */
				// eristyvyystason määritys
				conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				stmt = conn.prepareStatement(sql);

				tulosjoukko = stmt.executeQuery();
				stmt.close();

				if (tulosjoukko != null) {
					conn.commit(); // lopeta transaktio hyväksymällä
					conn.close(); // sulje yhteys kantaan

					asiakas = teeAsiakas(tulosjoukko);
					lainauslista = teeLainaus(tulosjoukko);
						
					tulosjoukko.close();

				} else // lainauksia ei löytynyt
				{
					lainauslista = null;
					conn.commit();
					conn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (conn != null && conn.isClosed() == false) {
				try {
					conn.rollback(); // peruuta transaktio
					conn.close(); // yhteys poikki
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}
		return lainauslista;
		//jotakin muuta tässä pitäisi palauttaa(?)
	}
	
	
	// tarvitseeko tätä koko metodia mihinkään???
	public List<Lainaus> teeLainausNumerot(ResultSet tulosjoukko) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		//ResultSet tulosjoukko = null; // SQL-kyselyn tulokset
		List<Lainaus> lainausnumerot = null;
		NiteenLainaus lainaus = null;
		
		try {
			conn = yhdista();
			if (conn != null){
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				// ????
				//String sqlSelect = "SELECT la_numero FROM Lainaus;";
				//stmt = conn.prepareStatement(sqlSelect);
				//tulosjoukko = stmt.executeQuery(sqlSelect);
				stmt.close();
				
				if (tulosjoukko != null && tulosjoukko.next()){
					conn.commit();
					conn.close();
					
					lainaus = teeNiteenLainaus(tulosjoukko);
					tulosjoukko.close();
				} else {
					lainaus = null;
					conn.commit();
					conn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (conn != null && conn.isClosed() == false) {
				try {
					conn.rollback();
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}
		return lainausnumerot;
	}
	
	// muodostetaan teeLainaukset() -metodin tulosteen perusteella oliot kirja, nide ja niteenlainaus (????)
	private NiteenLainaus teeNiteenLainaus (ResultSet tulosjoukko) throws SQLException {
			NiteenLainaus niteenLainaus = null;
			Kirja kirja = null;
			Nide nide = null;
			String isbn, nimi, kirjoittaja, painos, kustantaja;
			int nidenro;
			Date palautusPvm;
			
			if (tulosjoukko != null) {
				try {
					isbn = tulosjoukko.getString("isbn");
					nimi = tulosjoukko.getString("nimi");
					kirjoittaja = tulosjoukko.getString("kirjoittaja");
					painos = tulosjoukko.getString("painos");
					kustantaja = tulosjoukko.getString("kustantaja");
					kirja = new Kirja(isbn, nimi, kirjoittaja, painos, kustantaja);
					
					nidenro = tulosjoukko.getInt("nidenro");
					nide = new Nide(kirja, nidenro);
					
					palautusPvm = tulosjoukko.getDate("palautuspvm");
					niteenLainaus = new NiteenLainaus(nide, palautusPvm);
					
					} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
			}
			return niteenLainaus;
		}
	
	// muodostetaan teeLainaukset() -metodin tulosteen perusteella olio asiakas
	private Asiakas teeAsiakas(ResultSet tulosjoukko) throws SQLException {
		Asiakas asiakas = null;
		int numero;
		String etunimi;
		String sukunimi, osoite;
		String postinro;
		String postitmp;
		PostinumeroAlue posti = null;

		if (tulosjoukko != null) {
			try {
				numero = tulosjoukko.getInt("as_numero");
				etunimi = tulosjoukko.getString("as_etunimi");
				sukunimi = tulosjoukko.getString("as_sukunimi");
				osoite = tulosjoukko.getString("as_osoite");
				postinro = tulosjoukko.getString("as_postinro");
				postitmp = tulosjoukko.getString("postitmp");
				posti = new PostinumeroAlue(postinro, postitmp);
				asiakas = new Asiakas(numero, etunimi, sukunimi, osoite, posti);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return asiakas;
	}

	// lisätään oliot lainaus, asiakas ja niteenlainaus lainauslistaan(???)
	private List<Lainaus> teeLainaus(ResultSet tulosjoukko) throws SQLException {
		Lainaus lainaus = null;
		int numero, edel;
		Date lainausPvm;
		Asiakas lainaaja;
		NiteenLainaus nLainaus;
		boolean muuttuja = false;
		List<Lainaus> lainausLista = null;
		
		muuttuja = tulosjoukko.next();
		
		while (muuttuja) {
			try {
				numero = tulosjoukko.getInt("la_numero");
				lainausPvm = tulosjoukko.getDate("lainauspvm");
				System.out.println(numero);
				lainaus = new Lainaus(numero, lainausPvm); 
				if (lainausLista == null) {
					lainausLista = new ArrayList<Lainaus>();
				}
				lainausLista.add(lainaus);
				lainaaja = teeAsiakas(tulosjoukko);
				
				lainaus.setLainaaja(lainaaja);
				
				nLainaus = teeNiteenLainaus(tulosjoukko);
				lainaus.addNiteenLainaus(nLainaus);
				
				muuttuja = tulosjoukko.next();
				
				while (muuttuja && numero == tulosjoukko.getInt("la_numero")) {
					nLainaus = teeNiteenLainaus(tulosjoukko);
					lainaus.addNiteenLainaus(nLainaus);
					muuttuja = tulosjoukko.next();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return lainausLista;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ------------------------------------------------------------------------------------------
	
//	public Tili haeTili(String numero) throws SQLException {
//		String sql = "SELECT tilinro, saldo, a.numero AS asiakasnumero, "
//				+ "etunimi, sukunimi, osoite, p.postinro AS postinro, postitmp "
//				+ " FROM tili t JOIN asiakas a ON t.omistaja=a.numero "
//				+" JOIN postinumeroalue p ON a.postinro=p.postinro "
//				+" WHERE tilinro=?"; 
//		
//		PreparedStatement preparedStatement = null; // suoritettava SQL-lause
//		ResultSet tulosjoukko = null; // SQL-kyselyn tulokset
//		Connection conn = null;
//		Tili tili=null;
//		try {
//			conn = yhdista();
//			if (conn != null) {
//				conn.setAutoCommit(false);
//				/*
//				 * level - one of the following Connection constants:
//				 * Connection.TRANSACTION_READ_UNCOMMITTED ,
//				 * Connection.TRANSACTION_READ_COMMITTED ,
//				 * Connection.TRANSACTION_REPEATABLE_READ or
//				 * Connection.TRANSACTION_SERIALIZABLE.
//				 */
//				// eristyvyystason m��ritys
//				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//				preparedStatement = conn.prepareStatement(sql);
//				preparedStatement.setString(1, numero);
//				tulosjoukko = preparedStatement.executeQuery();
//				preparedStatement.close();
//
//				if (tulosjoukko != null && tulosjoukko.next()) {
//					conn.commit(); // lopeta transaktio hyv�ksym�ll�
//					conn.close(); // sulje yhteys kantaan heti
//
//					tili = teeTili(tulosjoukko);
//					tulosjoukko.close();
//				} else // tilia ei l�ytynyt
//				{
//					tili = null;
//					conn.commit();
//					conn.close();
//				}
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SQLException();
//		} finally {
//			if (conn != null && conn.isClosed() == false) {
//				try {
//					conn.rollback(); // peruuta transaktio
//					conn.close(); // yhteys poikki
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new SQLException();
//				}
//			}
//		}
//		return tili;
//	}

//	private Tili teeTili(ResultSet tulosjoukko) throws SQLException {
//		Tili tili = null;
//		String numero;
//		double saldo;
//		Asiakas omistaja;
//		
//
//		if (tulosjoukko != null) {
//			try {
//				// System.out.println(tulosjoukko.getInt("lainausnumero") + " "
//				// + tulosjoukko.getString("lainauspvm"));
//				numero = tulosjoukko.getString("tilinro");
//				saldo = tulosjoukko.getDouble("saldo");
//				omistaja = teeAsiakas(tulosjoukko);
//				tili = new Tili(numero, saldo, omistaja);
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw e;
//			}
//		}
//
//		return tili;
//	}
	//a.as_numero, a.as_postinro,  a.as_etunimi,  a.as_sukunimi, a.as_osoite, p.postitmp, 
	
//	public boolean tilinsiirto(Tili tili1, Tili tili2, double maara)
//			throws SQLException {
//		boolean ok = false;
//		
//		System.out.println("DAO TILINSIIRTO  " + maara +"\n" + tili1 + " \n" + tili2);
//		String sql ="UPDATE tili SET saldo = saldo - ? WHERE tilinro=?";
//		String sql2 = "UPDATE tili SET saldo = saldo + ? WHERE tilinro =?";
//		Connection conn = null;
//		PreparedStatement preparedStatement;
//		int  lkm =0, i;
//		int deadlock = 0;
//		final int TOISTO= 3;
//			
//		if (tili1 != null && tili2 != null)
//		{
//			while (deadlock != TOISTO) {
//			try
//			{
//				if (deadlock != 0)
//					Thread.sleep(10000); // odota hiukan
//				conn = yhdista(); // ota yhteys kantaan
//				if (conn != null)
//				{
//					conn.setAutoCommit(false);// poista automaattinen transaktion hyv�ksyminen
//					/*
//						 * level - one of the following Connection constants:
//						 * Connection.TRANSACTION_READ_UNCOMMITTED ,
//						 * Connection.TRANSACTION_READ_COMMITTED ,
//						 * Connection.TRANSACTION_REPEATABLE_READ or
//						 * Connection.TRANSACTION_SERIALIZABLE.
//						 */
//						// eristyvyystason m��ritys 
//					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//					preparedStatement = conn.prepareStatement(sql);
//						
//					preparedStatement.setDouble(1, maara);
//					preparedStatement.setString(2, tili1.getTilinro()); 
//					lkm = preparedStatement.executeUpdate();
//					preparedStatement.close();
//						
//					if (lkm == 1 )
//					{
//						Thread.sleep(10000);
//						preparedStatement = conn.prepareStatement(sql2);
//						preparedStatement.setDouble (1, maara);
//						preparedStatement.setString(2, tili2.getTilinro());
//							
//						lkm = preparedStatement.executeUpdate();
//						preparedStatement.close();
//					}
//					if (lkm == 1) // jos jokainen rivi lis�ttiin
//					{
//						ok = true;
//						conn.commit();  // hyv�ksy transaktio
//						conn.close();	// lopetta yhteys kantaan
//						deadlock = TOISTO; // kaikki hyvin, ei deadlockia
//					}
//					else
//					{
//						ok = false;
//						conn.rollback(); //hylk�� transaktio
//						conn.close();
//						deadlock = TOISTO; // ei deadlockia
//					}
//				}
//			}
//			catch (SQLException e)
//		     {
//		       	//e.printStackTrace();
//		       	System.out.println("SQLState: " + e.getSQLState());
//		    	System.out.println("errorCode " + e.getErrorCode());
//		    	if ( e.getSQLState().equals("40001") && e.getErrorCode()== 1213)
//		    	{
//		    		conn.rollback();  // peruuta transaktio
//		    		deadlock++;  // toistetaan trasnaktion uudelleen 
//		    		
//		    	}
//		    	else {
//		    		throw e;
//		    	}
//		     }
//		     catch (Exception e)
//		     {
//		       	e.printStackTrace();
//		       	throw new SQLException();
//		     }
//		     /*finally {
//				if (conn != null &&  conn.isClosed() == false) 
//				{
//					try 
//					{
//						conn.rollback();  // peruuta transaktio
//						conn.close();     // yhteys poikki
//					} 
//					catch(Exception e) 
//					{
//						e.printStackTrace();
//			        	throw new SQLException();
//					}
//				}*/
//			}// end of while
//						
//		}// end of if
//		if (conn != null &&  conn.isClosed() == false) 
//		{
//		try 
//		{
//			conn.rollback();  // peruuta transaktio
//			conn.close();     // yhteys poikki
//		} 
//		catch(Exception e) 
//		{
//			e.printStackTrace();
//        	throw new SQLException();
//		}
//	}
//		return ok;
//	}

	/**
	 * Hakee kannasta tilitaulun sis�ll�n ArrayList-tyyppiseen kokoelmaan
	 * yhdess� transaktiossa.
	 *
	 * @return kaikki kannan sis�lt�m�t tilit yhdess� kokoelmassa
	 * @throws OdottamatonTietokantaPoikkeus
	 *             jos tapahtuu tietokantavirhe
	 * @throws KannassaEiTilejaPoikkeus
	 *             jos kannasta ei l�ydy v�hint��n kahta tili�.
	 */
	
	
//	"SELECT la.la_numero, la.lainauspvm, a.as_numero, a.as_postinro,  a.as_etunimi,  a.as_sukunimi, a.as_osoite, p.postitmp, k.isbn, k.nimi, "
//	+ " k.kirjoittaja, k.painos,  k.kustantaja, n.nidenro, nl.palautuspvm FROM Lainaus la JOIN Asiakas a ON a.as_numero = la.as_numero " 
//	+ " JOIN PostinumeroAlue p ON p.postinro = a.as_postinro JOIN Niteenlainaus nl ON nl.la_numero = la.la_numero JOIN Nide n ON n.isbn = nl.isbn and nl.nidenro=n.nidenro"
//	+" JOIN Kirja k ON k.isbn = n.isbn;";
	

//	private Tili teeTili(ResultSet tulosjoukko) throws SQLException {
//	Tili tili = null;
//	String numero;
//	double saldo;
//	Asiakas omistaja;
//	
//
//	if (tulosjoukko != null) {
//		try {
//			// System.out.println(tulosjoukko.getInt("lainausnumero") + " "
//			// + tulosjoukko.getString("lainauspvm"));
//			numero = tulosjoukko.getString("tilinro");
//			saldo = tulosjoukko.getDouble("saldo");
//			omistaja = teeAsiakas(tulosjoukko);
//			tili = new Tili(numero, saldo, omistaja);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	return tili;
//}
	
	
	
	
	
	
	
//	public List<Tili> haeTilit() throws SQLException {
//
//		List<Tili> lista = null;
//		Connection conn = null;
//		String sql = "SELECT tilinro, saldo, a.numero AS asiakasnumero, "
//				+ "etunimi, sukunimi, osoite, p.postinro AS postinro, postitmp "
//				+ " FROM tili t JOIN asiakas a ON t.omistaja=a.numero "
//				+" JOIN postinumeroalue p ON p.postinro=a.postinro "
//				+ "ORDER BY tilinro";
//		PreparedStatement preparedStatement = null; // suoritettava SQL-lause
//		ResultSet tulosjoukko = null; // SQL-kyselyn tulokset
//		Tili tili = null;
//		Asiakas asiakas;
//		boolean jatkuu = false;
//	
//		try {
//			conn = yhdista();
//			if (conn != null) {
//				conn.setAutoCommit(false);
//				/*
//				 * level - one of the following Connection constants:
//				 * Connection.TRANSACTION_READ_UNCOMMITTED ,
//				 * Connection.TRANSACTION_READ_COMMITTED ,
//				 * Connection.TRANSACTION_REPEATABLE_READ or
//				 * Connection.TRANSACTION_SERIALIZABLE.
//				 */
//				// eristyvyystason m��ritys
//				conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
//				preparedStatement = conn.prepareStatement(sql);
//
//				tulosjoukko = preparedStatement.executeQuery();
//				preparedStatement.close();
//
//				if (tulosjoukko != null) {
//					conn.commit(); // lopeta transaktio hyv�ksym�ll�
//					conn.close(); // sulje yhteys kantaan
//
//					jatkuu = tulosjoukko.next();
//					while (jatkuu) {
//						tili = teeTili(tulosjoukko);
//						
//						if (lista == null)
//							lista = new ArrayList<Tili>();
//						lista.add(tili); // vie lainaus listaan
//
//						jatkuu = tulosjoukko.next();
//					}
//					tulosjoukko.close();
//
//				} else // lainauksia ei l�ytynyt
//				{
//					lista = null;
//					conn.commit();
//					conn.close();
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SQLException();
//		} finally {
//			if (conn != null && conn.isClosed() == false) {
//				try {
//					conn.rollback(); // peruuta transaktio
//					conn.close(); // yhteys poikki
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new SQLException();
//				}
//			}
//		}
//
//		return lista;
//
//	}
	
//	public static void main (String[] Args) {
//		
//		Dao dao = new Dao();
//		List<Lainaus> lainauslista;
//		
//		try {
//			lainauslista = dao.haeLainaukset();
//			for (int i = 0; i < lainauslista.size(); i++) {
//				System.out.println(lainauslista.get(i));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
