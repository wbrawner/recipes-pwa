package com.wbrawner.recipes.model;

import java.util.List;

public record RecipeRequest(
        String name,
        String description,
        List<String> images,
        List<String> ingredients,
        List<String> instructions,
        int prepTime,
        int cookTime
) {
}
