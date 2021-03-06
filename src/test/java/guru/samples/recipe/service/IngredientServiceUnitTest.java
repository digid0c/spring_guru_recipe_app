package guru.samples.recipe.service;

import guru.samples.recipe.converter.IngredientToIngredientViewConverter;
import guru.samples.recipe.converter.IngredientViewToIngredientConverter;
import guru.samples.recipe.converter.UnitOfMeasureToUnitOfMeasureViewConverter;
import guru.samples.recipe.converter.UnitOfMeasureViewToUnitOfMeasureConverter;
import guru.samples.recipe.domain.Ingredient;
import guru.samples.recipe.domain.Recipe;
import guru.samples.recipe.repository.IngredientRepository;
import guru.samples.recipe.repository.RecipeRepository;
import guru.samples.recipe.repository.UnitOfMeasureRepository;
import guru.samples.recipe.view.IngredientView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceUnitTest {

    private static final Long RECIPE_ID = 1L;
    private static final Long FIRST_INGREDIENT_ID = 1L;
    private static final Long SECOND_INGREDIENT_ID = 2L;
    private static final Long THIRD_INGREDIENT_ID = 3L;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientService tested;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tested = new IngredientServiceImpl(recipeRepository, unitOfMeasureRepository, ingredientRepository,
                new IngredientToIngredientViewConverter(new UnitOfMeasureToUnitOfMeasureViewConverter()),
                new IngredientViewToIngredientConverter(new UnitOfMeasureViewToUnitOfMeasureConverter()));
    }

    @Test
    public void shouldFindIngredientByIdAndRecipeId() {
        Recipe recipe = createRecipe();
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(recipe));

        IngredientView ingredient = tested.findByIngredientIdAndRecipeId(SECOND_INGREDIENT_ID, RECIPE_ID);

        verify(recipeRepository).findById(RECIPE_ID);
        assertThat(ingredient, is(notNullValue()));
        assertThat(ingredient.getId(), is(equalTo(SECOND_INGREDIENT_ID)));
        assertThat(ingredient.getRecipeId(), is(equalTo(RECIPE_ID)));
    }

    @Test
    public void shouldSaveIngredient() {
        IngredientView ingredient = IngredientView.builder()
                .id(THIRD_INGREDIENT_ID)
                .recipeId(RECIPE_ID)
                .build();
        Recipe recipe = createRecipe();
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.save(any())).then(returnsFirstArg());

        IngredientView savedIngredient = tested.save(ingredient);

        assertThat(savedIngredient.getId(), is(equalTo(THIRD_INGREDIENT_ID)));
        assertThat(savedIngredient.getRecipeId(), is(equalTo(RECIPE_ID)));
        verify(recipeRepository).findById(RECIPE_ID);
        verify(ingredientRepository).save(any());
    }

    @Test
    public void shouldDeleteIngredient() {
        Recipe recipe = createRecipe();
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(recipe));

        tested.deleteById(SECOND_INGREDIENT_ID, RECIPE_ID);

        verify(recipeRepository).findById(RECIPE_ID);
        verify(ingredientRepository).deleteById(SECOND_INGREDIENT_ID);
    }

    private Recipe createRecipe() {
        return Recipe.builder()
                .id(RECIPE_ID)
                .ingredients(new HashSet<>())
                .build()
                .addIngredient(Ingredient.builder()
                        .id(FIRST_INGREDIENT_ID)
                        .build())
                .addIngredient(Ingredient.builder()
                        .id(SECOND_INGREDIENT_ID)
                        .build());
    }
}
