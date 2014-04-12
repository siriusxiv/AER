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
package controllers.admin;

import models.Groupe;
import models.SousGroupe;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.gererGroupesEtSousGroupes;

public class GererGroupesEtSousGroupes extends Controller {

	public static Result main(){
		if(Admin.isAdminConnected())
			return ok(gererGroupesEtSousGroupes.render(Groupe.findAll()));
		else
			return Admin.nonAutorise();
	}

	/**
	 * Déplace un sous-groupe dans un autre groupe
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result deplacerSousGroupe(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			Integer groupe_id = Integer.parseInt(df.get("groupeId"));
			Groupe groupe = Groupe.find.byId(groupe_id);
			if(sous_groupe!=null && groupe!=null){
				sous_groupe.sous_groupe_groupe=groupe;
				sous_groupe.save();
			}
			return redirect("/gererGroupesEtSousGroupes");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Crée un groupe et l'ajoute dans la base de données.
	 * Il ne contient aucune espèce.
	 * @return
	 */
	public static Result creerGroupe(){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			String nomGroupe = df.get("nomGroupe");
			if(!nomGroupe.equals("") && Groupe.find.where().eq("groupe_nom", nomGroupe).findList().isEmpty()){
				new Groupe(nomGroupe).save();
			}
			return redirect("/gererGroupesEtSousGroupes");
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Crée un sous-groupe et l'ajoute dans la base de données.
	 * Il ne contient aucune espèce.
	 * @return
	 */
	public static Result creerSousGroupe(){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			String nomSousGroupe = df.get("nomSousGroupe");
			Integer groupe_id = Integer.parseInt(df.get("groupeId"));
			Groupe groupe = Groupe.find.byId(groupe_id);
			if(!nomSousGroupe.equals("") &&
					SousGroupe.find.where().eq("sous_groupe_nom", nomSousGroupe).findList().isEmpty()
					&& groupe!=null){
				new SousGroupe(nomSousGroupe, groupe).save();
			}
			return redirect("/gererGroupesEtSousGroupes");
		}else
			return Admin.nonAutorise();
	}
}
