package pers.lagomoro.stusystem.client.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.data.DataProfileImage;

public class ImageModel {

	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	
	private static HashMap<String, BufferedImage> userImageMap = new HashMap<String, BufferedImage>();

	//»æÍ¼¶ÔÆë³£Á¿
	public static final int ANCHOR_MIDDLE    = 5;
	public static final int ANCHOR_SOUTH     = 2;
	public static final int ANCHOR_WEST      = 4;
	public static final int ANCHOR_EAST      = 6;
	public static final int ANCHOR_NORTH     = 8;
	public static final int ANCHOR_NORTHWEST = 7;
	public static final int ANCHOR_NORTHEEST = 9;
	public static final int ANCHOR_SOUTHWEST = 1;
	public static final int ANCHOR_SOUTHEEST = 3;
	
	public static final String LOGO_DATA = "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";
	public static final Image lOGO_IMAGE = Toolkit.getDefaultToolkit().createImage(Base64.decodeBase64(LOGO_DATA));
	
	public static Image loadImage(String path, String fileName) {
		return haveImage(path + fileName) ? getImage(path + fileName) : addImage(path + fileName);
	}
	
	public static boolean haveImage(String path) {
		return imageMap.containsKey(path);
	}
	
	public static Image addImage(String path) {
		Image image = Toolkit.getDefaultToolkit().getImage(path);
		imageMap.put(path, image);
		return image;
	}
	
	public static Image getImage(String path) {
		return imageMap.get(path);
	}
	
	public static Image getLogoImage() {
		return lOGO_IMAGE;
	}
	
	public static String imageToBase64(String path) {
		String output = "";
		FileInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(path);
			outputStream = new ByteArrayOutputStream();
		    byte[] bytes = new byte[1024];
		    int length = 0;
		    while ((length = inputStream.read(bytes, 0, bytes.length)) != -1) {
		        outputStream.write(bytes, 0, length);
		    }
		    byte[] imageBytes = outputStream.toByteArray();
		    output = Base64.encodeBase64String(imageBytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(inputStream != null) inputStream.close();
				if(outputStream != null) outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	
	public static String imageToBase64(Image image) {
		String output = "";
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();  
			ImageIO.write(ImageBuffered(image), "png", outputStream);
			byte[] imageBytes = outputStream.toByteArray();
		    output = Base64.encodeBase64String(imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(outputStream != null) outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	
	public static BufferedImage getBase64Image(String image_data) {
		byte[] bytes = Base64.decodeBase64(image_data);
		ByteArrayInputStream imputStream = new ByteArrayInputStream(bytes, 0, bytes.length);
		try {
			return ImageIO.read(imputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage ImageBuffered(Image image) {
		if (image instanceof BufferedImage) return (BufferedImage) image;
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = bufferedImage.createGraphics();
		GraphicsController.setHint(graphics);
		graphics.drawImage(image, null, null);
		graphics.dispose();
		return bufferedImage;
	}
	
	public static BufferedImage ImageBuffered(Image image, int width, int height) {
		if (image instanceof BufferedImage) return (BufferedImage) image;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = bufferedImage.createGraphics();
		GraphicsController.setHint(graphics);
		graphics.drawImage(image, null, null);
		graphics.dispose();
		return bufferedImage;
	}

	public static BufferedImage getRoundImage(BufferedImage image) {
		if(image == null) return null;
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = output.createGraphics();
		GraphicsController.setHint(graphics);
		Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, width, height); 
		graphics.setClip(shape); 
		graphics.drawImage(image, 0, 0, width, height, null);
		graphics.dispose();
		return output;
	}
	
	public static void refreshUserProfile(LinkedList<DataProfileImage> imageList) {
		for(DataProfileImage image : imageList) {
			userImageMap.put(image.username, getRoundImage(getBase64Image(image.image_data)));
		}
	}
	
	public static BufferedImage getUserImage(String username){
		return userImageMap.get(username);
	}
}
