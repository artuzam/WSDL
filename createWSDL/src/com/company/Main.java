package com.company;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Main {

    public static void main(String[] args) {
	// write your code here
        getWSDL("HolaMundo");
    }

    public static String getWSDL(String serviceName){
        String WSDL = "";
        String types; //types = typesInicio + typesMedio + typesFinal

        String header = "<?xml version=\"1.0\" ?>\n" + "<definitions name=\"" + serviceName + "\" targetNamespace=\"urn:" + serviceName + "\"\n xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"\n xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"\n xmlns:tns=\"urn:" + serviceName + "\"\n xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n xmlns=\"http://schemas.xmlsoap.org/wsdl/\">\n" + "<types xmlns=\"http://schemas.xmlsoap.org/wsdl/\" />\n";

        String typesInicio = "<types>\n\t<schema>\n";
        String typesFinal = "\n\t<schema>\n</types>";

        ClassLoader classLoader = Main.class.getClassLoader();

        try {
            Class aClass = classLoader.loadClass("com.company.HolaMundo");
            Method[] metodos = aClass.getDeclaredMethods();
            for (Method metodo:
                 metodos) {
                System.out.println("Nombre del método: " + metodo.getName() + " - Retorna: " + metodo.getReturnType().getSimpleName());
                Parameter[] parametros = metodo.getParameters();
                System.out.println(parametros.length + " Parámetros: ");
                for (Parameter parametro :
                        parametros) {
                    System.out.println(parametro.getName() + " - " + parametro.getType().getSimpleName());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return WSDL;
    }
}
