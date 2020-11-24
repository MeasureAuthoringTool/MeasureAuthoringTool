package mat.client.validator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
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

    private final Map<Widget, ValidationInfo> validations;

    public ErrorHandler() {
        validations = new HashMap<>();
    }

    public Set<Widget> getWidgets() {
        return validations.keySet();
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        validations.keySet().forEach(v -> errors.addAll(validate(v)));
        return errors;
    }

    public ValidationInfo getValidations(Widget b) {
        ValidationInfo result = validations.get(b);
        if (result == null) {
            result = new ValidationInfo();
            validations.put(b, result);
        }
        return result;
    }

    public BlurHandler buildRequiredBlurHandler(ListBoxMVP b) {
        return buildBlurHandler(b, b.getElement(), NOT_SELECT_VALIDATION);
    }

    public BlurHandler buildBlurHandler(ListBoxMVP b, Validation v) {
        return buildBlurHandler(b, b.getElement(), v);
    }

    public BlurHandler buildRequiredBlurHandler(ListBoxMVP b, Element addErrorsAfter, Validation v) {
        return buildBlurHandler(b, b.getElement(), NOT_SELECT_VALIDATION);
    }

    public BlurHandler buildBlurHandler(ListBoxMVP b, Element addErrorsAfter, Validation v) {
        ValidationInfo validationInfo = getValidations(b);
        validationInfo.getValidations().add(v);
        validationInfo.setFieldToAddErrorMessagesAfter(addErrorsAfter);
        return (blurEvent) -> validate(b);
    }

    public BlurHandler buildRequiredBlurHandler(ValueBoxBase b) {
        return buildBlurHandler(b, b.getElement(), REQUIRED_FIELD_VALIDATION);
    }

    public BlurHandler buildBlurHandler(ValueBoxBase b, Validation v) {
        return buildBlurHandler(b, b.getElement(), v);
    }

    public BlurHandler buildBlurHandler(ValueBoxBase b, Element addErrorsAfter, Validation v) {
        ValidationInfo validationInfo = getValidations(b);
        validationInfo.setFieldToAddErrorMessagesAfter(addErrorsAfter);
        validationInfo.getValidations().add(v);
        return (blurEvent) -> validate(b);
    }

    public void setError(ValueBoxBase valueBox, Element addErrorsAfter, String error) {
        handleErrors(valueBox, addErrorsAfter, Collections.singletonList(error));
    }

    public void setError(ValueBoxBase valueBox, String error) {
        setError(valueBox, valueBox.getElement(), error);
    }

    public void setErrors(ValueBoxBase valueBox, String... errors) {
        handleErrors(valueBox, valueBox.getElement(), Arrays.asList(errors));
    }

    public void setErrors(ValueBoxBase valueBox, List<String> errors) {
        handleErrors(valueBox, valueBox.getElement(), errors);
    }

    public BlurHandler buildRequiredBlurHandler(TextBox b) {
        return buildBlurHandler(b, b.getElement(), REQUIRED_FIELD_VALIDATION);
    }

    public BlurHandler buildBlurHandler(TextBox b, Validation v) {
        return buildBlurHandler(b, b.getElement(), v);
    }

    public BlurHandler buildBlurHandler(TextBox b, Element addErrorsAfter, Validation v) {
        ValidationInfo validationInfo = getValidations(b);
        validationInfo.setFieldToAddErrorMessagesAfter(addErrorsAfter);
        validationInfo.getValidations().add(v);
        return (blurEvent) -> validate(b);
    }

    public void setError(TextBox valueBox, Element addErrorsAfter, String error) {
        handleErrors(valueBox, addErrorsAfter, Collections.singletonList(error));
    }

    public void setError(TextBox valueBox, String error) {
        setError(valueBox, valueBox.getElement(), error);
    }

    public void setErrors(TextBox valueBox, String... errors) {
        handleErrors(valueBox, valueBox.getElement(), Arrays.asList(errors));
    }

    public void setErrors(TextBox valueBox, List<String> errors) {
        handleErrors(valueBox, valueBox.getElement(), errors);
    }


    public void clearErrors() {
        validations.keySet().forEach((k) -> {
            ValidationInfo i = validations.get(k);
            clearErrors(k, i.getFieldToAddErrorMessagesAfter());
        });
    }

    public void clearErrors(Widget validationWidget, Element elementToAddErrorsAfter) {
        validationWidget.getElement().getStyle().setBorderColor("");
        while (isErrorMessage(elementToAddErrorsAfter.getNextSiblingElement())) {
            elementToAddErrorsAfter.getNextSiblingElement().removeFromParent();
        }
    }

    public void handleErrors(Widget validationWidget, Element elementToAddErrorsAfter, List<String> errors) {
        clearErrors(validationWidget, elementToAddErrorsAfter);
        if (errors != null && errors.size() > 0) {
            logger.info("errors=" + errors);
            validationWidget.getElement().getStyle().setBorderColor("red");
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

    private Object getValue(Widget widget) {
        if (widget instanceof ValueBoxBase) {
            return ((ValueBoxBase) widget).getValue();
        } else if (widget instanceof ListBoxMVP) {
            return ((ListBoxMVP) widget).getValue();
        } else {
            throw new IllegalArgumentException("Unsupported widget type: " + widget.getClass());
        }
    }

    private List<String> validate(Widget widget) {
        List<String> errors = new ArrayList<>();
        ValidationInfo result = validations.get(widget);
        if (result.getValidations() != null) {
            String value = getValue(widget).toString();
            result.getValidations().forEach(v -> {
                String errorMsg = v.validate(value);
                if (errorMsg != null) {
                    errors.add(errorMsg);
                }
            });

            if (!errors.isEmpty()) {
                handleErrors(widget, result.getFieldToAddErrorMessagesAfter(), errors);
            } else {
                clearErrors(widget, result.getFieldToAddErrorMessagesAfter());
            }
        }
        return errors;
    }
}
