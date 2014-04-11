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
import models.Groupe;
import models.SousGroupe;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.ajax.gererSousGroupe;

public class AjaxGererGroupeEtSousGroupes extends Controller {
	
	/**
	 * Renomme le groupe via ajax et enregistre le tout dans la base de données
	 * @param groupe_id
	 * @return
	 */
	public static Result renommerGroupe(Integer groupe_id){
		if(Admin.isAdminConnected()){
			Groupe groupe = Groupe.find.byId(groupe_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			groupe.groupe_nom=df.get("nouveauNom");
			groupe.save();
			return ok("Groupe "+groupe_id+" renommé avec succès");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Renomme le sous-groupe via ajax et enregistre le tout dans la base de données
	 * @param groupe_id
	 * @return
	 */
	public static Result renommerSousGroupe(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			sous_groupe.sous_groupe_nom=df.get("nouveauNom");
			sous_groupe.save();
			return ok("Sous-groupe "+sous_groupe_id+" renommé avec succès");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Permet de voir la page gererSousGroupe chargée par ajax
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result voirPageEditerSousGroupe(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			return ok(gererSousGroupe.render(sous_groupe));
		}else
			return Admin.nonAutorise();
	}
}
