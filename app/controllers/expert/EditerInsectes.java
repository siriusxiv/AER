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

import models.Groupe;
import controllers.admin.Admin;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.expert.editerInsectes;

public class EditerInsectes extends Controller {
	
	public static Result main(Integer groupe_id){
		Groupe groupe = Groupe.find.byId(groupe_id);
		if(MenuExpert.isExpertOn(groupe))
			return ok(editerInsectes.render(groupe));
		else
			return Admin.nonAutorise();
	}
}
