
package com.inra.coby.blz.manager;

import java.io.File ;

/**
 *
 * @author ryahiaoui
 */

public class Utils {
    
    public static String getProperty(String property) {
        return System.getProperty(property) != null
               ? System.getProperty(property) : ""     ;
    }

    public static String removeLastSlash(String directoryPath )           {
        if ( ! directoryPath.isEmpty()            && 
               directoryPath.trim().endsWith(File.separator))             {
            return directoryPath.substring ( 0     ,
                                             directoryPath.length() - 1 ) ;
        }
        return directoryPath ;
    }
    
    public static String removeWrappedSimpleOrDoubleQuotes( String str ) {
        if( str == null ) return str        ; 
        if( ( str.trim().startsWith("\"")  &&
              str.trim().endsWith("\"")  ) || 
            ( str.trim().startsWith("'")   &&
            str.trim().endsWith("'") ) )    {
            return str.substring( 1, str.length() - 1 )   ;
        }
        
        return str ;
    }
    
}
