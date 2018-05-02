package org.jenkinsci.plugin;

import model.Environment;
import model.PRInfo;

import java.io.*;
import java.util.List;
import java.util.Map;

class Report {
    private static final String beginning = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <body>\n" +
            "    <h1>Release notes report</h1>";
    private static final String end = "  </body>\n" +
            "</html>";

    static void generateReport(String path, Map<Environment, List<PRInfo>> prsInfo) throws IOException{
        StringBuilder reportText = new StringBuilder();
        reportText.append(beginning);

        for(Map.Entry<Environment, List<PRInfo>> entry: prsInfo.entrySet()){
            reportText.append("<h2>Environment: ").append(entry.getKey().getName()).append("</h2>");
            reportText.append("<p>Build date: ").append(entry.getKey().getBuildDate()).append("</p>");
            reportText.append("<p>Build version: ").append(entry.getKey().getBuildVersion()).append("</p>");
            reportText.append("<ul>");
            for(PRInfo info: entry.getValue()){
                reportText.append("<li>");
                reportText.append(info.getPRTitle()).append(" -\n");
                reportText.append(info.getAssignee()).append(" -\n");
                reportText.append(info.getStatus());
                reportText.append("</li>");
            }
            reportText.append("</ul>");
        }

        reportText.append(end);
        saveToFile(path, reportText.toString());
    }

    private static void saveToFile(String path, String output) throws IOException{
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path + "//report.html"), "utf-8"))) {
            writer.write(output);
        }
    }
}
