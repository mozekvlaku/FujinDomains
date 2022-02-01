package net.vespotok.fujin_domains.directory_service.helpers;

import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {
    private String category;
    private LoggingLevel level;
    private LDAPDomainName domainName;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public Logging(LoggingLevel level)
    {
        this.level = level;
        this.category = "";
    }

    public void setDomainName(LDAPDomainName domainName) {
        this.domainName = domainName;
    }

    public Logging(LoggingLevel level, LDAPDomainName domainName, String category)
    {
        this.level = level;
        this.domainName = domainName;
        this.category = category + " ";
    }
    public void log(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        String prepend = ANSI_GREEN+"Fujin Domains "+ANSI_RESET+"[";
        prepend += formatter.format(date);
        if (this.domainName != null)
        {
            prepend += " " + ANSI_BLUE+domainName.toNT4Style()+ANSI_RESET;
        }
        prepend += "] " + ANSI_CYAN + category + ANSI_RESET;

        switch (level)
        {
            case print:
                System.out.println(prepend + message);
                break;
            case silent:
                break;
            case writeAndPrint:
                //todo
            break;
        }
    }
    public void error(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        String prepend = ANSI_GREEN+"Fujin Domains "+ANSI_RESET+"[";
        prepend += formatter.format(date);
        if (this.domainName != null)
        {
            prepend += " " + ANSI_BLUE+domainName.toNT4Style()+ANSI_RESET;
        }
        prepend += "] "+ ANSI_CYAN + category  + ANSI_RESET;

        switch (level)
        {
            case print:
                System.out.println(prepend + ANSI_RED +  message + ANSI_RESET);
                break;
            case silent:
                break;
            case writeAndPrint:
                //todo
                break;
        }
    }
    public void success(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        String prepend = ANSI_GREEN+"Fujin Domains "+ANSI_RESET+"[";
        prepend += formatter.format(date);
        if (this.domainName != null)
        {
            prepend += " " + ANSI_BLUE+domainName.toNT4Style()+ANSI_RESET;
        }
        prepend += "] "+ ANSI_CYAN + category  + ANSI_RESET;

        switch (level)
        {
            case print:
                System.out.println(prepend + ANSI_GREEN + message + ANSI_RESET);
                break;
            case silent:
                break;
            case writeAndPrint:
                //todo
                break;
        }
    }
}
