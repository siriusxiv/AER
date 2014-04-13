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

import javax.naming.NamingException;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * Il existe des espèces qui ne possèdent pas de sousfamilles.
 * Afin de garder la base de données consistante, si un telle espèce
 * existe, alors sa sous famille (dans la base de données) possède le
 * même nom avec cependant le booléen sous_famille_existe valant FAUX.
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class SousFamille extends Model implements Comparator<SousFamille>{
	@Id
	public Integer sous_famille_id;
	@NotNull
	public String sous_famille_nom;
	@NotNull
	public boolean sous_famille_existe;
	@NotNull
	@ManyToOne
	public Famille sous_famille_famille;

	public static Finder<Integer,SousFamille> find = new Finder<Integer,SousFamille>(Integer.class, SousFamille.class);

	/**
	 * Trie les sous-familles
	 * @return
	 */
	public static List<SousFamille> findSousFamillesExistantesTriees(){
		List<SousFamille> sfs = find.where().eq("sous_famille_existe", true).findList();
		Collections.sort(sfs,new SousFamille());
		return sfs;
	}
	private SousFamille(){};

	/**
	 * Crée une sous famille et lance une PersistenceException si plusieurs familles portent le même nom,
	 * une NamingException si la famille en argument n'existe pas.
	 * @param nom
	 * @param existe
	 * @param sousFamilleOuFamilleId
	 * @throws PersistenceException
	 * @throws NamingException
	 */
	public SousFamille(String nom, boolean existe, Integer sousFamilleOuFamilleId) throws PersistenceException, NamingException{
		sous_famille_nom=nom;
		sous_famille_existe=existe;
		sous_famille_famille=Famille.find.byId(sousFamilleOuFamilleId);
		if(sous_famille_famille==null){
			throw new NamingException("La famille "+sousFamilleOuFamilleId+" n'existe pas !");
		}
	}

	/**
	 * Renvoie le nom de la sous-famille
	 */
	@Override
	public String toString(){
		if(sous_famille_existe)
			return sous_famille_nom;
		else
			return Espece.find.where().eq("espece_sousfamille", this).findUnique().toString();
	}
	/**
	 * Pour trier les listes de sous-familles selon la systématique de la première espèce dedans.
	 */
	@Override
	public int compare(SousFamille sf1, SousFamille sf2) {
		int sys1 = sf1.getSystematiquePremiereEspeceDansThis();
		int sys2 = sf2.getSystematiquePremiereEspeceDansThis();
		return (sys1<sys2 ? -1 : (sys1==sys2 ? 0 : 1));
	}

	/**
	 * Renvoie la liste des sous-familles existantes.
	 * @return 
	 */
	public static List<SousFamille> findSousFamillesExistantes(){
		return SousFamille.find.where().eq("sous_famille_existe", true).findList();
	}

	/**
	 * Trouve les sous-familles que l'on peut ajouter dans un sous-groupe
	 * @return
	 */
	public static List<SousFamille> findSousFamillesAjoutablesDansSousGroupe(){
		List<Espece> especesSansSousGroupe = Espece.findEspecesAjoutablesDansSousGroupe();
		List<SousFamille> sous_familles = new ArrayList<SousFamille>();
		for(Espece espece : especesSansSousGroupe){
			if(espece.espece_sousfamille.sous_famille_existe){
				if(!sous_familles.contains(espece.espece_sousfamille)){
					//On trouve toutes les espèces de cette sous-famille
					List<Espece> especesDansSousFamille = espece.espece_sousfamille.getEspecesDansThis();
					//Si toutes les espèces de cette sous-famille sont sans sous-groupes
					//on ajoute la sous-famille
					if(especesSansSousGroupe.containsAll(especesDansSousFamille)){
						sous_familles.add(espece.espece_sousfamille);
					}
				}
			}
		}
		return sous_familles;
	}
	
	/**
	 * Renvoie la liste des espèces dans cette sous-famille
	 * @return
	 */
	public List<Espece> getEspecesDansThis(){
		return Espece.find.where()
				.eq("espece_sousfamille", this)
				.orderBy("espece_systematique").findList();
	}
	
	/**
	 * Renvoie le numéro systématique de la première espèce dans cette sous-famille.
	 * Utile pour trier les ordres.
	 * @return
	 */
	public int getSystematiquePremiereEspeceDansThis(){
		Espece espece = Espece.find.where()
				.eq("espece_sousfamille", this)
				.setMaxRows(1).orderBy("espece_systematique").findUnique();
		if(espece==null)
			return -1;
		else
			return espece.espece_systematique;
	}
}
