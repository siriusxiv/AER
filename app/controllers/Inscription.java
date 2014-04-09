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

import functions.mail.VerifierMail;
import models.Membre;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.inscription;

public class Inscription extends Controller {

    public static Result main() {
    	return ok(inscription.render(""));
    }
    
    public static Result inscrire() throws NoSuchAlgorithmException, InvalidKeySpecException {
    	DynamicForm df = DynamicForm.form().bindFromRequest();
    	String nom = df.get("nom");
    	String civilite = df.get("civilite");
    	String email = df.get("email");
    	String passw = df.get("passw");
    	if(Membre.find.where().eq("membre_email",email).findList().isEmpty()){
    	Membre m = new Membre(nom,civilite,email,passw);
    	m.save();
    	VerifierMail.envoyerMailDeVerification(m);
    	return redirect("/");
    	}else	//L'adresse mail existe déjà
    		return redirect("/MailExisteDéjà");
    }
}