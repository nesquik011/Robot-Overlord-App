package com.marginallyclever.robotOverlord.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.jogamp.opengl.GL2;
import com.marginallyclever.convenience.JSONSerializable;
import com.marginallyclever.robotOverlord.RobotOverlord;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * An object in the world that can have a user interface.  Does not require physical presence.
 * @author danroyer
 *
 */
public class Entity implements JSONSerializable {
	
	private String displayName;
	private int pickName;

	protected ArrayList<Entity> children;
	protected Entity parent;
	
	
	// unique ids for all objects in the world.  
	// zero is reserved to indicate no object.
	// first 9 are reserved for MotionHelper.
	static private int pickNameCounter=10;
	
	private transient EntityControlPanel entityPanel;
	
	
	public Entity() {
		children = new ArrayList<Entity>();
		pickName = pickNameCounter++;
	}
	
	
	/**
	 * Get the {@link EntityControlPanel} for this class' superclass, then the EntityPanel for this class, and so on.
	 * 
	 * @param gui the main application instance.
	 * @return the list of EntityPanels 
	 */
	public ArrayList<JPanel> getContextPanel(RobotOverlord gui) {
		ArrayList<JPanel> list = new ArrayList<JPanel>();
		
		entityPanel = new EntityControlPanel(gui,this);
		list.add(entityPanel);

		return list;
	}
	
	
	/**
	 * Get all the {@link EntityControlPanel}s for this {@link Entity}.  
	 * <p>
	 * If this class is derived from Entity, get the panels for the derived Entities, too.  Normally this is called by {@link RobotOverlord}.
	 * 
	 * @param gui the main application instance.
	 * @return an ArrayList of all the panels for this Entity and all derived classes.
	 */
	public JComponent getAllContextPanels(RobotOverlord gui) {
		/*
		JPanel sum = new JPanel();
		BoxLayout layout = new BoxLayout(sum, BoxLayout.PAGE_AXIS);
		sum.setLayout(layout);
		ArrayList<JPanel> list = getContextPanel(gui);
		Iterator<JPanel> pi = list.iterator();
		while(pi.hasNext()) {
			JPanel p = pi.next();
			
			CollapsiblePanel oiwPanel = new CollapsiblePanel(p.getName());
			this.add(oiwPanel,c);
			JPanel contents = oiwPanel.getContentPane();
			contents.add(p);
			
		
			sum.add(p);
		}

		JPanel b = new JPanel(new BorderLayout());
		b.add(sum, BorderLayout.PAGE_START);
		*/
		JTabbedPane b = new JTabbedPane();
		ArrayList<JPanel> list = getContextPanel(gui);
		Iterator<JPanel> pi = list.iterator();
		while(pi.hasNext()) {
			JPanel p = pi.next();
			b.addTab(p.getName(), p);
		}
		
		return b;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public int getPickName() {
		return pickName;
	}
	
	public boolean hasPickName(int name) {
		return pickName==name;
	}
	
	public void pick() {}
	
	public void unPick() {}
	
	
	public void update(double dt) {
		Iterator<Entity> i = children.iterator();
		while(i.hasNext()) {
			Entity e=i.next();
			e.update(dt);
		}
	}
	
	public void render(GL2 gl2) {}
	
	
	public void addChild(Entity e) {
		children.add(e);
	}
	
	public void removeChild(Entity e) {
		int n=0;
		Iterator<Entity> i = children.iterator();
		while(i.hasNext()) {
			if(i==e) {
				children.remove(n);
				return;
			}
			++n;
		}
	}
	
	public ArrayList<Entity> getChildren() {
		return children;
	}
	
	public void removeParent() {
		parent=null;
	}

	public Entity getParent() {
		return parent;
	}
	
	public void setParent(Entity e) {
		parent = e;
	}
	
	/**
	 * Do you have a status to report to the GUI?
	 * Override this with your class' status.
	 * @return a String.  Cannot be null.  default is "".  
	 */
	public String getStatusMessage() {
		return "";
	}


	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject me = new JSONObject();
		
		// get class
		String className="";
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null) {
			className=enclosingClass.getName();
		} else {
			className=getClass().getName();
		}
		
		me.put("class",className);
		// entity name
		me.put("displayName", getDisplayName());

		JSONArray entities = new JSONArray();
		for( Entity e : children ) {
			entities.add(e.toJSON());
		}
		
		me.put("children", entities);

		return me;
	}


	@Override
	public void fromJSON(JSONObject arg0) throws IOException {
		
	}
}
