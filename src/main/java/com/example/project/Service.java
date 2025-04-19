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
import java.time.LocalDateTime;
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

    @Transactional
    public void csv() {
        try (FileReader reader = new FileReader("src/main/resources/100k-1.csv")) {
            HeaderColumnNameMappingStrategy<Equipment> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Equipment.class);

            CsvToBean<Equipment> csvToBean = new CsvToBeanBuilder<Equipment>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Equipment> equipments = csvToBean.parse();
            //equipmentRepository.saveAll(equipments);


            delete(equipments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void delete(List<Equipment> equipments) {
        List<Equipment> forRemove = new ArrayList<>();
        List<Equipment> clone = new ArrayList<>();
        List<Equipment> result = new ArrayList<>();
        List<Equipment> withConflict = new ArrayList<>();
        List<Equipment> withoutConflict = new ArrayList<>();

        for (Equipment equipment : equipments) {
            long time = System.currentTimeMillis();
            String type = equipment.getType();
            String code = equipment.getCi_code();
            if (type.equalsIgnoreCase("IP-адрес") || type.equals("DNS-запись")) {
                List<Equipment> subEquipment = clone.subList(clone.indexOf(equipment) + 1, clone.size());
                for (Equipment secEquipment : subEquipment) {
                    if (secEquipment.getCi_code().equalsIgnoreCase(code)) {
                        pairRepository.save(
                                new Pair(equipment.getId(), secEquipment.getId())
                        );
                        equipmentRepository.save(equipment);
                        equipmentRepository.save(secEquipment);
                        forRemove.addAll(List.of(equipment, secEquipment));
                        clone.removeAll(List.of(equipment, secEquipment));
                    }
                    else if (secEquipment.getDns().equalsIgnoreCase(equipment.getDns())) {
                        if (secEquipment.getType().equalsIgnoreCase(type)) {
                            Equipment mergedEquipment = cloneEquipment(equipment, secEquipment);
                            // Сохраняем новую объединенную сущность
                            equipmentRepository.save(mergedEquipment);
                            // Удаляем старые сущности
//                            equipmentRepository.delete(equipment);
//                            equipmentRepository.delete(secEquipment);
                            // Обновляем пары для новой сущности
                            clone.removeAll(List.of(equipment, secEquipment));
                        } else {
                            pairRepository.save(
                                    new Pair(equipment.getId(), secEquipment.getId())
                            );
                            equipmentRepository.save(equipment);
                            equipmentRepository.save(secEquipment);
                            forRemove.addAll(List.of(equipment, secEquipment));
                            clone.removeAll(List.of(equipment, secEquipment));
                        }
                    }
                }
            } else {
                List<Equipment> subEquipment = equipments.subList(equipments.indexOf(equipment), equipments.size());
                for (Equipment secEquipment : subEquipment) {
                    if (secEquipment.getCi_code().equalsIgnoreCase(code)) {
                        pairRepository.save(
                                new Pair(equipment.getId(), secEquipment.getId())
                        );
                        equipmentRepository.save(equipment);
                        equipmentRepository.save(secEquipment);
                        forRemove.addAll(List.of(equipment, secEquipment));
                        clone.removeAll(List.of(equipment, secEquipment));
                    }
                    else if (secEquipment.getSerial().equalsIgnoreCase(equipment.getSerial())) {
                        if (secEquipment.getType().equalsIgnoreCase(type)) {
                            Equipment mergedEquipment = cloneEquipment(equipment, secEquipment);
                            // Сохраняем новую объединенную сущность
                            equipmentRepository.save(mergedEquipment);
                            // Удаляем старые сущности
//                            equipmentRepository.delete(equipment);
//                            equipmentRepository.delete(secEquipment);
                            clone.removeAll(List.of(equipment, secEquipment));
                            // Обновляем пары для новой сущности
                        } else {
                            pairRepository.save(
                                    new Pair(equipment.getId(), secEquipment.getId())
                            );
                            equipmentRepository.save(equipment);
                            equipmentRepository.save(secEquipment);
                            forRemove.addAll(List.of(equipment, secEquipment));
                            clone.removeAll(List.of(equipment, secEquipment));
                        }
                    }
                }
            }
            System.out.println(equipments.indexOf(equipment) + " " + (System.currentTimeMillis() - time));

        }
    }

    public static Equipment cloneEquipment(Equipment priority, Equipment secondary)  // здесь тоже надо обработать ошибк
    {
        Equipment result = new Equipment();

        // Обрабатываем каждое поле согласно правилам приоритета
        result.setId(mergeField(priority.getId(), secondary.getId(), "id"));
        result.setCreated_on(mergeField(priority.getCreated_on(), secondary.getCreated_on(), "created_on"));
        result.setUpdated_on(LocalDateTime.now());
        result.setName(mergeField(priority.getName(), secondary.getName(), "name"));
        result.setCi_code(mergeField(priority.getCi_code(), secondary.getCi_code(), "ci_code"));
        result.setShort_name(mergeField(priority.getShort_name(), secondary.getShort_name(), "short_name"));
        result.setFull_name(mergeField(priority.getFull_name(), secondary.getFull_name(), "full_name"));
        result.setDescription(mergeField(priority.getDescription(), secondary.getDescription(), "description"));
        result.setNotes(mergeField(priority.getNotes(), secondary.getNotes(), "notes"));
        result.setStatus(mergeField(priority.getStatus(), secondary.getStatus(), "status"));
        result.setManufacturer(mergeField(priority.getManufacturer(), secondary.getManufacturer(), "manufacturer"));
        result.setSerial(mergeField(priority.getSerial(), secondary.getSerial(), "serial"));
        result.setModel(mergeField(priority.getModel(), secondary.getModel(), "model"));
        result.setLocation(mergeField(priority.getLocation(), secondary.getLocation(), "location"));
        result.setMount(mergeField(priority.getMount(), secondary.getMount(), "mount"));
        result.setHostname(mergeField(priority.getHostname(), secondary.getHostname(), "hostname"));
        result.setDns(mergeField(priority.getDns(), secondary.getDns(), "dns"));
        result.setIp(mergeField(priority.getIp(), secondary.getIp(), "ip"));
        result.setCpu_cores(mergeField(priority.getCpu_cores(), secondary.getCpu_cores(), "cpu_cores"));
        result.setCpu_freq(mergeField(priority.getCpu_freq(), secondary.getCpu_freq(), "cpu_freq"));
        result.setRam(mergeField(priority.getRam(), secondary.getRam(), "ram"));
        result.setTotal_volume(mergeField(priority.getTotal_volume(), secondary.getTotal_volume(), "total_volume"));
        result.setType(mergeField(priority.getType(), secondary.getType(), "type"));
        result.setCategory(mergeField(priority.getCategory(), secondary.getCategory(), "category"));
        result.setUser_org(mergeField(priority.getUser_org(), secondary.getUser_org(), "user_org"));
        result.setOwner_org(mergeField(priority.getOwner_org(), secondary.getOwner_org(), "owner_org"));
        result.setCode_mon(mergeField(priority.getCode_mon(), secondary.getCode_mon(), "code_mon"));

        return result;
    }

    /**
     * Объединяет два значения поля по правилам приоритета
     * @param priorityValue значение из приоритетной сущности
     * @param secondaryValue значение из вторичной сущности
     * @param fieldName имя поля для сообщения об ошибке
     * @return объединенное значение
     */
    private static <T> T mergeField(T priorityValue, T secondaryValue, String fieldName) // Сдезь надо обработать нашу ошибку
    {
        if (!isEmpty(priorityValue)) {
            return priorityValue;
        }

        if (!isEmpty(secondaryValue)) {
            return secondaryValue;
        }

        return secondaryValue;
    }

    /**
     * Проверяет, является ли значение "пустым" (null или пустая строка для String)
     */
    private static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }
        return false;
    }
}