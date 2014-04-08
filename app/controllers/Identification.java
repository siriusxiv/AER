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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import models.Droits;
import models.Membre;
import functions.credentials.Credentials;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.identification;

public class Identification extends Controller {

    public static Result main() {
    	return ok(identification.render(""));
    }
    public static Result connexionEchouée() {
    	return ok(identification.render("Votre combinaison nom d'utilisateur et mot de passe est incorrecte."));
    }
    
    /**
     * Se connecte si le membre rentre le bon identifiant et le bon mot de passe.
     * Redirige ensuite vers la page correspondant aux droits du membre.
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static Result connexion() throws NoSuchAlgorithmException, InvalidKeySpecException{
    	DynamicForm df = DynamicForm.form().bindFromRequest();
    	String username = df.get("login");
    	Credentials credentials = new Credentials(username);
    	if(credentials.connect(df.get("passw"))){
    		session("username",username);
    		if(df.get("memory")!=null)
    			session("memory","");
    		Membre membre = Membre.find.where().eq("membre_email", username).findUnique();
    		if(membre.membre_droits.equals(Droits.TEMOIN))
    			return redirect("/menuUtilisateur");
    		else if(membre.membre_droits.equals(Droits.EXPERT))
    			return redirect("/menuExpert");
    		else if(membre.membre_droits.equals(Droits.ADMIN))
    			return redirect("/menuAdmin");
    		else
    			return connexionEchouée();
    	}else
    		return connexionEchouée();
    }
    
}
