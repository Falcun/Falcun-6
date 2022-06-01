package net.mattbenson.gui.framework;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.mattbenson.gui.framework.components.MenuDraggable;
import net.mattbenson.gui.framework.components.MenuScrollPane;

public class Menu {
	private String title;
	private int x;
	private int y;
	private int width;
	private int height;
	private int mouseX;
	private int mouseY;
	private List<MenuComponent> components;
	
	public Menu(String title, int width, int height) {
		this.title = title;
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		components = new CopyOnWriteArrayList<>();
	}
	
	public void onRender(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		
		int passThroughIndex = -1;
		int index = components.size();
		
		for(MenuComponent component : components) {
			component.setRenderOffsetX(x);
			component.setRenderOffsetY(y);
			
			if(!component.passesThrough() && passThroughIndex == -1)
				passThroughIndex = index;
			
			index--;
		}
		
		Collections.reverse(components);	
		
		final int oldIndex = index;
		
		index = oldIndex;
		
		for(MenuComponent component : components) {
			if(index >= passThroughIndex - 1){
				this.mouseX = mouseX;
				this.mouseY = mouseY;
			} else if(component instanceof MenuDraggable) {
				index++;
				continue;
			} else {
				this.mouseX = Integer.MAX_VALUE;
				this.mouseY = Integer.MAX_VALUE;
			}
			
			component.onPreSort();
			component.onRender();
			index++;
		}
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public void onMouseClick(int button) {
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		boolean returnMode = false;
		for(MenuComponent component : components) {
			if(!returnMode || component instanceof MenuScrollPane) {
				component.onMouseClick(button);
			}
			
			if(!component.passesThrough()) {
				returnMode = true;
			}
		}
	}
	
	public void onMouseClickMove(int button) {
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		
		boolean returnMode = false;
		for(MenuComponent component : components) {
			if(!returnMode || component instanceof MenuScrollPane) {
				component.onMouseClickMove(button);
			}
			
			if(!component.passesThrough()) {
				returnMode = true;
			}
		}
	}
	
	public void onKeyDown(char character, int key) {
		for(MenuComponent component : components)
			component.onKeyDown(character, key);
	}
	
	public boolean onMenuExit(int key) {
		boolean cancel = false;
		
		for(MenuComponent component : components) {
			if(component.onExitGui(key)) {
				cancel = true;
			}
		}
		
		return cancel;
	}

	public void onScroll(int scroll) {
		Collections.sort(components, (a, b) -> Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority()));
		Collections.reverse(components);
		
		for(MenuComponent component : components) {
			component.onMouseScroll(scroll);
			
			if(!component.passesThrough())
				break;
		}
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void addComponent(MenuComponent component) {
		if(!components.contains(component)) {
			component.setParent(this);
			components.add(component);
		}
	}
	
	public void removeComponent(MenuComponent component) {
		if(components.contains(component)) {
			components.remove(component);
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
	
	public List<MenuComponent> getComponents() {
		return components;
	}
	
	public void setComponents(List<MenuComponent> components) {
		this.components = components;
	}
}