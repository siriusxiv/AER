package controllers.expert;

import java.util.List;

import controllers.admin.Admin;
import models.Groupe;
import models.Observation;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.expert.temoignagesValides;

public class ObservationsValidees extends Controller {

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
			Integer premierObservation=Math.min((page-1),observation.size() );
			Integer dernierObservation=Math.min((page*50-1), observation.size());
			List<Observation> observationsvues = observation.subList(premierObservation, dernierObservation);
			return ok(temoignagesValides.render(observationsvues, page));
		}else
			return Admin.nonAutorise();
	}
	

}
