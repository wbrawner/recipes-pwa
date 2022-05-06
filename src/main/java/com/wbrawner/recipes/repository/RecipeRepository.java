package com.wbrawner.recipes.repository;

import com.wbrawner.recipes.model.Recipe;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RecipeRepository extends ReactiveCrudRepository<Recipe, String> {
}
