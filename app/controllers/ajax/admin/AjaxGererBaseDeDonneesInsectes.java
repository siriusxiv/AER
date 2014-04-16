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
package controllers.ajax.admin;

import controllers.admin.Admin;
import models.Espece;
import models.EspeceSynonyme;
import models.SousFamille;
import models.Famille;
import models.SuperFamille;
import models.Ordre;
import models.Groupe;
import models.SousGroupe;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.gererBaseDeDonneesInsectes;

public class AjaxGererBaseDeDonneesInsectes extends Controller {
	
	/**
	 * Renomme l'espèce via ajax et enregistre le tout dans la base de données
	 * @param espece_id
	 * @return
	 */
	public static Result renommerEspece(Integer espece_id){
		if(Admin.isAdminConnected()){
			Espece espece = Espece.find.byId(espece_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			espece.espece_nom=df.get("nouveauNom");
			espece.save();
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Change l'auteur de l'espèce via ajax et enregistre le tout dans la base de données
	 * @param espece_id
	 * @return
	 */
	public static Result changerAuteurEspece(Integer espece_id){
		if(Admin.isAdminConnected()){
			Espece espece = Espece.find.byId(espece_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			espece.espece_auteur=df.get("nouvelAuteur");
			espece.save();
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	* Change le synonyme dans la base de données
	* @param espece_id
	 * @return
	 */
	 public static Result changerSynonyme(Integer synonyme_id) {
	 	if(Admin.isAdminConnected()){
	 		EspeceSynonyme synonyme = EspeceSynonyme.find.byId(synonyme_id);
	 		DynamicForm df = DynamicForm.form().bindFromRequest();
	 		synonyme.synonyme_nom=df.get("nouveauSyn");
	 		synonyme.save();
	 		return redirect("/gererBaseDeDonneesInsectes");
	 	} else
	 		return Admin.nonAutorise();
	}
}
