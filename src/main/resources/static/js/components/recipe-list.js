import {css, html, LitElement} from "../lit-core.min.js";
import {AnimatedLoader} from "./animated-loader.js";
import {User} from "../model/user.js";
import {StatefulElement} from "./stateful.js";

export class RecipeList extends StatefulElement {
    static get styles() {
        return css`
        
        `
    }

    render() {
        if (this.state.loading || !this.state.recipes) {
            return html`
                <animated-loader></animated-loader>`
        }
        if (this.error) {
            return html`<p class="error">${this.error}</p>`
        }
        if (this.state.recipes?.length === 0) {
            return html`<p>No recipes found</p>`
        }
        return html`
            <ul>
                ${this.state.recipes.map(recipe => {
                    html`
                        <li>
                            <span class="recipe-name">
                                ${recipe.name}
                            </span>
                            <span class="description">
                                ${recipe.description}
                            </span>
                        </li>`
                })}
            </ul>`
    }
}

customElements.define('recipe-list', RecipeList)