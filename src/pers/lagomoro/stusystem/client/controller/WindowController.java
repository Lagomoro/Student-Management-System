/*============================================================*/
/*  WindowManager.java                                        */
/*------------------------------------------------------------*/
/*  管理用户界面中不同的窗口，以保持其唯一性和稳定更新。                */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.controller;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JFrame;

import pers.lagomoro.stusystem.client.view.Flash_Loading;
import pers.lagomoro.stusystem.client.view.Window_Login;
import pers.lagomoro.stusystem.client.view.component.Flash;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.Window;
import pers.lagomoro.stusystem.client.view.component.WindowSceneable;
import pers.lagomoro.stusystem.client.view.widget.ButtonSceneTab;
import pers.lagomoro.stusystem.client.view.widget.ViewSceneCard;

public class WindowController {
	
	private static HashMap<String, Window> windowMap = new HashMap<String, Window>();
	private static HashMap<String, Flash> flashMap = new HashMap<String, Flash>();
	
	private static HashMap<String, String> linkMap = new HashMap<String, String>();
	private static HashMap<String, Point> linkDeltaMap = new HashMap<String, Point>();
	
	//窗口常量，用于维持窗口单一性
	public static final String KEYTAG = "::";

	public static final String WINDOW = "Window";
	public static final String WINDOW_LOGIN = "Window_Login";
	public static final String WINDOW_MAIN = "Window_Main";
	public static final String WINDOW_CHILD = "Window_Child";
	public static final String WINDOW_RTFEDITOR = "Window_RTFeditor";
	
	public static final String FLASH = "Flash";
	public static final String FLASH_LOADING = "Flash_Loading";

	//用户界面入口，由Main函数调用。
	public static void run() {
		loadLoginFrame();
	}
	
	//加载程序登录窗口。
    private static void loadLoginFrame() {
		Flash_Loading flashLoading = new Flash_Loading();
    	@SuppressWarnings("serial")
		Window_Login windowLogin = new Window_Login() {
    		@Override
    		protected void onFocusGain(WindowEvent e) {
    			super.onFocusGain(e);
    			flashLoading.requestFocus();
    			flashLoading.toFront();
    		}
    		@Override
    		protected void onDeiconified(WindowEvent e) {
    			super.onDeiconified(e);
    			flashLoading.active();
    			flashLoading.toFront();
    		}
    	};
    	focusWindow(windowLogin);
    	focusWindow(flashLoading);
    	windowLogin.setLocation(flashLoading.getX() + flashLoading.getWidth() - 20, flashLoading.getY() + (flashLoading.getHeight() - windowLogin.getHeight())/2 + 10);
    	applyWindowLink(windowLogin, flashLoading, flashLoading.getWidth() - 20, (flashLoading.getHeight() - windowLogin.getHeight())/2 + 10);
    	applyWindowLink(flashLoading, windowLogin, 0-(flashLoading.getWidth() - 20), 0-((flashLoading.getHeight() - windowLogin.getHeight())/2 + 10));
	}

    //窗口的实时更新
    public static void update() {
		for (String key : windowMap.keySet()) {
			if(windowMap.get(key).isVisible()) windowMap.get(key).update();
		}
		for (String key : flashMap.keySet()) {
			if(flashMap.get(key).isVisible()) flashMap.get(key).update();
		}
	}
    
	public static void refresh() {
    	for (String key : windowMap.keySet()) {
    		if(windowMap.get(key).isVisible()) windowMap.get(key).refresh();
		}
    	for (String key : flashMap.keySet()) {
    		if(flashMap.get(key).isVisible()) flashMap.get(key).refresh();
		}
	}
	
	//指定窗口是否存在
	public static boolean haveWindow(String key) {
		return windowMap.containsKey(key) || flashMap.containsKey(key);
	}
	
	//获取指定窗口
	public static Window getWindow(String key) {
		return windowMap.get(key);
	}
	//获取指定窗口
	public static Flash getFlash(String key) {
		return flashMap.get(key);
	}
	
	//添加新窗口
	public static void addWindow(Window window) {
		windowMap.put(window.getKey(), window);
	}
	
	public static void addWindow(Flash flash) {
		flashMap.put(flash.getKey(), flash);
	}
	
	//焦点转移至窗口
	public static void focusWindow(String key) {
		if(haveWindow(key)) {
			if(windowMap.containsKey(key)) {
				getWindow(key).requestFocus();
				getWindow(key).toFront();
				getWindow(key).refresh();
			}else {
				getFlash(key).requestFocus();
				getFlash(key).toFront();
				getFlash(key).refresh();
			}
			activeWindow(key);
		}
	}
	
	//焦点转移至窗口并自动创建
	public static void focusWindow(Window window) {
		if(haveWindow(window.getKey())) {
			focusWindow(window.getKey());
		}else {
			addWindow(window);
			activeWindow(window.getKey());
		}
	}
	public static void focusWindow(Flash flash) {
		if(haveWindow(flash.getKey())) {
			focusWindow(flash.getKey());
		}else {
			addWindow(flash);
			activeWindow(flash.getKey());
		}
	}
	
	//删除窗口
	public static void removeWindow(String key) {
		if(windowMap.containsKey(key)) {
			windowMap.get(key).dispose();
			windowMap.remove(key);
		}else {
			flashMap.get(key).dispose();
			flashMap.remove(key);
		}
	}
	
	//禁用窗口
	public static void disableWindow(String key) {
		if(windowMap.containsKey(key)) {
			windowMap.get(key).setEnabled(false);
		}else {
			flashMap.get(key).setEnabled(false);
		}
	}
	
	//启用窗口
	public static void enableWindow(String key) {
		if(windowMap.containsKey(key)) {
			windowMap.get(key).setEnabled(true);
		}else {
			flashMap.get(key).setEnabled(true);
		}
	}
	
	//显示窗口
	public static void activeWindow(String key) {
		if(windowMap.containsKey(key)) {
			windowMap.get(key).active();
		}else {
			flashMap.get(key).active();
		}
	}
	
	//隐藏窗口
	public static void deactiveWindow(String key) {
		if(windowMap.containsKey(key)) {
			windowMap.get(key).deactive();
		}else {
			flashMap.get(key).deactive();
		}
	}

	//处理窗口绑定
	public static void applyWindowLink(Window window, Window toWindow) {
		applyWindowLink(window.getKey(), toWindow.getKey(), 0, 0);
	}
	public static void applyWindowLink(Window window, Flash toFlash) {
		applyWindowLink(window.getKey(), toFlash.getKey(), 0, 0);
	}
	public static void applyWindowLink(Flash flash, Window toWindow) {
		applyWindowLink(flash.getKey(), toWindow.getKey(), 0, 0);
	}
	public static void applyWindowLink(Flash flash, Flash toFlash) {
		applyWindowLink(flash.getKey(), toFlash.getKey(), 0, 0);
	}
	public static void applyWindowLink(Window window, Window toWindow, int deltaX, int deltaY) {
		applyWindowLink(window.getKey(), toWindow.getKey(), deltaX, deltaY);
	}
	public static void applyWindowLink(Window window, Flash toFlash, int deltaX, int deltaY) {
		applyWindowLink(window.getKey(), toFlash.getKey(), deltaX, deltaY);
	}
	public static void applyWindowLink(Flash flash, Window toWindow, int deltaX, int deltaY) {
		applyWindowLink(flash.getKey(), toWindow.getKey(), deltaX, deltaY);
	}
	public static void applyWindowLink(Flash flash, Flash toFlash, int deltaX, int deltaY) {
		applyWindowLink(flash.getKey(), toFlash.getKey(), deltaX, deltaY);
	}
	public static void applyWindowLink(String key, String toKey, int deltaX, int deltaY) {
		linkMap.put(key, toKey);
		linkDeltaMap.put(key, new Point(deltaX, deltaY));
		if(windowMap.containsKey(key)) {
			getWindow(key).onApplyLink();
		}
	}
	
	//解除一个窗口的绑定
	public static void removeWindowLink(String key) {
		linkMap.remove(key);
		linkDeltaMap.remove(key);
		removeAllLinkTo(key);
		if(windowMap.containsKey(key)) {
			getWindow(key).onRemoveLink();
		}
	}
	public static void removeAllLinkTo(String toKey) {
		for (String key : linkMap.keySet()) {
			if(linkMap.get(key).equals(toKey)) removeWindowLink(key);
		}
	}
	
	//处理窗口事件
	public static void applyWindowMoving(String key, int deltaX, int deltaY) {
		applyWindowMoving(key, key, deltaX, deltaY);
	}
	public static void applyWindowMoving(String key, String interruptKey, int deltaX, int deltaY) {
		try{
			if(windowMap.containsKey(key)) {
				Point place = getWindow(key).getLocationOnScreen();
				getWindow(key).setLocation(place.x + deltaX, place.y + deltaY);
			}else {
				Point place = getFlash(key).getLocationOnScreen();
				getFlash(key).setLocation(place.x + deltaX, place.y + deltaY);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		for (String fromKey : linkMap.keySet()) {
			if(!fromKey.equals(interruptKey) && linkMap.get(fromKey).equals(key)) {
				applyWindowMoving(fromKey, interruptKey, deltaX, deltaY);
			}
		}
	}

	public static void applyWindowIconify(String key) {
		applyWindowIconify(key, key);
	}
	public static void applyWindowIconify(String key, String interruptKey) {
		if(windowMap.containsKey(key)) {
			getWindow(key).setExtendedState(JFrame.ICONIFIED);
		}else {
			getFlash(key).dispose();
		}
		for (String fromKey : linkMap.keySet()) {
			if(!fromKey.equals(interruptKey) && linkMap.get(fromKey).equals(key)) {
				applyWindowIconify(fromKey, interruptKey);
			}
		}
	}

	public static void applyWindowClose(String key) {
		removeWindow(key);
		removeWindowLink(key);
	}
	
	public static void applyCloseChild(){
		int count = 0;
		for (String key : windowMap.keySet()) {
			if(key.startsWith(WINDOW_CHILD)) count++;
		}
		String[] temp = new String[count];
		count = 0;
		for (String key : windowMap.keySet()) {
			if(key.startsWith(WINDOW_CHILD)) temp[count++] = key;
		}
		for (String key : temp) {
			applyWindowClose(key);
		}
	}
	
	public static void applyExit() {
		System.exit(0);
	}

	public static void processSceneDrag(ViewSceneCard parent, ButtonSceneTab tab, Scene scene, int x, int y) {
		for (String key : windowMap.keySet()) {
			if(windowMap.get(key) instanceof WindowSceneable) {
				WindowSceneable window = (WindowSceneable)windowMap.get(key);
				if(window.getTargetIndex(x, y) != -1) {
					if(window.getSceneController().equals(parent.getSceneController())) {
						parent.moveTabTo(tab, x, y);
					}else {
						parent.getSceneController().removeScene(scene.getKey());
						window.getSceneController().addSceneAt(scene, x, y);
						WindowController.focusWindow(window);
						window.getSceneController().focusScene(scene);
					}
					window.refresh();
					parent.refresh();
				}/*else {
					String a = Integer.toString((int)(Math.random()*10000000));
					Window_Child child = new Window_Child(a, a);
					parent.getSceneController().removeScene(scene.getKey());
					WindowController.focusWindow(child);
					child.getSceneController().addSceneAt(scene, x - 90, y - 190);
					child.setLocation(x, y);
					child.getSceneController().focusScene(scene);
					
					parent.refresh();
					child.refresh();
				}*/
			}
		}
	}
	
	public static Window getWindowFromScene(String sceneKey) {
		for (String key : windowMap.keySet()) {
			if(windowMap.get(key) instanceof WindowSceneable) {
				WindowSceneable window = (WindowSceneable)windowMap.get(key);
				if(window.getSceneController().haveScene(sceneKey)) {
					return window;
				}
			}
		}
		return null;
	}
	
	public static Window getWindowFromScene(Scene scene) {
		return getWindowFromScene(scene.getKey());
	}
	
	public static void focusScene(String sceneKey, SceneController sceneController) {
		for (String key : windowMap.keySet()) {
			if(windowMap.get(key) instanceof WindowSceneable) {
				WindowSceneable window = (WindowSceneable)windowMap.get(key);
				if(window.getSceneController().haveScene(sceneKey)) {
					window.getSceneController().focusScene(sceneKey);
					return;
				}
			}
		}
		sceneController.focusScene(sceneKey);
	}
	
	public static void focusScene(Scene scene, SceneController sceneController) {
		for (String key : windowMap.keySet()) {
			if(windowMap.get(key) instanceof WindowSceneable) {
				WindowSceneable window = (WindowSceneable)windowMap.get(key);
				if(window.getSceneController().haveScene(scene.getKey())) {
					window.getSceneController().focusScene(scene);
					return;
				}
			}
		}
		sceneController.focusScene(scene);
	}
	
	public static Scene getScene(String sceneKey) {
		for (String key : windowMap.keySet()) {
			if(windowMap.get(key) instanceof WindowSceneable) {
				WindowSceneable window = (WindowSceneable)windowMap.get(key);
				if(window.getSceneController().haveScene(sceneKey)) {
					return window.getSceneController().getScene(sceneKey);
				}
			}
		}
		return null;
	}
	
}
