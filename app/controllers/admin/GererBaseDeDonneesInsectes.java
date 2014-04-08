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
		return ok(gererBaseDeDonneesInsectes.render(Espece.findAll(), SousFamille.findAll()));
	}
	
	/**
	 * Ajoute l'Espèce à la base de données.
	 * @param groupe_id
	 * @return
	 */
	 public static Result ajouterNouvEspece(){
	if (Admin.isAdminConnected()){
		ajouterNouvelleEspece(avecSousFamille, sousFamilleOuFamille);
		}
		return redirect("/gererBaseDeDonneesInsectes");
	 }
}
