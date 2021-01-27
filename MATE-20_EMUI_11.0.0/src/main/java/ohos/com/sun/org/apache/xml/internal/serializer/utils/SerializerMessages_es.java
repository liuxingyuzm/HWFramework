package ohos.com.sun.org.apache.xml.internal.serializer.utils;

import java.util.ListResourceBundle;

public class SerializerMessages_es extends ListResourceBundle {
    @Override // java.util.ListResourceBundle
    public Object[][] getContents() {
        return new Object[][]{new Object[]{MsgKey.BAD_MSGKEY, "La clave de mensaje ''{0}'' no está en la clase de mensaje ''{1}''"}, new Object[]{MsgKey.BAD_MSGFORMAT, "Fallo de formato del mensaje ''{0}'' en la clase de mensaje ''{1}''."}, new Object[]{"ER_SERIALIZER_NOT_CONTENTHANDLER", "La clase de serializador ''{0}'' no implanta ohos.org.xml.sax.ContentHandler."}, new Object[]{"ER_RESOURCE_COULD_NOT_FIND", "No se ha encontrado el recurso [ {0} ].\n {1}"}, new Object[]{"ER_RESOURCE_COULD_NOT_LOAD", "No se ha podido cargar el recurso [ {0} ]: {1} \n {2} \t {3}"}, new Object[]{"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Tamaño de buffer menor o igual que 0"}, new Object[]{"ER_INVALID_UTF16_SURROGATE", "¿Se ha detectado un sustituto UTF-16 no válido: {0}?"}, new Object[]{"ER_OIERROR", "Error de E/S"}, new Object[]{"ER_ILLEGAL_ATTRIBUTE_POSITION", "No se puede agregar el atributo {0} después de nodos secundarios o antes de que se produzca un elemento. Se ignorará el atributo."}, new Object[]{"ER_NAMESPACE_PREFIX", "No se ha declarado el espacio de nombres para el prefijo ''{0}''."}, new Object[]{MsgKey.ER_STRAY_ATTRIBUTE, "El atributo ''{0}'' está fuera del elemento."}, new Object[]{"ER_STRAY_NAMESPACE", "Declaración del espacio de nombres ''{0}''=''{1}'' fuera del elemento."}, new Object[]{"ER_COULD_NOT_LOAD_RESOURCE", "No se ha podido cargar ''{0}'' (compruebe la CLASSPATH), ahora sólo se están utilizando los valores por defecto"}, new Object[]{"ER_ILLEGAL_CHARACTER", "Intento de realizar la salida del carácter del valor integral {0}, que no está representado en la codificación de salida de {1}."}, new Object[]{"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "No se ha podido cargar el archivo de propiedades ''{0}'' para el método de salida ''{1}'' (compruebe la CLASSPATH)"}, new Object[]{"ER_INVALID_PORT", "Número de puerto no válido"}, new Object[]{"ER_PORT_WHEN_HOST_NULL", "No se puede definir el puerto si el host es nulo"}, new Object[]{"ER_HOST_ADDRESS_NOT_WELLFORMED", "El formato de la dirección de host no es correcto"}, new Object[]{"ER_SCHEME_NOT_CONFORMANT", "El esquema no es válido."}, new Object[]{"ER_SCHEME_FROM_NULL_STRING", "No se puede definir un esquema a partir de una cadena nula"}, new Object[]{"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "La ruta de acceso contiene una secuencia de escape no válida"}, new Object[]{"ER_PATH_INVALID_CHAR", "La ruta de acceso contiene un carácter no válido: {0}"}, new Object[]{"ER_FRAG_INVALID_CHAR", "El fragmento contiene un carácter no válido"}, new Object[]{"ER_FRAG_WHEN_PATH_NULL", "No se puede definir el fragmento si la ruta de acceso es nula"}, new Object[]{"ER_FRAG_FOR_GENERIC_URI", "Sólo se puede definir el fragmento para un URI genérico"}, new Object[]{"ER_NO_SCHEME_IN_URI", "No se ha encontrado un esquema en el URI"}, new Object[]{"ER_CANNOT_INIT_URI_EMPTY_PARMS", "No se puede inicializar el URI con parámetros vacíos"}, new Object[]{"ER_NO_FRAGMENT_STRING_IN_PATH", "No se puede especificar el fragmento en la ruta de acceso y en el fragmento"}, new Object[]{"ER_NO_QUERY_STRING_IN_PATH", "No se puede especificar la cadena de consulta en la ruta de acceso y en la cadena de consulta"}, new Object[]{"ER_NO_PORT_IF_NO_HOST", "No se puede especificar el puerto si no se ha especificado el host"}, new Object[]{"ER_NO_USERINFO_IF_NO_HOST", "No se puede especificar la información de usuario si no se ha especificado el host"}, new Object[]{MsgKey.ER_XML_VERSION_NOT_SUPPORTED, "Advertencia: es necesario que la versión del documento de salida sea ''{0}''. Esta versión de XML no está soportada. La versión del documento de salida será ''1.0''."}, new Object[]{"ER_SCHEME_REQUIRED", "Se necesita un esquema."}, new Object[]{MsgKey.ER_FACTORY_PROPERTY_MISSING, "El objeto de propiedades transferido a SerializerFactory no tiene una propiedad ''{0}''."}, new Object[]{"ER_ENCODING_NOT_SUPPORTED", "Advertencia: el tiempo de ejecución de Java no soporta la codificación ''{0}''."}};
    }
}