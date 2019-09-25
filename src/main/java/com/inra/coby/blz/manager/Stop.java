
package com.inra.coby.blz.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.zeroturnaround.process.Processes;
import org.zeroturnaround.process.PidProcess;
import org.zeroturnaround.process.ProcessUtil;

/**
 *
 * @author ryahiaoui
 */

public class Stop {
    
    
    public static void process() throws Exception {
        
        String pidFile = System.getProperty("user.dir") + File.separator + "pidfile" ;

        if( ! Files.exists(Paths.get(pidFile))) {
            System.out.println("\n + No Blazegraph Process to Stop ! \n ") ;
            return ;
        }
        
        System.out.println(" \n + Stoping BlazeGraph Process...    ") ;
        
        int pid  = Integer.parseInt(new String(Files.readAllBytes(Paths.get(pidFile)))) ;
        PidProcess process = Processes.newPidProcess(pid) ;
        ProcessUtil.destroyGracefullyOrForcefullyAndWait( process               ,
                                                          50                    ,
                                                          TimeUnit.MILLISECONDS ,
                                                          100                   , 
                                                          TimeUnit.MILLISECONDS ) ;
        
        Files.delete(Paths.get(pidFile)) ;
        System.out.println(" + BlazeGraph Process Stoped \n " ) ;
    }
        
}
