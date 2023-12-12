package com.fastshop.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.CategoryRepo;
import com.fastshop.model.Category;

@Service
public class CategoryService {
	@Autowired
	CategoryRepo categoryRepo;

	public List<Category> getAllCategory() {
		return categoryRepo.findAll();
	}

	public void addCategory(Category category) {
		category.setDate(new Date());
		categoryRepo.save(category);
	}

	public void removeCategoryById(int id) {
		categoryRepo.deleteById(id);
	}

	public Optional<Category> getCategoryById(int id) {
		return categoryRepo.findById(id);
	}

}
