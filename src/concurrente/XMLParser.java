package concurrente;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Objects;

public class XMLParser {

    private final String petriNetFilePath;
    private int[] marcado;
    private int[][] incidenciaFrontward;
    private int[][] incidenciaBackward;
    private int[][] matrizInhibidora;

    /**
     * Constructor de clase
     * @param petriNetFilePath path del archivo xml que contiene la red de petri
     */
    public XMLParser(String petriNetFilePath)
    {
        this.petriNetFilePath = petriNetFilePath;
    }

    /**
     * Realizacion del parseo
     */
    public void setupParser()
    {
        setMarcado();
        setIncidencia();
    }

    /**
     * Setea el marcado inicial de la red
     */
    private void setMarcado()
    {
        int i, j;
        String aux;

        try
        {
            File file = new File(petriNetFilePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("place");

            this.marcado = new int[ nodeList.getLength() ];

            for ( i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;

                    aux = eElement.getElementsByTagName("name").item(0).getTextContent();
                    aux = aux.replaceAll(" ", "");
                    aux = aux.replaceAll("\n", "");
                    j = Integer.decode(aux.replaceFirst("P", ""));

                    aux = eElement.getElementsByTagName("initialMarking").item(0).getTextContent();
                    aux = aux.replaceAll(" ", "");
                    aux = aux.replaceAll("\n", "");
                    marcado[j] = Integer.decode(aux.replaceFirst("Default,", ""));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Getter del marcado inicial
     * @return array de entero conteniendo marcado inicial
     */
    public int[] getMarcado()
    {
        return this.marcado;
    }

    /**+
     * Setea las matrices de incidencia de la red de petri
     */
    private void setIncidencia()
    {
        int i, j;
        String source, target, peso;

        this.incidenciaFrontward = new int[1][1];
        this.incidenciaBackward = new int[1][1];

        try {
            File file = new File(petriNetFilePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("transition");

            this.incidenciaFrontward = new int[ marcado.length ][ nodeList.getLength() ];
            this.incidenciaBackward = new int[ marcado.length ][ nodeList.getLength() ];
            this.matrizInhibidora = new int[ nodeList.getLength() ][marcado.length];

            for(i = 0; i < incidenciaBackward.length; i++)
            {
                for(j = 0; j < incidenciaBackward[i].length; j++)
                {
                    incidenciaFrontward[i][j] = 0;
                    incidenciaBackward[i][j] = 0;
                }

            }

            nodeList = doc.getElementsByTagName("arc");

            for (i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;

                    source = eElement.getAttributeNode("source").getValue();
                    target = eElement.getAttributeNode("target").getValue();
                    peso = eElement.getElementsByTagName("inscription").item(0).getTextContent();
                    peso = peso.replaceAll("\n", "");
                    peso = peso.replaceAll(" ", "");
                    peso = peso.replaceAll("Default,", "");
                    if( Objects.requireNonNull(getArcType(eElement)).equalsIgnoreCase("inhibitor"))
                    {
                        matrizInhibidora    [Integer.decode(target.replaceAll("T", ""))]
                                            [Integer.decode(source.replaceAll("P", ""))]
                                            = 1;
                    }
                    else if( source.charAt(0) == 'P' )
                    {
                        incidenciaBackward  [Integer.decode(source.replaceAll("P", ""))]
                                            [Integer.decode(target.replaceAll("T", ""))]
                                            = Integer.decode(peso.replaceAll("Default,", ""));
                    }
                    else if( source.charAt(0) == 'T' )
                    {
                        incidenciaFrontward [Integer.decode(target.replaceAll("P", ""))]
                                            [Integer.decode(source.replaceAll("T", ""))]
                                            = Integer.decode(peso.replaceAll("Default,", ""));
                    }
                    else
                        throw new Exception();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Getter de incidenciaFrontward
     * @return matrix de enteros que representa la incidenciaFrontward
     */
    public int[][] getIncidenciaFrontward()
    {
        return this.incidenciaFrontward;
    }

    /**
     * Getter de incidenciaBackward
     * @return matrix de enteros que representa la incidenciaBackward
     */
    public int[][] getIncidenciaBackward()
    {
        return this.incidenciaBackward;
    }

    /**
     * Getter de matrizInhibidora
     * @return matrix de enteros que representa la matrizInhibidora
     */
    public int[][] getMatrizInhibidora()
    {
        return this.matrizInhibidora;
    }

    /**
     * Retorna el tipo de arco
     * @return String igual a "normal" o "inhibitor"
     */
    private String getArcType(Element parent)
    {
        for(Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if(child instanceof Element && "type".equals(child.getNodeName()))
                return ( (Element) child).getAttribute("value");
        }
        return null;
    }
}
