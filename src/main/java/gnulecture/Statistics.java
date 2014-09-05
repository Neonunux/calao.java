package gnulecture;

/***********************************************
 This file is part of the GnuLecture project (https://github.com/Neonunux/gnulecture/wiki).

 GnuLecture is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 GnuLecture is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GnuLecture.  If not, see <http://www.gnu.org/licenses/>.

 **********************************************/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Neonunux
 *
 */
public class Statistics {
	private static final Logger logger = LogManager.getLogger(Statistics.class
			.getName());
	private int notesPlayed;
	private int correctAnswers;
	private int wrongAnswers;
	private int wrongRhythms;

	private int totalScore;
	private int precisionAmount;
	private int avgPrecision;
	private long startTime;
	private int timeSpent;
	private int gameSpeed;

	public Statistics() {
		reset();
	}

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

	public void setGameSpeed(int speed) {
		gameSpeed = speed;
	}

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

	public int getNotesPlayed() {
		return notesPlayed;
	}

	public int getCorrectNumber() {
		return correctAnswers;
	}

	public int getWrongNumber() {
		return wrongAnswers;
	}

	public int getWrongRhythms() {
		return wrongRhythms;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getAveragePrecision() {
		return avgPrecision;
	}

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
	public void storeData(int gameType) {
		String fname = "GnuLectureGnuLectureStats_" + getDateTime("yyyyMM")
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
			System.err.println("Error: " + e.getMessage());
		}
	}
}
