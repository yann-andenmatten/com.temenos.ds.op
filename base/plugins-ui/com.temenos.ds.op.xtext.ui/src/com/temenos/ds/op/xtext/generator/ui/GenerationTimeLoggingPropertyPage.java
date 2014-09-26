package com.temenos.ds.op.xtext.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.dialogs.EmptyPreferencePage;
import org.eclipse.ui.internal.dialogs.EmptyPropertyPage;

import com.temenos.ds.op.xtext.generator.ui.GenerationTimeLoggingPropertyPage.GeneratorTimeModel;

public class GenerationTimeLoggingPropertyPage extends EmptyPropertyPage {
	
	private TableViewer tableViewer;
	
	
	@Override
	protected Control createContents(Composite parent) {
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		parent.setLayout(tableColumnLayout);
		tableViewer = new TableViewer(parent);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
	
		createTableViwerColumn(tableViewer,"Generator Id",new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if (element instanceof GeneratorTimeModel) {
					GeneratorTimeModel item = (GeneratorTimeModel) element;
					return item.id;
				}
				return super.getText(element);
			}
		}, new ColumnWeightData(1,50,true));

		createTableViwerColumn(tableViewer,"Time Consumed in ms",new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if (element instanceof GeneratorTimeModel) {
					GeneratorTimeModel item = (GeneratorTimeModel) element;
					return Integer.toString(item.timeConsumed);
				}
				return super.getText(element);
			}
		}, new ColumnWeightData(5,250,true));
				
		tableViewer.setInput(createModel());
		return parent;
	}
	
	class GeneratorTimeModel {
		private String id;
		private int timeConsumed;

		public GeneratorTimeModel(String id, int timeConsumed) {
			super();
			this.id = id;
			this.timeConsumed = timeConsumed;
		}
		
		
	}
	
	private List<GeneratorTimeModel> createModel(){
		List<GeneratorTimeModel> models = new ArrayList<GeneratorTimeModel>();
		IMemento memento = GenerationTimeLogger.getInstance().getMemento();
		if(memento != null){
			for(IMemento generator : memento.getChildren()){
				models.add(new GeneratorTimeModel(generator.getType(),generator.getInteger("time")));
			}
		}
		return models;
	}
	
	private void createTableViwerColumn(TableViewer tableViewer2, String string, CellLabelProvider labelProvider, ColumnLayoutData layoutData) {
		TableViewerColumn viewerNameColumn = new TableViewerColumn(tableViewer2, SWT.NONE);
		viewerNameColumn.getColumn().setText(string);
		viewerNameColumn.setLabelProvider(labelProvider);
		Layout layout = tableViewer2.getControl().getParent().getLayout();
		if (layout instanceof TableColumnLayout) {
			((TableColumnLayout) layout).setColumnData(viewerNameColumn.getColumn(), layoutData);
		}
	}
}

