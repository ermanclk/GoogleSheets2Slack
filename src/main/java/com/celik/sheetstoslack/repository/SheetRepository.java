package com.celik.sheetstoslack.repository;

import com.celik.sheetstoslack.entity.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<Sheet, Integer> {

    Sheet findBySheetId(String sheetId);

}
