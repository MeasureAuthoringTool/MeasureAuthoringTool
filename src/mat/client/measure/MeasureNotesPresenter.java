package mat.client.measure;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasureNotesPresenter implements MatPresenter{
	private SimplePanel simplePanel = new SimplePanel();
	private FlowPanel flowPanel = new FlowPanel();
	private TextBox measureNote1 = new TextBox();
	private TextBox measureNote2 = new TextBox();
	private TextBox measureNote3 = new TextBox();
	private Label measureNoteLabel1 = new Label("07/05/2013 12:22:23");
	private Label measureNoteLabel2 = new Label("09/05/2012 15:22:23");
	private Label measureNoteLabel3 = new Label("07/07/2013 9:22:45");
	private Button exportButton  = new PrimaryButton("Export to CSV","primaryGreyLeftButton");
	
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	private ClickHandler exportButtonClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			generateCSVFile("abc.csv");
		}
	};
	
	
	//private static final String MEASURE_OBS = "measureObservations";
	
	public MeasureNotesPresenter() {
		System.out.println("Created an instance of MeasureNotesPresenter >>>>");
		simplePanel.setStyleName("contentPanel");
		flowPanel.add(new Label("Measure Notes Section"));
		measureNote1.setText("Ryan Gosling");
		measureNote2.setText("Hugh Jackman");
		VerticalPanel verticalPanel = new VerticalPanel();
		HorizontalPanel hPanel1 = new HorizontalPanel();
		hPanel1.add(measureNote1);
		hPanel1.add(measureNoteLabel1);
		HorizontalPanel hPanel2 = new HorizontalPanel();
		hPanel2.add(measureNote2);
		hPanel2.add(measureNoteLabel2);
		HorizontalPanel hPanel3 = new HorizontalPanel();
		hPanel3.add(measureNote3);
		hPanel3.add(measureNoteLabel3);
		verticalPanel.add(hPanel1);
		verticalPanel.add(hPanel2);
		verticalPanel.add(hPanel3);
		verticalPanel.add(exportButton);
		flowPanel.add(verticalPanel);
		simplePanel.add(flowPanel);
		exportButton.addClickHandler(exportButtonClickHandler);
	}	
	 
	private native void alertMessage(String message) /*-{
		 	 
}-*/;
	
	
	private native void generateCSVFile(String fileName)/*-{
		var data = [["name1", "city1", "some other info"], ["name2", "city2", "more info"]];
		var csvContent = data[0];
		function joinData(infoArray, index){
				//if(index!=0){
					dataString = infoArray.join(",");
		   			csvContent += index < infoArray.length ? "\n" + dataString  : dataString;
				//}
		}
		for (i = 1, len = data.length; i < len; i++) {
			joinData(data,i);
		}
		
			
		//var uriContent = 'data:application/octet-stream,Content-Disposition: attachment; filename="name_of_file.csv",' + encodeURIComponent(csvContent);
		//var myWindow = window.open(uriContent, "abc");
		//myWindow.focus();
		
		
		if (navigator.appName != 'Microsoft Internet Explorer') {
        	//window.open('data:text/csv;charset=utf-8,' + escape(csvContent));
        	var popup = window.open('', 'csv', '');
        	popup.document.body.innerHTML = '<pre>' + csvContent + '</pre>';
    	}
    	else {
        	var popup = window.open('', 'csv', '');
        	popup.document.body.innerHTML = '<pre>' + csvContent + '</pre>';
        	
        	//var fso = new ActiveXObject("Scripting.FileSystemObject");
 			//var utf8Enc = new ActiveXObject("Utf8Lib.Utf8Enc");
 			//var flOutput = fso.CreateTextFile("c:\\exportNotes.txt", true); 
 			//true for overwrite
 			//flOutput.BinaryWrite(utf8Enc.UnicodeToUtf8(csvContent));
 			//flOutput.Close();
    	}
			
			//var encodedUri = 'data:application/octet-stream,' + encodeURIComponent(csvContent);
			//window.open(encodedUri);
		//	var link = document.createElement("a");
		//	link.setAttribute("href", encodedUri);
			//link.setAttribute("download", fileName);
		//	link.click();	
		
	 
}-*/;
	private native void generateCSVFileWithBlob(String fileName) /*-{
		//var csvContent = "data:text/csv;charset=utf-8,";
		//	data.forEach(function(infoArray, index){

		//	   dataString = infoArray.join(",");
		//	   csvContent += index < infoArray.length ? dataString+ "\n" : dataString;
		//   
		//	});
			
		//	var uriContent = "data:application/octet-stream," + encodeURIComponent(csv);
		//	var myWindow = window.open(uriContent, "Nutrient CSV");
		//	myWindow.focus();
			
		//	var encodedUri = encodeURI(csvContent);
			//window.open(encodedUri);
		//	var link = document.createElement("a");
		//	link.setAttribute("href", encodedUri);
		//	link.setAttribute("download", fileName);
		//	link.click();	 	
		
		
		//var keys = _.keys(data[0]);	 
		//var csv = keys.join(","); 
		//_(data).each(function(row) {
  		//	csv += "\n";
  		//	csv += _(keys).map(function(k) {
    	//		return row[k];
  		//	}).join(",");
		//});
		// trick browser into downloading file
		//var uriContent = "data:application/octet-stream," + encodeURIComponent(csv);
		//var myWindow = window.open(uriContent, fileName);
		//myWindow.focus();
		var saveAs = saveAs
  		|| (navigator.msSaveOrOpenBlob && navigator.msSaveOrOpenBlob.bind(navigator))
  		|| (function(view) {
		"use strict";
			var
				  doc = view.document
				  // only get URL when necessary in case BlobBuilder.js hasn't overridden it yet
				, get_URL = function() {
					return view.URL || view.webkitURL || view;
				}
				, URL = view.URL || view.webkitURL || view
				, save_link =!doc.createElementNS ? doc.createElement("a"): doc.createElementNS("http://www.w3.org/1999/xhtml", "a")
				, can_use_save_link =  !view.externalHost && "download" in save_link
				, click = function(node) {
					var event = doc.createEvent("MouseEvents");
					event.initMouseEvent(
						"click", true, false, view, 0, 0, 0, 0, 0
						, false, false, false, false, 0, null
					);
					node.dispatchEvent(event);
				}
				, webkit_req_fs = view.webkitRequestFileSystem
				, req_fs = view.requestFileSystem || webkit_req_fs || view.mozRequestFileSystem
				, throw_outside = function (ex) {
					(view.setImmediate || view.setTimeout)(function() {
						throw ex;
					}, 0);
				}
				, force_saveable_type = "application/octet-stream"
				, fs_min_size = 0
				, deletion_queue = []
				, process_deletion_queue = function() {
					var i = deletion_queue.length;
					while (i--) {
						var file = deletion_queue[i];
						if (typeof file === "string") { // file is an object URL
							URL.revokeObjectURL(file);
						} else { // file is a File
							file.remove();
						}
					}
					deletion_queue.length = 0; // clear queue
				}
				, dispatch = function(filesaver, event_types, event) {
					event_types = [].concat(event_types);
					var i = event_types.length;
					while (i--) {
						var listener = filesaver["on" + event_types[i]];
						if (typeof listener === "function") {
							try {
								listener.call(filesaver, event || filesaver);
							} catch (ex) {
								throw_outside(ex);
							}
						}
					}
				}
				, FileSaver = function(blob, name) {
					// First try a.download, then web filesystem, then object URLs
					var
						  filesaver = this
						, type = blob.type
						, blob_changed = false
						, object_url
						, target_view
						, get_object_url = function() {
							var object_url = get_URL().createObjectURL(blob);
							deletion_queue.push(object_url);
							return object_url;
						}
						, dispatch_all = function() {
							dispatch(filesaver, "writestart progress write writeend".split(" "));
						}
						// on any filesys errors revert to saving with object URLs
						, fs_error = function() {
							// don't create more object URLs than needed
							if (blob_changed || !object_url) {
								object_url = get_object_url(blob);
							}
							if (target_view) {
								target_view.location.href = object_url;
							} else {
		                        window.open(object_url, "_blank");
		                    }
							filesaver.readyState = filesaver.DONE;
							dispatch_all();
						}
						, abortable = function(func) {
							return function() {
								if (filesaver.readyState !== filesaver.DONE) {
									return func.apply(this, arguments);
								}
							};
						}
						, create_if_not_found = {create: true, exclusive: false}
						, slice
					;
					filesaver.readyState = filesaver.INIT;
					if (!name) {
						name = "download";
					}
					if (can_use_save_link) {
						object_url = get_object_url(blob);
						save_link.href = object_url;
						save_link.download = name;
						click(save_link);
						filesaver.readyState = filesaver.DONE;
						dispatch_all();
						return;
					}
					// Object and web filesystem URLs have a problem saving in Google Chrome when
					// viewed in a tab, so I force save with application/octet-stream
					// http://code.google.com/p/chromium/issues/detail?id=91158
					if (view.chrome && type && type !== force_saveable_type) {
						slice = blob.slice || blob.webkitSlice;
						blob = slice.call(blob, 0, blob.size, force_saveable_type);
						blob_changed = true;
					}
					// Since I can't be sure that the guessed media type will trigger a download
					// in WebKit, I append .download to the filename.
					// https://bugs.webkit.org/show_bug.cgi?id=65440
					if (webkit_req_fs && name !== "download") {
						name += ".download";
					}
					if (type === force_saveable_type || webkit_req_fs) {
						target_view = view;
					}
					if (!req_fs) {
						fs_error();
						return;
					}
					fs_min_size += blob.size;
					req_fs(view.TEMPORARY, fs_min_size, abortable(function(fs) {
						fs.root.getDirectory("saved", create_if_not_found, abortable(function(dir) {
							var save = function() {
								dir.getFile(name, create_if_not_found, abortable(function(file) {
									file.createWriter(abortable(function(writer) {
										writer.onwriteend = function(event) {
											target_view.location.href = file.toURL();
											deletion_queue.push(file);
											filesaver.readyState = filesaver.DONE;
											dispatch(filesaver, "writeend", event);
										};
										writer.onerror = function() {
											var error = writer.error;
											if (error.code !== error.ABORT_ERR) {
												fs_error();
											}
										};
										"writestart progress write abort".split(" ").forEach(function(event) {
											writer["on" + event] = filesaver["on" + event];
										});
										writer.write(blob);
										filesaver.abort = function() {
											writer.abort();
											filesaver.readyState = filesaver.DONE;
										};
										filesaver.readyState = filesaver.WRITING;
									}), fs_error);
								}), fs_error);
							};
							dir.getFile(name, {create: false}, abortable(function(file) {
								// delete file if it already exists
								file.remove();
								save();
							}), abortable(function(ex) {
								if (ex.code === ex.NOT_FOUND_ERR) {
									save();
								} else {
									fs_error();
								}
							}));
						}), fs_error);
					}), fs_error);
				}
				, FS_proto = FileSaver.prototype
				, saveAs = function(blob, name) {
					return new FileSaver(blob, name);
				}
			;
			FS_proto.abort = function() {
				var filesaver = this;
				filesaver.readyState = filesaver.DONE;
				dispatch(filesaver, "abort");
			};
			FS_proto.readyState = FS_proto.INIT = 0;
			FS_proto.WRITING = 1;
			FS_proto.DONE = 2;
		
			FS_proto.error =
			FS_proto.onwritestart =
			FS_proto.onprogress =
			FS_proto.onwrite =
			FS_proto.onabort =
			FS_proto.onerror =
			FS_proto.onwriteend =
				null;
		
			
			if (view.addEventListener){
  				view.addEventListener("unload", process_deletion_queue, false);
			} else if (view.attachEvent){
  				view.attachEvent("unload", process_deletion_queue);
			}
			//view.addEventListener("unload", process_deletion_queue, false);
			return saveAs;
		}(self));
		
		var data = [["name1", "city1", "some other info"], ["name2", "city2", "more info"]];
		var csvContent = "data:text/csv;charset=utf-8,";
		//data.forEach(function(infoArray, index){
		//	if(index!=0){
		//		dataString = infoArray.join(",");
		//   		csvContent += index < infoArray.length ? "\n" + dataString  : dataString;
		//	}
		//});
		
		function joinData(infoArray, index){
				//if(index!=0){
					dataString = infoArray.join(",");
		   			csvContent += index < infoArray.length ? "\n" + dataString  : dataString;
				//}
		}
		for (i = 0, len = data.length; i < len; i++) {
			joinData(data,i);
		}
		
		//var blob = new Blob([csvContent], {
    	//	type: "text/csv;charset=utf-8;",
		//});
		saveAs(csvContent, "thing.csv");
	
	}-*/;
	@Override
	public void beforeDisplay() {
		//simplePanel.clear();
	}


	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getWidget() {
		return simplePanel;
	}
}
