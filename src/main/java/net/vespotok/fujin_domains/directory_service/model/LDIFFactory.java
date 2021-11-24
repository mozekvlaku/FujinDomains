package net.vespotok.fujin_domains.directory_service.model;

import java.util.Arrays;

public class LDIFFactory {
    private LDAPDomain workingDomain;
    private LDAPUser ldapUser;

    public LDIFFactory(LDAPDomain domain, LDAPUser ldapUser) {
        this.workingDomain = domain;
        this.ldapUser = ldapUser;
    }

    public void parseLDIF(String LDIFString) throws Exception {
        LDIFString = LDIFString.replaceAll("#.*?\\n", "");
        String[] ldifs = LDIFString.split("\\n\\n");
        for(String ldif : ldifs)
        {
            String[] ldifLines = ldif.split("\\n");
            String dnLine = ldifLines[0].split(":")[1].trim();
            String actionLine = ldifLines[1].split(":")[1].trim();

            LDAPObject object = workingDomain.getObjectByDn(dnLine);

            for(String ldifLine : ldifLines)
            {
                    String[] ldifLineNuggets = ldifLine.split(":");
                    String attribute = ldifLineNuggets[0].trim();
                    String value = ldifLineNuggets[1].trim();

                    switch (attribute)
                    {
                        case "changeType":
                            actionLine = value;
                            break; 
                        case "dn":
                            dnLine = value;
                        break;
                        default:
                            this.actionLDIFLine(attribute, value, actionLine, object);
                            break;
                    }

            }
        }
    }

    private void actionLDIFLine(String attribute, String value, String action, LDAPObject object) throws Exception {
        switch (action)
        {
            case "modify":
                workingDomain.changeObject(object,LDAPAttributeEnum.valueOf(attribute),value,ldapUser);
                break;
        }
    }
}
