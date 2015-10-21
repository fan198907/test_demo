package com.android.qiadesi;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;


import android.content.Context;
import android.util.Xml;

public class ReadAndWriteXml {

	public String readXml(Context context,String myXml,String name){
		String value="";
		DocumentBuilderFactory  mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
			Document mDocument = mDocumentBuilder.parse(context.openFileInput(myXml));
//			Document mDocument = mDocumentBuilder.parse(context.getResources().getAssets().open("myXml", AssetManager.ACCESS_BUFFER));
			Element root = mDocument.getDocumentElement();
			NodeList mNodeList = root.getElementsByTagName(name);
			System.out.println("mNodeList= "+mNodeList.toString());
			Node node = mNodeList.item(0);
			value = node.getFirstChild().getNodeValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
			value="";
		}
		return value;
		
	}
	
	public ArrayList<String> readAddress(Context context,String myXml,String name){
		ArrayList<String> list = new ArrayList<String>();
		
		DocumentBuilderFactory  mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
			Document mDocument = mDocumentBuilder.parse(context.openFileInput(myXml));
//			Document mDocument = mDocumentBuilder.parse(context.getResources().getAssets().open("myXml", AssetManager.ACCESS_BUFFER));
			Element root = mDocument.getDocumentElement();
			NodeList mNodeList = root.getElementsByTagName(name);
			System.out.println("mNodeList= "+mNodeList.toString());
			for(int i=0;i<mNodeList.getLength();i++){
				String value="";
				Node node = mNodeList.item(i);
				value = node.getFirstChild().getNodeValue().toString();
				list.add(value);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void writeNameXml(Context context,String xmlNamePath,String userName,String password){
		OutputStream os;
		XmlSerializer mXmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			mXmlSerializer.setOutput(writer);
			mXmlSerializer.startDocument("UTF-8", true);
			mXmlSerializer.startTag("", "value");
			
			mXmlSerializer.startTag("", "userName");
			mXmlSerializer.text(userName);
			mXmlSerializer.endTag("", "userName");
			
			mXmlSerializer.startTag("", "password");
			mXmlSerializer.text(password);
			mXmlSerializer.endTag("", "password");
			
			mXmlSerializer.endTag("", "value");
			mXmlSerializer.endDocument();

			os = context.openFileOutput(xmlNamePath,Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(writer.toString());
			osw.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
}
