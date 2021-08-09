import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;



public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);

        writeString(json, "data.json");

        List<Employee> listXML = parseXML("data.xml");

        String json2 = listToJson(listXML);

        writeString(json2, "data2.json");
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> stuff = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            stuff = csv.parse();
            stuff.forEach(System.out :: println);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stuff;


    }

    public static String listToJson(List list) {

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder rustem = new GsonBuilder();
        Gson gson = rustem.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String jsonFile) {

        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {

        List<Employee> stuff = null;
        DocumentBuilderFactory factory = new DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        Node root = (Node) doc.getDefaultRootElement();
        System.out.println("Корневой элемент " + root.getNodeName());
        NodeList nodelist = root.getChildNodes();

        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                NamedNodeMap map = (NamedNodeMap) element.getAttributes();

                for (int a = 0; a < map.getLength(); a++) {

                    String b = map.item(a).getNodeName();

                    switch (b) {

                        case "id":long id = Long.valueOf(map.item(a).getNodeValue()).longValue();
                        break;

                        case "firstName":String firstName = map.item(a).getNodeValue();
                        break;

                        case "lastName":String lastName = map.item(a).getNodeValue();
                        break;

                        case "country":String country = map.item(a).getNodeValue();
                        break;

                        case "age":int age = Integer.parseInt(map.item(a).getNodeValue().trim());
                        break;
                        Employee employee = new Employee(id, firstName, lastName, country, age);
                        stuff.add(employee);
                    }
                }
            }
        }
        return stuff;
    }
}

