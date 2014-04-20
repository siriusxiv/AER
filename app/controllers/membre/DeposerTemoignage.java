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
package controllers.membre;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import models.Commune;
import models.Espece;
import models.Fiche;
import models.FicheHasMembre;
import models.InformationsComplementaires;
import models.Membre;
import models.Observation;
import models.StadeSexe;
import models.UTMS;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.membre.ficheDeTemoignage;

public class DeposerTemoignage extends Controller {
	
	@Security.Authenticated(SecuredMembre.class)
    public static Result main() {
		Membre membre = Membre.find.where().eq("membre_email", session("username")).findUnique();
    	return ok(ficheDeTemoignage.render(membre,""));
    }
	
	public static Result redirectMain(){
		return redirect("/ficheDeTemoignage");
	}
	
	@Security.Authenticated(SecuredMembre.class)
	public static Result post() throws ParseException{
		DynamicForm df = DynamicForm.form().bindFromRequest();
		String commune_nom = df.get("ville_nom_reel");
		Commune commune = Commune.find.where().eq("ville_nom_reel", commune_nom).findUnique();
		if(commune==null && !commune_nom.equals(""))
			return badRequest("La commune "+commune_nom+" n'est pas répertoriée !");
		String lieu_dit = df.get("lieu-dit");
		String utm_string = df.get("utm");
		UTMS utm = UTMS.find.byId(utm_string);
		if(utm==null)
			return badRequest("La maille "+utm_string+" n'est pas répertoriée !");
		String jour = df.get("jour");
		String mois = df.get("mois");
		String annee = df.get("annee");
		Calendar date = Calendar.getInstance();
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
		date.setTime(date_format.parse(jour+"/"+mois+"/"+annee));
		String memo = df.get("memo");
		Fiche fiche = new Fiche(commune,lieu_dit,utm,date,memo);
		fiche.save();

		int membre_position=1;
		String membre_nom;
		while( (membre_nom=df.get("membre_nom"+membre_position)) != null ){
			Membre membre = Membre.find.where().eq("membre_nom", membre_nom).findUnique();
			if(membre!=null)
				new FicheHasMembre(membre,fiche).save();
			else if(membre_position==1){
				fiche.delete();
				return badRequest("Le membre "+membre+" n'est pas référencé.");
			}
			membre_position++;
		}
		
		int observation_position=1;
		String especeId;
		while( (especeId = df.get("espece"+observation_position)) != null ){
			Espece espece = Espece.find.byId(Integer.parseInt(especeId));
			if(espece!=null){
				String determinateur = df.get("determinateur"+observation_position);
				String commentaires = df.get("commentaires"+observation_position);
				Observation observation = new Observation(fiche,espece,determinateur,commentaires);
				observation.save();
				int complement_position=1;
				String nombreSpecimens_string;
				while( (nombreSpecimens_string = df.get("nombreSpecimens"+observation_position+"-"+complement_position)) != null){
					Integer nombreSpecimens = nombreSpecimens_string.isEmpty() ? null : Integer.parseInt(nombreSpecimens_string);
					String stade_sexe_string = df.get("stadeSexePrecis"+observation_position+"-"+complement_position);
					StadeSexe stade_sexe;
					if(stade_sexe_string!=null){
						stade_sexe=StadeSexe.find.byId(Integer.parseInt(stade_sexe_string));
					}else{
						stade_sexe_string = df.get("stadeSexe"+observation_position+"-"+complement_position);
						stade_sexe=StadeSexe.find.byId(Integer.parseInt(stade_sexe_string));
					}
					new InformationsComplementaires(observation,nombreSpecimens,stade_sexe).save();
					complement_position++;
				}
			}
			observation_position++;
		}
		Membre membre = Membre.find.where().eq("membre_email", session("username")).findUnique();
		return ok(ficheDeTemoignage.render(membre,"Votre témoignage a été déposé avec succès !"));
	}
}
