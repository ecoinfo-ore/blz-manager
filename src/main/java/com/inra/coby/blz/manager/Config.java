
package com.inra.coby.blz.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import static org.toilelibre.libe.curl.Curl.curl;
import static com.inra.coby.blz.manager.Commands.rm;
import static com.inra.coby.blz.manager.Commands.extract;
import static com.inra.coby.blz.manager.Utils.removeLastSlash;

/**
 *
 * @author ryahiaoui
 */
public class Config {
    
    public static void with( String  dirBlz        ,
                             String  blzJarName    ,
                             String  ipHost        ,
                             String  namespace     ,
                             Integer readOnlyPort ) throws Exception {
      
      Stop.process()        ;
      forceRemoveDB(dirBlz) ;
      Start.with( dirBlz , blzJarName , namespace, readOnlyPort, readOnlyPort, "rw",  4, 4, 4 ) ;
      
      String currDir = System.getProperty("user.dir") ;
      
      extract("namespace", "dataloader.xml"     , currDir + File.separator + "namespace" ) ;
      extract("namespace", "RWStore.properties" , currDir + File.separator + "namespace" ) ;
        
      TimeUnit.SECONDS.sleep(2) ;

      String DATALOADER  = currDir + File.separator + "namespace" +  File.separator +  "dataloader.xml" ;
      
      String blazegraphHost = "http://" + ipHost + ":" + readOnlyPort + "/blazegraph/dataloader"       ;
      
      /* Update File params */
      
      String dataLoaderString = new String(Files.readAllBytes(Paths.get(DATALOADER))) ;
      Files.write(Paths.get(DATALOADER), dataLoaderString.replace( "PROPERTY_PATH-"   ,
                                                                   currDir        +
                                                                   File.separator +
                                                                   "namespace"    +
                                                                   File.separator )
                                                         .replace("MY_NAMESPACE", namespace )
                                                         .getBytes()) ;
      
      System.out.println(" + BlazegraphHost = " + blazegraphHost )    ;
        
      System.out.println( " + curl " +  "-k -X POST --data-binary @"  + DATALOADER     + 
                          " --header 'Content-Type:application/xml' " + blazegraphHost ) ;
        
      curl ( "-k -X POST --data-binary @" +DATALOADER  + " -H 'Content-Type:application/xml' " + blazegraphHost ) ;
    
      TimeUnit.SECONDS.sleep(1) ;
      
      Stop.process() ;
      
    }
    
    public static void forceRemoveDB ( String  dirBlz ) throws Exception {
        
      if( dirBlz != null && dirBlz.endsWith("/")) {
          dirBlz = removeLastSlash(dirBlz)        ;
      }
      
      if ( ! Files.exists( Paths.get(dirBlz + "/data/blazegraph.jnl" ))) {
         return ;
      }
      
      System.out.println(" \n Deleting Blz Database... " )                  ;
      System.out.println(" Press Enter to Cancel or just wait 3 seconds " ) ;
            
      int x = 3 ; // wait 3 seconds at most

      BufferedReader in = new BufferedReader(new InputStreamReader(System.in)) ;
      long startTime = System.currentTimeMillis() ;
      
      while ((System.currentTimeMillis() - startTime) < x * 1000
              && !in.ready()) {
      }

      if ( in.ready() ) {
          System.out.println(" + Deleting Blz Database Canceled ") ;
      } else {
          System.out.println(" + Deleting the Blz Database... " )  ;
          rm( dirBlz + "/data/blazegraph.jnl" )                    ;
          System.out.println(" + Blz Database Deleted " )          ; 
      }
    }
    
}
