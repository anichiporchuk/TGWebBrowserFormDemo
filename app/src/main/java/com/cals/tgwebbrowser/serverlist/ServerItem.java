package com.cals.tgwebbrowser.serverlist;

import java.util.ArrayList;
/***************Класс для представления элемента server из xml**************/
public class ServerItem {
	private String name;
	private String port;
	private String base_path;
	private String description;
	private String id;
	private String login;

	public ServerItem(String name, String port, String base_path, String description, String id) {
		this.name = name;
		this.port = port;
		this.base_path = base_path;
		this.description = description;
		this.id = id;
	}

	//геттеры
	public String getName() {
		return name;
	}
	public String getPort() {
		return port;
	}
	public String getPath() {
		return base_path;
	}
	public String getDescription() {
		return description;
	}
	public String getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}

	public ArrayList<String> getAll(){
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(name);
		ret.add(port);
		ret.add(base_path);
		ret.add(description);
		ret.add(id);
		return ret;
	}

	//формирует полную ссылку из полей класса
	public String getUrl() {
		String s = null;
		if (!(name.equals("")))
			s = "http://"+name;
		if (!(port.equals("")))
			s = s+":"+port;
		if (!(base_path.equals("")))
			s = s + "/" + base_path;
		return  s;
	}
	//формирует адрес сервера из полей класса
	public String getServUrl() {
		String s = null;
		if (!(name.equals("")))
			s = "http://"+name;
		if (!(port.equals("")))
			s = s+":"+port;
		return  s;
	}

	//cеттеры
	public void setName(String s) {
		name = s;
	}
	public void setPort(String s) {
		port = s;
	}
	public void setPath(String s) {
		base_path = s;
	}
	public void setDescription(String s) {
		description = s;
	}
	public void setLogin(String s) {
		login = s;
	}

	public void setId(String s) {
		id = s;

	}
}
