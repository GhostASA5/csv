package com.example.project;

import com.example.project.repository.EquipmentRepository;
import com.example.project.repository.PairRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final Notification notification;

    private final PairRepository pairRepository;

    private final EquipmentRepository equipmentRepository;
    private final EntityManager em;

    public void csv() {
        try (FileReader reader = new FileReader("src/main/resources/100k-1.csv")) {
            HeaderColumnNameMappingStrategy<Equipment> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Equipment.class);

            CsvToBean<Equipment> csvToBean = new CsvToBeanBuilder<Equipment>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Equipment> equipments = csvToBean.parse();
            List<Equipment> uniqueEquipmentList = new ArrayList<>(equipments.stream()
                    .collect(Collectors.toMap(
                            Equipment::getId,
                            equipment -> equipment,
                            (existing, replacement) -> existing // выбираем существующий при дубликате
                    ))
                    .values());
            equipmentRepository.saveAll(uniqueEquipmentList);

            delete(equipments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void importEquipmentFromCsv(List<Equipment> equipmentList) {
        equipmentList.forEach(equipment -> {
            // Проверяем существование оборудования
            Equipment existing = equipmentRepository.findById(equipment.getId()).orElse(null);

            if (existing != null) {
                // Копируем все поля из импортируемого объекта в существующий
                updateExistingEquipment(existing, equipment);
                equipmentRepository.save(existing);
            } else {
                // Создаем новую запись
                equipmentRepository.save(equipment);
            }
        });
    }

    private void updateExistingEquipment(Equipment existing, Equipment newData) {
        // Копируем все поля, кроме id и версии (если есть)
        existing.setName(newData.getName());
        existing.setCi_code(newData.getCi_code());
        // ... копируем все остальные поля
    }

    private void delete(List<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            String type = equipment.getType();
            String code = equipment.getCi_code();
            if (type.equalsIgnoreCase("IP-адрес") || type.equals("DNS-запись")) {
                for (Equipment secEquipment : equipments) {
                    if (secEquipment.getCi_code().equalsIgnoreCase(code)) {
                        pairRepository.save(
                                new Pair(equipment.getId(), secEquipment.getId())
                        );
                    }
                    if (secEquipment.getDns().equalsIgnoreCase(equipment.getDns())) {
                        if (secEquipment.getType().equalsIgnoreCase(type)) {
                            cloneEquipment(null);
                        } else {
                            pairRepository.save(
                                    new Pair(equipment.getId(), secEquipment.getId())
                            );
                        }
                    }
                }
            } else {
                for (Equipment secEquipment : equipments) {
                    if (secEquipment.getCi_code().equalsIgnoreCase(code)) {
                        pairRepository.save(
                                new Pair(equipment.getId(), secEquipment.getId())
                        );
                    }
                    if (secEquipment.getSerial().equalsIgnoreCase(equipment.getSerial())) {
                        if (secEquipment.getType().equalsIgnoreCase(type)) {
                            cloneEquipment(null);
                        } else {
                            pairRepository.save(
                                    new Pair(equipment.getId(), secEquipment.getId())
                            );
                        }
                    }
                }
            }
        }
    }

    private void cloneEquipment(List<Equipment> equipments) {

    }
}
