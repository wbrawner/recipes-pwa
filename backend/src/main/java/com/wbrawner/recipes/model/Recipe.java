package com.wbrawner.recipes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;

@Document
public class Recipe {
    @Id
    private final String id;
    private final String name;
    private final String description;
    private final List<String> images;
    private final List<String> ingredients;
    private final List<String> instructions;
    private final int prepTime;
    private final int cookTime;

    public Recipe() {
        this(null, "", null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), 0, 0);
    }

    public Recipe(
            String id,
            String name,
            String description,
            List<String> images,
            List<String> ingredients,
            List<String> instructions,
            int prepTime,
            int cookTime
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.images = images;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public static Recipe from(RecipeRequest request) {
        return from(request, null);
    }

    public static Recipe from(RecipeRequest request, String id) {
        return new Recipe(
                id,
                request.name(),
                request.description(),
                request.images(),
                request.ingredients(),
                request.instructions(),
                request.prepTime(),
                request.cookTime()
        );
    }
}
