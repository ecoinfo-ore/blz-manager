 package entry;

 import com.inra.coby.blz.manager.Stop;
 import com.inra.coby.blz.manager.Start;
 import com.inra.coby.blz.manager.Config;
 import static com.inra.coby.blz.manager.Utils.getProperty;
import static com.inra.coby.blz.manager.Utils.removeWrappedSimpleOrDoubleQuotes;

 public class Main {

    /* EX :
     java -DOp="Start"                                 \
          -DRwMode=ro                                  \
          -DRwPort=8888                                \
          -DRoPort=7777                                \
          -DNamespace=data                             \
          -DHost=localhost                             \
          -DXms=4                                      \
          -DXmx=4                                      \
          -DMaxMem=12                                  \
          -DBlzDir="/home/pipeline/libs/Blazegraph"    \
          -DBlzJarName="blazegraph_2_1_4.jar"          \
          -jar blz-manager-1.0-jar-with-dependencies.jar
    */
            
    public static void main(String[] args) throws Exception     {
        
        String operation  = removeWrappedSimpleOrDoubleQuotes(getProperty("Op") )           ; // start - stop - config 
        String rwMode     = removeWrappedSimpleOrDoubleQuotes(getProperty("RwMode"))        ; // ro - rw
        Integer rwPort    = Integer.parseInt(getProperty("RwPort"))                         ;
        Integer roPort    = Integer.parseInt(getProperty("RoPort"))                         ;
        String namespace  = removeWrappedSimpleOrDoubleQuotes(getProperty("Namespace") )    ;
        String host       = removeWrappedSimpleOrDoubleQuotes(getProperty("Host") )         ;
        String blzDir     = removeWrappedSimpleOrDoubleQuotes(getProperty("BlzDir")  )      ;
        String blzJarName = removeWrappedSimpleOrDoubleQuotes(getProperty("BlzJarName"))    ;
        String _xms       = removeWrappedSimpleOrDoubleQuotes(getProperty("Xms") )          ;
        String _xmx       = removeWrappedSimpleOrDoubleQuotes(getProperty("Xmx") )          ;
        String _maxMem    = removeWrappedSimpleOrDoubleQuotes(getProperty("MaxMem"))        ;
        
        if( System.getProperty("H") != null   || System.getProperty("Help") != null )       {
           System.out.println("                                                          ") ;
           System.out.println(" ######################################################## ") ;
           System.out.println(" ### Deduplicator  ###################################### ") ;
           System.out.println(" -------------------------------------------------------- ") ;
           System.out.println(" Total Arguments :  11                                    ") ;
           System.out.println("   Op         : Operation Type ( start - strop - sonfig ) ") ;
           System.out.println("   RwMode     : Mode ( ro - rw )                          ") ;
           System.out.println("   RwPort     : Port used when starting on rw mode        ") ;
           System.out.println("   RoPort     : Port used when starting on ro mode        ") ;
           System.out.println("   Namespace  : Default Namespace that will be used       ") ;
           System.out.println("   Host       : Blazegraph Host ( default = localhost )   ") ;
           System.out.println("   BlzDir     : Directory of Blazegraph                   ") ;
           System.out.println("   BlzJarName : Blazegraph Jar name                       ") ;
           System.out.println("   Xms        : XMS ( ex : -DXms=10 ) - in GigaByte       ") ;
           System.out.println("   Xmx        : XMX ( ex : -DXmx=10 ) - in GigaByte       ") ;
           System.out.println("   MaxMem     : Max Memory that will be used by Blazegrah ") ;
           System.out.println(" -------------------------------------------------------- ") ;
           System.out.println(" ######################################################## ") ;
           System.out.println("                                                          ") ;
           System.exit(0)    ;
        }
                     
        Integer xms    = 4 ;
        Integer xmx    = 4 ;
        Integer maxMem = 8 ;
        
        if( host.trim().isEmpty())       host      = "localhost" ;
        if( namespace.trim().isEmpty() ) namespace = "data"      ;
        
        if( ! _xms.trim().isEmpty() )    xms    = Integer.parseInt(_xms)    ;
        if( ! _xmx.trim().isEmpty() )    xmx    = Integer.parseInt(_xmx)    ;
        if( ! _maxMem.trim().isEmpty() ) maxMem = Integer.parseInt(_maxMem) ;
        
        if( operation.isEmpty() || operation.equalsIgnoreCase("start")) {
            Start.with( blzDir, blzJarName, namespace, roPort, rwPort, rwMode, xms, xmx, maxMem ) ;
            System.exit(0) ;
        }
        
        else if( operation.isEmpty() || operation.equalsIgnoreCase("stop")) {
            Stop.process() ;
            System.exit(0) ;
        } 
        
        else if( operation.isEmpty() || operation.equalsIgnoreCase("config")) {
            Config.with( blzDir, blzJarName , host, namespace , roPort )      ;
            System.exit(0) ;
        } 
        
    }    
 }
