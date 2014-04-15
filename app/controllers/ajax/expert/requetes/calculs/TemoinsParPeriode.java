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
package controllers.ajax.expert.requetes.calculs;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import controllers.ajax.expert.requetes.Calculs;
import models.FicheHasMembre;
import models.Membre;
import models.Observation;

public class TemoinsParPeriode implements Comparator<TemoinsParPeriode>{
	public Membre temoin;
	public int nombreDeTemoignages;

	public TemoinsParPeriode(Membre temoin, Integer nombreDeTemoignage){
		this.temoin=temoin;
		this.nombreDeTemoignages=nombreDeTemoignage;
	}

	@Override
	public String toString(){
		return temoin.membre_nom+" : "+nombreDeTemoignages;
	}

	/**
	 * Trie par ordre décroissant de nombre de témoignages
	 */
	@Override
	public int compare(TemoinsParPeriode t1, TemoinsParPeriode t2) {
		return (t1.nombreDeTemoignages<t2.nombreDeTemoignages ? 1 : (t1.nombreDeTemoignages==t2.nombreDeTemoignages ? 0 : -1));
	}
	private TemoinsParPeriode(){}


	public static List<TemoinsParPeriode> calculeTemoinsParPeriode(Map<String,String> info) throws ParseException {
		List<TemoinsParPeriode> temoins = new ArrayList<TemoinsParPeriode>();
		List<Observation> observations = Calculs.getObservations(info);
		//On commence la génération des témoins par période.
		int i=0;
		for(Observation observation : observations){
			List<FicheHasMembre> fhms = FicheHasMembre.find.where().eq("fiche", observation.getFiche()).findList();
			if(i%1000==0){System.out.println(i);}
			for(FicheHasMembre fhm : fhms){
				int position;
				if((position=position(fhm.membre,temoins))==-1){
					temoins.add(new TemoinsParPeriode(fhm.membre,1));
				}else{
					temoins.get(position).nombreDeTemoignages++;
				}
			}
			i++;
		}
		Collections.sort(temoins,new TemoinsParPeriode());
		return temoins;
	}

	/**
	 * Renvoie la position du membre dans la liste temoins
	 * @param membre
	 * @param temoins
	 * @return
	 */
	private static int position(Membre membre, List<TemoinsParPeriode> temoins) {
		int i = 0;
		while(i<temoins.size()){
			if(temoins.get(i).temoin.equals(membre))
				return i;
			i++;
		}
		return -1;
	}
}
