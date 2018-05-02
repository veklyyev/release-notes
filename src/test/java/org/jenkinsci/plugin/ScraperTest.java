package org.jenkinsci.plugin;

import business.PRInfoScraper;
import model.Environment;
import model.PRInfo;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScraperTest {

    @Test
    public void pRInfoListIntersections(){
        PRInfo prInfo1 = new PRInfo("Some titile", "open", "Yarik");
        PRInfo prInfo2 = new PRInfo("Some titile2", "open", "Yarik");
        PRInfo prInfo3 = new PRInfo("Some titile3", "open", "Yarik");
        PRInfo prInfo4 = new PRInfo("Some titile4", "open", "Yarik");
        PRInfo prInfo5 = new PRInfo("Some titile5", "open", "Yarik");
        PRInfo prInfo6 = new PRInfo("Some titile6", "open", "Yarik");

        Environment dev = new Environment();
        dev.setName("dev");
        dev.setBuildDate(getBuildDate("201805021100"));
        Environment qa = new Environment();
        qa.setName("qa");
        qa.setBuildDate(getBuildDate("201804301100"));
        Environment prod = new Environment();
        prod.setName("prod");
        prod.setBuildDate(getBuildDate("201804011100"));

        Map<Environment, List<PRInfo>> prsInfo = new HashMap<>();
        prsInfo.put(prod, Arrays.asList(prInfo1, prInfo2));
        prsInfo.put(qa, Arrays.asList(prInfo1, prInfo2, prInfo3, prInfo4));
        prsInfo.put(dev, Arrays.asList(prInfo1, prInfo2, prInfo3, prInfo4, prInfo5, prInfo6));



        Map<Environment, List<PRInfo>> prsInfoCut = PRInfoScraper.cutData(prsInfo);

        Assert.assertTrue("Size incorrect, found " + prsInfoCut.get(dev).size() + " Expected: 2",
                prsInfoCut.get(dev).size() == 2);
    }

    private Date getBuildDate(String dateString) {
        ZoneOffset offset = ZoneOffset.UTC;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        ZonedDateTime zdt = ZonedDateTime.of(dateTime, offset);
        return Date.from(zdt.toInstant());
    }
}
