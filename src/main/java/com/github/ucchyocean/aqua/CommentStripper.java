/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.aqua;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.ucchyocean.aqua.config.CommentConfig;
import com.github.ucchyocean.aqua.config.CommentConfigType;

/**
 * コメント除去ユーティリティクラス
 * @author ucchy
 */
public class CommentStripper {

    /**
     * コメントを除去する。
     * @param input 変換前のドキュメント
     * @param type コメント除去に使用するコメント解析設定
     * @return 変換後のドキュメント
     * @throws IOException コメント解析に失敗した場合
     */
    public static String deleteComments(String input, CommentConfig type) throws IOException {

        if ( type.getType() == CommentConfigType.BLOCK_COMMENT ) {
            return deleteBlockComment(input,
                    type.getBlockCommentStartSimbol(), type.getBlockCommentEndSimbol());
        } else if ( type.getType() == CommentConfigType.LINE_COMMENT ) {
            return deleteLineComment(input, type.getLineCommentSimbol());
        } else if ( type.getType() == CommentConfigType.SHELL ) {
            return deleteShellLineComment(input, type.getLineCommentSimbol(),
                    type.getExtraSimbol());
        } else if ( type.getType() == CommentConfigType.BAT ) {
            return deleteRegexLineComment(input, type.getLineCommentSimbol());
        } else if ( type.getType() == CommentConfigType.HTML ) {
            return deleteHtmlComments(input, type.getBlockCommentStartSimbol(),
                    type.getBlockCommentEndSimbol(), type.getExtraSimbol());
        } else if ( type.getType() == CommentConfigType.JSP ) {
            return deleteJspComments(input, type.getBlockCommentStartSimbol(),
                    type.getBlockCommentEndSimbol(), type.getExtraSimbol());
        } else {
            return deleteComplexComment(input, type.getBlockCommentStartSimbol(),
                    type.getBlockCommentEndSimbol(), type.getLineCommentSimbol());
        }
    }

    /**
     * ブロックコメントと行コメントが複合した形式のドキュメントから、コメントを解析して除去する。
     * @param input 変換前のドキュメント
     * @param blockStart ブロックコメントの開始シンボル
     * @param blockEnd ブロックコメントの終了シンボル
     * @param lineStart 行コメントのシンボル
     * @return 変換後のドキュメント
     * @throws IOException コメント解析に失敗した場合
     */
    private static String deleteComplexComment(String input, String blockStart,
            String blockEnd, String lineStart) throws IOException {

        StringBuffer content = new StringBuffer(input);

        while ( true ) {

            int bsIndex = content.indexOf(blockStart);
            int lIndex = content.indexOf(lineStart);

            if ( ( bsIndex != -1 && lIndex != -1 && bsIndex < lIndex )
                    || ( bsIndex != -1 && lIndex == -1 ) ) {
                // 両方見つかったがブロック型コメントのほうが先に現れた場合か、
                // ブロック型コメントのみ見つかった場合
                if ( content.indexOf(blockEnd, bsIndex) == -1 ) {
                    // 文字列中にブロック開始記号が入ってしまっていた場合
                    throw new IOException("Complex comment type anaysis error.");
                }
                int beIndex = content.indexOf(blockEnd, bsIndex) + blockEnd.length();

                // ブロックコメント部分の改行コードの個数を取得し、、ブロックコメントを消去して、代わりに改行コードをもとあった個数分挿入する
                int lfcount = countLFCodes(content.substring(bsIndex, beIndex));
                content.replace(bsIndex, beIndex, "");
                for ( int i=0; i<lfcount; i++ ) content.insert(bsIndex, "\n");

            } else if ( ( bsIndex != -1 && lIndex != -1 && bsIndex >= lIndex )
                    || ( bsIndex == -1 && lIndex != -1 ) ) {
                // 両方見つかったがライン型コメントのほうが先に現れた場合か、
                // ライン型コメントのみ見つかった場合
                int end = content.indexOf("\n", lIndex) + 1;
                if ( end == -1 ) {
                    end = content.length() - 1; // ファイルの最後にセット
                }
                content.replace(lIndex, end, "\n");

            } else {
                // コメントは削除し尽くした
                return content.toString();
            }
        }
    }

    /**
     * ブロックコメントを除去する。
     * @param input 変換前のドキュメント
     * @param blockStart ブロックコメントの開始シンボル
     * @param blockEnd ブロックコメントの終了シンボル
     * @return 変換後のドキュメント
     * @throws IOException コメント解析に失敗した場合
     */
    private static String deleteBlockComment(String input, String blockStart, String blockEnd) throws IOException {

        StringBuffer content = new StringBuffer(input);

        while ( content.indexOf(blockStart) != -1 ) {
            int start = content.indexOf(blockStart);
            if ( content.indexOf(blockEnd, start) == -1 ) {
                // 文字列中にブロック開始記号が入ってしまっていた場合など
                throw new IOException("Block comment type anaysis error.");
            }
            int end = content.indexOf(blockEnd, start) + blockEnd.length();

            // ブロックコメント部分の改行コードの個数を取得し、、ブロックコメントを消去して、代わりに改行コードをもとあった個数分挿入する
            int lfcount = countLFCodes(content.substring(start, end));
            content.replace(start, end, "");
            for ( int i=0; i<lfcount; i++ ) content.insert(start, "\n");
        }

        return content.toString();
    }

    /**
     * 行コメントを除去する。
     * @param input 変換前のドキュメント
     * @param lineStart 行コメントのシンボル
     * @return 変換後のドキュメント
     */
    private static String deleteLineComment(String input, String lineStart) {

        StringBuffer content = new StringBuffer(input);

        while ( content.indexOf(lineStart) != -1 ) {
            int start = content.indexOf(lineStart);
            int end = content.indexOf("\n", start);
            if ( end == -1 ) {
                end = content.length() - 1; // ファイルの最後にセット
            }
            content.replace(start, end, "");
        }

        return content.toString();
    }

    /**
     * Shell形式の行コメントを除去する。
     * @param input 変換前のドキュメント
     * @param lineStart 行コメントのシンボル
     * @param shellFlag ドキュメントの先頭に現れるコメント行（例外として削除を行わない）
     * @return 変換後のドキュメント
     */
    private static String deleteShellLineComment(String input, String lineStart, String[] shellFlag) {

        // 行コメントを除去する
        String content = deleteLineComment(input, lineStart);

        if ( shellFlag != null && shellFlag.length >= 1 && !shellFlag[0].isEmpty() ) {
            // 1行目を取得する
            String firstLine = "";
            int firstLineEnd = input.indexOf("\n");
            if ( firstLineEnd >= 0 ) {
                firstLine = input.substring(0, firstLineEnd);
            }

            // 1行目がshellFlagに一致していたなら、contentの先頭に足して戻す
            if ( firstLine.matches(shellFlag[0]) ) {
                content = firstLine + content;
            }
        }

        return content;
    }

    /**
     * 行コメントを除去する。<br/>
     * このタイプのコメントは、必ず行頭からコメントが始まる（行の途中からコメントが始まることは無い）。そのため、行コメントに一致する行は、行ごと除去する。
     * @param input 変換前のドキュメント
     * @param regex 行コメントのシンボル（正規表現）
     * @return 変換後のドキュメント
     */
    private static String deleteRegexLineComment(String input, String regex) {

        String[] lines = input.split("\n");
        StringBuffer buf = new StringBuffer();

        if ( lines.length == 0 ) {
            return input;
        }

        for ( String line : lines ) {
            if ( !line.matches(regex) ) {
                buf.append(line);
            }
            buf.append("\n");
        }

        return buf.toString();
    }

    /**
     * JSP形式のコメントを除去する。<br/>
     * このタイプのコメントは、まずJSPのコメントを除去し、次にHTMLのコメントを除去して、最後にJavaコード部分をJavaとしてコメント除去する。
     * @param input 変換前のドキュメント
     * @param blockStart
     * @param blockEnd
     * @param extra
     * @return 変換後のドキュメント
     * @throws IOException
     */
    private static String deleteJspComments(String input, String blockStart, String blockEnd,
            String[] extra) throws IOException {

        if ( input.length() == 0 ) {
            return input;
        }

        String scriptStartRegex = extra[0];
        String scriptEndRegex = extra[1];
        String scriptBlockStartSimbol = extra[2];
        String scriptBlockEndSimbol = extra[3];
        String scriptLineSimbol = extra[4];

        // JSPのコメント除去
        String temp2 = deleteBlockComment(input, blockStart, blockEnd);

        // HTMLのコメント除去
        String temp = deleteComments(temp2, AquaStepCounter.getCommentConfigManager().getConfig(".html"));

        // Javaのコメント除去
        StringBuffer output = new StringBuffer(temp);
        int contentEnd = temp.length() - 1;

        Pattern patternStart = Pattern.compile(scriptStartRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcherStart = patternStart.matcher(temp);

        Pattern patternEnd = Pattern.compile(scriptEndRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcherEnd = patternEnd.matcher(temp);

        while ( matcherStart.find() ) {
            int innerStart = matcherStart.end() + 1;
            matcherEnd.region(innerStart, contentEnd);
            if ( !matcherEnd.find() ) {
                // 文字列中にブロック開始記号が入ってしまっていた場合
                throw new IOException("JSP comment type anaysis error.");
            }
            int innerEnd = matcherEnd.start();
            String innerOrg = temp.substring(innerStart, innerEnd);
            String innerTemp = deleteBlockComment(innerOrg,
                    scriptBlockStartSimbol, scriptBlockEndSimbol);
            String innerDeleted = deleteLineComment(innerTemp, scriptLineSimbol);

            int replaceStart = output.indexOf(innerOrg);
            if ( replaceStart < 0 ) {
                throw new IOException("Internal error. JSP comment parse failed.");
                // このエラーはまず起きないはず。
            }
            int replaceEnd = replaceStart + innerOrg.length();
            output.replace(replaceStart, replaceEnd, innerDeleted);
        }

        return output.toString();
    }

    /**
     * HTML形式のコメントを除去する。<br/>
     * このタイプのコメントは、HTMLのブロックコメントと、scriptタグ/styleタグ内のJavaScriptコメントが、複合して現れる。
     * @param input 変換前のドキュメント
     * @param blockStart
     * @param blockEnd
     * @param extra
     * @return 変換後のドキュメント
     * @throws IOException
     */
    private static String deleteHtmlComments(String input, String blockStart, String blockEnd,
            String[] extra) throws IOException {

        StringBuffer output = new StringBuffer();
        int index = 0;

        String scriptStartRegex = extra[0];
        String scriptEndRegex = extra[1];
        String scriptBlockStartSimbol = extra[2];
        String scriptBlockEndSimbol = extra[3];
        String scriptLineSimbol = extra[4];

        Pattern patternStart = Pattern.compile(scriptStartRegex, Pattern.CASE_INSENSITIVE);
        Pattern patternEnd = Pattern.compile(scriptEndRegex, Pattern.CASE_INSENSITIVE);
        Matcher msStart = patternStart.matcher(input);
        Matcher msEnd = patternEnd.matcher(input);

        while ( true ) {

            if ( input.length() <= index ) {
                // コメントは削除し尽くした
                return output.toString();
            }

            int bsIndex = input.indexOf(blockStart, index);
            int msIndex = -1;
            if ( msStart.find(index) ) {
                msIndex = msStart.start();
            }

            if ( ( bsIndex != -1 && msIndex != -1 && bsIndex < msIndex )
                    || ( bsIndex != -1 && msIndex == -1 ) ) {
                // 両方見つかったがコメントブロックのほうが先に現れた場合か、
                // コメントブロックのみ見つかった場合
                if ( input.indexOf(blockEnd, bsIndex) == -1 ) {
                    throw new IOException("HTML comment type anaysis error.");
                    // ここに来ることは（おそらく）ほとんどない。
                }

                if ( index <= bsIndex - 1 ) {
                    output.append(input.substring(index, bsIndex));
                }
                index = input.indexOf(blockEnd, bsIndex) + blockEnd.length() + 1;

                int lfcount = countLFCodes(input.substring(bsIndex, index));
                for ( int i=0; i<lfcount; i++ ) output.append("\n");

            } else if ( ( bsIndex != -1 && msIndex != -1 && bsIndex >= msIndex )
                    || ( bsIndex == -1 && msIndex != -1 ) ) {
                // 両方見つかったがスクリプトタグのほうが先に現れた場合か、
                // スクリプトタグのみ見つかった場合
                if ( !msEnd.find( msStart.end() ) ) {
                    throw new IOException("HTML comment type anaysis error.");
                    // ここに来ることは（おそらく）ほとんどない。
                }
                int meIndex = msEnd.end();

                if ( index <= msIndex - 1 ) {
                    output.append( input.substring( index, msIndex ) );
                }
                output.append( deleteComplexComment( input.substring(msIndex, meIndex),
                        scriptBlockStartSimbol,
                        scriptBlockEndSimbol,
                        scriptLineSimbol ) );

                index = meIndex + 1;
                if ( input.substring(msStart.end(), index).indexOf("\n") != -1 ) {
                    output.append("\n");
                }

            } else {
                // コメントは削除し尽くした
                output.append( input.substring(index) );
                return output.toString();
            }
        }
    }

    /**
     * 文字列中に改行コードが何個入っているのかを調べて返す
     * @param src
     * @return
     */
    private static int countLFCodes(String src) {
        int count = 0;
        int index = 0;
        int next;
        while ((next = src.indexOf("\n", index)) != -1) {
            count++;
            index = next + 1;
        }
        return count;
    }
}
