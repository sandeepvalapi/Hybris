Example of a KeyStore with RSA Public and Private Key generated using RSA algorithm
On production environments demo keystore should be replaced by a production one 

Example how to generate a keystore using java keytool
>  keytool -genkey -keyalg RSA -alias test1 -keystore keystore.jks -keysize 2048