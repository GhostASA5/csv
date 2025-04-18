package com.example.project;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) throws IOException, CsvException {
//		String csvFile = "src/main/resources/100k-1.csv";
//
//		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
//			// 1. Читаем первые несколько строк для анализа
//			List<String[]> sampleRows = new ArrayList<>();
//			String[] nextLine;
//			int sampleSize = 5;
//
//			while ((nextLine = reader.readNext()) != null && sampleRows.size() < sampleSize) {
//				sampleRows.add(nextLine);
//			}
//
//			// 2. Определяем вероятную колонку с ID
//			int idColumnIndex = detectIdColumn(sampleRows);
//			System.out.println("Определена колонка с ID: " + idColumnIndex);
//
//			// 3. Возвращаемся в начало файла
//			reader.close();
//
//			// 4. Теперь ищем дубликаты в полном файле
//			findDuplicates(csvFile, idColumnIndex);
//		}
		SpringApplication.run(ProjectApplication.class, args);

	}

	private static int detectIdColumn(List<String[]> sampleRows) {
		if (sampleRows.isEmpty()) return 0;

		for (int col = 0; col < sampleRows.get(0).length; col++) {
			// Проверяем заголовок (первая строка)
			if (sampleRows.get(0)[col].toLowerCase().contains("id")) {
				return col;
			}
		}

		return 0;
	}

	private static void findDuplicates(String csvFile, int idColumnIndex)
			throws IOException, CsvException {
		Map<String, List<String[]>> recordsById = new HashMap<>();

		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
			List<String[]> allRecords = reader.readAll();

			for (String[] record : allRecords) {
				if (record.length > idColumnIndex) {
					String id = record[idColumnIndex];
					recordsById.computeIfAbsent(id, k -> new ArrayList<>()).add(record);
				}
			}

			// Выводим результаты
			System.out.println("\nНайдены следующие дубликаты:");
			boolean hasDuplicates = false;

			for (Map.Entry<String, List<String[]>> entry : recordsById.entrySet()) {
				if (entry.getValue().size() > 1) {
					hasDuplicates = true;
					System.out.println("\nID: " + entry.getKey() +
							" (количество: " + entry.getValue().size() + ")");

					for (String[] record : entry.getValue()) {
						System.out.println(Arrays.toString(record));
					}
				}
			}

			if (!hasDuplicates) {
				System.out.println("Дубликатов не найдено.");
			}

			for (Map.Entry<String, List<String[]>> entry : recordsById.entrySet()) {
				if (entry.getValue().size() > 1) {

					String[] start = entry.getValue().get(0);
					int startCount = countNonEmptyColumns(start);
					for (String[] record : entry.getValue()) {
						System.out.println(Arrays.toString(record));
						int thisCount = countNonEmptyColumns(record);
						if (thisCount > startCount) {
							for(int i = 0; i < record.length; i++) {
								if (Objects.equals(record[i], "")) {
									record[i] = start[i];
								}
							}
						} else {
							for(int i = 0; i < record.length; i++) {
								if (Objects.equals(record[i], "")) {
									start[i] = record[i];
								}
							}
						}
					}
				}
			}
		}
	}

	static int countNonEmptyColumns(String[] record) {
		int count = 0;
		for (String value : record) {
			if (value != null && !value.trim().isEmpty()) {
				count++;
			}
		}
		return count;
	}

}
