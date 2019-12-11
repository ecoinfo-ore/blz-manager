
package com.inra.coby.blz.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.io.FileOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import static org.toilelibre.libe.curl.Curl.curl;
import org.apache.commons.exec.PumpStreamHandler;
import static com.inra.coby.blz.manager.Commands.mkdir;
import static com.inra.coby.blz.manager.Commands.extract;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import static com.inra.coby.blz.manager.Utils.removeLastSlash;

/**
 *
 * @author ryahiaoui
 */
public class Start {
    
    public static void with( String  dirBlz              ,
                             String  blzJarName          ,
                             String  namespace           ,
                             Integer readOnlyPort        ,
                             Integer readWritePort       , 
                             String  rwMode              ,
                             Integer xms                 , // Gigabyte
                             Integer xmx                 , // Gigabyte
                             Integer maxDirectMemorySize ) throws Exception {
       
      String currDir = System.getProperty("user.dir") ;
      
      if( dirBlz != null && dirBlz.endsWith("/")) {
          dirBlz = removeLastSlash(dirBlz) ;
      }
      
      String _namespace = namespace == null ? "si" : namespace   ;
      String _xms = xms == null ? "-Xms12g" : "-Xms" + xms + "g" ;
      String _xmx = xmx == null ? "-Xmx12g" : "-Xmx" + xmx + "g" ;
      String _maxDirectMemorySize = maxDirectMemorySize == null ? "28g" : maxDirectMemorySize + "g" ;
      
      mkdir(dirBlz + "/logs/") ;
      
      String CMD = " java -server -XX:+UseG1GC "                                                +
                   _xms    + " "  +  _xmx                                                       +
                   " -XX:MaxDirectMemorySize=" + _maxDirectMemorySize + " "                     +
                   " -XX:+UseStringDeduplication "                                              +
                   " -Xloggc:" + dirBlz + "/logs/gc.txt "                                       +
                   " -verbose:gc -XX:+PrintGCDetails "                                          +
                   " -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps "                            +
                   " -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 "                      +
                   " -XX:GCLogFileSize=5M "                                                     +
                   " -server -Dorg.eclipse.jetty.server.Request.maxFormContentSize=2000000000 " +
                   " -Dcom.bigdata.rdf.sail.namespace=" + _namespace                            +
                   " -Dcom.bigdata.journal.AbstractJournal.file="                               + 
                   dirBlz + File.separator + "data"+ File.separator + "blazegraph.jnl "         ;
                    
      if( rwMode.equalsIgnoreCase("ro"))   {
         
         extract ("owerrideXMl", "webWithConfReadOnly.xml", currDir ) ;
         CMD +=  " -Djetty.overrideWebXml="+ currDir + File.separator +
                 "webWithConfReadOnly.xml -Djetty.port="              + 
                 readOnlyPort  + " -jar " + dirBlz + File.separator   +
                 blzJarName    ;
      } else  { // RW
         CMD += " -Djetty.port=" + readWritePort + " -jar " + dirBlz  +
                File.separator + blzJarName  ;
      }

      System.out.println(" + Cmd : " + CMD )              ; 
      
      DefaultExecuteResultHandler resultHandler = 
                        new DefaultExecuteResultHandler() ;

      CommandLine cmdLine = CommandLine.parse(CMD)        ;
      DefaultExecutor executor = new DefaultExecutor()    ;
      executor.setExitValue(0)                            ;     
      executor.setStreamHandler (
              new PumpStreamHandler( System.out , 
                                     System.err ,
                                     System.in  ) )       ;
  
             
      executor.execute(cmdLine, resultHandler )           ;
       
    
      resultHandler.waitFor(2000)                         ;
      
      if( resultHandler.hasResult() )              {
          System.out.println( "\n *** ERROR  *** " + 
                              resultHandler.getException()
                                           .getMessage()) ;
          System.out.println(" Check LOG FILES \n" )      ;
          System.exit(0)                                  ;
      }
      
      String pidFile = currDir + File.separator + "pidfile" ;
            
      cmdLine = CommandLine.parse("jps")                    ;
      executor = new DefaultExecutor()                      ;
      executor.setExitValue(0)                              ;
      executor.setStreamHandler (
              new PumpStreamHandler( new FileOutputStream(new File(pidFile))  ,
                                     new FileOutputStream(new File( pidFile)) ,
                                     System.in )     )                        ;
            
      executor.execute(cmdLine )                                              ;
       
      // executor.execute(cmdLine, resultHandler ) ;
      // resultHandler.waitFor(1000)               ;

      String blzProcessPIDLine = null ;
   
      try (Stream<String> stream = Files.lines(Paths.get(pidFile)))   {
	
          blzProcessPIDLine = stream.filter( line -> line.toLowerCase()
                                                         .contains("blazegraph") )
                                    .findFirst()
                                    .orElse(null) ;
      } catch ( IOException e ) {
	 System.out.println(" No BlazeGraph Process Found ! ") ;
         System.exit(0) ;
      }
      
      String pidBlz  = null ; 
      
      if( blzProcessPIDLine != null )    {
         pidBlz = blzProcessPIDLine.split(" ")[0]              ;
         Files.write( Paths.get(pidFile) , pidBlz.getBytes() ) ;
      }
      
      if( rwMode.trim().equalsIgnoreCase("rw"))        {
         curl ( "-k -X DELETE " + "http://localhost:"  +
         readWritePort + "/blazegraph/namespace/kb" )  ;
      }
      
      System.out.println(" + Pid Blz : " + pidBlz ) ;
    }
    
}
