package com.temenos.ds.op.xtext.generator.ui;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

public class GenerationTimeLogger {
	
	private static GenerationTimeLogger timeLogger;
	private XMLMemento memento;
	private static final String TIME= "time";
	
	private GenerationTimeLogger(){
		memento = XMLMemento.createWriteRoot("Generators");
	}

	public static GenerationTimeLogger getInstance(){
		if(timeLogger == null){
			timeLogger = new GenerationTimeLogger();
        }
        return timeLogger;
	}
	
	public XMLMemento getMemento(){
		return memento;
	}
	
	public void updateTime(String id,int time){
		
		IMemento idMemento = memento.getChild(id);
		if(idMemento == null){
			idMemento = memento.createChild(id);
			idMemento.putInteger(TIME, time);
		}
		else{
			int duration = idMemento.getInteger(TIME);
			idMemento.putInteger(TIME, duration+time);
		}
	}
}
