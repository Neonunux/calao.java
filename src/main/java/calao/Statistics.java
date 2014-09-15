package calao;

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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class Statistics.
 *
 * @author Neonunux
 */
public class Statistics {
	
	
	private static final Logger logger = LogManager.getLogger(Statistics.class
			.getName());
	
	/** The notes played. */
	private int notesPlayed;
	
	/** The correct answers. */
	private int correctAnswers;
	
	/** The wrong answers. */
	private int wrongAnswers;
	
	/** The wrong rhythms. */
	private int wrongRhythms;

	/** The total score. */
	private int totalScore;
	
	/** The precision amount. */
	private int precisionAmount;
	
	/** The avg precision. */
	private int avgPrecision;
	
	/** The start time. */
	private long startTime;
	
	/** The time spent. */
	private int timeSpent;
	
	/** The game speed. */
	private int gameSpeed;

	/**
	 * Instantiates a new statistics.
	 */
	public Statistics() {
		reset();
	}

	/**
	 * Reset.
	 */
	public void reset() {
		notesPlayed = 0;
		correctAnswers = 0;
		wrongAnswers = 0;
		wrongRhythms = 0;

		totalScore = 0;
		precisionAmount = 0;
		avgPrecision = 0;
		startTime = System.currentTimeMillis();
		timeSpent = 0;
		gameSpeed = 0;
	}

	/**
	 * Sets the game speed.
	 *
	 * @param speed the new game speed
	 */
	public void setGameSpeed(int speed) {
		this.gameSpeed = speed;
	}

	/**
	 * Note played.
	 *
	 * @param answerType the answer type
	 * @param score the score
	 */
	public void notePlayed(int answerType, int score) {
		notesPlayed++;
		if (answerType == 1) {
			correctAnswers++;
			precisionAmount += 100;
		} else if (answerType == 2) {
			wrongRhythms++;
			precisionAmount += 50;
		} else
			wrongAnswers++;
		totalScore += score;
		if (totalScore < 0)
			totalScore = 0;

		avgPrecision = precisionAmount / notesPlayed;
		timeSpent = (int) (System.currentTimeMillis() - startTime);
	}

	/**
	 * Gets the notes played.
	 *
	 * @return the notes played
	 */
	public int getNotesPlayed() {
		return notesPlayed;
	}

	/**
	 * Gets the correct number.
	 *
	 * @return the correct number
	 */
	public int getCorrectNumber() {
		return correctAnswers;
	}

	/**
	 * Gets the wrong number.
	 *
	 * @return the wrong number
	 */
	public int getWrongNumber() {
		return wrongAnswers;
	}

	/**
	 * Gets the wrong rhythms.
	 *
	 * @return the wrong rhythms
	 */
	public int getWrongRhythms() {
		return wrongRhythms;
	}

	/**
	 * Gets the total score.
	 *
	 * @return the total score
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * Gets the average precision.
	 *
	 * @return the average precision
	 */
	public int getAveragePrecision() {
		return avgPrecision;
	}

	/**
	 * Gets the date time.
	 *
	 * @param dateFormat the date format
	 * @return the date time
	 */
	private String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());

/*		logger.debug(DateUtils.now("dd MMMMM yyyy"));
		logger.debug(DateUtils.now("yyyyMMdd"));
		logger.debug(DateUtils.now("dd.MM.yy"));
		logger.debug(DateUtils.now("MM/dd/yy"));
		logger.debug(DateUtils.now("yyyy.MM.dd G 'at' hh:mm:ss z"));
		logger.debug(DateUtils.now("EEE, MMM d, ''yy"));
		logger.debug(DateUtils.now("h:mm a"));
		logger.debug(DateUtils.now("H:mm:ss:SSS"));
		logger.debug(DateUtils.now("K:mm a,z"));
		logger.debug(DateUtils.now("yyyy.MMMMM.dd GGG hh:mm aaa"));*/

	}

	/*
	 * Store statistics to a file. gameType can be: 0 - line, 1 - rhythm, 2 -
	 * score
	 * 
	 * File line syntax:
	 * DAY,HOURS,MINUTES,SECOND,gameType,notesPlayed,correctAnswers
	 * ,wrongAnswers,wrongRhythms,totalScore,avgPrecision,gameSpeed,timeSpent
	 */
	/**
	 * Store data.
	 *
	 * @param gameType the game type
	 */
	public void storeData(int gameType) {
		String fname = "CalaoCalaoStats_" + getDateTime("yyyyMM")
				+ ".sds";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname,
					true));
			String data = "" + getDateTime("dd,HH,mm,ss") + ","
					+ Integer.toString(gameType) + ",";
			data += Integer.toString(notesPlayed) + ","
					+ Integer.toString(correctAnswers) + ","
					+ Integer.toString(wrongAnswers) + ",";
			data += Integer.toString(wrongRhythms) + ","
					+ Integer.toString(totalScore) + ","
					+ Integer.toString(avgPrecision) + ",";
			data += Integer.toString(gameSpeed) + ","
					+ Integer.toString(timeSpent / 1000);
			data += (char) '\n';

			writer.write(data);
			writer.close();
		} catch (Exception e) // Catch exception if any
		{
			logger.error("Error: " + e.getMessage());
		}
	}
}
