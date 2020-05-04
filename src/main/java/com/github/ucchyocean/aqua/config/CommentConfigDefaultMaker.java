package com.github.ucchyocean.aqua.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommentConfigDefaultMaker {

    public static void main(String[] args) {

        String folder = "C:\\Users\\ucchy\\git\\AquaStepCounter\\AquaStepCounter\\src\\main\\resources\\config";

        CommentConfig config = new CommentConfig(
                "CCJava", "Analyzer for general source codes ver.1.0", CommentConfigType.COMPLEX,
                makeList(".c", ".cc", ".cpp", ".rc", ".h", ".hh", ".java", ".js", ".rul"),
                "/*", "*/", "//");
        config.save(new File(folder, "ccjava.properties"));

        config = new CommentConfig(
                "Bat", "Analyzer for bat scripts ver.1.0", CommentConfigType.SHELL,
                makeList(".bat"), "", "", "^@?[Rr][Ee][Mm](|[ \t\n\r].*)$");
        config.save(new File(folder, "bat.properties"));

        config = new CommentConfig(
                "Css", "Analyzer for css file ver.1.0", CommentConfigType.BLOCK_COMMENT,
                makeList(".css"), "/*", "*/", "");
        config.save(new File(folder, "css.properties"));

        config = new CommentConfig(
                "Html", "Analyzer for html file ver.1.0", CommentConfigType.HTML,
                makeList(".htm", ".html"), "<!--", "-->", "",
                "<(SCRIPT|STYLE)[^>]*>", "</(SCRIPT|STYLE)>", "/*", "*/", "//");
        config.save(new File(folder, "html.properties"));

        config = new CommentConfig(
                "Inf", "Analyzer for inf file ver.1.0", CommentConfigType.SHELL,
                makeList(".inf", ".idt", ".def"), "", "", ";.*$");
        config.save(new File(folder, "inf.properties"));

        config = new CommentConfig(
                "Jsp", "Analyzer for jsp file ver.1.0", CommentConfigType.JSP,
                makeList(".jsp", ".inc"), "<%--", "--%>", "",
                "<%[@=]?", "%>", "/*", "*/", "//");
        config.save(new File(folder, "jsp.properties"));

        config = new CommentConfig(
                "Properties", "Analyzer for properties/yaml file ver.1.0", CommentConfigType.SHELL,
                makeList(".properties", ".yml", ".yaml"), "", "", "^[ \t]*#.*$");
        config.save(new File(folder, "properties.properties"));

        config = new CommentConfig(
                "Sh", "Analyzer for shell file ver.1.0", CommentConfigType.SHELL,
                makeList(".sh"), "", "", "^[ \t]*#.*$", "^#!.*$");
        config.save(new File(folder, "sh.properties"));

        config = new CommentConfig(
                "Xml", "Analyzer for xml file ver.1.0", CommentConfigType.BLOCK_COMMENT,
                makeList(".xml", ".xsd", ".xsl", ".dtd", ".tld"), "<!--", "-->", "");
        config.save(new File(folder, "xml.properties"));
    }

    private static List<String> makeList(String ... src) {
        ArrayList<String> list = new ArrayList<String>();
        for ( String s : src ) {
            list.add(s);
        }
        return list;
    }
}
