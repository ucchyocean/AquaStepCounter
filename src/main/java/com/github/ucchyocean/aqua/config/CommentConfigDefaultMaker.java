package com.github.ucchyocean.aqua.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentConfigDefaultMaker {

    public static void main(String[] args) {

        String folder = "src\\main\\resources\\configs";
        (new File(folder)).mkdirs();

        CommentConfig config = new CommentConfig(
                "CCJava", "Analyzer for general source codes ver.1.0", CommentConfigType.COMPLEX,
                makeList(".c", ".cc", ".cpp", ".rc", ".h", ".hh", ".java", ".js", ".rul"),
                "/*", "*/", "//");
        config.save(new File(folder, "ccjava.yml"));

        config = new CommentConfig(
                "Bat", "Analyzer for bat scripts ver.1.0", CommentConfigType.BAT,
                makeList(".bat"), "", "", "^[ \t]*@?[Rr][Ee][Mm](|[ \t\n\r].*)$");
        config.save(new File(folder, "bat.yml"));

        config = new CommentConfig(
                "Css", "Analyzer for css file ver.1.0", CommentConfigType.BLOCK_COMMENT,
                makeList(".css"), "/*", "*/", "");
        config.save(new File(folder, "css.yml"));

        config = new CommentConfig(
                "Html", "Analyzer for html file ver.1.0", CommentConfigType.HTML,
                makeList(".htm", ".html"), "<!--", "-->", "",
                "<(SCRIPT|STYLE)[^>]*>", "</(SCRIPT|STYLE)>", "/*", "*/", "//");
        config.save(new File(folder, "html.yml"));

        config = new CommentConfig(
                "Inf", "Analyzer for inf file ver.1.0", CommentConfigType.SHELL,
                makeList(".inf", ".idt", ".def"), "", "", ";");
        config.save(new File(folder, "inf.yml"));

        config = new CommentConfig(
                "Jsp", "Analyzer for jsp file ver.1.0", CommentConfigType.JSP,
                makeList(".jsp", ".inc"), "<%--", "--%>", "",
                "<%[@=]?", "%>", "/*", "*/", "//");
        config.save(new File(folder, "jsp.yml"));

        config = new CommentConfig(
                "Properties", "Analyzer for properties/yaml file ver.1.0", CommentConfigType.SHELL,
                makeList(".properties", ".yml", ".yaml"), "", "", "#");
        config.save(new File(folder, "properties.yml"));

        config = new CommentConfig(
                "Sh", "Analyzer for shell file ver.1.0", CommentConfigType.SHELL,
                makeList(".sh"), "", "", "#", "^#!.*$");
        config.save(new File(folder, "sh.yml"));

        config = new CommentConfig(
                "Xml", "Analyzer for xml file ver.1.0", CommentConfigType.BLOCK_COMMENT,
                makeList(".xml", ".xsd", ".xsl", ".dtd", ".tld", ".ism"), "<!--", "-->", "");
        config.save(new File(folder, "xml.yml"));


        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(new File(folder, "list.txt")))) {
            for ( String line : new String[] {
                    "bat.yml", "ccjava.yml", "css.yml", "html.yml", "inf.yml",
                    "jsp.yml", "properties.yml", "sh.yml", "xml.yml"} ) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> makeList(String ... src) {
        ArrayList<String> list = new ArrayList<String>();
        for ( String s : src ) {
            list.add(s);
        }
        return list;
    }
}
