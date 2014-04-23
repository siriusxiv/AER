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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import models.Membre;
import controllers.admin.Admin;
import controllers.ajax.expert.requetes.calculs.MaChronologie;
import functions.excels.exports.MaChronologieExcel;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.membre.historique;

public class Historique extends Controller {

	public static Result main() throws ParseException, IOException {
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		if(temoin!=null){
			HashMap<String,String> info = new HashMap<String,String>();
			info.put("temoin", temoin.membre_nom);
			MaChronologie maChronologie = new MaChronologie(info,0);
			return ok(historique.render(maChronologie));
		}else
			return Admin.nonAutorise();
	}
	
	public static Result voirPage(int page) throws ParseException, IOException {
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		if(temoin!=null){
			HashMap<String,String> info = new HashMap<String,String>();
			info.put("temoin", temoin.membre_nom);
			MaChronologie maChronologie = new MaChronologie(info,page);
			return ok(historique.render(maChronologie));
		}else
			return Admin.nonAutorise();
	}

	/**
	 * Télécharge les témoignages.
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Security.Authenticated(SecuredMembre.class)
	public static Result telechargerHistorique() throws ParseException, IOException{
		Membre temoin = Membre.find.where().eq("membre_email", session("username")).findUnique();
		HashMap<String,String> info = new HashMap<String,String>();
		info.put("temoin", temoin.membre_nom);
		MaChronologie maChronologie = new MaChronologie(info,MaChronologie.TOUT);
		MaChronologieExcel maChronologieExcel = new MaChronologieExcel(info,maChronologie);
		maChronologieExcel.writeToDisk();
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(Play.application().configuration().getString("xls_generes.path")+maChronologieExcel.getFileName()));
			response().setHeader("Content-Disposition", "attachment; filename="+maChronologieExcel.getFileName());
			return ok(fis);
		} catch (FileNotFoundException e) {
			return notFound("404: File not found");
		}
	}
}
