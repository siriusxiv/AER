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

	@Override
	public String toString(){
		return super_famille_nom;
	}
}
