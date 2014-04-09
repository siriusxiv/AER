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
package controllers.expert;

import controllers.admin.Admin;
import models.Groupe;
import models.StadeSexeHierarchieDansGroupe;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.gererStadeSexe;

public class GererStadeSexe extends Controller{
	/**
	 * Affiche la page de gestion des stades et des sexes
	 * si l'utilisateur est expert dans le groupe donné.
	 * @param groupe_id
	 * @return
	 */
	public static Result main(Integer groupe_id){
		Groupe groupe = Groupe.find.byId(groupe_id);
		if(MenuExpert.isExpertOn(groupe))
			return ok(gererStadeSexe.render(groupe));
		else
			return Admin.nonAutorise();
	}
	
	/**
	 * Ajoute un stade/sexe père au groupe donné.
	 * @param groupe_id
	 * @return
	 */
	public static Result ajouterStadePere(Integer groupe_id){
		Groupe groupe = Groupe.find.byId(groupe_id);
		if(MenuExpert.isExpertOn(groupe)){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			int stade_sexe_id = Integer.parseInt(df.get("stade_sexe"));
			int position = Integer.parseInt(df.get("position"));
			StadeSexeHierarchieDansGroupe sshdg = new StadeSexeHierarchieDansGroupe(groupe,stade_sexe_id,position);
			sshdg.save();
			return redirect("/gererstadesexe/"+groupe_id);
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Ajoute un stade/sexe fils au groupe donné.
	 * @param groupe_id
	 * @return
	 */
	public static Result ajouterStadeFils(Integer groupe_id, Integer stade_sexe_pere_id){
		Groupe groupe = Groupe.find.byId(groupe_id);
		if(MenuExpert.isExpertOn(groupe)){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			int stade_sexe_id = Integer.parseInt(df.get("stade_sexe"));
			int position = Integer.parseInt(df.get("position"));
			StadeSexeHierarchieDansGroupe sshdg = new StadeSexeHierarchieDansGroupe(groupe,stade_sexe_id,stade_sexe_pere_id,position);
			sshdg.save();
			return redirect("/gererstadesexe/"+groupe_id);
		}else
			return Admin.nonAutorise();
	}
}
