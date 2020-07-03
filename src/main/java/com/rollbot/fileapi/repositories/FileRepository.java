package com.rollbot.fileapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;

public interface FileRepository extends JpaRepository<File, Integer> {
}
