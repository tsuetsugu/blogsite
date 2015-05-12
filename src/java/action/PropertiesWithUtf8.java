/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author lepra25-pc
 */
public class PropertiesWithUtf8 {
    static Properties loadUtf8Properties(String resourceName) throws IOException {
        try (InputStream is = PropertiesWithUtf8.class.getResourceAsStream(resourceName);
             InputStreamReader isr = new InputStreamReader(is, "UTF-8");
             BufferedReader reader = new BufferedReader(isr)) {
 
            Properties result = new Properties();
 
            // Properties#load() で渡す Reader オブジェクトを UTF-8 エンコーディング指定して生成した
            // InputStreamReader オブジェクトにする
            result.load(reader);
 
            return result;
        }
    }    
}
