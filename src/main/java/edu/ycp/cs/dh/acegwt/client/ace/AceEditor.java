//Copyright (c) 2011-2014, David H. Hovemeyer <david.hovemeyer@gmail.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package edu.ycp.cs.dh.acegwt.client.ace;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RequiresResize;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A GWT widget for the Ajax.org Code Editor (ACE).
 *
 * @see <a href="http://ace.ajax.org/">Ajax.org Code Editor</a>
 */
public class AceEditor extends Composite implements RequiresResize, HasText, TakesValue<String> {
    // Used to generate unique element ids for Ace widgets.
    /**
     * The next id.
     */
    private static int nextId = 0;

    /**
     * The element id.
     */
    private final String elementId;

    /**
     * The editor.
     */
    private JavaScriptObject editor;

    /**
     * The annotations.
     */
    private JsArray<AceAnnotation> annotations = JavaScriptObject.createArray().cast();

    /**
     * The div element.
     */
    private Element divElement;

    /**
     * The markers.
     */
    private HashMap<Integer, AceRange> markers = new HashMap<Integer, AceRange>();

    /**
     * The selection.
     */
    private AceSelection selection = null;

    /**
     * The command line.
     */
    private AceCommandLine commandLine = null;

    /**
     * Preferred constructor.
     */
    public AceEditor() {
        elementId = "_aceGWT" + nextId;
        nextId++;
        FlowPanel div = new FlowPanel();
        div.getElement().setId(elementId);
        initWidget(div);
        divElement = div.getElement();
    }

    /**
     * Do not use this constructor: just use the default constructor.
     *
     * @param unused this parameter is ignored
     */
    @Deprecated
    public AceEditor(boolean unused) {
        this();
    }

    /**
     * Call this method to start the editor.
     * Make sure that the widget has been attached to the DOM tree
     * before calling this method.
     */
    public native void startEditor() /*-{
        var editor = $wnd.ace.edit(this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::divElement);
        editor.getSession().setUseWorker(false);
        this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor = editor;

        // Store a reference to the (Java) AceEditor object in the
        // JavaScript editor object.
        editor._aceGWTAceEditor = this;

        // I have been noticing sporadic failures of the editor
        // to display properly and receive key/mouse events.
        // Try to force the editor to resize and display itself fully.  See:
        //    https://groups.google.com/group/ace-discuss/browse_thread/thread/237262b521dcea33
        editor.resize();
        this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::redisplay();
    }-*/;

    /**
     * Call this to force the editor contents to be redisplayed.
     * There seems to be a problem when an AceEditor is embedded in a LayoutPanel:
     * the editor contents don't appear, and it refuses to accept focus
     * and mouse events, until the browser window is resized.
     * Calling this method works around the problem by forcing
     * the underlying editor to redisplay itself fully. (?)
     */
    public native void redisplay() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.renderer.onResize(true);
        editor.renderer.updateFull();
        editor.resize();
        editor.focus();
    }-*/;

    /**
     * This method will hide editor completer pop up.
     */
    public native void detach() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        if (editor && editor.completer)
            editor.completer.detach();

    }-*/;

    /**
     * Cleans up the entire editor.
     */
    public native void destroy() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.destroy();
    }-*/;

    /**
     * Set the theme.
     *
     * @param theme the theme (one of the values in the {@link AceEditorTheme}
     *              enumeration)
     */
    public void setTheme(final AceEditorTheme theme) {
        setThemeByName(theme.getName());
    }

    /**
     * Set the theme by name.
     *
     * @param themeName the theme name (e.g., "twilight")
     */
    public native void setThemeByName(String themeName) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.setTheme("ace/theme/" + themeName);
    }-*/;
	
	
	/*public native void lockEditor(int howManyRows, int lastCol)-{
		var htmEditor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;

		var session = htmEditor.getSession(), Range = $wnd.ace
				.require("ace/range").Range, range = new Range(0, 0, 26, 0), markerId = session
				.addMarker(range, "readonly-highlight");

		//session.setMode("ace/mode/html");    

		range.start = session.doc.createAnchor(range.start);
		range.end = session.doc.createAnchor(range.end);
		range.end.$insertRight = true;

		//htmEditor.getSession().setMode("ace/mode/html");
		//htmEditor.setTheme("ace/theme/merbivore");
		htmEditor.setOptions({
			enableBasicAutocompletion : true,
			enableSnippets : true
		});

		htmEditor.setShowPrintMargin(false);
		htmEditor.setDisplayIndentGuides(false);
		htmEditor.setHighlightActiveLine(false);

		htmEditor.commands.on("afterExec", function(e) {
			if (e.command.name == "insertstring" && /^[\<.]$/.test(e.args)) {
				htmEditor.execCommand("startAutocomplete")
			}
		});
		htmEditor.setShowPrintMargin(false);
		htmEditor.setHighlightActiveLine(false);

		htmEditor.keyBinding.addKeyboardHandler({
			handleKeyboard : function(data, hash, keyString, keyCode, event) {

				if (hash === -1 || (keyCode <= 40 && keyCode >= 37)) {
					return false;
				}
				if (intersects(range)) {
					return {
						command : "null",
						passEvent : false
					};
				}
			}
		});

		htmEditor.commands.on("afterExec", function(e) {
			// activate autocomplete when paren or .(dot) is typed
			if (e.command.name == "insertstring" && /^[\\.\(.]$/.test(e.args)) {
				//editor.execCommand("startAutocomplete");
			}
			if (e.command.name == "insertstring" && /^[\w\:.]$/.test(e.args)) {
				//editor.execCommand("startAutocomplete");
			}
			if (e.command.name == "insertstring" && /^[\<.]$/.test(e.args)) {
				//htmEditor.execCommand("startAutocomplete");
			}
		});

		htmEditor.container.addEventListener("contextmenu", function(e) {
			e.preventDefault();
			return false;
		}, false);

		//To disable Cut Copy Paster on editor found this solution on Google Group.
		//stop = function(e) {
		//	e.stopPropagation();
		//	e.preventDefault();
		//	console.log(e)
	//	}
	//	document.querySelector(".ace_editor").addEventListener("copy", stop,
	//			true)
	//	document.querySelector(".ace_editor").addEventListener("cut", stop,
	//			true)
	//	document.querySelector(".ace_editor").addEventListener("paste", stop,
	//			true)
	//	// or
	//	htmEditor.onCopy = htmEditor.onCut = htmEditor.onPaste = htmEditor.getCopyText = function() {
	//		return ""
	//	}

		function before(obj, method, wrapper) {
			var orig = obj[method];
			obj[method] = function() {
				var args = Array.prototype.slice.call(arguments);
				return wrapper.apply(this, function() {
					return orig.apply(obj, orig);
				}, args);
			}

			return obj[method];
		}

		function intersects(range) {
			//alert(htmEditor.getSelectionRange().intersects(range));
			return htmEditor.getSelectionRange().intersects(range);
		}

		function preventReadonly(next) {
			if (intersects(range))
				return;
			next();
		}

	}-;*/

    /**
     * Set the mode.
     *
     * @param mode the mode (one of the values in the
     *             {@link AceEditorMode} enumeration)
     */
    public void setMode(final AceEditorMode mode) {
        setModeByName(mode.getName());
    }

    /**
     * Set the mode by name.
     *
     * @param shortModeName name of mode (e.g., "eclipse")
     */
    public native void setModeByName(String shortModeName) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var modeName = "ace/mode/" + shortModeName;
        var TheMode = $wnd.ace.require(modeName).Mode;
        editor.getSession().setMode(new TheMode());
    }-*/;

    /**
     * Enable a worker for the current session.
     *
     * @param useWorker the new use worker
     */
    public native void setUseWorker(boolean useWorker) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().setUseWorker(useWorker);
    }-*/;

    /**
     * Register a handler for change events generated by the editor.
     *
     * @param callback the change event handler
     */
    public native void addOnChangeHandler(AceEditorCallback callback) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().on("change", function (e) {
            callback.@edu.ycp.cs.dh.acegwt.client.ace.AceEditorCallback::invokeAceCallback(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
        });
    }-*/;

    /**
     * Register a handler for cursor position change events generated by the editor.
     *
     * @param callback the cursor position change event handler
     */
    public native void addOnCursorPositionChangeHandler(AceEditorCallback callback) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().selection.on("changeCursor", function (e) {
            callback.@edu.ycp.cs.dh.acegwt.client.ace.AceEditorCallback::invokeAceCallback(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
        });
    }-*/;

    /**
     * Give font size.
     *
     * @return font size
     */
    public native int getFontSize() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.getFontSize();
    }-*/;

    /**
     * Set font size.
     *
     * @param fontSize the font size to set, e.g., "16px"
     */
    public native void setFontSize(String fontSize) /*-{
        var elementId = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::elementId;
        var elt = $doc.getElementById(elementId);
        elt.style.fontSize = fontSize;
    }-*/;

    /**
     * Set integer font size.
     *
     * @param fontSize the font size to set, e.g., 16
     */
    public native void setFontSize(int fontSize) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.setFontSize(fontSize);
    }-*/;

    /**
     * Get the complete text in the editor as a String.
     *
     * @return the text in the editor
     */
    @Override
    public native String getText() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.getSession().getValue();
    }-*/;

    /**
     * Causes the editor to gain input focus.
     */
    public native void focus() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.focus();
    }-*/;

    /**
     * Retrieves the number of lines in the editor.
     *
     * @return The number of lines in the editor.
     */
    public native int getLineCount() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.session.getLength();
    }-*/;

    /**
     * Set the complete text in the editor from a String.
     *
     * @param text the text to set in the editor
     */
    @Override
    public native void setText(String text) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().setValue(text);
    }-*/;

    /**
     * Get the line of text at the given row number.
     *
     * @param row the row number
     * @return the line of text at that row number
     */
    public native String getLine(int row) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.getSession().getDocument().getLine(row);
    }-*/;

    /**
     * Insert given text at the cursor.
     *
     * @param text text to insert at the cursor
     */
    public native void insertAtCursor(String text) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.insert(text);
    }-*/;

    /**
     * Get the current cursor position.
     *
     * @return the current cursor position
     */
    public native AceEditorCursorPosition getCursorPosition() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var pos = editor.getCursorPosition();
        return this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::getCursorPositionImpl(DD)(pos.row, pos.column);
    }-*/;

    /**
     * Gets the cursor position impl.
     *
     * @param row    the row
     * @param column the column
     * @return the cursor position impl
     */
    private AceEditorCursorPosition getCursorPositionImpl(final double row, final double column) {
        return new AceEditorCursorPosition((int) row, (int) column);
    }

    /**
     * Gets the given document position as a zero-based index.
     *
     * @param position the position to obtain the absolute index of (base zero)
     * @return An index to the current location in the document
     */
    public int getIndexFromPosition(AceEditorCursorPosition position) {
        return getIndexFromPositionImpl(position.toJsObject());
    }

    /**
     * Gets the index from position impl.
     *
     * @param jsPosition the js position
     * @return the index from position impl
     */
    private native int getIndexFromPositionImpl(JavaScriptObject jsPosition) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.getSession().getDocument().positionToIndex(jsPosition);
    }-*/;

    /**
     * Gets a document position from a supplied zero-based index.
     *
     * @param index (base zero)
     * @return A position object showing the row and column of the supplied index in the document
     */
    public native AceEditorCursorPosition getPositionFromIndex(int index) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var jsPosition = editor.getSession().getDocument().indexToPosition(index);
        return @edu.ycp.cs.dh.acegwt.client.ace.AceEditorCursorPosition::create(II)(
            jsPosition.row,
            jsPosition.column
        );
    }-*/;

    /**
     * Set whether or not soft tabs should be used.
     *
     * @param useSoftTabs true if soft tabs should be used, false otherwise
     */
    public native void setUseSoftTabs(boolean useSoftTabs) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().setUseSoftTabs(useSoftTabs);
    }-*/;

    /**
     * Set tab size.  (Default is 4.)
     *
     * @param tabSize the tab size to set
     */
    public native void setTabSize(int tabSize) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().setTabSize(tabSize);
    }-*/;

    /**
     * Go to given line.
     *
     * @param line the line to go to
     */
    public native void gotoLine(int line) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.gotoLine(line);
    }-*/;

    /**
     * Set whether or not the horizontal scrollbar is always visible.
     *
     * @param hScrollBarAlwaysVisible true if the horizontal scrollbar is always
     *                                visible, false if it is hidden when not needed
     */
    public native void setHScrollBarAlwaysVisible(boolean hScrollBarAlwaysVisible) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.renderer.setHScrollBarAlwaysVisible(hScrollBarAlwaysVisible);
    }-*/;

    /**
     * Set whether or not the gutter is shown.
     *
     * @param showGutter true if the gutter should be shown, false if it should be hidden
     */
    public native void setShowGutter(boolean showGutter) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.renderer.setShowGutter(showGutter);
    }-*/;

    /**
     * Set or unset read-only mode.
     *
     * @param readOnly true if editor should be set to readonly, false if the
     *                 editor should be set to read-write
     */
    public native void setReadOnly(boolean readOnly) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.setReadOnly(readOnly);
    }-*/;


    /**
     * Set or unset highlighting of currently selected word.
     *
     * @param highlightSelectedWord true to highlight currently selected word, false otherwise
     */
    public native void setHighlightSelectedWord(boolean highlightSelectedWord) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.setHighlightSelectedWord(highlightSelectedWord);
    }-*/;

    /**
     * Set or unset the visibility of the print margin.
     *
     * @param showPrintMargin true if the print margin should be shown, false otherwise
     */
    public native void setShowPrintMargin(boolean showPrintMargin) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.renderer.setShowPrintMargin(showPrintMargin);
    }-*/;

    /**
     * Add an annotation to a the local <code>annotations</code> JsArray&lt;AceAnnotation&gt;, but does not set it on the editor.
     *
     * @param row    to which the annotation should be added
     * @param column to which the annotation applies
     * @param text   to display as a tooltip with the annotation
     * @param type   to be displayed (one of the values in the
     *               {@link AceAnnotationType} enumeration)
     */
    public void addAnnotation(final int row, final int column, final String text, final AceAnnotationType type) {
        annotations.push(AceAnnotation.create(row, column, text, type.getName()));
    }

    /**
     * Set any annotations which have been added via <code>addAnnotation</code> on the editor.
     */
    public native void setAnnotations() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var annotations = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::annotations;
        editor.getSession().setAnnotations(annotations);
    }-*/;


    /**
     * Clear any annotations from the editor and reset the local <code>annotations</code> JsArray&lt;AceAnnotation&gt;.
     */
    public native void clearAnnotations() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().clearAnnotations();
        this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::resetAnnotations()();
    }-*/;

    /**
     * Reset any annotations in the local <code>annotations</code> JsArray<AceAnnotation>.
     */
    private void resetAnnotations() {
        annotations = JavaScriptObject.createArray().cast();
    }

    /**
     * Remove a command from the editor.
     *
     * @param command the command (one of the values in the
     *                {@link AceCommand} enumeration)
     */
    public void removeCommand(final AceCommand command) {
        removeCommandByName(command.getName());
    }

    /**
     * Execute a command with no arguments. See {@link AceCommand}
     * values for example.
     *
     * @param command the command (one of the values in the
     *                {@link AceCommand} enumeration)
     */
    public void execCommand(AceCommand command) {
        execCommand(command, null);
    }

    /**
     * Execute a command with arguments (in case args is not null).
     * See {@link AceCommand} values for example.
     *
     * @param command the command (one of the values in the
     *                {@link AceCommand} enumeration)
     * @param args    command arguments (string or map)
     */
    public void execCommand(AceCommand command, AceCommandArgs args) {
        execCommand(command.getName(), args);
    }

    /**
     * Execute a command possibly containing string argument.
     *
     * @param command the command which could be one or two words separated
     *                by whitespaces
     */
    public native void execCommand(String command) /*-{
        var parts = command.split(/\s+/);
        this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::execCommand(Ljava/lang/String;Ljava/lang/String;)(parts[0], parts[1]);
    }-*/;

    /**
     * Execute a command with arguments (in case args is not null).
     *
     * @param command one word command
     * @param arg     the arg
     */
    public void execCommand(String command, String arg) {
        execCommandHidden(command, arg);
    }

    /**
     * Execute a command with arguments (in case args is not null).
     *
     * @param command one word command
     * @param args    command arguments of type {@link AceCommandArgs}
     */
    public void execCommand(String command, AceCommandArgs args) {
        execCommandHidden(command, args);
    }

    /**
     * Exec command hidden.
     *
     * @param command the command
     * @param args    the args
     */
    private native void execCommandHidden(String command, Object args) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        if (args && typeof args !== "string")
            args = args.@edu.ycp.cs.dh.acegwt.client.ace.AceCommandArgs::getValue()();
        editor.commands.exec(command, editor, args);
        editor.focus();
    }-*/;

    /**
     * Remove commands, that may not be required, from the editor.
     *
     * @param command to be removed, one of
     *                "gotoline", "findnext", "findprevious", "find", "replace", "replaceall"
     */
    public native void removeCommandByName(String command) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.commands.removeCommand(command);
    }-*/;

    public native void replace(String newString) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var lastRow = editor.getSession().getLength();
        var lastRowLength = editor.getSession().getRowLength(lastRow);
        var Range = $wnd.ace.require('ace/range').Range;
        var myRange = new Range(0, 0, lastRow, lastRowLength);
        editor.getSession().replace(myRange, newString)
    }-*/;

    /**
     * Construct java wrapper for registered Ace command.
     *
     * @param command name of command
     * @return command description
     */
    public native AceCommandDescription getCommandDescription(String command) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var obj = editor.commands.commands[command];
        if (!obj)
            return null;
        return @edu.ycp.cs.dh.acegwt.client.ace.AceCommandDescription::fromJavaScript(Lcom/google/gwt/core/client/JavaScriptObject;)(obj);
    }-*/;

    /**
     * List names of all Ace commands.
     *
     * @return names of all Ace commands
     */
    public native List<String> listCommands() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var ret = @java.util.ArrayList::new()();
        for (var command in editor.commands.commands)
            ret.@java.util.ArrayList::add(Ljava/lang/Object;)(command);
        return ret;
    }-*/;

    /**
     * Add user defined command.
     *
     * @param description command description
     */
    public native void addCommand(AceCommandDescription description) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var command = description.@edu.ycp.cs.dh.acegwt.client.ace.AceCommandDescription::toJavaScript(Ledu/ycp/cs/dh/acegwt/client/ace/AceEditor;)(this);
        editor.commands.addCommand(command);
    }-*/;

    /**
     * Set whether to use wrap mode or not.
     *
     * @param useWrapMode true if word wrap should be used, false otherwise
     */
    public native void setUseWrapMode(boolean useWrapMode) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().setUseWrapMode(useWrapMode);
    }-*/;

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.ResizeComposite#onResize()
     */
    @Override
    public void onResize() {
        redisplay();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.TakesValue#setValue(java.lang.Object)
     */
    @Override
    public void setValue(String value) {
        this.setText(value);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.TakesValue#getValue()
     */
    @Override
    public String getValue() {
        return this.getText();
    }

    /**
     * Set whether or not autocomplete is enabled.
     *
     * @param b true if autocomplete should be enabled, false if not
     */
    public native void setAutocompleteEnabled(boolean b) /*-{
        // See: https://github.com/ajaxorg/ace/wiki/How-to-enable-Autocomplete-in-the-Ace-editor
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        if (b) {
            $wnd.ace.require("ace/ext/language_tools");
            editor.setOptions({enableBasicAutocompletion: true});
        } else {
            editor.setOptions({enableBasicAutocompletion: false});
        }
    }-*/;


    /**
     * Removes all existing completers from the langtools<br><br>
     * This can be used to disable all completers including local completers, which can be very useful
     * when completers are used on very large files (as the local completer tokenizes every word to put in the selected list).<br><br>
     * <strong>NOTE:</strong> This method may be removed, and replaced with another solution. It works at point of check-in, but treat this as unstable for now.
     */
    public native static void removeAllExistingCompleters() /*-{
        var langTools = $wnd.ace.require("ace/ext/language_tools");
        langTools.setCompleters([]);
    }-*/;


    /**
     * Add an {@link AceCompletionProvider} to provide
     * custom code completions.
     *
     * <strong>Warning</strong>: this is an experimental feature of AceGWT.
     * It is possible that the API will change in an incompatible way
     * in future releases.
     *
     * @param provider the {@link AceCompletionProvider}
     */
    public native static void addCompletionProvider(AceCompletionProvider provider) /*-{
        var langTools = $wnd.ace.require("ace/ext/language_tools");
        var completer = {
            getCompletions: function (editor, session, pos, prefix, callback) {
                var callbackWrapper =
                    @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::wrapCompletionCallback(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
                var aceEditor = editor._aceGWTAceEditor;
                provider.@edu.ycp.cs.dh.acegwt.client.ace.AceCompletionProvider::getProposals(Ledu/ycp/cs/dh/acegwt/client/ace/AceEditor;Ledu/ycp/cs/dh/acegwt/client/ace/AceEditorCursorPosition;Ljava/lang/String;Ledu/ycp/cs/dh/acegwt/client/ace/AceCompletionCallback;)(
                    aceEditor,
                    @edu.ycp.cs.dh.acegwt.client.ace.AceEditorCursorPosition::create(II)(pos.row, pos.column),
                    prefix,
                    callbackWrapper
                );
            },
            getDocTooltip: function (item) {
                if ((!item.docHTML) && item.aceGwtHtmlTooltip != null) {
                    item.docHTML = item.aceGwtHtmlTooltip;
                }
            }
        };
        langTools.addCompleter(completer);
    }-*/;

    /**
     * Adds a static marker into this editor.
     *
     * @param range   an {@link AceRange}.
     * @param clazz   a CSS class that must be applied to the marker.
     * @param type    an {@link AceMarkerType}.
     * @param inFront set to 'true' if the marker must be in front of the text, 'false' otherwise.
     * @return The marker ID. This id can be then use to remove a marker from the editor.
     */
    public native int addMarker(AceRange range, String clazz, AceMarkerType type, boolean inFront) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        var markerID = editor.getSession().addMarker(range, clazz, type.@edu.ycp.cs.dh.acegwt.client.ace.AceMarkerType::getName()(), inFront);
        this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::addMarker(ILedu/ycp/cs/dh/acegwt/client/ace/AceRange;)(markerID, range);
        return markerID;
    }-*/;

    /**
     * Adds a floating marker into this editor (the marker follows lines changes as insertions, suppressions...).
     *
     * @param range an {@link AceRange}.
     * @param clazz a CSS class that must be applied to the marker.
     * @param type  an {@link AceMarkerType}.
     * @return The marker ID. This id can be then use to remove a marker from the editor.
     */
    public native int addFloatingMarker(AceRange range, String clazz, AceMarkerType type) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        range.start = editor.getSession().doc.createAnchor(range.start);
        range.end = editor.getSession().doc.createAnchor(range.end);
        return this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::addMarker(Ledu/ycp/cs/dh/acegwt/client/ace/AceRange;Ljava/lang/String;Ledu/ycp/cs/dh/acegwt/client/ace/AceMarkerType;Z)
        (
            range,
            clazz,
            type,
            false
        );
    }-*/;

    /**
     * Removes the marker with the specified ID.
     *
     * @param markerId the marker ID.
     */
    public native void removeMarker(int markerId) /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        editor.getSession().removeMarker(markerId);
        this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::removeRegisteredMarker(I)(markerId);
    }-*/;

    /**
     * Gets all the displayed markers.
     *
     * @return A Mapping between markerID and the displayed range.
     */
    public HashMap<Integer, AceRange> getMarkers() {
        return markers;
    }

    /**
     * Remove all the displayed markers.
     */
	/*public void removeAllMarkers() {
		List<Integer> key = new ArrayList<Integer>(markers.keySet());
		for (Integer id : markers.keySet()) {
			removeMarker(id);
		}
	}*/
    public void removeAllMarkers() {
        List<Integer> key = new ArrayList<Integer>(markers.keySet());
        for (int i = 0; i < key.size(); i++) {
            removeMarker(key.get(i));
        }
    }

    /**
     * Adds the marker.
     *
     * @param id    the id
     * @param range the range
     */
    private void addMarker(int id, AceRange range) {
        markers.put(id, range);
    }

    /**
     * Removes the registered marker.
     *
     * @param id the id
     */
    private void removeRegisteredMarker(int id) {
        AceRange range = markers.remove(id);
        range.detach();
    }

    /**
     * Prepare a wrapper around Ace Selection object.
     *
     * @return a wrapper around Ace Selection object
     */
    public AceSelection getSelection() {
        if (selection == null) {
            selection = new AceSelection(getSelectionJS());
        }
        return selection;
    }

    /**
     * Gets the selection js.
     *
     * @return the selection js
     */
    private native JavaScriptObject getSelectionJS() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.getSession().getSelection();
    }-*/;

    /**
     * Bind command line and editor. For default implementation of command line
     * you can use <code> AceCommandLine cmdLine = new AceDefaultCommandLine(textBox) </code>
     * where textBox could be for instance standard GWT TextBox or TextArea.
     *
     * @param cmdLine implementation of command line
     */
    public void initializeCommandLine(AceCommandLine cmdLine) {
        commandLine = cmdLine;
        commandLine.setCommandLineListener(new AceCommandLineListener() {
            @Override
            public void onCommandEntered(String command) {
                execCommand(command);
            }
        });
    }

    /**
     * Wrap completion callback.
     *
     * @param jsCallback the js callback
     * @return the ace completion callback
     */
    private static AceCompletionCallback wrapCompletionCallback(JavaScriptObject jsCallback) {

        return new AceCompletionCallbackImpl(jsCallback);
    }

    //Method Added by MAT Team Start :

    /**
     * Added Key down handler for Ace Editor.
     *
     * @param keyDownHandler the key down handler
     * @return the handler registration
     */
    public HandlerRegistration addKeyDownHandler(KeyDownHandler keyDownHandler) {
        return this.addDomHandler(keyDownHandler, KeyDownEvent.getType());
    }

    /**
     * Reterive ReadOnly State for Ace Editor.
     * <p>
     * Return Boolean.
     *
     * @return true, if is read only
     */
    public native boolean isReadOnly() /*-{
        var editor = this.@edu.ycp.cs.dh.acegwt.client.ace.AceEditor::editor;
        return editor.getReadOnly();
    }-*/;

    /**
     * This method is used for CQL definitions, functions and parameters to be
     * available for AceEditor dynamically on Keyboard Shortcuts.
     */
    public native void addAutoCompletions()/*-{
        $wnd.definitioList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createDefinitionsJsArrayString();

        $wnd.paramList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createParamsJsArrayString();

        $wnd.funcsList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createfuncsJsArrayString();

        $wnd.allAttributeList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createAttributesJsArrayString();

        $wnd.dataTypeList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createDatatypesJsArrayString();

        $wnd.valueSetList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createValueSetJsArrayString();

        $wnd.timingList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createTimingJsArrayString();

        $wnd.unitsList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createUnitsJsArrayString();

        $wnd.keywordsList = @edu.ycp.cs.dh.acegwt.client.ace.AceEditor::createKeywordsJsArrayString();

    }-*/;

    /**
     * Creates the timing js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createTimingJsArrayString() {
        List<String> timingList = MatContext.get().getCqlConstantContainer().getCqlTimingList();

        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (String string : timingList) {
            jsArray.push(string);
        }

        return jsArray;
    }

    /**
     * Creates the value set js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createValueSetJsArrayString() {
        List<CQLIdentifierObject> valueSetList = new ArrayList<CQLIdentifierObject>();
        valueSetList.addAll(MatContext.get().getValuesets());
        valueSetList.addAll(MatContext.get().getIncludedValueSetNames());
        valueSetList.addAll(MatContext.get().getIncludedCodeNames());

        List<String> terminologyStrings = new ArrayList<>();
        for (CQLIdentifierObject valueset : valueSetList) {
            terminologyStrings.add(valueset.toString());
        }

        Collections.sort(terminologyStrings);

        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (CQLIdentifierObject valueset : valueSetList) {
            jsArray.push(valueset.toString());
        }


        return jsArray;
    }

    /**
     * Creates the datatypes js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createDatatypesJsArrayString() {
        Set<String> dataTypeList = new HashSet<>();

        if (MatContext.get().isCurrentModelTypeFhir()) {
            MatContext.get().getCqlConstantContainer().getFhirDataTypes().keySet().forEach(
                    s -> dataTypeList.addAll(splitPeriod(s)));
        } else {
            dataTypeList.addAll(MatContext.get().getCqlConstantContainer().getCqlDatatypeList());
        }

        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (String string : dataTypeList) {
            if (!string.equals(MatContext.PLEASE_SELECT)) {
                jsArray.push("[\"" + string + "\"]");
            }
        }
        return jsArray;
    }

    private static List<String> splitPeriod(String s) {
        if (s == null || s.equals("")) {
            return new ArrayList<>();
        } else {
            List<String> result = new ArrayList<>();
            ;
            String[] split = s.split(".");
            for (String sp : split) {
                result.add(sp);
            }
            return result;
        }
    }

    /**
     * Creates the definitions js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createDefinitionsJsArrayString() {
        List<CQLIdentifierObject> defineList = new ArrayList<CQLIdentifierObject>();
        defineList.addAll(MatContext.get().getDefinitions());
        defineList.addAll(MatContext.get().getIncludedDefNames());
        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();

        for (CQLIdentifierObject definition : defineList) {
            jsArray.push(definition.toString());
        }

        return jsArray;
    }

    /**
     * Creates the params js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createParamsJsArrayString() {
        List<CQLIdentifierObject> paramList = new ArrayList<CQLIdentifierObject>();
        paramList.addAll(MatContext.get().getParameters());
        paramList.addAll(MatContext.get().getIncludedParamNames());
        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (CQLIdentifierObject parameter : paramList) {
            jsArray.push(parameter.toString());
        }
        return jsArray;
    }

    /**
     * Createfuncs js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createfuncsJsArrayString() {
        List<CQLIdentifierObject> funcsList = new ArrayList<CQLIdentifierObject>();
        funcsList.addAll(MatContext.get().getFuncs());
        funcsList.addAll(MatContext.get().getIncludedFuncNames());
        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (CQLIdentifierObject function : funcsList) {
            jsArray.push(function.toString() + "()");
        }

        List<String> preDefFuncsList = new ArrayList<String>();
        preDefFuncsList.addAll(MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlFunctionsList());
        for (String func : preDefFuncsList) {
            jsArray.push(func);
        }
        return jsArray;
    }

    /**
     * Creates the attributes js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createAttributesJsArrayString() {
        Set<String> attrList = new HashSet<>();

        if (MatContext.get().isCurrentModelTypeFhir()) {
            MatContext.get().getCqlConstantContainer().getFhirDataTypes().values().
                    forEach(v -> attrList.addAll(
                            v.getAttributes().values().stream().
                                    map(a -> a.getFhirElement()).collect(Collectors.toList())));
        } else {
            attrList.addAll(MatContext.get().getCqlConstantContainer().getCqlAttributeList());
        }

        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (String string : attrList) {
            jsArray.push(convertToCamelCase(string));
        }
        return jsArray;
    }

    /**
     * Creates the keywords js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createKeywordsJsArrayString() {
        List<String> keywordsList = MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList();
        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (String string : keywordsList) {
            jsArray.push(string);
        }
        return jsArray;
    }

    /**
     * Convert to camel case.
     *
     * @param str the str
     * @return the string
     */
    private static String convertToCamelCase(String str) {
        String result = "";
        char firstChar = str.charAt(0);
        result = result + Character.toLowerCase(firstChar);
        for (int i = 1; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            char previousChar = str.charAt(i - 1);
            if (previousChar == ' ') {
                result = result + Character.toUpperCase(currentChar);
            } else {
                result = result + currentChar;
            }
        }
        return result.replaceAll(" ", "");
    }

    /**
     * Creates the units js array string.
     *
     * @return the js array string
     */
    private static JsArrayString createUnitsJsArrayString() {
        List<String> unitsList = MatContext.get().getShorcutKeyUnits();
        JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
        for (String string : unitsList) {
            jsArray.push(string);
        }
        return jsArray;
    }


}
