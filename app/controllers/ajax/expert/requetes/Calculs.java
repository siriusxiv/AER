/*********************************************************************************
 * 
 *   Copyright 2014 BOUSSEJRA Malik Olivier, HALDEBIQUE Geoffroy, ROYER Johan
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 ********************************************************************************/
package controllers.ajax.expert.requetes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.ajax.expert.requetes.calculs.MaillesParEspece;
import controllers.ajax.expert.requetes.calculs.CarteSomme;
import controllers.ajax.expert.requetes.calculs.CarteSommeBiodiversite;
import controllers.ajax.expert.requetes.calculs.ChronologieDUnTemoin;
import controllers.ajax.expert.requetes.calculs.HistogrammeDesImagos;
import controllers.ajax.expert.requetes.calculs.TemoinsParPeriode;
import functions.excels.exports.MaillesParEspeceExcel;
import functions.excels.exports.CarteSommeBiodiversiteExcel;
import functions.excels.exports.CarteSommeExcel;
import functions.excels.exports.ChronologieDUnTemoinExcel;
import functions.excels.exports.HistogrammeDesImagosExcel;
import functions.excels.exports.TemoinsParPeriodeExcel;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.expert.requetes.ajax.resultats.temoinsParPeriode;
import views.html.expert.requetes.ajax.resultats.histogrammeDesImagos;
import views.html.expert.requetes.ajax.resultats.carteSomme;
import views.html.expert.requetes.ajax.resultats.carteSommeBiodiversite;
import views.html.expert.requetes.ajax.resultats.maillesParEspece;
import views.html.expert.requetes.ajax.resultats.chronologieDUnTemoin;

public class Calculs extends Controller {
	
	public static Result temoinsParPeriode() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		List<TemoinsParPeriode> temoins = TemoinsParPeriode.calculeTemoinsParPeriode(info);
		TemoinsParPeriodeExcel tppe = new TemoinsParPeriodeExcel(info,temoins);
		tppe.writeToDisk();
		return ok(temoinsParPeriode.render(temoins,info,tppe.getFileName()));
	}
	public static Result histogrammeDesImagos() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		HistogrammeDesImagos hdi = new HistogrammeDesImagos(info);
		HistogrammeDesImagosExcel hdie = new HistogrammeDesImagosExcel(info,hdi);
		hdie.writeToDisk();
		return ok(histogrammeDesImagos.render(hdi,info,hdie.getFileName()));
	}
	public static Result carteSomme() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		CarteSomme cs = new CarteSomme(info);
		CarteSommeExcel cse = new CarteSommeExcel(info,cs);
		cse.writeToDisk();
		return ok(carteSomme.render(cs,info,cse.getFileName()));
	}
	public static Result carteSommeBiodiversite() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		CarteSommeBiodiversite csb = new CarteSommeBiodiversite(info);
		CarteSommeBiodiversiteExcel csbe = new CarteSommeBiodiversiteExcel(info,csb);
		csbe.writeToDisk();
		return ok(carteSommeBiodiversite.render(csb,info,csbe.getFileName()));
	}
	public static Result chronologieDUnTemoin() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		ChronologieDUnTemoin cdut = new ChronologieDUnTemoin(info);
		ChronologieDUnTemoinExcel cdute = new ChronologieDUnTemoinExcel(info,cdut);
		cdute.writeToDisk();
		return ok(chronologieDUnTemoin.render(cdut,info,cdute.getFileName()));
	}
	public static Result maillesParEspece() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		MaillesParEspece mpe = new MaillesParEspece(info);
		MaillesParEspeceExcel mpee = new MaillesParEspeceExcel(info,mpe);
		mpee.writeToDisk();
		return ok(maillesParEspece.render(mpe,info,mpee.getFileName()));
	}
	
	/**
	 * Tranforme la DynamicForm en Map pour pouvoir l'utiliser librement plus tard.
	 * @param df
	 * @return
	 */
	public static Map<String,String> getData(DynamicForm df){
		Map<String,String> info = new HashMap<String,String>();
		info.put("groupe", df.get("groupe"));
		info.put("sous_groupe", df.get("sous_groupe"));
		info.put("espece", df.get("espece"));
		info.put("stade", df.get("stade"));
		info.put("maille", df.get("maille"));
		info.put("temoin", df.get("temoin"));
		info.put("jour1", df.get("jour1"));
		info.put("mois1", df.get("mois1"));
		info.put("annee1", df.get("annee1"));
		info.put("jour2", df.get("jour2"));
		info.put("mois2", df.get("mois2"));
		info.put("annee2", df.get("annee2"));
		return info;
	}
	
	/**
	 * Renvoie la date 1 de le Map sous forme de Calendar
	 * @param info
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getDataDate1(Map<String,String> info) throws ParseException{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
		c.setTime(date_format.parse(info.get("jour1")+"/"+info.get("mois1")+"/"+info.get("annee1")));
		return c;
	}
	/**
	 * Renvoie la date 2 de le Map sous forme de Calendar
	 * @param info
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getDataDate2(Map<String,String> info) throws ParseException{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
		c.setTime(date_format.parse(info.get("jour2")+"/"+info.get("mois2")+"/"+info.get("annee2")));
		return c;
	}
}
