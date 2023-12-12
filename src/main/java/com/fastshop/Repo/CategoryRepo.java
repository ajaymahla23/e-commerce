package com.fastshop.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastshop.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

	public List<Category> findByNameContaining(String name);

}
