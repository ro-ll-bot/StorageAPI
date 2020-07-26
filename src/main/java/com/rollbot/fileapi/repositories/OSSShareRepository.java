package com.rollbot.fileapi.repositories;

import com.rollbot.fileapi.entity.OSSShared;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OSSShareRepository extends JpaRepository<OSSShared, Integer> {
    List<OSSShared> findAllBySharedUserId(int sharedUserId);
}
