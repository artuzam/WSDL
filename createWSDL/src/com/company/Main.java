package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }

    public static String getWSDL(){
        String WSDL = "";

        String header = "<?xml version=\"1.0\" ?>\n" + "\"<definitions name=\\\"$this->service_name\\\" targetNamespace=\\\"urn:$this->service_name\\\" xmlns:wsdl=\\\"http://schemas.xmlsoap.org/wsdl/\\\" xmlns:soap=\\\"http://schemas.xmlsoap.org/wsdl/soap/\\\" xmlns:tns=\\\"urn:$this->service_name\\\" xmlns:xsd=\\\"http://www.w3.org/2001/XMLSchema\\\" xmlns:SOAP-ENC=\\\"http://schemas.xmlsoap.org/soap/encoding/\\\" xmlns=\\\"http://schemas.xmlsoap.org/wsdl/\\\">\\n\"" + "<types xmlns=\"http://schemas.xmlsoap.org/wsdl/\" />\n";




        return WSDL;
    }
}
