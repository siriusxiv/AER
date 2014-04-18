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
package functions.excels.exports;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import models.Espece;
import models.Fiche;
import models.FicheHasMembre;
import models.Groupe;
import models.InformationsComplementaires;
import models.Observation;
import models.SousGroupe;
import models.StadeSexe;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import controllers.ajax.expert.requetes.calculs.ChronologieDUnTemoin;
import functions.excels.Excel;

public class ChronologieDUnTemoinExcel extends Excel {

	public ChronologieDUnTemoinExcel(Map<String,String> info,ChronologieDUnTemoin cdut){
		super();
		Sheet sheet = wb.createSheet("Carte somme");
		Espece espece = Espece.find.byId(Integer.parseInt(info.get("espece")));
		SousGroupe sous_groupe = SousGroupe.find.byId(Integer.parseInt(info.get("sous_groupe")));
		Groupe groupe = Groupe.find.byId(Integer.parseInt(info.get("groupe")));
		StadeSexe stade_sexe = StadeSexe.find.byId(Integer.parseInt(info.get("stade")));
		String maille = info.get("maille");
		String temoin = info.get("temoin");
		String date1 = info.get("jour1")+"/"+info.get("mois1")+"/"+info.get("annee1");
		String date2 = info.get("jour2")+"/"+info.get("mois2")+"/"+info.get("annee2");
		String titre = "Chronologie des témoignages ";
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
		titre+=" faits par "+temoin;
		titre+=" du "+date1+" au "+date2;
		sheet.createRow(0).createCell(0).setCellValue(titre);
		sheet.addMergedRegion(new CellRangeAddress(
				0, //first row (0-based)
				0, //last row  (0-based)
				0, //first column (0-based)
				12  //last column  (0-based)
				));
		Row rowHead = sheet.createRow(1);
		rowHead.createCell(0).setCellValue("Fiche ID");
		rowHead.createCell(1).setCellValue("UTM");
		rowHead.createCell(2).setCellValue("Lieu-dit");
		rowHead.createCell(3).setCellValue("Commune");
		rowHead.createCell(4).setCellValue("Dép.");
		rowHead.createCell(5).setCellValue("Date");
		rowHead.createCell(6).setCellValue("Espèce");
		rowHead.createCell(7).setCellValue("Nombre");
		rowHead.createCell(8).setCellValue("Stade/Sexe");
		rowHead.createCell(9).setCellValue("Témoins");
		rowHead.createCell(10).setCellValue("Mémo");
		SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
		int i = 2;
		for(InformationsComplementaires complements : cdut.chronologie){
			Row row = sheet.createRow(i);
			Observation observation = complements.informations_complementaires_observation;
			Fiche fiche = observation.observation_fiche;
			row.createCell(0).setCellValue(fiche.fiche_id);
			row.createCell(1).setCellValue(fiche.fiche_utm.utm);
			row.createCell(2).setCellValue(fiche.fiche_lieudit);
			if(fiche.fiche_commune!=null){
				row.createCell(3).setCellValue(fiche.fiche_commune.ville_nom_aer);
				row.createCell(4).setCellValue(fiche.fiche_commune.ville_departement.departement_code);
			}
			String date;
			if(fiche.fiche_date_min==null)
				date = dateFormat.format(fiche.fiche_date.getTime());
			else
				date = dateFormat.format(fiche.fiche_date_min.getTime())
							+" à "+dateFormat.format(fiche.fiche_date.getTime());
			row.createCell(5).setCellValue(date);
			row.createCell(6).setCellValue(observation.observation_espece.espece_nom);
			Integer nombre = complements.informations_complementaires_nombre_de_specimens;
			if(nombre==null)
				row.createCell(7).setCellValue("?");
			else
				row.createCell(7).setCellValue(nombre);
			row.createCell(8).setCellValue(complements.informations_complementaires_stade_sexe.stade_sexe_intitule);
			StringBuilder membres = new StringBuilder();
			List<FicheHasMembre> fhms = fiche.getFicheHasMembre();
			for(int j = 0 ; j<fhms.size()-1 ; j++){
				membres.append(fhms.get(j).membre);
				membres.append(", ");
			}
			if(!fhms.isEmpty())
				membres.append(fhms.get(fhms.size()-1).membre);
			else
				membres.append("et al.");
			row.createCell(9).setCellValue(membres.toString());
			row.createCell(10).setCellValue(fiche.fiche_memo);
			i++;
		}
		for(int j = 0; j<11 ; j++)
			sheet.autoSizeColumn(j);
	}

}
