package com.example.campusdetectionv2.support;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlContentHandler extends DefaultHandler{
	private List<BrowsingData> dataList = null;
	String tagName;
	BrowsingData data;
	boolean success = false;
	public void startDocument() throws SAXException {
		dataList =  new ArrayList<BrowsingData>();
	}

	public void endDocument() throws SAXException {
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attr) throws SAXException {
		tagName = localName;
		if("report".equals(tagName)){
			for(int i=0;i<attr.getLength();i++){
				if("id".equals(attr.getLocalName(i))){
					data = new BrowsingData();
					data.setId(Integer.valueOf(attr.getValue(0)));					 
				} 
			}			
		}else if("reports".equals(tagName))
			success = true;
		 		 
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if("report".equals(localName)){
			dataList.add(data);
			data = null;
		}
		tagName = null;
	}
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String character = new String(ch,start,length);
		if(tagName!=null){
			if("username".equals(tagName)){
				data.setUsername(character);
			}
			else if("type".equals(tagName)){
				data.setType(character);
			}
			else if("remark".equals(tagName)){
				data.setRemark(character);
			}
			else if("lat".equals(tagName)){
				data.setLat(Double.valueOf(character));
			}
			else if("lng".equals(tagName)){
				data.setLng(Double.valueOf(character));
			}
			else if("userlat".equals(tagName)){
				data.setUserLat(Double.valueOf(character));
			}
			else if("userlng".equals(tagName)){
				data.setUserLng(Double.valueOf(character));
			}
			else if("time".equals(tagName)){
				data.setTime(character);
			}
			else if("submit_time".equals(tagName)){
				data.setSubmitTime(character);
			}
			else if("picpath".equals(tagName)){
				data.setPicpath(character);
			}
			else if("flag".equals(tagName)){
				if("true".equals(character))
					data.setFlag(true);
				else			
					data.setFlag(false);
			}
		}	
	}
	public List<BrowsingData> getDataList() {
		return dataList;
	}

	public boolean isSuccess() {
		return success;
	}

}
