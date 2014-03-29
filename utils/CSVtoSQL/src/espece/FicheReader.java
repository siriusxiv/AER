package espece;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FicheReader {

	static ErrorFile errorFile;
	static Connection connect = null;

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/aer?"+"user=root&password=");

		doIt();

		connect.close();
	}

	public static void doIt() throws IOException, SQLException{
		errorFile = new ErrorFile("errors");
		Fiches carabinae = new Fiches("FICHES_Carabinae.txt");
		Observation.addFile("OBSERVATIONS_Carabinae.csv", carabinae);
		Fiches cicindelinae = new Fiches("FICHES_Cicindelinae.txt");
		Observation.addFile("OBSERVATIONS_Cicindelinae.csv", cicindelinae);
		Fiches heteroceres = new Fiches("FICHES_Hétérocères.txt");
		Observation.addFile("OBSERVATIONS_Hétérocères.csv", heteroceres);
		Fiches odonates = new Fiches("FICHES_Odonates.txt");
		Observation.addFile("OBSERVATIONS_Odonates.csv", odonates);
		Fiches orthoptera = new Fiches("FICHES_Orthoptera.txt");
		Observation.addFile("OBSERVATIONS_Orthoptera.csv", orthoptera);
		Fiches rhopaloceres = new Fiches("FICHES_Rhopalocères.txt");
		Observation.addFile("OBSERVATIONS_Rhopalocères.csv", rhopaloceres);
		Fiches scarabaeoidea = new Fiches("FICHES_Scarabaeoidea.txt");
		Observation.addFile("OBSERVATIONS_Scarabaeoidea.csv", scarabaeoidea);
		Fiches zygaenidae = new Fiches("FICHES_Zygaenidae.txt");
		Observation.addFile("OBSERVATIONS_Zygaenidae.csv", zygaenidae);
		
		Fiches toutesLesFiches = new Fiches(carabinae, cicindelinae, heteroceres, odonates, orthoptera, rhopaloceres, scarabaeoidea, zygaenidae);
		
		FileWriter fw = new FileWriter("out.sql");
		toutesLesFiches.writesWith(fw);
		fw.append("\n");
		Observation.writesWith(fw);
		fw.flush();
		fw.close();
		errorFile.getStatus();
		errorFile.flush();
		System.out.println(toutesLesFiches.fiches.size()+" témoignages traités soit "+Observation.observations.size()+" observations");
	}

}
