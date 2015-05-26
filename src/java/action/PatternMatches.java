/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lepra25-pc
 */
public class PatternMatches {

    /**
     * 正規表現のパターン郡列挙オブジェクト
     */
    public enum PatternEnum {

        /**
         * 英数字のみ: ^[a-zA-Z0-9]+$
         */
        ALNUM("^[a-zA-Z0-9]+$"),
        /**
         * 英数句読文字:!"#$%&'()*+,-./:;<=>?@[]^_`{
         */
        PUNCT("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$");

        /**
         * 正規表現
         */
        private final String regex;

        private PatternEnum(final String regex) {
            this.regex = regex;
        }

        /**
         * 正規表現を保持する{@code Pattern}オブジェクトを返却する
         */
        @SuppressWarnings("unqualified-field-access")
        public Pattern toPattern() {
            return Pattern.compile(regex);
        }
    }

    public static boolean findMatches(Pattern pattern, CharSequence input) {
        final Matcher m = pattern.matcher(input);
        return m.find();
    }
}
