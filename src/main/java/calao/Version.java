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
	private String rest;


	private String release;
	private String versionString;

	private static final Logger logger = LogManager.getLogger(Version.class
			.getName());

	public Version() {
		this(null);
	}

	public Version(String v) {
		if (v == null)
			versionString = getBuildStringFromPomXml();
		else
			versionString = v;
		parseAttributesFromVersionString(versionString);
		
	}

	public void parseAttributesFromVersionString(String v) {
		logger.trace("versionString " + v);
		String parts[] = v.split("\\.");
		
		setMajor(Integer.parseInt(parts[0]));
		logger.trace("Major " + major);

		setMinor(Integer.parseInt(parts[1]));
		logger.trace("Minor " + minor);
		
		String rel = parts[2];
		String buildAndRelease[] = rel.split("[\\._-]");
		if (buildAndRelease[0].matches("^[0-9]{4,}$*")){
			setBuild(Integer.parseInt(buildAndRelease[0]));
		}
//		String r = buildAndRelease[1];
//		if (r != null)
//			setRest(r);
		setRelease(rel);
		logger.trace("Release " + rel);
		if (rel.matches("-"))
			;

	}

	public String getBuildStringFromPomXml() {
		Model model = null;
		FileReader reader = null;
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();

		try {
			File pomfile = new File("pom.xml");
			reader = new FileReader(pomfile);
			model = mavenreader.read(reader);
			model.setPomFile(pomfile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		MavenProject project = new MavenProject(model);

		logger.trace("version " + project.getVersion());
		return project.getVersion();
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

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getVersionString() {
		return versionString;
	}

	public void setVersionString(String versionString) {
		this.versionString = versionString;
	}
	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

}
