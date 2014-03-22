package espece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class CSVReader {
	static Connection connect = null;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/aer?"+"user=root&password=");
		
		outEspeces();
		
		connect.close();
	}
	
	public static void outEspeces()  throws IOException, ClassNotFoundException, SQLException {
		FileReader fr = new FileReader("ESPECES NEW.csv");
		BufferedReader br = new BufferedReader(fr);
		String line;
		br.readLine();
		ArrayList<Espece> especes = new ArrayList<Espece>();
		while((line=br.readLine())!=null){
			//System.out.println(line);
			Espece espece = new Espece(line);
			especes.add(espece);
			System.out.println(espece.systematique+":"+espece.sous_famille);
			//System.out.println(new Espece(line));
		}
		br.close();
		FileWriter fw = new FileWriter("out.sql");
		fw.append("INSERT INTO `espece` (`espece_id`, `espece_nom`, `espece_auteur`, `espece_sousfamille_sous_famille_id`, `espece_systematique`, `espece_commentaires`) VALUES\n");
		for(Espece e : especes){
			fw.append(e.toString());
		}
		fw.close();
	}
}
