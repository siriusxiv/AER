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

import models.Fiche;
import models.Membre;
import controllers.admin.Admin;
import controllers.ajax.expert.requetes.calculs.ChronologieDUnTemoin;
import functions.excels.exports.ChronologieDUnTemoinExcel;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.membre.historique;

public class Historique extends Controller {

	public static Result main() throws ParseException, IOException {
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		if(temoin!=null){
			HashMap<String,String> info = new HashMap<String,String>();
			info.put("groupe", "0");
			info.put("sous_groupe", "0");
			info.put("espece", "0");
			info.put("stade", "0");
			info.put("maille", "");
			info.put("temoin", temoin.membre_nom);
			Calendar plus_vieux = Fiche.getPlusVieuxTemoignage().fiche_date;
			info.put("jour1", Integer.toString(plus_vieux.get(Calendar.DAY_OF_MONTH)));
			info.put("mois1", Integer.toString(plus_vieux.get(Calendar.MONTH)));
			info.put("annee1", Integer.toString(plus_vieux.get(Calendar.YEAR)));
			Calendar now = Calendar.getInstance();
			info.put("jour2", Integer.toString(now.get(Calendar.DAY_OF_MONTH)));
			info.put("mois2", Integer.toString(now.get(Calendar.MONTH)));
			info.put("annee2", Integer.toString(now.get(Calendar.YEAR)));
			ChronologieDUnTemoin cdut = new ChronologieDUnTemoin(info);
			ChronologieDUnTemoinExcel cdute = new ChronologieDUnTemoinExcel(info,cdut);
			cdute.writeToDisk();
			return ok(historique.render(cdut,cdute.getFileName()));
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Télécharge un fichier.
	 * @return
	 * @throws FileNotFoundException 
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result telechargerFichier(String filename){
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(Play.application().configuration().getString("xls_generes.path")+filename));
			response().setHeader("Content-Disposition", "attachment; filename="+filename);
			return ok(fis);
		} catch (FileNotFoundException e) {
			return notFound("404: File not found");
		}
	}
}
