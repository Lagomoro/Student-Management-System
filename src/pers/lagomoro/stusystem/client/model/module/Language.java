package pers.lagomoro.stusystem.client.model.module;

import java.util.LinkedHashMap;
import java.util.Set;

public class Language {
	
	protected String packname;
	protected String author;
	protected String description;
	protected String version;
	protected String submit;
	
	protected LinkedHashMap<String, String> languageMap = new LinkedHashMap<String, String>();
	
	public Language(){
		this.initialize();
	}

	protected void initialize() {
		
	}
	
	public void add(String key, String value) {
		this.languageMap.put(key, value);
	}

	public String get(String key) {
		return this.languageMap.get(key);
	}
	
	public Set<String> getOutput() {
		return this.languageMap.keySet();
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}
	
}
