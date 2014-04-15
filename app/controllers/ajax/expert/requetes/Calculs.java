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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Espece;
import models.Groupe;
import models.InformationsComplementaires;
import models.Observation;
import models.SousGroupe;
import models.StadeSexe;
import models.UTMS;
import controllers.ajax.expert.requetes.calculs.TemoinsParPeriode;
import functions.excels.TemoinsParPeriodeExcel;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.expert.requetes.ajax.resultats.temoinsParPeriode;

public class Calculs extends Controller {
	
	public static Result temoinsParPeriode() throws ParseException, IOException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		List<TemoinsParPeriode> temoins = TemoinsParPeriode.calculeTemoinsParPeriode(info);
		TemoinsParPeriodeExcel tppe = new TemoinsParPeriodeExcel(info,temoins);
		tppe.writeToDisk();
		return ok(temoinsParPeriode.render(temoins,info,tppe.getFileName()));
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
	
	public static List<Observation> getObservations(Map<String,String> info) throws ParseException{
		Espece espece = Espece.find.byId(Integer.parseInt(info.get("espece")));
		SousGroupe sous_groupe = SousGroupe.find.byId(Integer.parseInt(info.get("sous_groupe")));
		Groupe groupe = Groupe.find.byId(Integer.parseInt(info.get("groupe")));
		StadeSexe stade_sexe = StadeSexe.find.byId(Integer.parseInt(info.get("stade")));
		List<UTMS> mailles = UTMS.parseMaille(info.get("maille"));
		Calendar date1 = Calculs.getDataDate1(info);
		Calendar date2 = Calculs.getDataDate2(info);
		List<Observation> observations;
		if(espece!=null){
			observations = Observation.find.where()
								.eq("observation_espece",espece)
								.between("observation_fiche.fiche_date", date1.getTime(), date2.getTime())
								.in("observation_fiche.fiche_utm", mailles)
								.findList();
		}else if(sous_groupe!=null){
			observations = Observation.find.where()
								.eq("observation_espece.espece_sous_groupe",sous_groupe)
								.between("observation_fiche.fiche_date", date1.getTime(), date2.getTime())
								.in("observation_fiche.fiche_utm", mailles)
								.findList();
		}else if(groupe!=null){
			observations = Observation.find.where()
								.eq("observation_espece.espece_sous_groupe.sous_groupe_groupe",groupe)
								.between("observation_fiche.fiche_date", date1.getTime(), date2.getTime())
								.in("observation_fiche.fiche_utm", mailles)
								.findList();
		}else{
			observations = Observation.find.where()
					.between("observation_fiche.fiche_date", date1.getTime(), date2.getTime())
					.in("observation_fiche.fiche_utm", mailles)
					.findList();
		}
		//On enlève les stades/sexes non concernés
		List<Observation> observationsAvecStadeSexe = new ArrayList<Observation>();
		if(stade_sexe!=null){
			for(Observation observation : observations){
				List<InformationsComplementaires> complements = InformationsComplementaires.find.where().eq("informations_complementaires_observation", observation).findList();
				for(InformationsComplementaires complement : complements){
					if(stade_sexe.equals(complement.informations_complementaires_stade_sexe)
							&& !observationsAvecStadeSexe.contains(observation)){
						observationsAvecStadeSexe.add(observation);
					}
				}
			}
			observations = observationsAvecStadeSexe;
		}
		return observations;
	}
}
