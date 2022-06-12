package com.example.application.views.main;

import ServiceLayer.Objects.User;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.helger.commons.annotation.Nonempty;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.stream.IntStream;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

public class PaymentForm extends FormLayout {
    UserService service;
    @NotNull
    @Nonempty
    @NotBlank
    TextField creditCardNumber = createNumericTextField(16, "Credit Card Number", "Please enter a valid, 16 digit number.");
    @NotNull @Nonempty @NotBlank
    TextField cvv = createNumericTextField(3, "CVV", "Please enter a valid, 3 digit CVV.");
    Select<Integer> month = createMonthSelect();
    Select<Integer> year = createYearSelect();
    Button payButton = new Button("Pay");
    PaymentMethod method = new PaymentMethod();

    Binder<PaymentMethod> binder;

    public PaymentForm(){
        binder = new BeanValidationBinder<>(PaymentMethod.class);
        binder.bindInstanceFields(this);
        this.service = (UserService)Load("service");
        payButton.setEnabled(false);
        binder.forField(creditCardNumber).withValidator(cardNum -> cardNum.length() == 16 &&
                cardNum.chars().allMatch(Character::isDigit), "Credit Card must be a 16 digit number")
                .bind(PaymentMethod::getCreditCardNumber, PaymentMethod::setCreditCardNumber);
        binder.forField(cvv).withValidator(cvvNum -> cvvNum.length() == 3 &&
                cvvNum.chars().allMatch(Character::isDigit), "CVV must be a 3 digit number")
                .bind(PaymentMethod::getCvv, PaymentMethod::setCvv);
        binder.forField(month).withValidator(monthNum -> monthNum != null && monthNum >= 1 &&
                monthNum <= 12, "Month value must be between 1 and 12")
                .bind(PaymentMethod::getMonth, PaymentMethod::setMonth);
        binder.forField(year).withValidator(yearNum -> yearNum != null && yearNum >= Calendar.getInstance().get(Calendar.YEAR) &&
                yearNum <= Calendar.getInstance().get(Calendar.YEAR) + 10, "Year must be in the range of next 10 years")
                .bind(PaymentMethod::getYear, PaymentMethod::setYear);
        payButton.addClickListener(click -> validateAndPay());
        binder.addStatusChangeListener(evt -> payButton.setEnabled(binder.isValid()));
        binder.setBean(method);
        add(creditCardNumber, cvv, month, year, payButton);
    }

    private void validateAndPay() {
        if(binder.isValid()) {
            fireEvent(new PayEvent(this, binder.getBean(), service));
        }
    }

    private TextField createNumericTextField(int length, String label, String errorMessage) {
        TextField field = new TextField();
        field.setMinLength(length);
        field.setMaxLength(length);
        field.setPattern("\\d+");
        field.setRequired(true);
        field.setLabel(label);
        field.setErrorMessage(errorMessage);
        return field;
    }

    private Select<Integer> createMonthSelect() {
        Select<Integer> month = new Select<>();
        month.setItems(IntStream.range(1, 13).boxed().toList());
        month.setRequiredIndicatorVisible(true);
        month.setLabel("Month");
        return month;
    }

    private Select<Integer> createYearSelect() {
        Select<Integer> year = new Select<>();
        int yearNum = Calendar.getInstance().get(Calendar.YEAR);
        year.setItems(IntStream.range(yearNum, yearNum+10).boxed().toList());
        year.setRequiredIndicatorVisible(true);
        year.setLabel("Year");
        return year;
    }


    public static abstract class PaymentFormEvent extends ComponentEvent<PaymentForm> {
        private PaymentMethod method;
        public PaymentFormEvent(PaymentForm source, PaymentMethod method, UserService service) {
            super(source, false);
            this.method = method;
        }
    }

    public static class PayEvent extends PaymentFormEvent{
        public PayEvent(PaymentForm source, PaymentMethod method, UserService service) {
            super(source, method, service);
            var res = service.purchaseCartFromShop(method.getCreditCardNumber(), method.getIntCvv(), method.getMonth(), method.getYear());
            if(res.isOk()){
                createDialog();
                notifySuccess("Thank you for your purchase!");
            }
            else{
                notifyError("Payment failed!");
            }
        }

        private void createDialog(){
            Dialog dialog = new Dialog();
            dialog.setModal(true);
            VerticalLayout layout = new VerticalLayout();
            Label label = new Label("Thank you for your purchase!");
            Button closeButton = new Button("Close");
            closeButton.getElement().getStyle().set("margin-left", "auto");
            layout.add(label, closeButton);
            closeButton.addClickListener(e -> {
                dialog.close();
                UI.getCurrent().getPage().reload();
            });
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);
            dialog.add(layout);
            dialog.open();
        }
    }
}
