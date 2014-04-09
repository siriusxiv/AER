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

import java.util.List;

import models.Espece;
import models.SousFamille;
import models.Famille;
import models.SuperFamille;
import models.Ordre;
import models.SousGroupe;
import models.Groupe;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import javax.naming.NamingException;
import javax.persistence.PersistenceException; 
import views.html.gererBaseDeDonneesInsectes;

/**
 * Gère les fonctions pour gérer la base de données des insectes
 * @author geoffroy
 *
 */
public class GererBaseDeDonneesInsectes extends Controller {
	
	/**
	 * Affiche la page principale
	 * @return
	 */
	public static Result main() {
		return ok(gererBaseDeDonneesInsectes.render(Espece.findAll(), SousFamille.findSousFamilleExistantes(), Famille.findAll(), SuperFamille.findAll(), Ordre.findAll()));
	}
	/******* mise en place des tris des insectes **********/
	public static Result listeEspecesParSousFamille(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer sousfam_id = Integer.parseInt(df.get("sous_famille_tri"));
		SousFamille sousfam = SousFamille.find.byId(sousfam_id);
		if (sousfam_id == 0) {
			return redirect("/gererBaseDeDonneesInsectes");
		} else {	
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesSousFamille(sousfam), SousFamille.findSousFamilleExistantes(), Famille.findAll(), SuperFamille.findAll(), Ordre.findAll()));
		}
	}
	public static Result listeEspecesParFamille(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer fam_id = Integer.parseInt(df.get("famille_tri"));
		Famille fam = Famille.find.byId(fam_id);
		if (fam_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesFamille(fam), SousFamille.findSousFamilleExistantes(), Famille.findAll(), SuperFamille.findAll(), Ordre.findAll()));
		}
	}
	public static Result listeEspecesParSuperFamille(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer superfam_id = Integer.parseInt(df.get("super_famille_tri"));
		SuperFamille superfam = SuperFamille.find.byId(superfam_id);
		if (superfam_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesSuperFamille(superfam), SousFamille.findSousFamilleExistantes(), Famille.findAll(), SuperFamille.findAll(), Ordre.findAll()));
		}
	}
	public static Result listeEspecesParOrdre(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer ordre_id = Integer.parseInt(df.get("ordre_tri"));
		Ordre ordre = Ordre.find.byId(ordre_id);
		if (ordre_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesOrdre(ordre), SousFamille.findSousFamilleExistantes(), Famille.findAll(), SuperFamille.findAll(), Ordre.findAll()));
		}
	}
	
	/**
	 * Ajoute l'Espèce à la base de données. Ne marche que sur les espèces au milieu ou début.
	 * @return
	 */
	public static Result ajouterNouvEspece() throws NamingException, PersistenceException{
	if (Admin.isAdminConnected()){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer numero_systematique = Integer.parseInt(df.get("systématique"));
		Espece espece_avant = Espece.findBySystematique(numero_systematique);
		String nomSousFamilleOuFamille = new String();
		boolean avecSousFam = df.get("aUneSousFamille").equals("sousfam");
		if (avecSousFam) {
			nomSousFamilleOuFamille = df.get("sousfamille");
		} else {
			nomSousFamilleOuFamille = df.get("famille");
		}
		espece_avant.ajouterNouvelleEspece(avecSousFam, nomSousFamilleOuFamille);
	}
	return redirect("/gererBaseDeDonneesInsectes");
}
}
