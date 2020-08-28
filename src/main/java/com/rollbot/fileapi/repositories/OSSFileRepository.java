package com.rollbot.fileapi.repositories;

import com.rollbot.fileapi.entity.OSSFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OSSFileRepository extends JpaRepository<OSSFile, Integer> {
  Optional<OSSFile> findByFilePath(String filePath);
  List<OSSFile> findAllByUserId(int userId);

  // First of all find the user id's here.
  // Than with that information get the user informations.
  // Maybe you can create user information table here with
  // a little bit of information.
  // Also find non-expired files.
  //List<OSSFile> sharedFileWithUserInformation();

  // Maybe it is not a need because all file paths are unique.
  Optional<OSSFile> findByUserIdAndFilePath(int userId, String filePath);
}
