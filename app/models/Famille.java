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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Famille extends Model implements Comparator<Famille>{
	@Id
	public Integer famille_id;
	@NotNull
	public String famille_nom;
	@NotNull
	@ManyToOne
	public SuperFamille famille_super_famille;

	public static Finder<Integer,Famille> find = new Finder<Integer,Famille>(Integer.class, Famille.class);

	public static List<Famille> findAll(){
		return find.findList();
	}
	/**
	 * Trie les familles
	 * @return
	 */
	public static List<Famille> findAllTriees() {
		List<Famille> familles = find.all();
		Collections.sort(familles, new Famille());
		return familles;
	}
	/**
	 * Pour trier les listes de familles selon la systématique de la première espèce dedans.
	 */
	@Override
	public int compare(Famille f1, Famille f2) {
		int sys1 = f1.getSystematiquePremiereEspeceDansThis();
		int sys2 = f2.getSystematiquePremiereEspeceDansThis();
		return (sys1<sys2 ? -1 : (sys1==sys2 ? 0 : 1));
	}
	
	@Override
	public String toString(){
		return famille_nom;
	}

	/**
	 * Trouve les familles que l'on peut ajouter dans un sous-groupe
	 * @return
	 */
	public static List<Famille> findFamillesAjoutablesDansSousGroupe(){
		List<Espece> especesSansSousGroupe = Espece.findEspecesAjoutablesDansSousGroupe();
		List<Famille> familles = new ArrayList<Famille>();
		for(Espece espece : especesSansSousGroupe){
			if(!familles.contains(espece.espece_sousfamille.sous_famille_famille)){
				//On trouve toutes les espèces de cette la famille de l'espèce
				List<Espece> especesDansFamille = espece.espece_sousfamille.sous_famille_famille.getEspecesDansThis();
				//Si toutes les espèces de cette famille sont sans sous-groupes
				//on ajoute la famille
				if(especesSansSousGroupe.containsAll(especesDansFamille)){
					familles.add(espece.espece_sousfamille.sous_famille_famille);
				}
			}
		}
		return familles;
	}
	
	/**
	 * Renvoie la liste des espèces dans cette famille
	 * @return
	 */
	public List<Espece> getEspecesDansThis(){
		return Espece.find.where()
				.eq("espece_sousfamille.sous_famille_famille", this)
				.orderBy("espece_systematique").findList();
	}
	
	/**
	 * Renvoie le numéro systématique de la première espèce dans cette famille.
	 * Utile pour trier les familles.
	 * @return
	 */
	public int getSystematiquePremiereEspeceDansThis(){
		Espece espece = Espece.find.where()
				.eq("espece_sousfamille.sous_famille_famille", this)
				.setMaxRows(1).orderBy("espece_systematique").findUnique();
		if(espece==null)
			return -1;
		else
			return espece.espece_systematique;
	}
}
