
## Blz-Manager

==

Manage Blazegraph Database

==

```bash
   mvn clean install assembly:single
```

Example :

```bash

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

```

-DOp = Start / Stop / Config 
