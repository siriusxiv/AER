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


import java.io.File;

import models.Confidentialite;
import models.Droits;
import models.Membre;
import controllers.admin.Admin;
import controllers.membre.SecuredMembre;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import views.html.membre.*;
import views.html.admin.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }
    
    /**
	 * Pour accéder aux images uploadées
	 * @param filename
	 * @return l'image
	 */
	public static Result view(String filename) {
	    File file  = new File(play.Play.application().configuration().getString("image.path") + filename);
	    return ok(file);
	}
    
    /*Pages de l'utilisateur  */
    @Security.Authenticated(SecuredMembre.class)
    public static Result menuUtilisateur() {
    	return ok( menuUtilisateur.render());
    }
    
    public static Result historique() {
    	return ok( historique.render());
    }
    
    
    /* Pages de l'expert  */
    
    

    
   /******* Results de la page liste de membres **********/
    public static Result listeMembres(String orderBy, String sortDirection){
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.findAll(orderBy, sortDirection), Droits.findAll(),Confidentialite.findAll()));
    	}else
			return Admin.nonAutorise();
    }
    
    public static Result listeMembresTemoinActif(Boolean isTemoinActif) {
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.selectMembresTemoinActif(isTemoinActif), Droits.findAll(),Confidentialite.findAll()));
    	}else
			return Admin.nonAutorise();
    }
    
    public static Result listeMembresAbonne(Boolean isAbonne) {
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.selectMembresAbonne(isAbonne), Droits.findAll(),Confidentialite.findAll()));
    	}else
			return Admin.nonAutorise();
    }
    
    public static Result listeMembresConfidentialite(Integer confidentialite) {
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.selectMembresConfidentialite(confidentialite), Droits.findAll(),Confidentialite.findAll()));
    }else
			return Admin.nonAutorise();
    }
    
    public static Result listeMembresDroits(Integer droits) {
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.selectMembresDroits(droits), Droits.findAll(),Confidentialite.findAll()));
    	}else
			return Admin.nonAutorise();
    }
    
    public static Result listeMembresInscrit(Boolean isInscrit) {
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.selectMembresInscrit(isInscrit), Droits.findAll(),Confidentialite.findAll()));
    	}else
			return Admin.nonAutorise();
    }
    
    public static Result listeMembresPrecis(String nom) {
    	if(Admin.isAdminConnected()){
    	return ok( listeMembres.render(Membre.selectMembres(nom), Droits.findAll(),Confidentialite.findAll()));
    	}else
			return Admin.nonAutorise();
    }
    
    /*******************************************************************/
    public static Result listeTemoignages() {
    	return ok(listeTemoignages.render());
    }
    
}
