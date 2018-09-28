package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String serviceName;
        String className;

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite el nombre del nuevo servicio: ");
        serviceName = sc.nextLine();
        System.out.println("Digite el nombre de la clase que desea exponer. El nombre debe contener el package y pertenecer a este proyecto: ");
        className = sc.nextLine();

        String WSDL = getWSDL(serviceName,className);
        try {
            File WSDLfile = new File("newWSDL.wsdl");
            FileWriter writer = new FileWriter(WSDLfile);
            writer.write(WSDL);
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String getWSDL(String serviceName, String className){
        String WSDL = "";
        String urlWSDL = "http://elServicio.nombreUnico"; //No estoy seguro de qué poner aquí. En el ejemplo del profe se ve algo como "http://titanic.ecci.ucr.ac.cr:80/~bsolano/HolaMundoServiceDocumentLiteral/ y en del PHP agarra las varas del servidor en el que se está corriendo"
        String types = ""; //types = typesInicio + typesMedio + typesFinal
        String mensajes = "";
        String puerto = "";
        String vinculaciones = "";
        String puntoC = "";

        /**Descripción WSDL de la clase**/
        String header = "<?xml version=\"1.0\" encoding\"UTF-8\"?>\n" +
                "<definitions name=\"" + serviceName +
                "\"\n targetNamespace=\"urn:" + serviceName +
                "\"\n xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"" +
                "\n xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"" +
                "\n xmlns:tns=\"urn:" + serviceName + "\"" +
                "\n xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
                "\n xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
                "\n xmlns=\"http://schemas.xmlsoap.org/wsdl/\">\n\n";

        /**Tipos complejos**/
        String typesInicio = "  <types xmlns=\"http://schemas.xmlsoap.org/wsdl/\"/>\n" +
                "    <xsd:schema targetNamespace=\"urn:" + serviceName + "\">\n";
        String typesMedio = "";
        ClassLoader classLoader = Main.class.getClassLoader();

        try {
            Class aClass = classLoader.loadClass(className);
            Method[] metodos = aClass.getDeclaredMethods();
            for (Method metodo : metodos) {
                String tipoRetorno = getTipo(metodo.getReturnType().getSimpleName());
                typesMedio += "      <xsd:element name=\"" + metodo.getName() + "\">\n" +
                        "        <xsd:complexType>\n" +
                        "          <xsd:sequence>\n";
                Parameter[] parametros = metodo.getParameters();
                for (Parameter parametro : parametros) {
                    String tipoP = getTipo(parametro.getType().getSimpleName());
                    typesMedio += "            <xsd:element name=\"" + metodo.getName() + "_" + parametro.getName() + "\" type=\"" + tipoP + "\" />\n";
                }
                typesMedio += "          </xsd:sequence>\n" +
                        "        </xsd:complexType>\n" +
                        "      </xsd:element>\n";
                typesMedio += "      <xsd:element name=\"" + metodo.getName() + "Return\">\n" +
                        "        <xsd:complexType>\n" +
                        "          <xsd:sequence>\n" +
                        "            <xsd:element name=\"" + metodo.getName() + "Result\" type=\"" + tipoRetorno + "\" />\n" +
                        "          </xsd:sequence>\n" +
                        "        </xsd:complexType>\n" +
                        "      </xsd:element>\n";
            }
            String typesFinal = "    <schema>\n" +
                    "  </types>\n\n";
            types = typesInicio + typesMedio + typesFinal;

            /**Mensajes para comunicarse con la clase**/
            for (Method metodo : metodos) {
                mensajes += "  <message name=\"" + metodo.getName() + "Request\">\n" +
                        "    <part name=\"parameters\" element=\"tns:" + metodo.getName() + "\" />\n" +
                        "  </message>\n" +
                        "  <message name=\"" + metodo.getName() + "Response\">\n" +
                        "    <part name=\"parameters\" element=\"tns:" + metodo.getName() + "Return\" />\n" +
                        "  </message>\n\n";
            }

            /**Puerto para comunicar con la clase**/
            puerto = "  <portType name=\"" + serviceName + "Port\">\n";
            for (Method metodo : metodos) {
                puerto += "    <operation name=\"" + metodo.getName() + "\">\n" +
                        "      <input message=\"tns:" + metodo.getName() + "Request\" />\n" +
                        "      <output message=\"tns:" + metodo.getName() + "Response\" />\n" +
                        "    </operation>\n";
            }
            puerto += "  </portType>\n\n";

            /**Vinculaciones de los llamados con el transporte - Document, SOAP/Literal over HTTP**/
            vinculaciones = "  <binding name=\"" + serviceName + "Binding\" type=\"tns:" + serviceName + "Port\">\n" +
                    "    <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\" />\n";
            for (Method metodo : metodos) {
                vinculaciones += "      <operation name=\"" + metodo.getName() + "\">\n" +
                        "        <soap:operation soapAction=\"urn:" + serviceName + "#" + serviceName + "#" + metodo.getName() + "\" style=\"document\" />\n" + //Aquí no estoy seguro de si es el mismo serviceName o si debería haber otro parámetro
                        "        <input>\n" +
                        "          <soap:body use=\"literal\" />\n" +
                        "        </input>\n" +
                        "        <output>\n" +
                        "          <soap:body use=\"literal\" />\n" +
                        "        </output>\n" +
                        "      </operation>\n";
            }
            vinculaciones += "  </binding>\n\n";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /**Punto de comunicación con la clase**/
        puntoC = "  <service name=\"" + serviceName + "\">\n" +
                "    <documentation />\n" +
                "    <port name=\"" + serviceName + "Port\" binding=\"tns:" + serviceName + "Binding\">\n" +
                "      <soap:address location=\"" + urlWSDL + "\" />\n" +
                "    </port>\n" +
                "  <service>\n\n";

        WSDL = header+types+mensajes+puerto+vinculaciones+puntoC+"</definitions>";

        System.out.println(WSDL); //como va quedando el WSDL

        return WSDL;
    }

    private static String getTipo(String simpleName) {
        List<String> tipos = Arrays.asList("String", "boolean", "int", "integer", "float", "double"); //para verifcar si es un tipo primitivo
        String tipo = "";
        if (tipos.contains(simpleName)) {
            if (simpleName.equals("String")) {
                tipo = "xsd:string";
            }
            else if (simpleName.equals("int") || simpleName.equals("integer")) {
                tipo = "xsd:int";
            }
            else if (simpleName.equals("float") || simpleName.equals("double")) {
                tipo = "xsd:decimal";
            }
            else if (simpleName.equals("boolean")) {
                tipo = "xsd:boolean";
            }
        }
        else {
            //El tipo no es de los primitivos:
        }
        return tipo;
    }
}
