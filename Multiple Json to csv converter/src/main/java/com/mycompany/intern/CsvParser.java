/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import com.mycompany.intern.CsvVo;

/**
 *
 * @author snkgs
 */
public class CsvParser {
    
        //different funtions to extract headers or records from the CSV files
    
        public static List<CsvVo> getRecodrsFromACsv(File file, List<String> keys) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<CsvVo> records = new ArrayList<>();
		boolean isHeader = true;

		String line = null;
		while ((line = br.readLine()) != null) {
			if (isHeader) {// first line is header
				isHeader = false;
				continue;
			}
			CsvVo record = new CsvVo(file.getName());
			String[] lineSplit = line.split(",");
			for (int i = 0; i < lineSplit.length; i++) {
				record.put(keys.get(i), lineSplit[i]);
			}
			records.add(record);
		}

		br.close();

		return records;
	}

	public static List<String> getHeadersFromACsv(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> headers = null;

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split(",");
			headers = new ArrayList<>(Arrays.asList(lineSplit));
			break;
		}

		br.close();

		return headers;
	}

	public static void writeToCsv(final File file, final Set<String> headers, final List<CsvVo> records)
			throws IOException {
		FileWriter csvWriter = new FileWriter(file);

		// write headers
		String sep = "";
		String[] headersArr = headers.toArray(new String[headers.size()]);
		for (String header : headersArr) {
			csvWriter.append(sep);
			csvWriter.append(header);
			sep = ",";
		}

		csvWriter.append("\n");

		// write records at each line
		for (CsvVo record : records) {
			sep = "";
			for (int i = 0; i < headersArr.length; i++) {
				csvWriter.append(sep);
				csvWriter.append(record.get(headersArr[i]));
				sep = ",";
			}
			csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	}
    
}
