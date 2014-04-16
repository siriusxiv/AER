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

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import controllers.admin.Admin;
import functions.excels.ImportExcel;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.expert.deposerTemoignagesImportExcel;

public class DeposerTemoignagesImportExcel extends Controller {
	public static Result main(){
		if(MenuExpert.isExpertConnected()){
			return ok(deposerTemoignagesImportExcel.render(""));
		}else
			return Admin.nonAutorise();
	}

	public static Result post() throws IOException{
		if(MenuExpert.isExpertConnected()){
			MultipartFormData body = request().body().asMultipartFormData();
			FilePart fp = body.getFile("xls");
			try {
				if(fp!=null){
					FileInputStream fis = new FileInputStream(fp.getFile());
					ImportExcel ie = new ImportExcel(fis);
					ie.checkRows();
					if(ie.noError()){
						return ok(deposerTemoignagesImportExcel.render("L'import s'est déroulé avec succès."));
					}else{
						return ok(deposerTemoignagesImportExcel.render(ie.getErrorReport()));
					}
				}else
					return badRequest(deposerTemoignagesImportExcel.render("Le fichier que vous avez envoyé n'est pas valide."));
			} catch (InvalidFormatException e) {
				return badRequest(deposerTemoignagesImportExcel.render("Le fichier que vous avez envoyé n'est pas un fichier Excel .xls conforme."));
			}
		}else
			return Admin.nonAutorise();
	}
}
