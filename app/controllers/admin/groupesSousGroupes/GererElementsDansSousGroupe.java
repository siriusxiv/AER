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
package controllers.admin.groupesSousGroupes;

import models.Espece;
import models.EspeceHasSousGroupe;
import models.Famille;
import models.FamilleHasSousGroupe;
import models.Groupe;
import models.Ordre;
import models.OrdreHasSousGroupe;
import models.SousFamille;
import models.SousFamilleHasSousGroupe;
import models.SousGroupe;
import models.SuperFamille;
import models.SuperFamilleHasSousGroupe;
import controllers.admin.Admin;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.gererGroupesEtSousGroupes;

public class GererElementsDansSousGroupe extends Controller {

	public static Result mainAvecSousGroupeOuvert(Integer sous_groupe_id){
		if(Admin.isAdminConnected())
			return ok(gererGroupesEtSousGroupes.render(Groupe.findAll(),sous_groupe_id));
		else
			return Admin.nonAutorise();
	}

	/**
	 * Ajoute une espèce dans un sous-groupe.
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result ajouterEspece(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			Integer espece_id = Integer.parseInt(df.get("espece"));
			Espece espece = Espece.find.byId(espece_id);
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			if(espece!=null && sous_groupe!=null && Espece.findEspecesAjoutablesDansSousGroupe().contains(espece)){
				new EspeceHasSousGroupe(espece,sous_groupe).save();
				espece.metAJourSousGroupes();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Ajoute une sous-famille dans un sous-groupe.
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result ajouterSousFamille(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			Integer sous_famille_id = Integer.parseInt(df.get("sous_famille"));
			SousFamille sous_famille = SousFamille.find.byId(sous_famille_id);
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			if(sous_famille!=null && sous_groupe!=null && SousFamille.findSousFamillesAjoutablesDansSousGroupe().contains(sous_famille)){
				new SousFamilleHasSousGroupe(sous_famille,sous_groupe).save();
				for(Espece espece : sous_famille.getEspecesDansThis())
					espece.metAJourSousGroupes();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Ajoute une famille dans un sous-groupe.
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result ajouterFamille(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			Integer famille_id = Integer.parseInt(df.get("famille"));
			Famille famille = Famille.find.byId(famille_id);
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			if(famille!=null && sous_groupe!=null && Famille.findFamillesAjoutablesDansSousGroupe().contains(famille)){
				new FamilleHasSousGroupe(famille,sous_groupe).save();
				for(Espece espece : famille.getEspecesDansThis())
					espece.metAJourSousGroupes();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Ajoute une super-famille dans un sous-groupe.
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result ajouterSuperFamille(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			Integer super_famille_id = Integer.parseInt(df.get("super_famille"));
			SuperFamille super_famille = SuperFamille.find.byId(super_famille_id);
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			if(super_famille!=null && sous_groupe!=null && SuperFamille.findSuperFamillesAjoutablesDansSousGroupe().contains(super_famille)){
				new SuperFamilleHasSousGroupe(super_famille,sous_groupe).save();
				for(Espece espece : super_famille.getEspecesDansThis())
					espece.metAJourSousGroupes();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Ajoute un ordre dans un sous-groupe.
	 * @param sous_groupe_id
	 * @return
	 */
	public static Result ajouterOrdre(Integer sous_groupe_id){
		if(Admin.isAdminConnected()){
			DynamicForm df = DynamicForm.form().bindFromRequest();
			Integer ordre_id = Integer.parseInt(df.get("ordre"));
			Ordre ordre = Ordre.find.byId(ordre_id);
			SousGroupe sous_groupe = SousGroupe.find.byId(sous_groupe_id);
			if(ordre!=null && sous_groupe!=null && Ordre.findOrdresAjoutablesDansSousGroupe().contains(ordre)){
				new OrdreHasSousGroupe(ordre,sous_groupe).save();
				for(Espece espece : ordre.getEspecesDansThis())
					espece.metAJourSousGroupes();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Enlève une espèce d'un sous-groupe.
	 * @param ehsg_id
	 * @return
	 */
	public static Result enleverEspece(Integer ehsg_id){
		if(Admin.isAdminConnected()){
			EspeceHasSousGroupe ehsg = EspeceHasSousGroupe.find.byId(ehsg_id);
			Integer sous_groupe_id=-1;
			if(ehsg!=null){
				sous_groupe_id=ehsg.sous_groupe.sous_groupe_id;
				ehsg.espece.espece_sous_groupe=null;
				ehsg.espece.save();
				ehsg.delete();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();		
	}
	
	/**
	 * Enlève une sous-famille d'un sous-groupe.
	 * @param sofhsg_id
	 * @return
	 */
	public static Result enleverSousFamille(Integer sofhsg_id){
		if(Admin.isAdminConnected()){
			SousFamilleHasSousGroupe sofhsg = SousFamilleHasSousGroupe.find.byId(sofhsg_id);
			Integer sous_groupe_id=-1;
			if(sofhsg!=null){
				sous_groupe_id=sofhsg.sous_groupe.sous_groupe_id;
				for(Espece espece : sofhsg.sous_famille.getEspecesDansThis()){
					espece.espece_sous_groupe=null;
					espece.save();
				}
				sofhsg.delete();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();		
	}

	/**
	 * Enlève une famille d'un sous-groupe.
	 * @param fhsg_id
	 * @return
	 */
	public static Result enleverFamille(Integer fhsg_id){
		if(Admin.isAdminConnected()){
			FamilleHasSousGroupe fhsg = FamilleHasSousGroupe.find.byId(fhsg_id);
			Integer sous_groupe_id=-1;
			if(fhsg!=null){
				sous_groupe_id=fhsg.sous_groupe.sous_groupe_id;
				for(Espece espece : fhsg.famille.getEspecesDansThis()){
					espece.espece_sous_groupe=null;
					espece.save();
				}
				fhsg.delete();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();		
	}

	/**
	 * Enlève une super-famille d'un sous-groupe.
	 * @param sufhsg_id
	 * @return
	 */
	public static Result enleverSuperFamille(Integer sufhsg_id){
		if(Admin.isAdminConnected()){
			SuperFamilleHasSousGroupe sufhsg = SuperFamilleHasSousGroupe.find.byId(sufhsg_id);
			Integer sous_groupe_id=-1;
			if(sufhsg!=null){
				sous_groupe_id=sufhsg.sous_groupe.sous_groupe_id;
				for(Espece espece : sufhsg.super_famille.getEspecesDansThis()){
					espece.espece_sous_groupe=null;
					espece.save();
				}
				sufhsg.delete();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();		
	}

	/**
	 * Enlève un ordre d'un sous-groupe.
	 * @param ohsg_id
	 * @return
	 */
	public static Result enleverOrdre(Integer ohsg_id){
		if(Admin.isAdminConnected()){
			OrdreHasSousGroupe ohsg = OrdreHasSousGroupe.find.byId(ohsg_id);
			Integer sous_groupe_id=-1;
			if(ohsg!=null){
				sous_groupe_id=ohsg.sous_groupe.sous_groupe_id;
				for(Espece espece : ohsg.ordre.getEspecesDansThis()){
					espece.espece_sous_groupe=null;
					espece.save();
				}
				ohsg.delete();
			}
			return redirect("/gererGroupesEtSousGroupes/sousgroupe/"+sous_groupe_id);
		}else
			return Admin.nonAutorise();		
	}
}
