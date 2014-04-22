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

import java.util.List;
import controllers.admin.Admin;
import models.Espece;
import models.EspeceSynonyme;
import models.SousFamille;
import models.Famille;
import models.SuperFamille;
import models.Ordre;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

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
	* Change la systématique de l'espèce et remet tout dans le bon ordre
	* @param nouv_systematique
	 * @param espece_id
	 * @return
	 */
	 public static Result changerSystematiqueEspece(Integer espece_id){
	 	 if (Admin.isAdminConnected()){
	 	 	 Espece espece = Espece.find.byId(espece_id);
	 	 	 DynamicForm df = DynamicForm.form().bindFromRequest();
	 	 	 String nouvelle_systematique = df.get("nouv_systematique");
	 	 	 Integer nouv_systematique = Integer.parseInt(nouvelle_systematique);
	 	 	 if(espece.espece_systematique < nouv_systematique){
	 	 	 	List<Espece> especesEntre = Espece.find.where().gt("espece_systematique",espece.espece_systematique).le("espece_systematique",nouv_systematique).findList();
	 	 	 	for (Espece e : especesEntre){
	 	 	 	 	e.espece_systematique--;
	 	 	 	 	e.save();
	 	 	 	}
	 	 	 espece.espece_systematique = nouv_systematique;
	 	 	 espece.save();
	 	 	 } else {
	 	 	 	List<Espece> especesEntre = Espece.find.where().lt("espece_systematique",espece.espece_systematique).ge("espece_systematique",nouv_systematique).findList();
	 	 	 	for (Espece e : especesEntre){
	 	 	 	 	e.espece_systematique++;
	 	 	 	 	e.save();
	 	 	 	}
	 	 	 espece.espece_systematique = nouv_systematique;
	 	 	 espece.save();
	 	 	 }
	 	 	 return redirect("/gererBaseDeDonneesInsectes");
	 	 } else 
	 	 	return Admin.nonAutorise();
	 }
	
	/**
	* Change le commentaire associé à l'espèce
	* @param espece_id
	 * @return
	 */
	 public static Result changerCommentaireEspece(Integer espece_id){
	 	 if(Admin.isAdminConnected()){
	 	 	 Espece espece = Espece.find.byId(espece_id);
	 	 	 DynamicForm df = DynamicForm.form().bindFromRequest();
	 	 	 espece.espece_commentaires=df.get("nouveauCommentaire");
	 	 	 espece.save();
	 	 	 return redirect("/gererBaseDeDonneesInsectes");
	 	 } else 
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
	
	/**
	 * Renomme la sous famille et enregistre le tout dans la base de données
	 * @param sous_famille_id
	 * @return
	 */
	public static Result renommerSousFamille(Integer sous_famille_id){
		if(Admin.isAdminConnected()){
			SousFamille sousfam = SousFamille.find.byId(sous_famille_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			sousfam.sous_famille_nom=df.get("nouveauNomSsFam");
			sousfam.save();
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Renomme la famille et enregistre le tout dans la base de données
	 * @param famille_id
	 * @return
	 */
	public static Result renommerFamille(Integer famille_id){
		if(Admin.isAdminConnected()){
			Famille fam = Famille.find.byId(famille_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			fam.famille_nom=df.get("nouveauNomFam");
			fam.save();
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Renomme la super-famille et enregistre le tout dans la base de données
	 * @param super_famille_id
	 * @return
	 */
	public static Result renommerSuperFamille(Integer super_famille_id){
		if(Admin.isAdminConnected()){
			SuperFamille superfam = SuperFamille.find.byId(super_famille_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			superfam.super_famille_nom=df.get("nouveauNomSuperFam");
			superfam.save();
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
	
	/**
	 * Renomme l'ordre et enregistre le tout dans la base de données
	 * @param ordre_id
	 * @return
	 */
	public static Result renommerOrdre(Integer ordre_id){
		if(Admin.isAdminConnected()){
			Ordre ordre = Ordre.find.byId(ordre_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			ordre.ordre_nom=df.get("nouveauNomOrd");
			ordre.save();
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
}
