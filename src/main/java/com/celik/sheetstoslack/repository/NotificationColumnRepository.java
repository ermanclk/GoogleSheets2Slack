package com.celik.sheetstoslack.repository;

import com.celik.sheetstoslack.entity.NotificationColumn;
import com.celik.sheetstoslack.entity.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationColumnRepository extends JpaRepository<NotificationColumn, Integer> {

    NotificationColumn findBySheetAndAndRowNumberAndIndex(Sheet sheet, int number, int index);
}
