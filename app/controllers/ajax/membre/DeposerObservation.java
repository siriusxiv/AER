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
package controllers.ajax.membre;

import models.Groupe;
import models.StadeSexe;
import controllers.membre.SecuredMembre;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.membre.ajax.observation;
import views.html.membre.ajax.listeEspeces;
import views.html.membre.ajax.informationsComplementaires;
import views.html.membre.ajax.stadeSexePrecis;

public class DeposerObservation extends Controller {
	/**
	 * Affiche la page où on dépose une observation
	 * @param observation_position
	 * @param groupe_precedent_id : le groupe sélectionné précédemment
	 * @return
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result main(Integer observation_position, Integer groupe_precedent_id){
		return ok(observation.render(observation_position,groupe_precedent_id));
	}
	
	/**
	 * Met à jour la liste d'espèce pour un groupe donné
	 * @param groupe_id
	 * @return
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result getListeEspeces(Integer groupe_id){
		Groupe groupe = Groupe.find.byId(groupe_id);
		return ok(listeEspeces.render(groupe));
	}

	/**
	 * Affiche une information complémentaire pour une observation donnée.
	 * @param observation_position
	 * @param groupe_id
	 * @param complement_position
	 * @return
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result getComplement(Integer observation_position, Integer groupe_id, Integer complement_position){
		Groupe groupe = Groupe.find.byId(groupe_id);
		return ok(informationsComplementaires.render(observation_position,groupe,complement_position));
	}
	
	/**
	 * Donne la liste des stades_sexes ayant comme père le stade
	 * d'id stade_sexe_pere_id.
	 * @param groupe_id
	 * @param stade_sexe_pere_id
	 * @param observation_position
	 * @param complement_position
	 * @return
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result getStadeSexePrecis(Integer groupe_id, Integer stade_sexe_pere_id, Integer observation_position, Integer complement_position){
		Groupe groupe = Groupe.find.byId(groupe_id);
		StadeSexe stadesexe = StadeSexe.find.byId(stade_sexe_pere_id);
		return ok(stadeSexePrecis.render(stadesexe,stadesexe.getStadeSexeFilsPourTelGroupe(groupe),observation_position,complement_position));
	}
}
