package mat.client.shared;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 GwtBootstrap3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


/**
 * Container for drop down menu items.
 * <p/>
 * <strong>Must</strong> be encapsulated in a {@link org.gwtbootstrap3.client.ui.ButtonGroup} to build button dropdowns.
 *
 * @author Sven Jacobs
 */
public class DropDownSubMenu extends ListElement {


    public DropDownSubMenu() {
    	setStyleName("dropdown-submenu");
    }
}

