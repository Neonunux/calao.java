package calao;

import java.io.File;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;

public class Version {

	private int major;
	private int minor;
	private int build;
	private String version;
	
	private static final Logger logger = LogManager.getLogger(Version.class
			.getName());
	
	public Version(){
		major = 3;
		minor = 4;
		build = getBuildFromPomXml();
	}
	public int getBuildFromPomXml(){
		int i =2800;
//		try {
//			//String pomXml = getClass().getResource("icon.png");
//			//File pom = new File();
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
		
		
		Model model = null;
		FileReader reader = null;
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();

		try {
		    File pomfile = new File("pom.xml"); 
			reader = new FileReader(pomfile); // <-- pomfile is your pom.xml
		     model = mavenreader.read(reader);
		     model.setPomFile(pomfile);
		}catch(Exception ex){
		     // do something better here
		     ex.printStackTrace();
		}

		MavenProject project = new MavenProject(model);
		version = project.getVersion(); // <-- thats what you need
		logger.debug("version " + version);
		
		return i;
	}
	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}


	
}
