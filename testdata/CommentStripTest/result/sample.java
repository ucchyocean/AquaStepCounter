





package com.github.ucchyocean.aqua;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.github.ucchyocean.aqua.config.CommentConfigManager;
import com.github.ucchyocean.aqua.ui.MainWindow;





public class AquaStepCounter {

    private static final AquaStepCounter instance = new AquaStepCounter();

    private CommentConfigManager manager;
    private AquaStepCounterConfig config;

    


    public AquaStepCounter() {

        
        config = AquaStepCounterConfig.load();

        
        manager = CommentConfigManager.loadFromDefaultFiles();
    }

    



    public static AquaStepCounter getInstance() {
        return instance;
    }

    



    public static AquaStepCounterConfig getConfig() {
        return instance.config;
    }

    



    public static CommentConfigManager getCommentConfigManager() {
        return instance.manager;
    }

    



    public static void main(String[] args) {

        CommandLineParser clp = new CommandLineParser(args);

        if ( clp.hasParam(CommandLineParser.KEY_HELP) ) {
            
            System.out.println(Messages.SOFTWARE_NAME + " - v" + Messages.SOFTWARE_VERSION);
            System.out.println();
            System.out.println("Usage: AquaStepCounter.exe "
                    + "[-?] [-ui] [-new (folder path)] [-old (folder path)] [-r (report file path)]");
            System.out.println();
            return;
        }

        
        boolean showUI = clp.size() == 0
                || clp.hasParam(CommandLineParser.KEY_UI)
                || (!clp.hasParam(CommandLineParser.KEY_NEW) && !clp.hasParam(CommandLineParser.KEY_OLD));

        
        boolean configModified = false;
        if ( clp.hasParam(CommandLineParser.KEY_DC) ) {
            getConfig().setStripComment(true);
            configModified = true;
        }
        if ( clp.hasParam(CommandLineParser.KEY_CC) ) {
            getConfig().setStripComment(false);
            configModified = true;
        }
        if ( clp.hasParam(CommandLineParser.KEY_DW) ) {
            getConfig().setStripWhite(true);
            configModified = true;
        }
        if ( clp.hasParam(CommandLineParser.KEY_CW) ) {
            getConfig().setStripWhite(false);
            configModified = true;
        }
        if ( showUI && configModified ) {
            
            getConfig().save();
        }

        
        if ( showUI ) {
            

            MainWindow window = new MainWindow();
            boolean runThread = false;
            if ( clp.hasParam(CommandLineParser.KEY_NEW) ) {
                window.setNewFolder(clp.get(CommandLineParser.KEY_NEW));
                runThread = true;
            }
            if ( clp.hasParam(CommandLineParser.KEY_OLD) ) {
                window.setOldFolder(clp.get(CommandLineParser.KEY_OLD));
                runThread = true;
            }
            if ( runThread ) window.runCompare();

            window.open();

        } else {
            

            String oldFolder = clp.get(CommandLineParser.KEY_OLD);
            String newFolder = clp.get(CommandLineParser.KEY_NEW);
            FolderDiffer differ = new FolderDiffer(oldFolder, newFolder,
                    getConfig().isStripWhite(), getConfig().isStripComment());
            FolderDifferResult result = differ.getDiffData();
            String[] exportData = result.getExportData();

            if ( clp.hasParam(CommandLineParser.KEY_REPORT) ) {
                
                String report = clp.get(CommandLineParser.KEY_REPORT);

                try (BufferedWriter writer = new BufferedWriter( new FileWriter(report) )) {
                    for ( int i=0; i<exportData.length; i++ ) {
                        writer.write(exportData[i]);
                        writer.newLine();
                    }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();

                    
                    System.exit(1);
                }

            } else {
                
                for ( String line : exportData ) {
                    System.out.println(line);
                }
            }
        }
    }
}
