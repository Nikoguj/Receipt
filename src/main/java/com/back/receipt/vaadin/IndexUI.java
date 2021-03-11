package com.back.receipt.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;

@Route("")
public class IndexUI extends VerticalLayout implements AfterNavigationObserver, PageConfigurator {

    public IndexUI() {
        add(new H5("Redirection to the login page..."));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        UI.getCurrent().navigate(RoomsUI.class);
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("og:title", "Receipt Web Application");
        settings.addMetaTag("og:type", "website");
        settings.addMetaTag("og:image", "");
        settings.addMetaTag("og:url", "http://34.107.24.110:8080/login");
    }
}