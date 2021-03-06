package es.antoniomc.uMusic.utils;


import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.antoniomc.uMusic.App;

public class xmlReader {

	/**
	 * Método que obtiene de un xml el dato del nombre de la etiqueta que pasamos por parámetro
	 * 
	 * @param data nombre de la etiqueta
	 * @return valor de la etiqueta en el xml
	 */
  public static String getConectionInfo(String data) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder;
    Document doc = null;
    String url = null;
    try {
      builder = factory.newDocumentBuilder();
      doc = builder.parse(App.class.getResourceAsStream("server.xml"));

      XPathFactory xpathFactory = XPathFactory.newInstance();

      XPath xpath = xpathFactory.newXPath();
      XPathExpression expr = xpath.compile("/conexion/" + data + "/text()");
      url = (String) expr.evaluate(doc, XPathConstants.STRING);

    } catch (XPathExpressionException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }

    return url;
  }

}