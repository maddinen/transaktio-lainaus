package kontrolli;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kohdeluokat.*;
import dao.*;



@WebServlet("/Lainaus")
public class LainausServletti extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
		

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
		 *      response)
		 */
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {

			RequestDispatcher disp;
			String action = request.getParameter("action");

			System.out.println("action = " + action);

			if (action == null) {
				disp = request.getRequestDispatcher("etusivu.jsp");
				disp.forward(request, response);
			} else if (action.equalsIgnoreCase("hae tietyn lainauksen tiedot")) {
				request.setAttribute("lainaus", null);

//				disp = request.getRequestDispatcher("tili.jsp");
//				disp.forward(request, response);
//			} else if (action.equalsIgnoreCase("hae tili")) {
//				haeTili(request,response);
//				
//			} else if (action.equalsIgnoreCase("tallenna tilinsiirto")) {
//				talletaTilinsiirto(request, response);
//
//			} else if (action.equalsIgnoreCase("tee tilin siirto")) {
//				valmistaTilinsiirto(request, response);

			}
		}

//		private void valmistaTilinsiirto(HttpServletRequest request,
//				HttpServletResponse response) {
//			RequestDispatcher disp;
//			Dao dao = new Dao();
//			try {
//				List<Tili> lista = dao.haeTilit();
//
//				if (lista != null) {
//					request.setAttribute("tililista", lista);
//
//					disp = request.getRequestDispatcher("tilinsiirto.jsp");
//
//					disp.forward(request, response);
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}

//		private void haeTili(HttpServletRequest request,HttpServletResponse response) {
//			
//			RequestDispatcher disp;
//			System.out.println("HAE TILI");
//			String tilinro = request.getParameter("tilinro");
//			
//			try {
//				if (tilinro != null && tilinro.trim().length()>0) {
//					Dao dao = new Dao();
//					Tili tili = dao.haeTili(tilinro);
//
//					if (tili != null) {
//						request.setAttribute("tili", tili);
//					}
//					else {
//						request.setAttribute("tilinro", tilinro);
//						request.setAttribute("tilinroVIRHE",  true);
//					}
//				} else {  // tilinumeroa ei annettu 
//
//					request.setAttribute("tilinroPUUTTUU",  true);
//					
//				}
//				disp = request.getRequestDispatcher("tili.jsp");
//				disp.forward(request, response);
//				
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}

//		private void talletaTilinsiirto(HttpServletRequest request,
//				HttpServletResponse response) {
//			RequestDispatcher disp;
//			Dao dao = new Dao();
//			boolean ok = true, siirtoOk = false;
//			String tilinro1 = request.getParameter("tili1");
//			String tilinro2 = request.getParameter("tili2");
//			String maaraStr = request.getParameter("maara");
//			//System.out.println(tilinro1 + " " + tilinro2 + " " + maaraStr);
//			Tili tili1, tili2;
//			request.setAttribute("tili1", tilinro1);
//			request.setAttribute("tili2", tilinro2);
//			request.setAttribute("maara", maaraStr);
//			try {
//				tili1 = dao.haeTili(tilinro1);
//				tili2 = dao.haeTili(tilinro2);
//				if (tili1 == null) {
//					request.setAttribute("tili1VIRHE", true);
//					request.setAttribute("tili1", tilinro1);
//					ok = false;
//				}
//				if (tili2 == null) {
//					request.setAttribute("tili2VIRHE", true);
//					request.setAttribute("tili2", tilinro2);
//					ok = false;
//				}
//				double summa = Double.parseDouble(maaraStr);
//				if (summa <= 0)
//					throw new NumberFormatException();
//				if (ok == true) {
//					if (tili1.getSaldo() - summa >= 0) {// onko tilin1 saldo
//														// riittava?
//						siirtoOk = dao.tilinsiirto(tili1, tili2, summa);
//						request.setAttribute("SiirtoOK", siirtoOk);
//					
//					} else { // tililla1 ei ole katetta
//						request.setAttribute("EIKATETTA", true);
//					}
//				} 
//				valmistaTilinsiirto(request, response);
//				
//			} catch (NumberFormatException e) {
//
//				request.setAttribute("maaraVIRHE", true);
//				request.setAttribute("maara", maaraStr);
//				valmistaTilinsiirto(request, response);
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
		 *      response)
		 */
		protected void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
		}

	}
	

