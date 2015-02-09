cd ../WebCrawlerServerCommon
mvn source:jar install
cd ../WebCrawlerServerSlave
mvn eclipse:eclipse -DdownloadSources=true
mvn spring-boot:run 
