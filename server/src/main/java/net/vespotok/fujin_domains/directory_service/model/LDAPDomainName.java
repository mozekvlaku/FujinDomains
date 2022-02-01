package net.vespotok.fujin_domains.directory_service.model;

import javax.persistence.*;

@Entity
public class LDAPDomainName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NT4Name")
    private String NT4Name;
    @Column(name = "NT5Name")
    private String NT5Name;
    @Column(name = "DN")
    private String DN;


    private String defaultTld = "local";

    public LDAPDomainName() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getNT4Name() {
        return NT4Name;
    }

    public void setNT4Name(String NT4Name) {
        this.NT4Name = NT4Name;
        createFromNT4Name(NT4Name);
    }

    public String getNT5Name() {
        return NT5Name;
    }

    public void setNT5Name(String NT5Name) {
        this.NT5Name = NT5Name;
        createFromNT5Name(NT5Name);
    }

    public String getDN() {
        return DN;
    }

    public void setDN(String DN) {
        this.DN = DN;
        createFromDN(DN);
    }
}