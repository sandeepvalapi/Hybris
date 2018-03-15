
 There are two zips which are generated and relevant for deployment:

 1) {timestamp}_hybrisServer-Platform.zip
    This is the hybrisServer (Platform with embedded Tomcat) that don't need to be installed on every deployment.
 
 2) {timestamp}_hybrisServer-AllExtensions.zip
    This is the "deployable" zip with ALL extensions. We recommend to replace the production directories completely 
    (delete old directories first). 

 Additionally {timestamp}_hybrisServer-Config.zip is also created which contains the /config folder of the developing
 system. If on the production system no /config folder exists you can create a new one 

 NOTE: it's always safe to replace the production /bin/ directory completely with these two zips. 
 (see production directory layout below). 
 It might be neccessary to reset some file permissions for start batch files when running on linux systems
    
    
    Production directory layout:
    =============================
    
       /bin/platform                \
       /bin/platform/..              |  {timestamp}_hybrisServer-Platform.zip       (replace on every deploy or only on Platform change)
       /bin/platform/tomcat/      /
       
       /bin/ext-hybris             -    {timestamp}_hybrisServer-AllExtensions.zip   (replace on every deploy)  


              
       The following files should NEVER be replaced during deployment:
       (location of these directories can be specified in /bin/platform/env.properties)
       
       /data                         | all data files like media files, lucene search index, ...
       /logs                         | all logfiles
              
       /config                      \
       /config/local.properties      | your main production configuration file (e.g. define webroots here)
       /config/localextensions.xml   | definition which extensions should be active
       /config/licence/..            | your production licence
       /config/tomcat/..             | your tomcat configuration (normally don't need to be changed, port settings 
                                    /  can be changed directly in local.properties directly)
                                    
      IF settings where changed which are needed for production environment (like new extensions) have a look into 
      {timestamp}_hybrisServer-Config.zip - this zip contains the config folder of the developing system. 
      DO NOT OVERWRITE THE PRODUCTION CONFIG FOLDER WITH THIS ZIP FILE!  
                                           


 Installation Procedure
 ======================
 
   1) Stop running tomcat
   2) Replace /bin/ directories as explained above
   3) Run "ant" in the platform directory. This will 
         - precompile jsp's
         - copy the Tomcat configuration  
   4) Start the hybrisServer
   
       