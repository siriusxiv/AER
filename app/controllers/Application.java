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

package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    /*Page de login ou d'inscription */
    
    public static Result identification() {
    	return ok(identification.render());
    }
    
    public static Result inscription() {
    	return ok(inscription.render());
    }
    
    public static Result retrouverMotDePasse() {
    	return ok(retrouverMotDePasse.render());
    }
    
    /*Pages de l'utilisateur  */
    	
    public static Result menuUtilisateur() {
    	return ok(menuUtilisateur.render());
    }
    
    public static Result informationsPersonnelles() {
    	return ok(informationsPersonnelles.render());
    }
    
    public static Result consulterDonnees() {
    	return ok(consulterDonnees.render());
    }
    
    public static Result historique() {
    	return ok(historique.render());
    }
    
    
    /* Pages de l'expert  */
    
    public static Result menuExpert() {
    	return ok(menuExpert.render());
    }
    
    public static Result temoignagesAValider() {
    	return ok(temoignagesAValider.render());
    }
    
    public static Result temoignagesValides() {
    	return ok(temoignagesValides.render());
    }
    
    /*Pages de l'admin */
    
    public static Result menuAdmin() {
    	return ok(menuAdmin.render());
    }
    
    public static Result assignerExpert() {
    	return ok(AssignerExpert.render());
    }
    
    public static Result demandesInscription() {
    	return ok(demandesInscription.render());
    }
    
    public static Result gererBasesDeDonneesInsectes() {
    	return ok(gererBasesDeDonneesInsectes.render());
    }
    
    public static Result listeMembres() {
    	return ok(listeMembres.render());
    }
    
    public static Result listeTemoignages() {
    	return ok(listeTemoignages.render());
    }
    
    /*Fiche de Témoignage */
    public static Result ficheDeTemoignage() {
    	return ok(ficheDeTemoignage.render());
    }
    
    
}
