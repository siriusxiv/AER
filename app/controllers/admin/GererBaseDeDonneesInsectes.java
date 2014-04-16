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

import java.io.IOException;

import models.Espece;
import models.EspeceSynonyme;
import models.Image;
import models.SousFamille;
import models.Famille;
import models.SuperFamille;
import models.Ordre;
import models.SousGroupe;
import models.Groupe;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;

import javax.naming.NamingException;
import javax.persistence.PersistenceException; 

import functions.UploadImage;
import views.html.admin.gererBaseDeDonneesInsectes;

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
		if (Admin.isAdminConnected()){
		return ok(gererBaseDeDonneesInsectes.render(Espece.findAll(), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		} else {
		return Admin.nonAutorise();
		}
	}
	
	/******* mise en place des tris des insectes **********/
	public static Result listeEspecesParSousFamille(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer sousfam_id = Integer.parseInt(df.get("sous_famille_tri"));
		SousFamille sousfam = SousFamille.find.byId(sousfam_id);
		if (sousfam_id == 0) {
			return redirect("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesSousFamille(sousfam), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		}
	}
	public static Result listeEspecesParFamille(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer fam_id = Integer.parseInt(df.get("famille_tri"));
		Famille fam = Famille.find.byId(fam_id);
		if (fam_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesFamille(fam), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		}
	}
	public static Result listeEspecesParSuperFamille(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer superfam_id = Integer.parseInt(df.get("super_famille_tri"));
		SuperFamille superfam = SuperFamille.find.byId(superfam_id);
		if (superfam_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesSuperFamille(superfam), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		}
	}
	public static Result listeEspecesParOrdre(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer ordre_id = Integer.parseInt(df.get("ordre_tri"));
		Ordre ordre = Ordre.find.byId(ordre_id);
		if (ordre_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesOrdre(ordre), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		}
	}
	public static Result listeEspecesParSousGroupe(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer sous_groupe_id = Integer.parseInt(df.get("sous_groupe_tri"));
		SousGroupe sousg = SousGroupe.find.byId(sous_groupe_id);
		if (sous_groupe_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesSousGroupe(sousg), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		}
	}
	public static Result listeEspecesParGroupe(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Integer groupe_id = Integer.parseInt(df.get("groupe_tri"));
		Groupe groupe = Groupe.find.byId(groupe_id);
		if (groupe_id == 0) {
			return redirect ("/gererBaseDeDonneesInsectes");
		} else {
			return ok(gererBaseDeDonneesInsectes.render(Espece.selectEspecesGroupe(groupe), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll()));
		}
	}

	/**
	 * Ajoute l'Espèce à la base de données.
	 * @return
	 * @throws NamingException
	 * @throws PersistenceException
	 * @throws IOException
	 */
	public static Result ajouterNouvEspece() throws NamingException, PersistenceException, IOException{
		if (Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			MultipartFormData body = request().body().asMultipartFormData();
			String nom = df.get("nom");
			String auteur = df.get("auteur");
			Integer numero_systematique = Integer.parseInt(df.get("systématique"));
			Integer nomSousFamilleOuFamille = null;
			String avecSousFamille = df.get("aUneSousFamille");
			String commentaires = df.get("commentaires");
			if(avecSousFamille!=null && !nom.equals("") && !auteur.equals("")){
				boolean avecSousFam = avecSousFamille.equals("sousfam");
				if (avecSousFam) {
					nomSousFamilleOuFamille = Integer.parseInt(df.get("sous_famille"));
				} else {
					nomSousFamilleOuFamille = Integer.parseInt(df.get("famille"));
				}
				if(nomSousFamilleOuFamille!=null){
					FilePart fp = body.getFile("photo");
					Image photo = UploadImage.upload(fp);
					Espece espece;
					if(photo!=null)
						espece = new Espece(nom,auteur,numero_systematique,commentaires,photo);
					else
						espece = new Espece(nom,auteur,numero_systematique,commentaires);
					espece.ajouterNouvelleEspece(avecSousFam, nomSousFamilleOuFamille);
				}else
					System.out.println("Erreur lors de l'ajout:"+avecSousFamille+","+nom+","+auteur+","+nomSousFamilleOuFamille);
			}else
				System.out.println("Erreur lors de l'ajout:"+avecSousFamille+","+nom+","+auteur);
			return redirect("/gererBaseDeDonneesInsectes");
		}else
			return Admin.nonAutorise();
	}
	
	/** Ajoute la sous-famille à la base de données
	* @return
	* @throws PersistenceException
	 * @throws NamingException
	 */
	public static Result ajouterNouvSousFamille() throws NamingException, PersistenceException{
		if (Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			String nom = df.get("nom");
			String fam = df.get("sous_famille_ajout");
			if (fam!=null){
				Integer fam_id = Integer.parseInt(fam);
				SousFamille.ajouterSousFamille(nom, true, fam_id);
			} else {
				System.out.println("Erreur lors de l'ajout:"+fam+", "+nom);
			}
			return redirect("/gererBaseDeDonneesInsectes");
		} else {
			return Admin.nonAutorise();
		}
	}
	
	/**Ajoute la famille à la base de données
	* @return
	* @throws NamingException
	 * @throws PersistenceException
	 */
	 public static Result ajouterNouvFamille() throws NamingException, PersistenceException{
	 	 if(Admin.isAdminConnected()){
	 	 	 DynamicForm df = DynamicForm.form().bindFromRequest();
	 	 	 String nom = df.get("nom");
	 	 	 Integer superFamilleOuOrdreId = null;
	 	 	 String avecSuperFamille = df.get("aUneSuperFamille");
	 	 	 if(avecSuperFamille!=null){
	 	 	 	 boolean avecSuperFam = avecSuperFamille.equals("superfam");
	 	 	 	 if(avecSuperFam){
	 	 	 	 	 superFamilleOuOrdreId = Integer.parseInt(df.get("famille_ajout"));
	 	 	 	 } else { 
	 	 	 	 	 superFamilleOuOrdreId = Integer.parseInt(df.get("famille_ajout_ordre"));
	 	 	 	 }
	 	 	 	 if(superFamilleOuOrdreId!=null){
	 	 	 	 	 Famille famille = new Famille(nom);
	 	 	 	 	 famille.ajouterNouvelleFamille(avecSuperFam, superFamilleOuOrdreId);
	 	 	 	 } else {
	 	 	 	 	 System.out.println("Erreur lors de l'ajout : "+avecSuperFamille+", "+nom+", "+superFamilleOuOrdreId);
	 	 	 	 }
	 	 	 } else {
	 	 	 	 System.out.println("Erreur lors de l'ajout : "+avecSuperFamille+", "+nom);
	 	 	 }
	 	 	 return redirect("/gererBaseDeDonneesInsectes");
	 	 } else {
	 	 	 return Admin.nonAutorise();
	 	 }
	 }
	
	/** Ajoute la super-famille à la base de données
	* @return
	 * @throws NamingException
	 * @throws PersistenceException
	 */ 
	public static Result ajouterNouvSuperFamille() throws NamingException, PersistenceException{
		if (Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			String nom = df.get("nom");
			Integer ordre_id = Integer.parseInt(df.get("super_famille_ajout"));
			SuperFamille.ajouterSuperFamille(nom, true, ordre_id);
			return redirect("/gererBaseDeDonneesInsectes");
		} else {
			return Admin.nonAutorise();
		}
	}
	
	/** Ajoute l'ordre à la base de données
	* @return
	*/
	public static Result ajouterNouvOrdre(){
		if (Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			String nom = df.get("nom");
			Ordre.ajouterOrdre(nom);
			return redirect("/gererBaseDeDonneesInsectes");
		} else {
			return Admin.nonAutorise();
		}
	}
	
	/** Change la sous-famille d'une espèce
	* @return
	*/
	public static Result changerSousFamille(Integer espece_id){
		if(Admin.isAdminConnected()){
			Espece espece = Espece.find.byId(espece_id);
			DynamicForm df = DynamicForm.form().bindFromRequest();
			String sousfam = df.get("changerSousFamille");
			if (sousfam!=null){
				espece.espece_sousfamille = SousFamille.find.byId(Integer.parseInt(sousfam));
				espece.save();
			} else {
				System.out.println("Erreur lors du changement par "+sousfam);
			}
			return redirect("/gererBaseDeDonneesInsectes");
		} else {
			return Admin.nonAutorise();
		}
	}
	
	/**Ajoute un synonyme à la base de données
	* @return
	* @throws NamingException
	 * @throws PersistenceException
	 */
	 public static Result ajouterSynonyme(Integer espece_id) throws NamingException, PersistenceException {
	 	 if(Admin.isAdminConnected()){
	 	 	 DynamicForm df = DynamicForm.form().bindFromRequest();
	 	 	 String nomSyn = df.get("nomSyn");
	 	 	 String erreurOrigineAER = df.get("origineAER");
	 	 	 if(erreurOrigineAER!=null){
	 	 	 	 boolean origineAER = erreurOrigineAER.equals("oui");
	 	 	 	 EspeceSynonyme.ajouterSynonyme(nomSyn, origineAER, espece_id);
	 	 	 } else { 
	 	 	 	EspeceSynonyme.ajouterSynonyme(nomSyn, false, espece_id);
	 	 	}
	 	 	return redirect("/gererBaseDeDonneesInsectes");
	 	 } else {
	 	 	 return Admin.nonAutorise();
	 	 }
	 }
	 
	 /** Supprime le synonyme de la base de données
	 * @return
	 */
	 public static Result supprimerSynonyme(Integer synonyme_id){
	 	 if(Admin.isAdminConnected()){
	 	 	 EspeceSynonyme.supprimerSynonyme(synonyme_id);
	 	 	 return redirect("/gererBaseDeDonneesInsectes");
	 	 } else
	 	 	return Admin.nonAutorise();
	 }
	 
}
