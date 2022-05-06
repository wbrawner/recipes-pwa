package com.wbrawner.recipes.controller;

import com.wbrawner.recipes.model.Recipe;
import com.wbrawner.recipes.model.RecipeRequest;
import com.wbrawner.recipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Secured("USER")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    @ResponseBody
    public Flux<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @PostMapping
    @ResponseBody
    public Mono<ResponseEntity<?>> newRecipe(@RequestBody RecipeRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body("name is required"));
        }
        return recipeRepository.save(Recipe.from(request))
                .map(recipe -> ResponseEntity.ok().body(recipe));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Mono<ResponseEntity<?>> updateRecipe(
            @PathVariable(value = "id", required = false) String id,
            @RequestBody RecipeRequest request
    ) {
        return recipeRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    if (request.name() == null || request.name().isBlank()) {
                        return Mono.just(ResponseEntity.badRequest().body("name is required"));
                    }
                    return recipeRepository.save(Recipe.from(request, id))
                            .map(ResponseEntity::ok);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteRecipe(@PathVariable("id") String id) {
        return recipeRepository.deleteById(id);
    }
}
