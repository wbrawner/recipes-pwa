export class Recipe {
    /**
     * @type string
     */
    id;

    /**
     * @type string
     */
    name;

    /**
     * @type string
     */
    description;

    /**
     * array of images for the recipe
     * @type Array<string>
     */
    images;

    /**
     * @type Array<string>
     */
    ingredients;

    /**
     * @type Array<string>
     */
    instructions;

    /**
     * time (in seconds) to prepare the recipe
     * @type number
     */
    prepTime;

    /**
     * time (in seconds) to cook the recipe
     * @type number
     */
    cookTime;
}