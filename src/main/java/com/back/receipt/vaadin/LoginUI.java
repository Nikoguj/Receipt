package com.back.receipt.vaadin;

import com.back.receipt.domain.dto.UserRegisterDto;
import com.back.receipt.security.controller.UserControllerSecurity;
import com.back.receipt.security.repository.UserRepository;
import com.back.receipt.security.service.EmailOrUsernameExistsException;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("login")
@PageTitle("Login and Register")
public class LoginUI extends VerticalLayout implements PageConfigurator {

    @Autowired
    private UserControllerSecurity userControllerSecurity;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    private Label label;
    private TextField userNameTextField;
    private PasswordField passwordField;

    private TextField userNameField;
    private PasswordField registerPasswordField;
    private PasswordField passwordConfirmField;
    private EmailField emailField;

    public LoginUI() {
        SplitLayout horizontalLayout = new SplitLayout();
        horizontalLayout.getStyle().set("border-style", "solid");
        horizontalLayout.getStyle().set("border-color", "#f3f5f7");
        horizontalLayout.getStyle().set("border-width", "8px");
        horizontalLayout.getStyle().set("border-radius", "8px 8px 8px 8px");
        horizontalLayout.setWidth("40%");
        horizontalLayout.setHeight("60%");
        horizontalLayout.addToPrimary(loginView());
        horizontalLayout.addToSecondary(registerView());

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        add(horizontalLayout);
    }

    public VerticalLayout loginView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);

        H5 loginH5 = new H5("Sign In");

        label = new Label("Please login...");

        userNameTextField = new TextField();
        userNameTextField.setLabel("Username");
        userNameTextField.setPlaceholder("Username");
        userNameTextField.focus();


        passwordField = new PasswordField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("Password");
        passwordField.addKeyDownListener(Key.ENTER, (ComponentEventListener<KeyDownEvent>) keyDownEvent -> authenticateAndNavigate());

        Button submitButton = new Button("LOGIN");
        submitButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            authenticateAndNavigate();
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(label, userNameTextField, passwordField, submitButton);

        verticalLayout.add(loginH5, formLayout);

        return verticalLayout;
    }

    private void authenticateAndNavigate() {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(userNameTextField.getValue(), passwordField.getValue());
        try {
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

            String requestedURI = "/rooms";

            this.getUI().ifPresent(ui -> ui.navigate(StringUtils.removeStart(requestedURI, "/")));
        } catch (BadCredentialsException e) {
            label.getStyle().set("color", "red");
            label.setText("Invalid username or password. Please try again.");
        }
    }

    public VerticalLayout registerView() {
        VerticalLayout registerVerticalLayout = new VerticalLayout();
        registerVerticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        registerVerticalLayout.setAlignItems(Alignment.CENTER);

        H5 registerH5 = new H5("Sign Up");

        Binder<UserRegisterDto> binder = new Binder<>(UserRegisterDto.class);
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        binder.setBean(userRegisterDto);

        userNameField = new TextField();
        userNameField.setPlaceholder("Username");
        setupUsernameField(binder, userNameField, userRegisterDto);

        registerPasswordField = new PasswordField();
        setupPasswordField(binder, registerPasswordField);

        passwordConfirmField = new PasswordField();
        setupPasswordConfirmField(binder, userRegisterDto, passwordConfirmField);

        emailField = new EmailField("Email");
        emailField.setPlaceholder("Email");
        setupEmailField(binder, emailField);

        Button registerButton = new Button("REGISTER");

        HorizontalLayout correctRegistrationLayout = new HorizontalLayout();
        correctRegistrationLayout.setAlignItems(Alignment.CENTER);
        correctRegistrationLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        Icon icon = new Icon(VaadinIcon.CHECK);
        icon.setColor("green");

        Label correctRegistrationLabel = new Label("Successfully registered, you can log in");
        correctRegistrationLayout.add(icon, correctRegistrationLabel);

        correctRegistrationLayout.setVisible(false);

        registerButton.addClickListener(event -> {
            correctRegistrationLayout.setVisible(false);
            if (binder.writeBeanIfValid(userRegisterDto)) {
                try {
                    userControllerSecurity.registerUser(userRegisterDto);

                    userNameField.setReadOnly(true);
                    registerPasswordField.setReadOnly(true);
                    passwordConfirmField.setReadOnly(true);
                    emailField.setReadOnly(true);

                    correctRegistrationLayout.setVisible(true);
                } catch (EmailOrUsernameExistsException e) {
                    e.printStackTrace();
                }
            } else {
                BinderValidationStatus<UserRegisterDto> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                Notification notification = new Notification(errorText, 2000, Notification.Position.MIDDLE);
                notification.open();
            }
        });

        FormLayout registerFormLayout = new FormLayout();
        registerFormLayout.add(userNameField, registerPasswordField, passwordConfirmField, emailField, registerButton, correctRegistrationLayout);

        registerVerticalLayout.add(registerH5, registerFormLayout);
        return registerVerticalLayout;
    }

    private void setupEmailField(Binder<UserRegisterDto> binder, EmailField emailField) {
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");

        binder.forField(emailField)
                .withValidator(new StringLengthValidator(
                        "Please add the email", 1, null
                ))
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(UserRegisterDto::getEmail, UserRegisterDto::setEmail);
    }

    private void setupPasswordConfirmField(Binder<UserRegisterDto> binder, UserRegisterDto userRegisterDto, PasswordField passwordConfirmField) {
        passwordConfirmField.setLabel("Confirm Password");
        passwordConfirmField.setPlaceholder("Confirm your password");

        Binder.Binding<UserRegisterDto, String> confirmPasswordBinder = binder.forField(passwordConfirmField)
                .withValidator(value -> value.equals(userRegisterDto.getPassword()), "Passwords are not matching")
                .bind(UserRegisterDto::getPasswordConfirm, UserRegisterDto::setPasswordConfirm);

        passwordConfirmField.addValueChangeListener(event -> confirmPasswordBinder.validate());
        registerPasswordField.addValueChangeListener(event -> confirmPasswordBinder.validate());
    }

    private void setupPasswordField(Binder<UserRegisterDto> binder, PasswordField passwordField) {
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("Enter password");

        Binder.Binding<UserRegisterDto, String> passwordBinder = binder.forField(passwordField)
                .withValidator(value -> value.matches(".*[a-z].*"), "Should be at least one letter")
                .withValidator(value -> !(value.length() < 5), "Should be longer than 5")
                .withValidator(value -> value.matches(".*\\d.*"), "Should be at least one number")
                .bind(UserRegisterDto::getPassword, UserRegisterDto::setPassword);

        passwordField.addValueChangeListener(event -> passwordBinder.validate());
    }

    private void setupUsernameField(Binder<UserRegisterDto> binder, TextField userNameField, UserRegisterDto userRegisterDto) {
        userNameField.setLabel("Username");

        Binder.Binding<UserRegisterDto, String> usernameBinder = binder.forField(userNameField)
                .withValidator(value -> !(value.length() < 5), "Should be longer than 5")
                .withValidator(value -> !(userRepository.findByUserName(value).isPresent()), "Username is taken")
                .bind(UserRegisterDto::getUserName, UserRegisterDto::setUserName);

        userNameField.addValueChangeListener(event -> usernameBinder.validate());
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("og:title", "Receipt Web Application");
        settings.addMetaTag("og:type", "website");
        settings.addMetaTag("og:image", "");
        settings.addMetaTag("og:url", "http://34.107.24.110:8080/login");

    }
}
