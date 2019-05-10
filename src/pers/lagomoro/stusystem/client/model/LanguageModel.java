/*============================================================*/
/*  LanguageManager.java                                      */
/*------------------------------------------------------------*/
/*  管理多语言和系统词条，以便于切换语言和修改的便利性。                */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.model;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.UIManager;

import pers.lagomoro.stusystem.client.model.module.DefaultLanguage;
import pers.lagomoro.stusystem.client.model.module.Language;

public class LanguageModel {

	public static final String DEFAULT_FONT = "Microsoft YaHei";
	public static final int DEFAULT_FONTSIZE = 18;
	public static final String DEFAULT_VERSION = "V2.0.1";
	
	private static final String LANGUAGEPACK_PATH = "./language";
	private static final String OUTPUT_NAME = "\\emptyLanguage.ini";
	
	//存储词条的哈希表
	private static HashMap<String, Language> languageMap = new HashMap<String, Language>();
	private static DefaultLanguage defaultLangage = new DefaultLanguage();
	
	//从词条中获取条目
	public static String get(String key) {
		String value = getLanguage().get(key);
		return value == null ? defaultLangage.get(key) : value;
	}
	public static String get(String key1, String place1, String key2) {
		return get(key1) + place1 + get(key2);
	}
	public static String get(String key1, String place1, String key2, String place2, String key3) {
		return get(key1) + place1 + get(key2) + place2 + get(key3);
	}
	
	//初始化全部词条
	public static void launch() {
		setDefaultFont();
		register();
	}
	
	//初始化
	private static void register() {
		File file = new File(LANGUAGEPACK_PATH);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File language : files) {
                if (language.getAbsolutePath().endsWith(".ini")) {
                    addLanguage(language);
                }
            }
        } else {
        	file.mkdir();
        }
	}
	
	//导入语言
	public static void addLanguage(File file) {
		Language language = new Language();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader input = new BufferedReader(reader);
			String line;
				line = input.readLine();
			String[] temp;
			while(line != null) {
				if(line.length() > 0) {
					if(line.startsWith("@")) {
						temp = line.split(" ");
						switch(temp[0]) {
						case "@packname" : language.setPackname(line.split("@packname ")[1]);break;
						case "@author" : language.setAuthor(line.split("@author ")[1]);break;
						case "@description" : language.setDescription(line.split("@description ")[1]);break;
						case "@version" : language.setVersion(line.split("@version ")[1]);break;
						case "@submit" : language.setSubmit(line.split("@submit ")[1]);break;
						default:break;
						}
					} else if(line.split(" = ").length > 1){
						temp = line.split(" = ");
						language.add(temp[0], temp[1]);
					}
				}
				line = input.readLine();
			}
			reader.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        languageMap.put(language.getPackname(), language);
	}
	
	//导出空白语言包
	public static void outputDefaultLanguage(String path) {
		try {
			File file = new File(path + OUTPUT_NAME);
			file.createNewFile();
	        BufferedWriter output = new BufferedWriter(new FileWriter(file));
	        output.write("@packname " + "\r\n");
			output.write("@author " + "\r\n");
			output.write("@description " + "\r\n");
			output.write("@version " + "\r\n");
			output.write("@submit " + "\r\n");
			for (String key : defaultLangage.getOutput()) {
				output.write(key + " = " + "\r\n");
			}
			output.flush();
			output.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	//获取当前语言
	public static Language getLanguage() {
		Language language = languageMap.get(StorageModel.getStringVariable(StorageModel.SYSTEM_LANGUAGE));
		return language == null ? defaultLangage : language;
	}
	
	public static Language getLanguage(String key) {
		Language language = languageMap.get(key);
		return language == null ? defaultLangage : language;
	}

	//获取设置用语言列表
	public static Set<String> getLanguagePackList() {
		return languageMap.keySet();
	}
	
	//语言切换
	public static void setLanguage(String packName) {
		StorageModel.setVariable(StorageModel.SYSTEM_LANGUAGE, packName);
	}
	
	//获取默认字体
	public static Font getDefaultFont() {
		return new Font(DEFAULT_FONT, Font.PLAIN, DEFAULT_FONTSIZE);
	}
	public static Font getDefaultFont(int size) {
		return new Font(DEFAULT_FONT, Font.PLAIN, size);
	}
	public static Font getDefaultFont(int hint, int size) {
		return new Font(DEFAULT_FONT, hint, size);
	}
	
	private static void setDefaultFont() {
		Font font = getDefaultFont();
		String modules[] = { "Label", "CheckBox", "PopupMenu","MenuItem", "CheckBoxMenuItem",
				"JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
				"TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
				"OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
				"ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
				"PasswordField","TextField", "Table", "Label", "Viewport",
				"RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame"
		}; 
		for (String module : modules) {
			 UIManager.put(module + ".font", font); 
		}
	}
	
	//获取字体列表
	public static String[] getFontList() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return e.getAvailableFontFamilyNames();
	}
	
	//获取字号列表
	public static LinkedHashMap<String, Double> getFontSizeMap() {
		LinkedHashMap<String, Double> fontSizeMap = new LinkedHashMap<String, Double>();
//		fontSizeMap.put("#7", 46.0);
//		fontSizeMap.put("#6", 30.0);
//		fontSizeMap.put("#5", 23.0);
//		fontSizeMap.put("#4", 18.0);
//		fontSizeMap.put("#3", 15.0);
//		fontSizeMap.put("#2", 12.0);
//		fontSizeMap.put("#1", 10.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::42")  , 42.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::36")  , 36.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::26")  , 26.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::24")  , 24.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::22")  , 22.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::18")  , 18.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::16")  , 16.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::15")  , 15.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::14")  , 14.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::12")  , 12.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::10.5"), 10.5);
		fontSizeMap.put(LanguageModel.get("System->FontSize::9")   , 9.0);
		fontSizeMap.put(LanguageModel.get("System->FontSize::7.5") , 7.5);
		fontSizeMap.put(LanguageModel.get("System->FontSize::6.5") , 6.5);
		fontSizeMap.put(LanguageModel.get("System->FontSize::5.5") , 5.5);
		fontSizeMap.put("5"   , 5.0);
		fontSizeMap.put("5.5" , 5.5);
		fontSizeMap.put("6.5" , 6.5);
		fontSizeMap.put("7.5" , 7.5);
		fontSizeMap.put("8"   , 8.0);
		fontSizeMap.put("9"   , 9.0);
		fontSizeMap.put("10"  , 10.0);
		fontSizeMap.put("10.5", 10.5);
		fontSizeMap.put("11"  , 11.0);
		fontSizeMap.put("12"  , 12.0);
		fontSizeMap.put("14"  , 14.0);
		fontSizeMap.put("16"  , 16.0);
		fontSizeMap.put("18"  , 18.0);
		fontSizeMap.put("20"  , 20.0);
		fontSizeMap.put("22"  , 22.0);
		fontSizeMap.put("24"  , 24.0);
		fontSizeMap.put("26"  , 26.0);
		fontSizeMap.put("28"  , 28.0);
		fontSizeMap.put("36"  , 36.0);
		fontSizeMap.put("48"  , 48.0);
		fontSizeMap.put("72"  , 72.0);
		return fontSizeMap;
	}
	
}
