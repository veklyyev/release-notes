package org.jenkinsci.plugin;

import model.Environment;
import model.PRInfo;

import java.io.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

class Report {

    private static void saveToFile(String path, String output) throws IOException{
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path + "//report.html"), "utf-8"))) {
            writer.write(output);
        }
    }

    static void generateReport(String path, Map<Environment, List<PRInfo>> prsInfo) throws IOException{
        List<Environment> environments = prsInfo.keySet().stream()
                .sorted(Comparator.comparing(Environment::getBuildDate).reversed())
                .collect(Collectors.toList());

        String reportText = html(
                head(
                        title("Report")
                ),
                body(
                     h1("Release notes report"),
                        h5("Report generation date: " + new Date().toString()),
                        each(environments, environment ->
                                div(h2("Environment: " + environment.getName()),
                                        p("Build date " + environment.getBuildDate().toString()),
                                        p("Build version " + environment.getBuildVersion()),
                                        ul(
                                        each(prsInfo.get(environment), pr ->
                                                li(pr.getPRTitle() + " " + pr.getAssignee() + " " + pr.getStatus())
                                        ))
                                )
                        )
                )
        ).render();
        saveToFile(path, reportText);
    }
}
