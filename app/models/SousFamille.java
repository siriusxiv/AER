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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class SousFamille extends Model{
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

	@Override
	public String toString(){
		if(sous_famille_existe)
			return sous_famille_nom;
		else
			return Espece.find.where().eq("espece_sous_famille", this).findUnique().toString();
	}
}
