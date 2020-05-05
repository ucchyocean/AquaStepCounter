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

    public static String deleteComments(String input, CommentConfig type) throws IOException {

        if ( type.getType() == CommentConfigType.BLOCK_COMMENT ) {
            return deleteBlockComment(input,
                    type.getBlockCommentStartSimbol(), type.getBlockCommentEndSimbol());
        } else if ( type.getType() == CommentConfigType.LINE_COMMENT ) {
            return deleteLineComment(input, type.getLineCommentSimbol());
        } else if ( type.getType() == CommentConfigType.SHELL ) {
            return deleteShellLineComment(input, type.getLineCommentSimbol(),
                    type.getExtraSimbol());
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
                    throw new IOException("Format error.");
                    // 文字列中にブロック開始記号が入ってしまっていた場合
                    // TODO 対応したいけれど今のところどうしようもない。
                }
                int beIndex = content.indexOf(blockEnd, bsIndex) + blockEnd.length();
                if ( content.substring(bsIndex, beIndex).indexOf("\n") != -1 ) {
                    content.replace(bsIndex, beIndex, "\n");
                } else {
                    content.replace(bsIndex, beIndex, "");
                }

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

    private static String deleteBlockComment(String input, String blockStart, String blockEnd) throws IOException {

        StringBuffer content = new StringBuffer(input);

        while ( content.indexOf(blockStart) != -1 ) {
            int start = content.indexOf(blockStart);
            if ( content.indexOf(blockEnd, start) == -1 ) {
                throw new IOException("Format error.");
                // 文字列中にブロック開始記号が入ってしまっていた場合
                // TODO 対応したいけれど今のところどうしようもない。
            }
            int end = content.indexOf(blockEnd, start) + blockEnd.length();
            if ( content.substring(start, end).indexOf("\n") != -1 ) {
                content.replace(start, end, "\n");
            } else {
                content.replace(start, end, "");
            }
        }

        return content.toString();
    }

    private static String deleteLineComment(String input, String lineStart) {

        StringBuffer content = new StringBuffer(input);

        while ( content.indexOf(lineStart) != -1 ) {
            int start = content.indexOf(lineStart);
            int end = content.indexOf("\n", start);
            if ( end == -1 ) {
                end = content.length() - 1; // ファイルの最後にセット
            } else {
                end += 1; // 改行コード込みで置き換える
            }
            content.replace(start, end, "\n");
        }

        return content.toString();
    }

    /**
     * Shell形式の行コメントを除去する。Shell形式では、行頭にしかコメントがこない（行の途中からのコメントは無い）。
     * @param input 変換前のドキュメント
     * @param regex コメント開始シンボル（正規表現）
     * @param shellFlag ドキュメントの先頭に現れるコメント行（例外として削除を行わない）
     * @return 変換後のドキュメント
     */
    private static String deleteShellLineComment(String input, String regex, String[] shellFlag) {

        String[] lines = input.split("\n");
        StringBuffer buf = new StringBuffer();

        if ( lines.length == 0 ) {
            return input;
        }

        // 最初の1行目が、shellFlagに該当するかどうか確認する
        if ( ( (shellFlag == null || shellFlag.length == 0 || shellFlag[0].length() == 0)
                || !lines[0].matches(shellFlag[0]) ) && !lines[0].matches(regex) ) {
            buf.append(lines[0] + "\n");
        }

        // 2行目以降を確認する
        for ( int i=1; i<lines.length; i++ ) {
            if ( !lines[i].matches(regex) ) {
                buf.append(lines[i] + "\n");
            }
        }

        return buf.toString();
    }

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

        String temp = deleteBlockComment(input, blockStart, blockEnd);
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
                throw new IOException("Format error.");
                // 文字列中にブロック開始記号が入ってしまっていた場合
                // TODO 対応したいけれど今のところどうしようもない。
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
                    throw new IOException("Format error.");
                    // ここに来ることは（おそらく）ほとんどない。
                }

                if ( index <= bsIndex - 1 ) {
                    output.append(input.substring(index, bsIndex));
                }
                index = input.indexOf(blockEnd, bsIndex)
                        + blockEnd.length() + 1;
                if ( input.substring(bsIndex, index).indexOf("\n") != -1 ) {
                    output.append("\n");
                }

            } else if ( ( bsIndex != -1 && msIndex != -1 && bsIndex >= msIndex )
                    || ( bsIndex == -1 && msIndex != -1 ) ) {
                // 両方見つかったがスクリプトタグのほうが先に現れた場合か、
                // スクリプトタグのみ見つかった場合
                if ( !msEnd.find( msStart.end() ) ) {
                    throw new IOException("Format error.");
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
}
