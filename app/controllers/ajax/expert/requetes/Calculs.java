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
package controllers.ajax.expert.requetes;

import java.util.HashMap;
import java.util.Map;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

public class Calculs extends Controller {
	
	public static Result temoinsParPeriode(){
		DynamicForm df = DynamicForm.form().bindFromRequest();
		Map<String,String> info = getData(df);
		
		return ok();
	}
	
	/**
	 * Tranforme la DynamicForm en Map pour pouvoir l'utiliser librement plus tard.
	 * @param df
	 * @return
	 */
	public static Map<String,String> getData(DynamicForm df){
		Map<String,String> map = new HashMap<String,String>();
		map.put("groupe", df.get("groupe"));
		map.put("sous_groupe", df.get("sous_groupe"));
		map.put("espece", df.get("espece"));
		map.put("stade", df.get("stade"));
		map.put("maille", df.get("maille"));
		map.put("temoin", df.get("temoin"));
		map.put("jour1", df.get("jour1"));
		map.put("mois1", df.get("mois1"));
		map.put("annee1", df.get("annee1"));
		map.put("jour2", df.get("jour2"));
		map.put("mois2", df.get("mois2"));
		map.put("annee2", df.get("annee2"));
		return map;
	}
}
