package Processing;

import Pojo.Schema.Attribute;
import Pojo.Schema.Dimension;
import Pojo.Schema.StarSchema;
import Pojo.Specs.CuboidSpecList;
import Pojo.Specs.Spec;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CuboidSpecManipulation {
    StarSchema schema;
    CuboidSpecList globalSpec;
    String schemaName;

    public CuboidSpecManipulation(String schemaName) {
        this.schemaName = schemaName;
        setStarSchema();
    }

    public CuboidSpecManipulation(){}

    public void setStarSchema() {
        ReadWriteXmlFile readWriteXmlFile= new ReadWriteXmlFile();
        schema = readWriteXmlFile.readStarSchema(schemaName);
    }

    public HashMap<Attribute, String> getAttributes() {
        HashMap<Attribute, String> attributes = new HashMap<Attribute, String>();
        for (Dimension d : schema.getDimension()) {
            String dimName = d.getName();
            for (Attribute a : d.getAttributes())
                attributes.put(a, dimName);
        }
        return attributes;
    }




    public String checkConfigExist(HashMap<Attribute, String> attributes) {
        CuboidCreation cc = new CuboidCreation();
        CuboidSpecList c = readSpec(schemaName);
        globalSpec= c;
        if (c == null)
            return "Error creating spec file. Please try again!";
        HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
        String specName = new String();
        for (Map.Entry<Attribute,String> entry : attributes.entrySet()) {
            if(map.containsKey(entry.getValue())) {
                ArrayList<Integer> list = map.get(entry.getValue());
                list.add((entry.getKey().getCode()));
            }
            else {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(entry.getKey().getCode());
                map.put(entry.getValue(),list);
            }
        }
        for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()){
            ArrayList<Integer> list = entry.getValue();
            Collections.sort(list);
            specName += entry.getKey() + "_";
            for(Integer  i : list)
                specName += i + "_";
        }
        specName = specName.substring(0, specName.length() - 1);
        for (Spec s : c.getSpeclist()) {
            if (s.getName().equalsIgnoreCase(specName))
                return "Config Exists!";
        }
        System.out.println(specName);

//        // Write in xml and make tables in db.
//        Spec s = new Spec();
//        s.setName(specName);
//        s.setCustomName(name);
//        ArrayList<String> attr_list = new ArrayList<String>();
//        for (Map.Entry<Attribute,String> entry : attributes.entrySet()) {
//            attr_list.add(entry.getValue() + "_" + entry.getKey().getName());
//        }
//        c.addSpec(s);
//        ArrayList<ArrayList<String>> queriesAndTables = cc.generateQueryFromAttr(attributes, schema);
//        try {
//            boolean r = cc.createCuboids(queriesAndTables.get(0), "store");
//            c.addTables(queriesAndTables.get(1));
//            boolean t = writeSpecInXml(c);
//        } catch (Exception  e) {
//            return "Error creating spec. Please try again.";
//        }
        return "Ready to create the config!";
    }


    public CuboidSpecList readSpec(String schemaName) {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println(currentDirectory);
        String fName = currentDirectory + "/storage/" + schemaName + "_spec.xml";
        File f = new File(fName);
        try {
            if (f.exists()) {
                JAXBContext jaxbContext = JAXBContext.newInstance(CuboidSpecList.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                CuboidSpecList specList = (CuboidSpecList) jaxbUnmarshaller.unmarshal(f);
                globalSpec= specList;
                return specList;
            } else {
                f.createNewFile();
                return new CuboidSpecList();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean writeSpecInXml(HashMap<Attribute, String> attributes, String name)
            throws JAXBException, IOException {
        String currentDirectory = System.getProperty("user.dir");
        String fName = currentDirectory + "/storage/" + this.schemaName + "_spec.xml";
        HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
        String specName = new String();
        for (Map.Entry<Attribute,String> entry : attributes.entrySet()) {
            if(map.containsKey(entry.getValue())) {
                ArrayList<Integer> list = map.get(entry.getValue());
                list.add((entry.getKey().getCode()));
            }
            else {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(entry.getKey().getCode());
                map.put(entry.getValue(),list);
            }
        }
        for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()){
            ArrayList<Integer> list = entry.getValue();
            Collections.sort(list);
            specName += entry.getKey() + "_";
            for(Integer  i : list)
                specName += i + "_";
        }
        specName = specName.substring(0, specName.length() - 1);
        Spec s = new Spec();
        s.setName(specName);
        s.setCustomName(name);
        s.setAttribute(new ArrayList<Attribute>(attributes.keySet()));
        try {
            File f = new File(fName);
            JAXBContext jaxbContext = JAXBContext.newInstance(CuboidSpecList.class);
            Marshaller marshallerObj = jaxbContext.createMarshaller();
            if(f.exists())
            {
                globalSpec.addSpec(s);
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshallerObj.marshal(globalSpec, new FileOutputStream(fName));
            }
        } catch (JAXBException jxb) {
            jxb.printStackTrace();
            return false;
        } catch (IOException io) {
            io.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public CuboidSpecList showAvailableSpec(String schemaName) {

        String currentDirectory = System.getProperty("user.dir");
        System.out.println(currentDirectory);
        //TODO replace "here" with schemaName
        String fName = currentDirectory + "/storage/" + schema.getName() + "_spec.xml";
        try {
            File file = new File(fName);
            JAXBContext jaxbContext = JAXBContext.newInstance(CuboidSpecList.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CuboidSpecList specList = (CuboidSpecList) jaxbUnmarshaller.unmarshal(file);
            globalSpec = specList;
            return specList;
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}