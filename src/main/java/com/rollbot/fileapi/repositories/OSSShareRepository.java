package com.rollbot.fileapi.repositories;

import com.rollbot.fileapi.entity.OSSShared;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OSSShareRepository extends JpaRepository<OSSShared, Integer> {
    List<OSSShared> findAllBySharedUserId(int sharedUserId);
    //Optional<OSSShared> findBySharedUserIdAndSharedFile_FilePath(int sharedUserId, String sharedFile_path);
}
