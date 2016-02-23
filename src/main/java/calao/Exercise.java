/***********************************************
This file is part of the Calao project (https://github.com/Neonunux/calao/wiki).

Calao is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Calao is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Calao.  If not, see <http://www.gnu.org/licenses/>.

**********************************************/
package calao;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The Class Exercise.
 *
 * @author Neonunux
 */
public class Exercise 
{
	
	
	private static final Logger logger =  LogManager.getLogger(Exercise.class.getName());
	
	
	Preferences appPrefs;
	
	/** The type. */
	int type; // 0 - notes in line, 1 - rhythm, 2 - score
	
	/** The title. */
	String title; // user defined exercise title
	
	/** The clef mask. */
	int clefMask;
	
	/** The acc. */
	Accidentals acc;
	
	/** The time sign. */
	int timeSign;
	
	/** The speed. */
	int speed;
	
	/** The randomize. */
	int randomize;
	
	/** The notes. */
	Vector<Note> notes;
	
	/** The notes2. */
	Vector<Note> notes2;
	
	/**
	 * Instantiates a new exercise.
	 *
	 * @param p the p
	 */
	public Exercise(Preferences p)
	{
		appPrefs = p;
		notes = new Vector<Note>();
		notes2 = new Vector<Note>();
		reset();
	}
	
	/**
	 * Reset.
	 */
	public void reset()
	{
		type = -1;
		title = "";
		clefMask = -1;
		acc = new Accidentals("", 0, appPrefs);
		timeSign = -1;
		speed = 60;
		randomize = 0;
		notes.clear();
		notes2.clear();
	}
	
	/**
	 * Sets the type.
	 *
	 * @param t the new type
	 */
	public void setType(int t)
	{
		this.type = t;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param t the new title
	 */
	public void setTitle(String t)
	{
		logger.debug("Set exercise title to - " + t);
		this.title = t;
	}
	
	/**
	 * Sets the clef mask.
	 *
	 * @param mask the new clef mask
	 */
	public void setClefMask(int mask)
	{
		logger.debug("Set exercise clefs mask to - " + mask);
		this.clefMask = mask;
	}
	
	/**
	 * Sets the measure.
	 *
	 * @param mes the new measure
	 */
	public void setMeasure(int mes)
	{
		logger.debug("Set exercise measure to - " + mes);
		this.timeSign = mes;
	}
	
	/**
	 * Sets the speed.
	 *
	 * @param s the new speed
	 */
	public void setSpeed(int s)
	{
		logger.debug("Set exercise speed to - " + s);
		this.speed = s;
	}
	
	/**
	 * Adds the sequence.
	 *
	 * @param d the d
	 * @param root the root
	 * @param n the n
	 */
	private void addSequence(Document d, Element root, Vector<Note> n)
	{
		Element exSequence = d.createElement("sequence");
		root.appendChild(exSequence);
		
		for (int i = 0; i < n.size(); i++)
		{
			Element exNote = d.createElement("note");
			exSequence.appendChild(exNote);
			
			Note tmpNote = n.get(i);
			exNote.setAttribute("t", Integer.toString(tmpNote.type));
			exNote.setAttribute("p", Integer.toString(tmpNote.pitch));
			exNote.setAttribute("l", Integer.toString(tmpNote.level));
			exNote.setAttribute("ts", Double.toString(tmpNote.timestamp));
			exNote.setAttribute("d", Double.toString(tmpNote.duration));
			exNote.setAttribute("c", Integer.toString(tmpNote.clef));
			if (tmpNote.altType != 0)
				exNote.setAttribute("a", Integer.toString(tmpNote.altType));
/*
			Element nType = d.createElement("t");
			nType.appendChild(d.createTextNode(Integer.toString(tmpNote.type)));
			exNote.appendChild(nType);		

			Element nPitch = d.createElement("p");
			nPitch.appendChild(d.createTextNode(Integer.toString(tmpNote.pitch)));
			exNote.appendChild(nPitch);

			Element nLevel = d.createElement("l");
			nLevel.appendChild(d.createTextNode(Integer.toString(tmpNote.level)));
			exNote.appendChild(nLevel);

			Element nTime = d.createElement("ts");
			nTime.appendChild(d.createTextNode(Double.toString(tmpNote.timestamp)));
			exNote.appendChild(nTime);				

			Element nDur = d.createElement("d");
			nDur.appendChild(d.createTextNode(Double.toString(tmpNote.duration)));
			exNote.appendChild(nDur);

			Element nClef = d.createElement("c");
			nClef.appendChild(d.createTextNode(Integer.toString(tmpNote.clef)));
			exNote.appendChild(nClef);	

			if (tmpNote.altType != 0)
			{
				Element nAlt = d.createElement("a");
				nAlt.appendChild(d.createTextNode(Integer.toString(tmpNote.altType)));
				exNote.appendChild(nAlt);	
			}
*/
		}
	}
	
	/**
	 * Save to xml.
	 */
	public void saveToXML()
	{
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("exercise");
			doc.appendChild(rootElement);

			// ************************* HEADER FIELDS ***********************************
			Element staff = doc.createElement("header");
			rootElement.appendChild(staff);

			// set attribute to staff element
			//Attr attr = doc.createAttribute("id");
			//attr.setValue("1");
			//staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");
			Element exVersion = doc.createElement("version");
			exVersion.appendChild(doc.createTextNode(Integer.toString(3))); // remember to update this value in case more tags are added
			staff.appendChild(exVersion);

			Element exType = doc.createElement("type");
			exType.appendChild(doc.createTextNode(Integer.toString(type)));
			staff.appendChild(exType);

			Element exTitle = doc.createElement("title");
			exTitle.appendChild(doc.createTextNode(title));
			staff.appendChild(exTitle);

			Element clef = doc.createElement("clef");
			clef.appendChild(doc.createTextNode(Integer.toString(clefMask)));
			staff.appendChild(clef);
	 
			Element accType = doc.createElement("accType");
			accType.appendChild(doc.createTextNode(acc.getType()));
			staff.appendChild(accType);
			
			Element accCount = doc.createElement("accCount");
			accCount.appendChild(doc.createTextNode(Integer.toString(acc.getNumber())));
			staff.appendChild(accCount);
	 
			Element measure = doc.createElement("measure");
			measure.appendChild(doc.createTextNode(Integer.toString(timeSign)));
			staff.appendChild(measure);
			
			Element exSpeed = doc.createElement("speed");
			exSpeed.appendChild(doc.createTextNode(Integer.toString(speed)));
			staff.appendChild(exSpeed);
			
			Element exRandom = doc.createElement("random");
			exRandom.appendChild(doc.createTextNode(Integer.toString(randomize)));
			staff.appendChild(exRandom);
			
			
			// ************************ SEQUENCE ****************************
			if (notes.size() > 0)
				addSequence(doc, rootElement, notes);
			if (notes2.size() > 0)
				addSequence(doc, rootElement, notes2);
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Exercises" + File.separator + title + ".xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			
			transformer.transform(source, result);
	 
			logger.info("XML File successfully saved !");
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
	
	
	/**
	 * Gets the tag value.
	 *
	 * @param sTag the s tag
	 * @param eElement the e element
	 * @return the tag value
	 */
	private static String getTagValue(String sTag, Element eElement) 
	{
		NodeList elList = eElement.getElementsByTagName(sTag);
		if (elList == null || elList.getLength() == 0) return "-99";
		NodeList nlList = elList.item(0).getChildNodes();
		if (nlList == null || nlList.getLength() == 0) return "";
 
        Node nValue = (Node) nlList.item(0);
 
        if (nValue != null)
        	return nValue.getNodeValue();
        else
        	return "";
	}
	  
	/**
	 * Load from file.
	 *
	 * @param path the path
	 */
	public void loadFromFile(String path)
	{
		int version = 0;
		int levOffset = 0; // offset to be added to notes levels
		notes.clear();
		notes2.clear();
		try
		{
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
	 
			logger.debug("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("header");
			//logger.debug("-----------------------");
	 
		    Node nNode = nList.item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
	 
				version = Integer.parseInt(getTagValue("version", eElement));
				if (version < 0)
					levOffset = 2;
				type = Integer.parseInt(getTagValue("type", eElement));
				title = getTagValue("title", eElement);
				clefMask = Integer.parseInt(getTagValue("clef", eElement));
				acc = new Accidentals(getTagValue("accType", eElement), Integer.parseInt(getTagValue("accCount", eElement)), appPrefs);
				timeSign = Integer.parseInt(getTagValue("measure", eElement));
				speed = Integer.parseInt(getTagValue("speed", eElement));
				randomize = Integer.parseInt(getTagValue("random", eElement));

				logger.debug("Type: " + type);
			    logger.debug("Title: " + title);
			    logger.debug("Clef: " + clefMask);
		        logger.debug("accType: " + acc.getType());
			    logger.debug("accCount: " + acc.getNumber());
			    logger.debug("Time signature: " + timeSign);
			    logger.debug("Speed: " + speed);
			    logger.debug("Randomize: " + randomize);
			}
			
			NodeList sList = doc.getElementsByTagName("sequence");
			// cycle through sequences
			for (int seq = 0; seq < sList.getLength(); seq++)
			{
			   Element sElem = (Element)sList.item(seq);
			   NodeList notesList = sElem.getElementsByTagName("note");
			   if (notesList != null)
			   {
				   logger.debug("Sequence #" + seq + ": notes: " + notesList.getLength());
				   for (int n = 0; n < notesList.getLength(); n++)
				   {
					   Note tmpNote;
					   int nType = 0, nPitch = 0, nLevel = 0, nClef = 0, nAlt = 0;
					   double nStamp = 0, nDur = 0;
					   Element nElem = (Element)notesList.item(n);
					   if (version < 3)
					   {
						   nType = Integer.parseInt(getTagValue("t", nElem));
						   nPitch = Integer.parseInt(getTagValue("p", nElem));
						   nLevel = Integer.parseInt(getTagValue("l", nElem)) + levOffset;
						   nStamp = Double.parseDouble(getTagValue("ts", nElem));
						   nDur = Double.parseDouble(getTagValue("d", nElem));
						   nClef = Integer.parseInt(getTagValue("c", nElem));
						   nAlt = Integer.parseInt(getTagValue("a", nElem));
					   }
					   else
					   {
						   nType = Integer.parseInt(nElem.getAttribute("t"));
						   nPitch = Integer.parseInt(nElem.getAttribute("p"));
						   nLevel = Integer.parseInt(nElem.getAttribute("l")) + levOffset;
						   nStamp = Double.parseDouble(nElem.getAttribute("ts"));
						   nDur = Double.parseDouble(nElem.getAttribute("d"));
						   nClef = Integer.parseInt(nElem.getAttribute("c"));
						   String alt = nElem.getAttribute("a");
						   if (alt != "")
							   nAlt = Integer.parseInt(alt);
					   }

//					   tmpNote = new Note(0, nClef, nLevel, nPitch, nType, false, 0);
//					   if (nAlt > -3)
//						   tmpNote.altType = nAlt;
//					   tmpNote.setTimeStamp(nStamp);
//					   tmpNote.duration = nDur;
//					   if (seq == 0)
//						   notes.add(tmpNote);
//					   else if (seq == 1)
//					   {
//						   tmpNote.secondRow = true;
//						   notes2.add(tmpNote);
//					   }
				   }
			   }
			   
			}
			
		  } 
		  catch (Exception e) 
		  {
			e.printStackTrace();
		  }
	}
}
