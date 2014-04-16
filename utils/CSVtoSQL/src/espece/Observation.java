package espece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

public class Observation {

	static ArrayList<Observation> observations = new ArrayList<Observation>();
	static int compteur_id = 0;
	
	int id;
	String line_origine;
	
	Fiche fiche;
	String espece;
	boolean imago;

	public Observation(String line, Fiches fiches) throws NamingException{
		line_origine=line;
		compteur_id++;
		id=compteur_id;
		String[] col = line.split("µ");
		fiche=fiches.find(col[2]);
		espece=col[1];
		imago=col[3].equals("TRUE") || col[3].equals("1");
	}

	public static void addFile(String filename, Fiches fiches) throws IOException{
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(filename));
		int line_compteur = 1;
		br.readLine();
		String line;
		while((line=br.readLine())!=null){
			line_compteur++;
			try {
				Observation o = new Observation(line,fiches);
				observations.add(o);
			} catch (NamingException e) {
				FicheReader.errorFile.write(filename+": "+line_compteur+". "+line);
				e.printStackTrace();
			}
		}
		FicheReader.errorFile.flush();
		br.close();
	}
	
	public static void writesWith(FileWriter fw) throws IOException, SQLException{
		fw.append("INSERT INTO `observation` (`observation_id`,`observation_espece_espece_id`,`observation_fiche_fiche_id`,`obervation_vue_par_expert`,`observation_validee`,`observation_date_derniere_modification`,`observation_date_validation`) VALUES\n");
		for(Observation o : observations.subList(0, observations.size()-1)){
			fw.append("("+o.id+","+o.getEspece()+","+o.fiche.id+",1,1,"+o.fiche.getSQLDateMax()+","+o.fiche.getSQLDateMax()+"),\n");
		}
		Observation o = observations.get(observations.size()-1);
		fw.append("("+o.id+","+o.getEspece()+","+o.fiche.id+",1,1,"+o.fiche.getSQLDateMax()+","+o.fiche.getSQLDateMax()+");\n");
		
		//Info complémentaires
		fw.append("\nINSERT INTO `informations_complementaires` (`informations_complementaires_observation_observation_id`,`informations_complementaires_stade_sexe_stade_sexe_id`) VALUES\n");
		for(Observation o1 : observations.subList(0, observations.size()-1)){
			fw.append("("+o1.id+","+o1.getImago()+"),\n");
		}
		Observation o1 = observations.get(observations.size()-1);
		fw.append("("+o1.id+","+o1.getImago()+");\n");
		
	}

	private Integer getImago() {
		if(imago)
			return 1;
		else
			return null;
	}

	private Integer getEspece() throws SQLException, IOException {
		int res = 0;
		Statement statement = FicheReader.connect.createStatement();
		String query = "SELECT espece_id FROM espece WHERE espece_nom='"+this.espece+"';";
		ResultSet resultSet = statement.executeQuery(query);
		while(resultSet.next())
			res = resultSet.getInt("espece_id");
		if(res==0){
			query = "SELECT synonyme_espece_espece_id FROM espece_synonyme WHERE synonyme_nom='"+this.espece+"';";
			resultSet = statement.executeQuery(query);
			while(resultSet.next())
				res = resultSet.getInt("synonyme_espece_espece_id");
		}
		if(res==0)
			FicheReader.errorFile.erreurEspece(line_origine);
		return res;
		
	}
}
