# servicio-pps

Servicio para resolver el matching entre donaciones y ofrecimientos en el sitio web del Proyecto Koinonía http://www.proyectokoinonia.org.ar/

Se utilizó Spring Boot para procesar las solicitudes, interactuando con la base de datos en MySQL.

A partir del texto plano, se llevan a cabo las siguientes etapas de forma secuencial:
 - Validación Ortográfica
 - Detección de Recursos
 - Obtención de Synsets Asociados
 
La comunicación del servicio es a través de tres tipos de requests:
 - (HTTP GET)  URL = '/textanalyzer' PARAMS = plainText
 - (HTTP POST) URL = '/synsetselection' PARAMS = synsetSelection, estaDonando
 - (HTTP GET)  URL = '/synsetcounter' PARAMS = listaPalabras(opt)
