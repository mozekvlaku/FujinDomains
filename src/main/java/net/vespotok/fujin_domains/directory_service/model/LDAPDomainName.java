package net.vespotok.fujin_domains.directory_service.model;

public class LDAPDomainName {
    private String NT4Name;
    private String NT5Name;
    private String DN;

    private String defaultTld = "local";

    public LDAPDomainName(String name, LDAPDomainNameTypeEnum domainNameType)
    {
        switch(domainNameType)
        {
            case DN:
                createFromDN(name);
                break;
            case NT4Style:
                createFromNT4Name(name);
                break;
            case Win2000Style:
                createFromNT5Name(name);
                break;
        }
    }

    private void createFromNT4Name(String name)
    {
        this.NT4Name = name;
        this.NT5Name = name.toLowerCase() + "." + this.defaultTld;
        this.DN = "dc="+name.toLowerCase()+",dc="+this.defaultTld;
    }

    private void createFromNT5Name(String name)
    {
        String[] levelNameArray = name.split("\\.");
        String baseLevelName = levelNameArray[(levelNameArray.length-2)];

        this.NT4Name = baseLevelName.toUpperCase();
        this.NT5Name = name;
        this.DN = "dc="+String.join(",dc=", levelNameArray);
    }

    private void createFromDN(String name)
    {
        String[] levelNameArray = name.split(",dc=");
        //remove first dc=
        levelNameArray[0] = levelNameArray[0].substring(3);
        String baseLevelName = levelNameArray[(levelNameArray.length-2)];
        this.NT4Name = baseLevelName.toUpperCase();
        this.NT5Name = String.join(".", levelNameArray);
        this.DN = name;
    }

    public String toDN()
    {
        return this.DN;
    }

    public String toWin2000Style()
    {
        return this.NT5Name;
    }
    public String toNT4Style()
    {
        return this.NT4Name;
    }
}