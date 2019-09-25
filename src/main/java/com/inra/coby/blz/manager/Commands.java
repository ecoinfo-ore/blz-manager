
package com.inra.coby.blz.manager;

import entry.Main;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ryahiaoui
 */
public class Commands {
    
    public static boolean mkdir(String directory ) {
      File fDirectory = new File(directory) ;
      if( ! fDirectory.exists() )   {
          System.out.println(" Create Folder : " + directory ) ;
          return fDirectory.mkdir() ;
      }
      return false ; 
    }
    
    public static void rm(String path ) throws IOException {
      System.out.println(" Remove Path : " + path ) ;
      if(new File(path).isDirectory()) {
        FileUtils.deleteDirectory(new File(path))   ;
      }
      else {
        new File(path).delete() ;
      }
    }
    
    public static void extract( String path , String prg , String dest ) throws IOException {
        OutputStream os  ;
        mkdir(dest)      ;
        try (InputStream is = Main.class.getClassLoader()
                                        .getResource( path + "/" + prg )
                                        .openStream())                 {
            os = new FileOutputStream( dest + File.separator + prg )   ;
            byte[] b = new byte[2048]             ;
            int length                            ;
            while ((length = is.read(b)) != -1 )  {
                  os.write(b, 0, length)          ;
            }
        }
        os.close() ;
    }
       
}
