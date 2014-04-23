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
package controllers.membre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import models.Commune;
import models.Espece;
import models.Groupe;
import models.InformationsComplementaires;
import models.Membre;
import models.Observation;
import models.StadeSexe;
import models.UTMS;
import controllers.admin.Admin;
import controllers.ajax.expert.requetes.calculs.MaChronologie;
import controllers.expert.MenuExpert;
import functions.excels.exports.MaChronologieExcel;
import play.Play;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.expert.editeTemoignagesAValider;
import views.html.membre.historique;
import views.html.membre.ajax.editeTemoignageMembre;

public class Historique extends Controller {

	public static Result main() throws ParseException, IOException {
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		if(temoin!=null){
			HashMap<String,String> info = new HashMap<String,String>();
			info.put("temoin", temoin.membre_nom);
			session("page","0");
			MaChronologie maChronologie = new MaChronologie(info,0);
			return ok(historique.render(maChronologie));
		}else
			return Admin.nonAutorise();
	}

	public static Result voirPage(int page) throws ParseException, IOException {
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		if(temoin!=null){
			HashMap<String,String> info = new HashMap<String,String>();
			info.put("temoin", temoin.membre_nom);
			session("page",Integer.toString(page));
			MaChronologie maChronologie = new MaChronologie(info,page);
			return ok(historique.render(maChronologie));
		}else
			return Admin.nonAutorise();
	}

	@Security.Authenticated(SecuredMembre.class)
	public static Result editer(Long observation_id){
		Observation observation= Observation.find.byId(observation_id);
		return ok(editeTemoignageMembre.render(observation));
	}
	
	@Security.Authenticated(SecuredMembre.class)
	public static Result editerPoster(Long observation_id){
		Observation observation = Observation.find.byId(observation_id);
		DynamicForm df = DynamicForm.form().bindFromRequest();
		String utm = df.get("utm");
		String lieudit= df.get("lieudit");
		String commune_nom = df.get("ville_nom_reel");
		Integer espece_id = Integer.parseInt(df.get("espece_id"));
		String determinateur = df.get("determinateur");
		String memo = df.get("memo");
		Espece espece = Espece.find.byId(espece_id);
		for(InformationsComplementaires complement : observation.getInfos()){
			String nombre_str = df.get("nombre"+complement.informations_complementaires_id);
			if(!nombre_str.isEmpty()){
				try{
					complement.informations_complementaires_nombre_de_specimens=Integer.parseInt(nombre_str);
				}catch(NumberFormatException e){
					complement.informations_complementaires_nombre_de_specimens=null;
				}
			}else
				complement.informations_complementaires_nombre_de_specimens=null;
			int stade_sexe_id = Integer.parseInt(df.get("stadesexe"+complement.informations_complementaires_id));
			StadeSexe stade_sexe = StadeSexe.find.byId(stade_sexe_id);
			if(espece.espece_sous_groupe.sous_groupe_groupe.getStadesSexes().contains(stade_sexe)){
				complement.informations_complementaires_stade_sexe=stade_sexe;
			}
			if(stade_sexe==null)
				complement.informations_complementaires_stade_sexe=null;
			complement.update();
		}
		observation.observation_fiche.fiche_utm=UTMS.find.byId(utm);
		observation.observation_fiche.fiche_lieudit=lieudit;
		if(commune_nom.isEmpty())
			observation.observation_fiche.fiche_commune=null;
		else{
			Commune commune = Commune.find.where().eq("ville_nom_reel", commune_nom).findUnique();
			if(commune!=null)
				observation.observation_fiche.fiche_commune=commune;
		}
		observation.observation_fiche.fiche_memo=memo;
		observation.observation_fiche.update();
		observation.observation_espece=espece;
		observation.observation_determinateur=determinateur;
		observation.observation_date_derniere_modification=Calendar.getInstance();
		observation.observation_validee=Observation.EN_SUSPEND;
		observation.update();
		return redirect("/historique/page/"+session("page"));
	}

	/**
	 * Télécharge les témoignages.
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result telechargerHistorique() throws ParseException, IOException{
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		HashMap<String,String> info = new HashMap<String,String>();
		info.put("temoin", temoin.membre_nom);
		MaChronologie maChronologie = new MaChronologie(info,MaChronologie.TOUT);
		MaChronologieExcel maChronologieExcel = new MaChronologieExcel(info,maChronologie);
		maChronologieExcel.writeToDisk();
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(Play.application().configuration().getString("xls_generes.path")+maChronologieExcel.getFileName()));
			response().setHeader("Content-Disposition", "attachment; filename="+maChronologieExcel.getFileName());
			return ok(fis);
		} catch (FileNotFoundException e) {
			return notFound("404: File not found");
		}
	}
}
