package guru.samples.recipe.controller;

import guru.samples.recipe.service.ImageService;
import guru.samples.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static java.lang.String.format;

@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    @Autowired
    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String getUploadForm(@PathVariable Long recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findViewById(recipeId));
        return "recipe/image-upload-form";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String upload(@PathVariable Long recipeId, @RequestParam(name = "image") MultipartFile image) {
        imageService.save(recipeId, image);
        return format("redirect:/recipe/%d/details", recipeId);
    }
}
