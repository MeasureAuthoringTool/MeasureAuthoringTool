package mat.client.measure;

import mat.client.MatPresenter;
import mat.client.measure.ManageMeasurePresenter.BaseDisplay;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.SearchResults;
import mat.model.MeasureNotesModel.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MeasureNotesPresenter implements MatPresenter{
	
 	MeasureServiceAsync service = MatContext.get().getMeasureService();
 	 		
 	public NotesDisplay notesDisplay; 
 	
 	public static interface NotesDisplay extends BaseDisplay{
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public HasClickHandlers getExportButton();
		public void cancelComposedNote();
		void buildDataTable(SearchResults<Result> results);
		public TextArea getMeasureNoteComposer();
		public SuccessMessageDisplay getSuccessMessageDisplay();
		public ErrorMessageDisplay getErrorMessageDisplay();
		public TextBox getMeasureNoteTitle();
		
	}
 	 
 	public MeasureNotesPresenter(NotesDisplay notesDisplay){
 		this.notesDisplay=notesDisplay;
 		System.out.println("Created an instance of MeasureNotesPresenter >>>>");
 		notesDisplay.getExportButton().addClickHandler(new ClickHandler() { 
 			@Override
 			public void onClick(ClickEvent event) {
 				System.out.println("Export code to be written here");
 			}
 		});
 		
 		
 		notesDisplay.getSaveButton().addClickHandler(new ClickHandler() {
 			@Override
 			public void onClick(ClickEvent event) {
 				System.out.println("Saving the note to the database >>>> ");
 				saveMeasureNote();
 						
 			}
 		}); 
 		 				
 		notesDisplay.getCancelButton().addClickHandler(new ClickHandler() {
 			@Override
 			public void onClick(ClickEvent event) {
 				//notesDisplay.cancelComposedNote();
 			}	
 		}); 
 	}
 	
 	private MeasureNotesModel getAllMeasureNotesByMeasureID(){
 		//TODO for retriving measure notes
 		return null; 		
 	}
 	
 	
 	private void saveMeasureNote(){
 		String noteTitle = notesDisplay.getMeasureNoteTitle().getText();
 		String noteDescription = notesDisplay.getMeasureNoteComposer().getText();
 		if(noteTitle != null && !noteTitle.isEmpty() && noteDescription != null && !noteDescription.isEmpty()){
 			
 			service.saveMeasureNote(noteTitle, noteDescription,MatContext.get().getCurrentMeasureId(),MatContext.get().getLoggedinUserId(), new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					notesDisplay.getSuccessMessageDisplay().setMessage("The measure note is saved successfully");
					notesDisplay.getMeasureNoteComposer().setText("");
					notesDisplay.getMeasureNoteTitle().setText("");
				}
				
				@Override
				public void onFailure(Throwable caught) {
					notesDisplay.getErrorMessageDisplay().setMessage("Failed to save measure note" );
					
				}
			});		
 		}else{
 			notesDisplay.getSuccessMessageDisplay().clear();
 			notesDisplay.getErrorMessageDisplay().setMessage("Please enter Title and Description for the measure note before saving");
 		}
 	}
 	
	
	private native void generateCSVFile()/*-{
		var data = [["name1", "city1", "some other info"], ["name2", "city2", "more info"]];
		var csvContent = data[0];
		function joinData(infoArray, index){
				if(index!=0){
					dataString = infoArray.join(",");
		   			csvContent += index < infoArray.length ? "\n" + dataString  : dataString;
				}
		}
		for (i = 0, len = data.length; i < len; i++) {
			joinData(data,i);
		}
		
		
		if (navigator.appName != 'Microsoft Internet Explorer') {
        	//window.open('data:text/csv;charset=utf-8,' + escape(csvContent));
        	
        	var saveAs = saveAs || (navigator.msSaveOrOpenBlob && navigator.msSaveOrOpenBlob.bind(navigator))
  			|| (function(view) {
				"use strict";
				 var doc = view.document
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
					var filesaver = this
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
				};
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
			   FS_proto.onwriteend =null;
		  	   
		  	   if (view.addEventListener){
  				 view.addEventListener("unload", process_deletion_queue, false);
			   } else if (view.attachEvent){
			   	//For IE
  				view.attachEvent("unload", process_deletion_queue);
			   }
			   return saveAs;
			}(self));
			var blob = new Blob([csvContent], {
    			type: "text/csv;charset=utf-8;",
			});
		   saveAs(blob, "exportMeasureNotes.csv");
    	}
    	else {
        	var popup = window.open('', 'csv', '');
        	popup.document.body.innerHTML = '<pre>' +  + '</pre>';
      	}
}-*/;
	
	

	@Override
	public void beforeDisplay() {
		// TODO Auto-generated method stub
		notesDisplay.asWidget();
	}
	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		notesDisplay.asWidget();
	}
	@Override
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return notesDisplay.asWidget();
	}
	
}	