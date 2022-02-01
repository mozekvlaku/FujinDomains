package net.vespotok.fujin_domains.credential_provider;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Credential {
    private String credentialUUID;
    private UserObject userObject;
    private Date expires;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Credential(UserObject userObject)
    {
        this.userObject = userObject;
        UUID uuid = UUID.randomUUID();
        this.credentialUUID = uuid.toString();

        expires = addHoursToJavaUtilDate(new Date(), 24);
        Logging l = new Logging(LoggingLevel.print, userObject.getDomainName(),"Credential Provider");
        l.log("Created new credential " + this.credentialUUID + " expiring in " + formatter.format(expires));
    }

    public String getUUID() {
        return credentialUUID;
    }

    public UserObject getUser() {
        return userObject;
    }

    public Date getExpires()
    {
        return expires;
    }

    public void renew()
    {
        expires = addHoursToJavaUtilDate(new Date(), 24);
    }

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}
