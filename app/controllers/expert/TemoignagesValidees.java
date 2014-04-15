package controllers.expert;

import java.util.List;

import controllers.admin.Admin;
import models.Espece;
import models.Groupe;
import models.Observation;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.expert.temoignagesValides;

public class TemoignagesValidees extends Controller {

	/**
	 * Renvoie la liste des observations validées, il n'en revoit que 50, la page permet de savoir à quel endroit de la liste on se trouve).
	 * @param groupe_id
	 * @param page
	 * @return
	 */
	public static Result observationsValidees (Integer groupe_id, Integer page){
		Groupe groupe = Groupe.find.byId(groupe_id);
		if(MenuExpert.isExpertOn(groupe)){
			Integer valide=Observation.VALIDEE;
			List<Observation> observation= Observation.find.where().eq("observation_validee",valide).eq("observation_espece.espece_sous_groupe.sous_groupe_groupe",groupe).findList();
			List<Espece> especes= Espece.find.where().eq("espece_sous_groupe.sous_groupe_groupe", groupe).findList();
			Integer premierObservation=Math.min((page-1),observation.size() );
			Integer dernierObservation=Math.min((page*50-1), observation.size());
			Integer nbpages = observation.size()/50+1;
			List<Observation> observationsvues = observation.subList(premierObservation, dernierObservation);
			return ok(temoignagesValides.render(observationsvues, page,nbpages, groupe, especes));
		}else
			return Admin.nonAutorise();
	}
	/**
	 * Ne renvoit que les observations de l'espece voulue
	 * @param groupe_id
	 * @param page
	 * @param espece_id
	 * @return
	 */
	public static Result observationsValideesEspece (Integer groupe_id, Integer page, Integer espece_id){
		Groupe groupe = Groupe.find.byId(groupe_id);
		if(MenuExpert.isExpertOn(groupe)){
			Integer valide=Observation.VALIDEE;
			List<Observation> observation= Observation.find.where().eq("observation_validee",valide).eq("observation_espece.espece_sous_groupe.sous_groupe_groupe",groupe).eq("observation_espece.espece_id", espece_id).findList();
			List<Espece> especes= Espece.find.where().eq("espece_sous_groupe.sous_groupe_groupe", groupe).findList();
			Integer premierObservation=Math.min((page-1),observation.size() );
			Integer dernierObservation=Math.min((page*50-1), observation.size());
			Integer nbpages = observation.size()/50+1;
			List<Observation> observationsvues = observation.subList(premierObservation, dernierObservation);
			return ok(temoignagesValides.render(observationsvues, page,nbpages, groupe, especes));
		}else
			return Admin.nonAutorise();
	}
	
}
