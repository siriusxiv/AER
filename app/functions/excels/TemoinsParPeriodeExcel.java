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
package functions.excels;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import models.Espece;
import models.Groupe;
import models.SousGroupe;
import models.StadeSexe;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import controllers.ajax.expert.requetes.calculs.TemoinsParPeriode;

public class TemoinsParPeriodeExcel extends Excel{
	
	public TemoinsParPeriodeExcel(Map<String,String> info, List<TemoinsParPeriode> temoins) throws IOException{
		super();
		Sheet sheet = wb.createSheet("Témoins par période");
		Espece espece = Espece.find.byId(Integer.parseInt(info.get("espece")));
		SousGroupe sous_groupe = SousGroupe.find.byId(Integer.parseInt(info.get("sous_groupe")));
		Groupe groupe = Groupe.find.byId(Integer.parseInt(info.get("groupe")));
		StadeSexe stade_sexe = StadeSexe.find.byId(Integer.parseInt(info.get("stade")));
		String maille = info.get("maille");
		String date1 = info.get("jour1")+"/"+info.get("mois1")+"/"+info.get("annee1");
		String date2 = info.get("jour2")+"/"+info.get("mois2")+"/"+info.get("annee2");
		String titre = "Liste des témoins ayant fait une observation ";
		if(espece!=null)
			titre+="de "+espece.espece_nom;
		else if(sous_groupe!=null)
			titre+="de "+sous_groupe;
		else if(groupe!=null)
			titre+="de "+groupe;
		if(stade_sexe!=null)
			titre+=" au stade "+stade_sexe;
		if(!maille.equals(""))
			titre+=" dans la maille "+maille;
		titre+=" du "+date1+" au "+date2;
		sheet.createRow(0).createCell(0).setCellValue(titre);
		int i = 1;
		for(TemoinsParPeriode temoin : temoins){
			Row row = sheet.createRow(i);
			row.createCell(0);
			row.createCell(1);
			sheet.getRow(i).getCell(0).setCellValue(temoin.temoin.toString());
			sheet.getRow(i).getCell(1).setCellValue(temoin.nombreDeTemoignages);
			i++;
		}
	}
}
