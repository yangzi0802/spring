package com.shiner.spring.xml;

import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.net.URL;

public class XmlReaderUtil {

    private String configLocations;

    public XmlReaderUtil(){}

    public XmlReaderUtil(String configLocations){
        this.configLocations = configLocations;
    }


    public String readConfigs(){
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            SAXReader reader = new SAXReader(xmlReader);
            URL url = this.getClass().getClassLoader().getResource(configLocations);

        } catch (SAXException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
