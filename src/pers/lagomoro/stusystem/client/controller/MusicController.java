package pers.lagomoro.stusystem.client.controller;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class MusicController {

	private static double[] fftSource = new double[2048];
	private static double[] drawInstance = new double[128];
	private static double[] dropInstance = new double[128];
	private static int[] dropTicks = new int[128];
	private static int pointer = 0;
	
	private static int playStatus = 0;
	private static String nowMusic = "";
	
	private static Color playColor = new Color(255, 255, 255);
	
	private static double[] playBeats = new double[6];
	private static double[] realBeats = new double[6];
	private static int[] playBeatTicks = new int[6];
	
	private static long maxTime = 0;
	private static long currentTime = 0;
	
	private static HashMap<Long, LinkedList<String>> lyricMap = new HashMap<Long, LinkedList<String>>();
	private static LinkedList<String> currentLyric;
	private static long currentLyricTimeStamp = 0;
	
	private static final int MAX_VALUE = 500;
	private static final int MIN_VALUE_COLOR = 180;

	public static void put(double value) {
		fftSource[pointer] = value;
		pointer ++;
		if(pointer == fftSource.length) {
			pointer = 0;
			calculate();
			fftSource = new double[2048];
		}
	}
	
	protected static void calculate(){
		if(fftSource.length > 400) {
			FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
			Complex[] result = fft.transform(fftSource, TransformType.FORWARD);
			double temp = 0;
			int count = 0;
			double tempColor = 0;
			int countColor = 0;
			double tempBeat = 0;
			int countBeat = 0;
			for(int i = 0, j = 0, k = 0, h = 0; i < fftSource.length/4; i++, j++, k++, h++) {
				if(j == fftSource.length / 4 / 128) {
					drawInstance[count] = temp / 44100 * 300;
					if(drawInstance[count] > MAX_VALUE) drawInstance[count] = MAX_VALUE;
					
					dropInstance[count] = Math.max(dropInstance[count] - (double)dropTicks[count]/4, 0);
					if(dropInstance[count] < drawInstance[count]) {
						dropInstance[count] = (int) drawInstance[count];
						dropTicks[count] = 1;
					}else {
						dropTicks[count] ++;
					}
					j = 0;
					temp = 0;
					count ++;
				}
				if(k == fftSource.length / 4 / 4) {
					double value = tempColor / 44100 * 255;
					changeColor(countColor, (int) (countColor == 0 ? Math.max(value/ 5, MIN_VALUE_COLOR) : countColor == 1 ? Math.max(value, MIN_VALUE_COLOR) : Math.max(value*2, MIN_VALUE_COLOR)));
					k = 0;
					tempColor = 0;
					countColor ++;
				}
				if(h == fftSource.length / 4 / 8) {
					double value = tempBeat / 44100 * 50;
					switch(countBeat) {
					case 0 : changePlayBeats(5, value/12);break;
					case 1 : changePlayBeats(4, value/5);break;
					case 2 : changePlayBeats(3, value/2);break;
					case 3 : changePlayBeats(2, value);break;
					case 4 : changePlayBeats(1, value*1.5);break;
					case 5 : changePlayBeats(0, value*2);break;
					}
					h = 0;
					tempBeat = 0;
					countBeat ++;
				}
				double abs = result[i].abs();
				tempBeat += abs;
				tempColor += abs;
				temp += abs;
			}
		}
	}
	
	private static void changeColor(int place, int value) {
		switch (place) {
		case 0:playColor = new Color(
				Math.min(playColor.getRed() + (value - playColor.getRed())/8, 255),
				Math.max(playColor.getGreen() - 5, MIN_VALUE_COLOR),
				Math.max(playColor.getBlue() - 5, MIN_VALUE_COLOR));break;
		case 1:playColor = new Color(
				Math.max(playColor.getRed() - 5, MIN_VALUE_COLOR),
				Math.min(playColor.getGreen() + (value - playColor.getGreen())/8, 255),
				Math.max(playColor.getBlue() - 5, MIN_VALUE_COLOR));break;
		case 2:playColor = new Color(
				Math.max(playColor.getRed() - 5, MIN_VALUE_COLOR),
				Math.max(playColor.getGreen() - 5, MIN_VALUE_COLOR),
				Math.min(playColor.getBlue() + (value - playColor.getBlue())/8, 255));break;
		}
	}
	
	private static void changePlayBeats(int place, double value) {
		if(realBeats[place] < value) {
			realBeats[place] = value;
		}else{
			realBeats[place] *= 0.95;
		}
		playBeats[place] = Math.max(playBeats[place] - (double)playBeatTicks[place]/4, 0);
		if(playBeats[place] < value) {
			playBeats[place] += (value - playBeats[place])/ 8;
			playBeatTicks[place] = 1;
		}else{
			playBeatTicks[place] ++;
		}
	}

	protected static void loadLyrics(String filepath) throws IOException {
		File file = new File(filepath.substring(0, filepath.length() - 4) + ".lrc");
		currentLyric = null;
		currentLyricTimeStamp = 0;
		lyricMap.clear();
		if(file.exists()) {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader input = new BufferedReader(reader);
			Pattern pattern = Pattern.compile("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]");
			String line = null;
			while ((line = input.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					long timeStamp = getTimeStamp(matcher.group(1), matcher.group(2), matcher.group(3));
					String text = line.substring(matcher.end());
					if(lyricMap.containsKey(timeStamp)) {
						lyricMap.get(timeStamp).add(text);
					}else {
						LinkedList<String> timeText = new LinkedList<String>();
						timeText.add(text);
						lyricMap.put(timeStamp, timeText);
					}
				}
			}
			reader.close();
			input.close();
		}
	}

	protected static long getTimeStamp(String minute, String second, String mills) {
		return Integer.parseInt(minute) * 60000 + Integer.parseInt(second) * 1000 + Integer.parseInt(mills);
	}
	
	protected static void processLyric(long time) {
		if(lyricMap.size() == 0) return;
		long tempTimeStamp = 0;
		for(long timeStamp : lyricMap.keySet()) {
			if(timeStamp <= time && tempTimeStamp < timeStamp) {
				tempTimeStamp = timeStamp;
			}
		}
		if(currentLyricTimeStamp == tempTimeStamp) return;
		currentLyricTimeStamp = tempTimeStamp;
		currentLyric = lyricMap.get(tempTimeStamp);
	}
	
	public static void play(String filepath) {
		if(playStatus > 0) return;
		new Thread(() -> {
			AudioInputStream inputStream = null;
			SourceDataLine player = null;
			try {
				File file = new File(filepath);
				loadLyrics(filepath);
				playStatus = 1;
				currentTime = 0;
				maxTime = 0;
				nowMusic = file.getName().substring(0, file.getName().length() - 4);
				inputStream = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(inputStream);
				maxTime = clip.getMicrosecondLength()/1000;
				clip.close();
				inputStream = AudioSystem.getAudioInputStream(file);
				player = AudioSystem.getSourceDataLine(inputStream.getFormat());
				player.open();
				player.start();
				byte[] buffer = new byte[4];
				int length;
				while((length = inputStream.read(buffer)) != -1 && playStatus > 0) {
					currentTime = player.getMicrosecondPosition()/1000;
					processLyric(currentTime);
					if(inputStream.getFormat().getChannels() == 2) {
						if(inputStream.getFormat().getSampleRate() == 16) {
							put((buffer[1] << 8) | buffer[0]);
							put((buffer[3] << 8) | buffer[2]);
						} else {
							put(buffer[1]);
							put(buffer[3]);
						}
					} else {
						if(inputStream.getFormat().getSampleRate() == 16) {
							put((buffer[1] << 8) | buffer[0]);
							put((buffer[3] << 8) | buffer[2]);
						} else {
							put(buffer[1]);
							put(buffer[3]);
						}
					}
					player.write(buffer, 0, length);
					while(playStatus > 1) player.drain();
					player.start();
				}
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(inputStream != null) inputStream.close();
					if(player != null) player.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			playStatus = 0;
		}).start();
	}
	
	public static void stop() {
		playStatus = 0;
	}
	
	public static void pause() {
		playStatus = 2;
	}
	
	public static void replay() {
		playStatus = 1;
	}
	
	public static double[] getDrawInstance() {
		return drawInstance;
	}
	
	public static double[] getDropInstance() {
		return dropInstance;
	}
	
	public static int getPlayStatus() {
		return playStatus;
	}
	
	public static Color getPlayColor() {
		return playColor;
	}
	
	public static String getNowMusic() {
		return nowMusic;
	}
	
	public static double getProcess() {
		return ((double)currentTime)/maxTime;
	}
	
	public static long getCurrentTime() {
		return currentTime;
	}
	
	public static long getMaxTime() {
		return maxTime;
	}
	
	public static LinkedList<String> getCurrentLyric() {
		return currentLyric;
	}
	
	public static double[] getPlayBeats() {
		return playBeats;
	}
	
	public static double[] getRealBeats() {
		return realBeats;
	}
}
