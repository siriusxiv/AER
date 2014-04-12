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

package models;

import java.util.ArrayList;
import java.util.List; 

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Ordre extends Model {
	@Id
	public Integer ordre_id;
	@NotNull
	public String ordre_nom;
	
	public static Finder<Integer,Ordre> find = new Finder<Integer,Ordre>(Integer.class, Ordre.class);

	public static List<Ordre> findAll(){
		return find.findList();
	}
	
	@Override
	public String toString(){
		return ordre_nom;
	}
	
	/**
	 * Trouve les ordres que l'on peut ajouter dans un sous-groupe
	 * @return
	 */
	public static List<Ordre> findOrdresAjoutablesDansSousGroupe(){
		List<Espece> especesSansSousGroupe = Espece.findEspecesAjoutablesDansSousGroupe();
		List<Ordre> ordres = new ArrayList<Ordre>();
		for(Espece espece : especesSansSousGroupe){
			//On trouve toutes les espèces de cette la famille de l'espèce
			List<Espece> especesDansOrdre = Espece.find.where().eq("espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre", espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre).findList();
			//Si toutes les espèces de cette famille sont sans sous-groupes
			//on ajoute la famille
			if(especesSansSousGroupe.containsAll(especesDansOrdre)){
				ordres.add(espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre);
				especesSansSousGroupe.removeAll(especesDansOrdre);
			}
		}
		return ordres;
	}
}
