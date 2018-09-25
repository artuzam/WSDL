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

        String header = "<?xml version=\"1.0\" ?>\n" + "\"<definitions name=\\\"" + serviceName + "\\\" targetNamespace=\\\"urn:" + serviceName + "\\\" xmlns:wsdl=\\\"http://schemas.xmlsoap.org/wsdl/\\\" xmlns:soap=\\\"http://schemas.xmlsoap.org/wsdl/soap/\\\" xmlns:tns=\\\"urn:" + serviceName + "\\\" xmlns:xsd=\\\"http://www.w3.org/2001/XMLSchema\\\" xmlns:SOAP-ENC=\\\"http://schemas.xmlsoap.org/soap/encoding/\\\" xmlns=\\\"http://schemas.xmlsoap.org/wsdl/\\\">\\n\"" + "<types xmlns=\"http://schemas.xmlsoap.org/wsdl/\" />\n";

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
