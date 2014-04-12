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
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * Il existe des familles qui ne possèdent pas de superfamilles.
 * Afin de garder la base de données consistante, si un telle famille
 * existe, alors sa super famille (dans la base de données) possède le
 * même nom avec cependant le booléen super_famille_existe valant FAUX.
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class SuperFamille extends Model{
	@Id
	public Integer super_famille_id;
	@NotNull
	public String super_famille_nom;
	@NotNull
	public boolean super_famille_existe;
	@ManyToOne
	@NotNull
	public Ordre super_famille_ordre;

	public static Finder<Integer,SuperFamille> find = new Finder<Integer,SuperFamille>(Integer.class, SuperFamille.class);

	public static List<SuperFamille> findAll(){
		return find.findList();
	}

	@Override
	public String toString(){
		return super_famille_nom;
	}

	public static List<SuperFamille> findSuperFamillesExistantes() {
		return find.where().eq("super_famille_existe", true).findList();
	}

	/**
	 * Trouve les super-familles que l'on peut ajouter dans un sous-groupe
	 * @return
	 */
	public static List<SuperFamille> findSuperFamillesAjoutablesDansSousGroupe(){
		List<Espece> especesSansSousGroupe = Espece.findEspecesAjoutablesDansSousGroupe();
		List<SuperFamille> super_familles = new ArrayList<SuperFamille>();
		for(Espece espece : especesSansSousGroupe){
			if(espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_existe){
				//On trouve toutes les espèces de cette la super-famille de l'espèce
				List<Espece> especesDansSuperFamille = Espece.find.where().eq("espece_sousfamille.sous_famille_famille.famille_super_famille", espece.espece_sousfamille.sous_famille_famille.famille_super_famille).findList();
				//Si toutes les espèces de cette famille sont sans sous-groupes
				//on ajoute la famille
				if(especesSansSousGroupe.containsAll(especesDansSuperFamille)){
					super_familles.add(espece.espece_sousfamille.sous_famille_famille.famille_super_famille);
					especesSansSousGroupe.removeAll(especesDansSuperFamille);
				}
			}
		}
		return super_familles;
	}
}
