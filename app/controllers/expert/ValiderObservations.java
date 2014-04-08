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

import play.mvc.Controller;
import play.mvc.Result;
import views.html.temoignagesAValider;
import models.Observation;


/**
 * Gère les fonctions liées à la validation d'observations par les experts.
 * 
 * @author johan
 *
 */

public class ValiderObservations extends Controller {
	
 /**
  * Permet d'afficher la page liée au processus de validation des témoignages
  * @return
  */
	
	public static Result temoignagesAValider(Integer validation) {
    	return ok( temoignagesAValider.render(Observation.observationsEtat(validation)));
    }
	
	/**
	 * renvoit les témoignages non vus
	 * @return
	 */
	public static Result temoignagesNonVus() {
    	return ok( temoignagesAValider.render(Observation.nonVus()));
    }
	
	/**
	 * renvoit les témoignages en suspend
	 * @return
	 */
	public static Result temoignagesEnSuspends() {
    	return ok( temoignagesAValider.render(Observation.enSuspend()));
    }
    
	
}
