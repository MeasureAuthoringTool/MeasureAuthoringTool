package mat.client.validator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.shared.ListBoxMVP;

import java.util.*;
import java.util.logging.Logger;

public class ErrorHandler {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getSimpleName());
    public static final String REQUIRED = "This field is required.";
    private static final String ERROR_CLASS = "MatEHError";

    public interface Validation {
        String validate(String value);
    }

    public static class ValidationInfo {
        List<Validation> validations;
        Element fieldToAddErrorMessagesAfter;

        public ValidationInfo() {
            validations = new ArrayList<>();
        }

        public ValidationInfo(List<Validation> validations, Element fieldToAddErrorMessagesAfter) {
            this.validations = validations;
            this.fieldToAddErrorMessagesAfter = fieldToAddErrorMessagesAfter;
        }

        public List<Validation> getValidations() {
            return validations;
        }

        public void setValidations(List<Validation> validations) {
            this.validations = validations;
        }

        public Element getFieldToAddErrorMessagesAfter() {
            return fieldToAddErrorMessagesAfter;
        }

        public void setFieldToAddErrorMessagesAfter(Element fieldToAddErrorMessagesAfter) {
            this.fieldToAddErrorMessagesAfter = fieldToAddErrorMessagesAfter;
        }
    }

    public static final Validation REQUIRED_FIELD_VALIDATION = (v) -> (v == null || v.trim().equals("")) ?
            REQUIRED :
            null;

    public static final Validation NOT_SELECT_VALIDATION = (v) -> (v == null ||
            v.trim().equals("") ||
            v.trim().equalsIgnoreCase("--Select--")) ?
            REQUIRED :
            null;

    private final Map<Element, ValidationInfo> validations;

    public ErrorHandler() {
        validations = new HashMap<>();
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        validations.keySet().forEach(v -> errors.addAll(validate(v)));
        return errors;
    }

    public ValidationInfo getValidations(Element field) {
        return validations.computeIfAbsent(field, f -> new ValidationInfo());
    }

    public BlurHandler buildRequiredBlurHandler(Widget field) {
        return buildRequiredBlurHandler(field, field);
    }

    public BlurHandler buildRequiredBlurHandler(Widget field, Widget addErrorsAfter) {
        return buildBlurHandler(field, addErrorsAfter, buildRequiredValidation(field));
    }

    public BlurHandler buildBlurHandler(Widget field, Validation v) {
        return buildBlurHandler(field, field, v);
    }

    public BlurHandler buildBlurHandler(Widget field, Widget addErrorsAfter, Validation v) {
        ValidationInfo validationInfo = getValidations(field.getElement());
        validationInfo.setFieldToAddErrorMessagesAfter(addErrorsAfter.getElement());
        validationInfo.getValidations().add(v);
        return (blurEvent) -> validate(field.getElement());
    }

    public void setFieldError(Element field, Element addErrorsAfter, String error) {
        handleErrors(field, addErrorsAfter, Collections.singletonList(error));
    }

    public void setFieldError(Element field, String error) {
        setFieldError(field, field, error);
    }

    public void setFieldErrors(Element field, String... errors) {
        handleErrors(field, field, Arrays.asList(errors));
    }

    public void setFieldErrors(Element field, List<String> errors) {
        handleErrors(field, field, errors);
    }


    public void clearErrors() {
        validations.keySet().forEach((k) -> {
            ValidationInfo i = validations.get(k);
            clearErrors(k, i.getFieldToAddErrorMessagesAfter());
        });
    }

    public void clearErrors(Element validatedElement) {
        clearErrors(validatedElement, validatedElement);
    }

    public void clearErrors(Element validatedElement, Widget widgetToAddErrorsAfter) {
        clearErrors(validatedElement, widgetToAddErrorsAfter.getElement());
    }

    public void handleErrors(Element validatedElement, Widget widgetToAddErrorsAfter, List<String> errors) {
        handleErrors(validatedElement, widgetToAddErrorsAfter.getElement(), errors);
    }

    public void clearErrors(Element validatedElement, Element elementToAddErrorsAfter) {
        validatedElement.getStyle().setBorderColor("");
        while (isErrorMessage(elementToAddErrorsAfter.getNextSiblingElement())) {
            elementToAddErrorsAfter.getNextSiblingElement().removeFromParent();
        }
    }

    public void handleErrors(Element validatedElement, Element elementToAddErrorsAfter, List<String> errors) {
        clearErrors(validatedElement, elementToAddErrorsAfter);
        if (errors != null && errors.size() > 0) {
            logger.info("errors=" + errors);
            validatedElement.getStyle().setBorderColor("red");
            Element html = new HTML(
                    "<i class=\"fa fa-exclamation-circle\"></i> <b>" + errors.get(0) + "</b>"
            ).getElement();
            html.addClassName(ERROR_CLASS);
            html.getStyle().setColor("red");
            elementToAddErrorsAfter.getParentElement().appendChild(html);
        }
    }

    private boolean isErrorMessage(Element e) {
        return e != null && e.hasClassName(ERROR_CLASS);
    }

    private List<String> validate(Element element) {
        List<String> errors = new ArrayList<>();
        ValidationInfo result = validations.get(element);
        if (result.getValidations() != null) {
            String value = element.getPropertyString("value");
            result.getValidations().forEach(v -> {
                String errorMsg = v.validate(value);
                if (errorMsg != null) {
                    errors.add(errorMsg);
                }
            });

            if (!errors.isEmpty()) {
                handleErrors(element, result.getFieldToAddErrorMessagesAfter(), errors);
            } else {
                clearErrors(element, result.getFieldToAddErrorMessagesAfter());
            }
        }
        return errors;
    }

    private Validation buildRequiredValidation(Widget w) {
        if (w instanceof ListBoxMVP) {
            return NOT_SELECT_VALIDATION;
        } else if (w instanceof CellTable) {
            CellTable t = (CellTable) w;
            SelectionModel m = t.getSelectionModel();
            if (m instanceof SingleSelectionModel) {
                SingleSelectionModel sm = (SingleSelectionModel) m;
                if (sm == null) {
                    throw new RuntimeException("No selection model found.");
                }
                return s -> sm.getSelectedObject() == null ? REQUIRED : null;
            } else if (m instanceof MultiSelectionModel) {
                MultiSelectionModel mm = (MultiSelectionModel) m;
                if (mm == null) {
                    throw new RuntimeException("No selection model found.");
                }
                return s -> mm.getSelectedSet().size() == 0 ? REQUIRED : null;
            } else {
                throw new RuntimeException("Unknown selection model encountered.");
            }
        } else {
            return REQUIRED_FIELD_VALIDATION;
        }
    }
}
