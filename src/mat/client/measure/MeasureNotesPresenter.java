package mat.client.measure;

import java.util.List;

import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import mat.DTO.MeasureNoteDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.measure.MeasureNotesView.Observer;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureNotesResult;
import mat.client.shared.ManageMeasureNotesModelValidator;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.shared.ConstantMessages;
import org.gwtbootstrap3.client.ui.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureNotesPresenter.
 */
public class MeasureNotesPresenter implements MatPresenter{
	
	/** The service. */
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	/** The notes display. */
	private NotesDisplay notesDisplay;
	
	/**
	 * The Interface NotesDisplay.
	 */
	public static interface NotesDisplay {
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the export button.
		 * 
		 * @return the export button
		 */
		public HasClickHandlers getExportButton();
		
		/**
		 * Cancel composed note.
		 */
		public void cancelComposedNote();
		
		/**
		 * Gets the measure note composer.
		 * 
		 * @return the measure note composer
		 */
		//public RichTextArea getMeasureNoteComposer();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public MessageAlert getSuccessMessageDisplay();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public MessageAlert getErrorMessageDisplay();
		
		/**
		 * Gets the measure note title.
		 * 
		 * @return the measure note title
		 */
		public TextBox getMeasureNoteTitle();
		
		/**
		 * Gets the notes result.
		 * 
		 * @return the notes result
		 */
		public MeasureNotesModel getNotesResult();
		
		/**
		 * Display view.
		 */
		public void displayView();
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Sets the observer.
		 * 
		 * @param observer
		 *            the new observer
		 */
		public void setObserver(Observer observer);
		
		/**
		 * Sets the notes result.
		 * 
		 * @param notesResult
		 *            the new notes result
		 */
		public void setNotesResult(MeasureNotesModel notesResult);

		Summernote getToolBar();
	}
	
	/**
	 * Instantiates a new measure notes presenter.
	 * 
	 * @param notesDisplay
	 *            the notes display
	 */
	public MeasureNotesPresenter(NotesDisplay notesDisplay){
		this.notesDisplay=notesDisplay;
		notesDisplay.getExportButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				generateCSVToExportMeasureNotes();
			}
		});
		
		notesDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveMeasureNote();
			}
		});
		
		notesDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetWidget();
			}
		});
	}
	
	/**
	 * Generate csv to export measure notes.
	 */
	private void generateCSVToExportMeasureNotes(){
		MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(), null, "MEASURE_NOTES_EXPORT", "Measure Notes Exported", ConstantMessages.DB_LOG);
		String url = GWT.getModuleBaseURL() + "export?id=" + MatContext.get().getCurrentMeasureId() + "&format=exportMeasureNotesForMeasure";
		Window.open(url + "&type=save","_blank", "");
	}
	
	/**
	 * Retrieving all the Measure Notes of the current measure id and displaying.
	 * MeasureNotesView.Observer is set here onSuccess method.
	 */
	private void search(){
		String measureID = MatContext.get().getCurrentMeasureId();
		showSearchingBusy(true);
		service.getAllMeasureNotesByMeasureID(measureID, new AsyncCallback<MeasureNotesModel>() {
			@Override
			public void onSuccess(MeasureNotesModel result) {
				showSearchingBusy(false);
				notesDisplay.setNotesResult(result);
				notesDisplay.displayView();
				notesDisplay.setObserver(new Observer() {
					@Override
					public void onDeleteClicked(MeasureNoteDTO result) {
						if(MatContext.get().getMeasureLockService().checkForEditPermission()){
							service.deleteMeasureNotes(result, new AsyncCallback<Void>() {
								@Override
								public void onSuccess(Void result) {
									clearMessages();
									notesDisplay.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getMEASURE_NOTES_DELETE_SUCCESS_MESSAGE());
									search();
								}
								@Override
								public void onFailure(Throwable caught) {
									showSearchingBusy(false);
									clearMessages();
									notesDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								}
							});
						}
						
					}
					
					@Override
					public void onSaveClicked(MeasureNoteDTO measureNoteDTO) {
						service.updateMeasureNotes(measureNoteDTO, MatContext.get().getLoggedinUserId(), new AsyncCallback<Void>() {
							@Override
							public void onSuccess(Void result) {
								clearMessages();
								notesDisplay.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getMEASURE_NOTES_SAVE_SUCCESS_MESSAGE());
								search();
							}
							@Override
							public void onFailure(Throwable caught) {
								clearMessages();
								notesDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}
						});
					}
				});
			}
			@Override
			public void onFailure(Throwable caught) {
				clearMessages();
				notesDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			
		});
	}
	
	/**
	 * Saving Measure Notes.
	 */
	private void saveMeasureNote(){
		String noteTitle = notesDisplay.getMeasureNoteTitle().getText();
//		String noteDescription = notesDisplay.getMeasureNoteComposer().getHTML();
		String noteDescription = notesDisplay.getToolBar().getCode();
		MeasureNoteDTO model = new MeasureNoteDTO();
		model.setNoteTitle(noteTitle);
		model.setNoteDesc(noteDescription);
		model.scrubForMarkUp();
		ManageMeasureNotesModelValidator modelValidator = new ManageMeasureNotesModelValidator();
		List<String> messageList = modelValidator.validation(model);
		if (messageList.size() == 0) {
			showSearchingBusy(true);
			service.saveMeasureNote(model,MatContext.get().getCurrentMeasureId(),MatContext.get().getLoggedinUserId(), new AsyncCallback<SaveMeasureNotesResult>() {
				
				@Override
				public void onSuccess(SaveMeasureNotesResult result) {
					if(result.isSuccess()) {
						showSearchingBusy(false);
						notesDisplay.getErrorMessageDisplay().clearAlert();
						notesDisplay.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getMEASURE_NOTES_SAVE_SUCCESS_MESSAGE());
						notesDisplay.getToolBar().setCode("");
						notesDisplay.getMeasureNoteTitle().setText("");
						search();
					} else {
						if(result.getFailureReason() == SaveMeasureNotesResult.INVALID_DATA){
							notesDisplay.getSuccessMessageDisplay().clearAlert();
							notesDisplay.getErrorMessageDisplay().createAlert("Invalid input data.");
						} else {
							notesDisplay.getSuccessMessageDisplay().clearAlert();
							notesDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					showSearchingBusy(false);
					notesDisplay.getSuccessMessageDisplay().clearAlert();
					notesDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					
				}
			});
		}else{
			notesDisplay.getSuccessMessageDisplay().clearAlert();
			notesDisplay.getErrorMessageDisplay().createAlert(messageList);
		}
	}
	
	/**
	 * Show or Hide Loading Message. Show Loading Message when 'busy' is
	 * true and hide Loading Message when 'busy' is false.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(boolean busy){
		if(busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		
		((Button)notesDisplay.getSaveButton()).setEnabled(!busy);
		((Button)notesDisplay.getCancelButton()).setEnabled(!busy);
	}
	
	//TO Do : This Native Jave code can be used to create CSV on client side. Presently it works for FF not in IE. IE10 has included support for blob. When Mat will move to IE 10 this can be used.
	
	/*private native void generateCSVFile()-{
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
}-;*/
	
	/**
	 * Clearing Success Message and Error Message display.
	 */
	private void clearMessages() {
		notesDisplay.getSuccessMessageDisplay().clearAlert();
		notesDisplay.getErrorMessageDisplay().clearAlert();
	}
	
	/**
	 * Clear the Measure Title and Measure Description input fields.
	 * Clear Success Message and Error Message display.
	 */
	private void resetWidget() {
		clearMessages();
		notesDisplay.getToolBar().clear();
		notesDisplay.getToolBar().setCode("");
		notesDisplay.getMeasureNoteTitle().setText("");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		resetWidget();
		search();
		notesDisplay.asWidget();
		MeasureComposerPresenter.setSubSkipEmbeddedLink("contentPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		resetWidget();
		notesDisplay.asWidget();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return notesDisplay.asWidget();
	}
	
}