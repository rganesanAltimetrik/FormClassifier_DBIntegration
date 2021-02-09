package com.example.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.model.Image;


@Repository
public interface ImageDAO extends CrudRepository<Image,Integer>{

	
}
