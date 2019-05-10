package pers.lagomoro.stusystem.data;

import java.util.Date;

@Deprecated
public class InfoUserChat {
	
	public String imageUrl = " ";
	public String nickname = " ";
	public String text     = " ";
	public int count       = 0;
	public Date timestamp  = new Date();
	
	public InfoUserChat() {
		
	}

	public InfoUserChat(String imageUrl, String nickname, String text, int count, Date timestamp) {
		this.imageUrl = imageUrl;
		this.nickname = nickname;
		this.text = text;
		this.count = count;
		this.timestamp = timestamp;
	}
}
