/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intern;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author snkgs
 */
public class CsvVo {
    
        private Map<String, String> keyVal;

	public CsvVo(String id) {
		keyVal = new LinkedHashMap<>();
	}

	public Map<String, String> getKeyVal() {
		return keyVal;
	}

	public void setKeyVal(Map<String, String> keyVal) {
		this.keyVal = keyVal;
	}

	public void put(String key, String val) {
		keyVal.put(key, val);
	}

	public String get(String key) {
		return keyVal.get(key);
	}
    
}
