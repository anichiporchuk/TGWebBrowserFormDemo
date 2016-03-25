package com.cals.tgwebbrowser.serverlist;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/***************Класс для обработки дерева xml********************/
public class ServerRoot {
	Element root;

	public ServerRoot(Element r) {
		root = r;
	}

	//формирует список серверов из xml
	public ArrayList<ServerItem> FormServerList(){
		ArrayList<ServerItem> servers = new ArrayList<ServerItem>();
		for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeName().trim().equals("server")) {
				//если при разборе дерева наткнулись на сервер - создаем новый элемент типа ServerItem и заполняем его поля атрибутами из xml
				NodeList attrtitles = child.getChildNodes();
				ServerItem newServer = new ServerItem("","","","", "");
				for (int gtemp = 0; gtemp <attrtitles.getLength(); gtemp++) {
					//адрес сервера
					if (attrtitles.item(gtemp).getNodeName().trim().equals("name"))
						newServer.setName(attrtitles.item(gtemp).getTextContent());

					//порт
					if (attrtitles.item(gtemp).getNodeName().trim().equals("port"))
						newServer.setPort(attrtitles.item(gtemp).getTextContent());

					//базовый путь
					if (attrtitles.item(gtemp).getNodeName().trim().equals("base_path"))
						newServer.setPath(attrtitles.item(gtemp).getTextContent());

					//описание
					if (attrtitles.item(gtemp).getNodeName().trim().equals("description"))
						newServer.setDescription(attrtitles.item(gtemp).getTextContent());
					//id и логин					
					if (attrtitles.item(gtemp).getNodeName().trim().equals("key")) {
						NamedNodeMap attrs = attrtitles.item(gtemp).getAttributes();
						if (attrs != null){
							for (int i = 0; i < attrs.getLength(); i++) {
								Node att = attrs.item(i);
								String a = att.getNodeName();
								if (a.trim().equals("login"))
									newServer.setLogin(att.getNodeValue());
							}
							newServer.setId(attrtitles.item(gtemp).getTextContent());
						}
					}
				}
				servers.add(newServer);
			}
		}
		return servers;
	}

	//формирование списка первых строк элементов спиннера
	public ArrayList<String> FormServer1(){
		ArrayList<String> Server1 = new ArrayList<String>();
		ArrayList<ServerItem> servers = FormServerList();
		for (ServerItem server: servers) {
			String s = server.getDescription();
			//если описание задано - в первую строку добавляем его. Если нет - добавляем url
			if (s != null){
				if (!(s.equals("")))
					Server1.add(s);
				else
					Server1.add(server.getUrl());
			}
		}
		return Server1;
	}

	//формирование списка вторых строк элементов спиннера
	public ArrayList<String> FormServer2(){
		ArrayList<String> Server2 = new ArrayList<String>();
		ArrayList<ServerItem> servers = FormServerList();
		for (ServerItem server: servers) {
			String s = server.getDescription();
			//если было описание - во вторую строку пишем url, иначе ничего не пишем.
			if (s != null){
				if (!(s.equals("")))
					Server2.add(server.getUrl());
				else
					Server2.add("");
			}
		}
		return Server2;
	}
	public ArrayList<String> FormIds(){
		ArrayList<String> Ids = new ArrayList<String>();
		ArrayList<ServerItem> servers = FormServerList();
		for (ServerItem server: servers) {
			String s = server.getId();
			Ids.add(s);
		}
		return Ids;
	}

}