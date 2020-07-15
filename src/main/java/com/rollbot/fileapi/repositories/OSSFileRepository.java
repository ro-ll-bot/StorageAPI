package com.rollbot.fileapi.repositories;

import com.rollbot.fileapi.entity.OSSFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OSSFileRepository extends JpaRepository<OSSFile, Integer> {
  Optional<OSSFile> findByFilePath(String filePath);
}
