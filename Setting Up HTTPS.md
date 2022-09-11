## ESTABLISHING HTTPS FOR TESTING SPRING BOOT REST SERVICES:

In order to establish safe, encrypted communication with a service, administrators must
provide HTTPS communication for the service.  For development testing they may accomplish this
using a self-signed certificate with a self-signed root certificate.  Testers must then import
this root certificate into any clients used for testing, such as Postman or a client browser.

In order to generate the self-signed root certificate, the admin will need to use the
**openssl** package.  This package comes with most recent Linux distributions or is
easily available via the disbribution package manager of the operating system.

1. GENERATE THE SELF-SIGNED ROOT CERT USING OPENSSL: 
```
   openssl ecparam -out myroot.key -name prime256v1 -genkey       ## CREATE THE ROOT KEY
   openssl req -new -sha256 -key myroot.key -out myroot.csr       ## GENERATE SIGNING REQUEST FOR ROOT KEY
   openssl x509 -req -sha256 -days 365 -in myroot.csr -signkey myroot.key -out myroot.crt   ## SELF-SIGN AND GENERATE THE ROOT CERTIFICATE
```
2. GENERATE A PUBLIC/PRIVATE KEY PAIR FOR THE REST API ENDPOINT USING KEYTOOL:
   When generating the p/p key pair be sure to set the common name to the endpoint of the service, ie., CN=myservice.example.com
```
   keytool -genkeypair -keyalg rsa -keystore mysite.jks -alias mysitekey -storepass password -validity 365 -keysize 4096 -storetype pkcs12
   keytool -certreq -keystore mysite.jks -alias mysitekey -storepass password -file mysite.csr  ## GENERATE CERTIFICATE SIGNING REQUEST
```
3. SIGN THE NEW KEY CERTIFICATE REQUEST WITH THE ROOT CERTIFICATE:
```
   openssl x509 -req -in mysite.csr -CA  myroot.crt -CAkey myroot.key -CAcreateserial -out mysite.crt -days 365 -sha256  ## SIGN API CERT REQ W/ROOT KEY
```
4. IMPORT THE SIGNED CERTIFICATE INTO THE KEYSTORE FOR THE API SERVICE:
```
   keytool -importcert -keystore mysite.jks -alias mysitekey -storepass password
```
5. VERIFY THE KEYSTORE BY LISTING THE CONTENTS:
```
   keytool -list -keystore mysite.jks

Your keystore contains 2 entries

myroot, Aug 17, 2022, trustedCertEntry, 
Certificate fingerprint (SHA-256): 8D:40:87:FC:76:15:6E:76:DC:20:29:09:52:99:65:B9:C6:D9:8A:10:00:A0:DC:06:6C:28:04:9C:93:29:03:56
mysitekey, Aug 17, 2022, PrivateKeyEntry, 
Certificate fingerprint (SHA-256): 2C:57:5D:93:36:CD:96:98:09:2D:C9:58:F7:07:7D:8B:ED:3B:04:FE:03:8F:2C:D8:1D:8D:B3:8B:60:26:EB:4C

```
6. ADD THE FOLLOWING SETTINGS TO THE application.properties FILE:
```
### ENABLE HTTPS COMMUNICATION
server.ssl.enabled=true
server.port=8443
server.ssl.key-alias=mysitekey
server.ssl.key-store=classpath:mysite.jks
server.ssl.key-store-type=jks
server.ssl.key-store-password=password
```
6. IMPORT THE ROOT CERTIFICATE INTO ANY CLIENT APPLICATION ROOT CERTIFICATE KEYSTORE:
   For example, import the root certicate into the CACERTS for the runtime of the client java application.
```
   sudo keytool -import -cacerts -alias myroot -file myroot.crt
```
