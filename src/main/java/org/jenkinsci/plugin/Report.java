package org.jenkinsci.plugin;

import model.Environment;
import model.PRInfo;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<Environment> environments = prsInfo.keySet().stream()
                .sorted(Comparator.comparing(Environment::getBuildDate).reversed())
                .collect(Collectors.toList());

        for(Environment env: environments){
            List<PRInfo> pullRequests = prsInfo.get(env);
            reportText.append("<h2>Environment: ").append(env.getName()).append("</h2>");
            reportText.append("<p>Build date: ").append(env.getBuildDate()).append("</p>");
            reportText.append("<p>Build version: ").append(env.getBuildVersion()).append("</p>");
            reportText.append("<ul>");
            for(PRInfo pr: pullRequests){
                reportText.append("<li>");
                reportText.append(pr.getPRTitle()).append(" -\n");
                reportText.append(pr.getAssignee()).append(" -\n");
                reportText.append(pr.getStatus());
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
