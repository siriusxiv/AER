package espece;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Taxon,AUTEUR,Synonyme ou nom erroné,Groupe,Sous-Groupe,Ordre,Super famille,Famille,Sous_famille,SYSTEMATIQUE,Commentaire
 * Calopteryx splendens,"(Harris, 1782)",,Odonata,Zygoptera,Odonata,Calopterygoidea,Calopterygidae,,1,
 * 
 * (1,'Calopteryx splendens','(Harris, 1782)',1,1,'')
 * @author malik
 *
 */
public class Espece {
	String taxon;
	String auteur;
	String sous_famille;
	Integer systematique;
	String commentaire;

	Espece(String csvline){
		String[] colonnes = csvline.split(";");
		taxon=colonnes[0];
		auteur=colonnes[1];
		sous_famille=colonnes[8];
		systematique=Integer.parseInt(colonnes[9]);
		if(colonnes.length==11)
			commentaire=colonnes[10];
		else
			commentaire="";
	}

	@Override
	public String toString(){
		try {
			return "("+systematique+",'"+taxon+"','"+auteur+"',"+this.getSousFamille()+","+systematique+",'"+commentaire+"'),\n";
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			return e.toString();
		}
	}

	private Integer getSousFamille() throws SQLException, ClassNotFoundException {
		if(sous_famille.equals(""))
			return systematique;
		else{
			Statement statement = CSVReader.connect.createStatement();
			String query = "SELECT sous_famille_id FROM sous_famille WHERE sous_famille_nom='"+sous_famille+"';";
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
				return resultSet.getInt("sous_famille_id");
		}
		return -1;
	}
}
